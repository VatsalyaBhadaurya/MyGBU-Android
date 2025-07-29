package com.vatty.mygbu.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.vatty.mygbu.R
import com.vatty.mygbu.adapter.NotificationHistoryAdapter
import com.vatty.mygbu.data.model.CurriculumNotification
import com.vatty.mygbu.viewmodel.AdminDashboardViewModel
import java.util.*

class CurriculumNotificationsFragment : Fragment() {
    
    private lateinit var viewModel: AdminDashboardViewModel
    private lateinit var notificationHistoryAdapter: NotificationHistoryAdapter
    
    private lateinit var spinnerNotificationProgram: AutoCompleteTextView
    private lateinit var spinnerNotificationSemester: AutoCompleteTextView
    private lateinit var etNotificationTitle: TextInputEditText
    private lateinit var etNotificationMessage: TextInputEditText
    private lateinit var cbNotifyStudents: CheckBox
    private lateinit var cbNotifyFaculty: CheckBox
    private lateinit var cbNotifyMentors: CheckBox
    private lateinit var btnSendNotification: Button
    private lateinit var rvNotificationHistory: RecyclerView
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_curriculum_notifications, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewModel = ViewModelProvider(requireActivity())[AdminDashboardViewModel::class.java]
        
        initializeViews(view)
        setupRecyclerView()
        setupSpinners()
        setupClickListeners()
        observeData()
        
        // Load initial data
        viewModel.loadCurriculumNotifications()
        viewModel.loadPrograms()
    }
    
    private fun initializeViews(view: View) {
        spinnerNotificationProgram = view.findViewById(R.id.spinner_notification_program)
        spinnerNotificationSemester = view.findViewById(R.id.spinner_notification_semester)
        etNotificationTitle = view.findViewById(R.id.et_notification_title)
        etNotificationMessage = view.findViewById(R.id.et_notification_message)
        cbNotifyStudents = view.findViewById(R.id.cb_notify_students)
        cbNotifyFaculty = view.findViewById(R.id.cb_notify_faculty)
        cbNotifyMentors = view.findViewById(R.id.cb_notify_mentors)
        btnSendNotification = view.findViewById(R.id.btn_send_notification)
        rvNotificationHistory = view.findViewById(R.id.rv_notification_history)
    }
    
    private fun setupRecyclerView() {
        notificationHistoryAdapter = NotificationHistoryAdapter(
            onViewDetailsClick = { notification ->
                Toast.makeText(context, "View details for: ${notification.title}", Toast.LENGTH_SHORT).show()
            }
        )
        
        rvNotificationHistory.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = notificationHistoryAdapter
        }
    }
    
    private fun setupSpinners() {
        // Semester Spinner
        val semesters = listOf("All Semesters") + (1..8).map { "Semester $it" }
        val semesterAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, semesters)
        spinnerNotificationSemester.setAdapter(semesterAdapter)
    }
    
    private fun setupClickListeners() {
        btnSendNotification.setOnClickListener {
            sendNotification()
        }
    }
    
    private fun observeData() {
        viewModel.curriculumNotifications.observe(viewLifecycleOwner) { notifications ->
            notificationHistoryAdapter.submitList(notifications)
        }
        
        viewModel.programs.observe(viewLifecycleOwner) { programs ->
            val programNames = programs.map { it.name }
            val programAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, programNames)
            spinnerNotificationProgram.setAdapter(programAdapter)
        }
    }
    
    private fun sendNotification() {
        val program = spinnerNotificationProgram.text.toString()
        val semester = spinnerNotificationSemester.text.toString()
        val title = etNotificationTitle.text.toString()
        val message = etNotificationMessage.text.toString()
        
        if (program.isEmpty() || title.isEmpty() || message.isEmpty()) {
            Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_SHORT).show()
            return
        }
        
        if (!cbNotifyStudents.isChecked && !cbNotifyFaculty.isChecked && !cbNotifyMentors.isChecked) {
            Toast.makeText(context, "Please select at least one recipient type", Toast.LENGTH_SHORT).show()
            return
        }
        
        val recipients = mutableListOf<String>().apply {
            if (cbNotifyStudents.isChecked) add("STUDENTS")
            if (cbNotifyFaculty.isChecked) add("FACULTY")
            if (cbNotifyMentors.isChecked) add("MENTORS")
        }
        
        val semesterNumber = if (semester == "All Semesters") null else {
            semester.replace("Semester ", "").toIntOrNull()
        }
        
        val notification = CurriculumNotification(
            id = UUID.randomUUID().toString(),
            title = title,
            message = message,
            programId = program,
            programName = program,
            semester = semesterNumber,
            recipients = recipients,
            sentDate = Date(),
            sentBy = "Admin User",
            recipientCount = recipients.size * 50 // Mock count
        )
        
        viewModel.sendCurriculumNotification(
            notification,
            onSuccess = {
                Toast.makeText(context, "Notification sent successfully", Toast.LENGTH_SHORT).show()
                clearForm()
            },
            onError = { error ->
                Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
            }
        )
    }
    
    private fun clearForm() {
        spinnerNotificationProgram.text.clear()
        spinnerNotificationSemester.text.clear()
        etNotificationTitle.text?.clear()
        etNotificationMessage.text?.clear()
        cbNotifyStudents.isChecked = true
        cbNotifyFaculty.isChecked = true
        cbNotifyMentors.isChecked = true
    }
} 