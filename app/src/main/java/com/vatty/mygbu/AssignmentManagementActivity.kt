package com.vatty.mygbu

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.vatty.mygbu.data.model.SavedAssignment
import com.vatty.mygbu.databinding.ActivityAssignmentManagementBinding
import com.vatty.mygbu.enums.AssignmentStatus
import com.vatty.mygbu.utils.DateUtils
import android.util.Log
import java.util.*

class AssignmentManagementActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityAssignmentManagementBinding
    private var attachedFileUri: Uri? = null
    private var attachedFileName: String? = null
    private val savedAssignments = mutableListOf<SavedAssignment>()
    private lateinit var assignmentsAdapter: SavedAssignmentsAdapter
    
    companion object {
        private const val TAG = "AssignmentManagement"
    }
    
    // File picker launcher
    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            attachedFileUri = it
            attachedFileName = getFileName(it)
            binding.tvAttachedFile.text = getString(R.string.file_attached, attachedFileName ?: getString(R.string.unknown_file))
            binding.tvAttachedFile.isVisible = true
            Toast.makeText(this, getString(R.string.file_attached, attachedFileName ?: getString(R.string.unknown_file)), Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssignmentManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        // Log activity startup
        Log.i(TAG, "AssignmentManagementActivity started")
        
        setupToolbar()
        setupClickListeners()
        setupRecyclerView()
        loadSampleAssignments()
    }
    
    private fun setupToolbar() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }
    
    private fun setupClickListeners() {
        binding.etDueDate.setOnClickListener {
            showDatePicker()
        }
        
        binding.btnAttachFile.setOnClickListener {
            // Launch file picker for documents
            filePickerLauncher.launch("*/*")
        }
        
        binding.btnSaveAssignment.setOnClickListener {
            saveAssignment()
        }
        
        binding.cardStudentSubmissions.setOnClickListener {
            // Navigate to student submissions
            Toast.makeText(this, getString(R.string.view_student_submissions, 15, 8), Toast.LENGTH_SHORT).show()
        }
        
        binding.cardGradedAssignments.setOnClickListener {
            // Navigate to graded assignments
            Toast.makeText(this, "View Graded Assignments - 12 graded", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun setupRecyclerView() {
        assignmentsAdapter = SavedAssignmentsAdapter(savedAssignments)
        binding.rvSavedAssignments.layoutManager = LinearLayoutManager(this)
        binding.rvSavedAssignments.adapter = assignmentsAdapter
    }
    
    private fun loadSampleAssignments() {
        savedAssignments.addAll(listOf(
            SavedAssignment(
                title = "Data Structures Assignment 1",
                courseCode = "CS201",
                dueDate = "25/12/2024",
                status = AssignmentStatus.ACTIVE,
                attachmentName = "assignment1.pdf"
            ),
            SavedAssignment(
                title = "Programming Lab Exercise",
                courseCode = "CS101",
                dueDate = "20/12/2024",
                status = AssignmentStatus.DRAFT,
                attachmentName = "lab_exercise.docx"
            )
        ))
        assignmentsAdapter.notifyDataSetChanged()
    }
    
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = DateUtils.formatDate(
                    Calendar.getInstance().apply {
                        set(year, month, dayOfMonth)
                    }.time
                )
                binding.etDueDate.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
    
    private fun saveAssignment() {
        val title = binding.etAssignmentTitle.text.toString().trim()
        val description = binding.etAssignmentDescription.text.toString().trim()
        val dueDate = binding.etDueDate.text.toString().trim()
        
        when {
            title.isEmpty() -> {
                Toast.makeText(this, getString(R.string.please_enter_assignment_title), Toast.LENGTH_SHORT).show()
                return
            }
            description.isEmpty() -> {
                Toast.makeText(this, getString(R.string.please_enter_assignment_description), Toast.LENGTH_SHORT).show()
                return
            }
            dueDate.isEmpty() -> {
                Toast.makeText(this, getString(R.string.please_select_due_date), Toast.LENGTH_SHORT).show()
                return
            }
        }
        
        // Create new assignment
        val newAssignment = SavedAssignment(
            title = title,
            courseCode = "CS101", // Default course
            dueDate = dueDate,
            status = AssignmentStatus.DRAFT,
            attachmentName = attachedFileName
        )
        
        // Add to saved assignments
        savedAssignments.add(0, newAssignment) // Add at top
        assignmentsAdapter.notifyItemInserted(0)
        
        Toast.makeText(this, getString(R.string.assignment_created_successfully), Toast.LENGTH_SHORT).show()
        Log.d(TAG, "Assignment created: $title")
        
        // Clear form
        clearForm()
    }
    
    private fun clearForm() {
        binding.etAssignmentTitle.setText("")
        binding.etAssignmentDescription.setText("")
        binding.etDueDate.setText("")
        attachedFileUri = null
        attachedFileName = null
        binding.tvAttachedFile.isVisible = false
    }
    
    private fun getFileName(uri: Uri): String? {
        return try {
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val nameIndex = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                    if (nameIndex >= 0) it.getString(nameIndex) else getString(R.string.unknown_file)
                } else getString(R.string.unknown_file)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting file name", e)
            getString(R.string.unknown_file)
        }
    }
} 