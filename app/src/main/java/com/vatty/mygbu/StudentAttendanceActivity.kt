package com.vatty.mygbu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.vatty.mygbu.data.model.StudentAttendance
import com.vatty.mygbu.data.model.StudentSubjectAttendance
import com.vatty.mygbu.enums.AttendanceStatus
import com.vatty.mygbu.utils.BottomNavigationHelper
import com.vatty.mygbu.viewmodel.StudentDashboardViewModel

class StudentAttendanceActivity : AppCompatActivity() {
    
    private lateinit var viewModel: StudentDashboardViewModel
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvOverallAttendance: TextView
    private lateinit var btnApplyLeave: MaterialButton
    
    private lateinit var attendanceAdapter: StudentAttendanceAdapter
    
    // File picker for leave application
    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedFile ->
            handleFileSelection(selectedFile)
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_attendance)
        
        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[StudentDashboardViewModel::class.java]
        
        initializeViews()
        setupClickListeners()
        setupBottomNavigation()
        setupRecyclerView()
        setupObservers()
        
        // Load attendance data
        viewModel.loadStudentDashboardData()
    }
    
    private fun initializeViews() {
        bottomNavigation = findViewById(R.id.bottom_navigation)
        recyclerView = findViewById(R.id.recycler_view)
        tvOverallAttendance = findViewById(R.id.tv_overall_attendance)
        btnApplyLeave = findViewById(R.id.btn_apply_leave)
        
        // Back button
        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            onBackPressed()
        }
    }
    
    private fun setupClickListeners() {
        btnApplyLeave.setOnClickListener {
            showLeaveApplicationDialog()
        }
    }
    
    private fun setupBottomNavigation() {
        BottomNavigationHelper.setupBottomNavigation(this, bottomNavigation, StudentAttendanceActivity::class.java)
    }
    
    private fun setupRecyclerView() {
        attendanceAdapter = StudentAttendanceAdapter { attendance ->
            onAttendanceClicked(attendance)
        }
        
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@StudentAttendanceActivity)
            adapter = attendanceAdapter
        }
    }
    
    private fun setupObservers() {
        viewModel.attendancePercentage.observe(this) { percentage ->
            tvOverallAttendance.text = "${percentage}%"
        }
        
        // Mock attendance data - in real app this would come from ViewModel
        val mockAttendanceData = listOf(
            StudentSubjectAttendance("Data Structures", 85.5, 17, 20),
            StudentSubjectAttendance("Algorithms", 90.0, 18, 20),
            StudentSubjectAttendance("Database Systems", 80.0, 16, 20),
            StudentSubjectAttendance("Computer Networks", 88.0, 22, 25),
            StudentSubjectAttendance("Software Engineering", 92.0, 23, 25)
        )
        attendanceAdapter.submitList(mockAttendanceData)
    }
    
    private fun onAttendanceClicked(attendance: StudentSubjectAttendance) {
        showAttendanceDetailsDialog(attendance)
    }
    
    private fun showAttendanceDetailsDialog(attendance: StudentSubjectAttendance) {
        AlertDialog.Builder(this)
            .setTitle("${attendance.subject} Attendance")
            .setMessage("""
                Subject: ${attendance.subject}
                Attendance: ${attendance.percentage}%
                Classes Attended: ${attendance.classesAttended}
                Total Classes: ${attendance.totalClasses}
                
                Status: ${getAttendanceStatus(attendance.percentage)}
            """.trimIndent())
            .setPositiveButton("Apply Leave") { _, _ ->
                showLeaveApplicationDialog(attendance.subject)
            }
            .setNegativeButton("Close", null)
            .show()
    }
    
    private fun getAttendanceStatus(percentage: Double): String {
        return when {
            percentage >= 75.0 -> "Good"
            percentage >= 60.0 -> "Average"
            else -> "Poor"
        }
    }
    
    private fun showLeaveApplicationDialog(subject: String? = null) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_leave_application, null)
        
        val etReason = dialogView.findViewById<TextInputEditText>(R.id.et_reason)
        val etStartDate = dialogView.findViewById<TextInputEditText>(R.id.et_start_date)
        val etEndDate = dialogView.findViewById<TextInputEditText>(R.id.et_end_date)
        val etDays = dialogView.findViewById<TextInputEditText>(R.id.et_days)
        
        AlertDialog.Builder(this)
            .setTitle("Apply for Leave")
            .setView(dialogView)
            .setPositiveButton("Submit") { _, _ ->
                val reason = etReason.text.toString().trim()
                val startDate = etStartDate.text.toString().trim()
                val endDate = etEndDate.text.toString().trim()
                val days = etDays.text.toString().trim()
                
                if (reason.isEmpty() || startDate.isEmpty() || endDate.isEmpty() || days.isEmpty()) {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                
                // Show file picker for supporting documents
                showFilePickerDialog(reason, startDate, endDate, days, subject)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun showFilePickerDialog(reason: String, startDate: String, endDate: String, days: String, subject: String?) {
        AlertDialog.Builder(this)
            .setTitle("Supporting Documents")
            .setMessage("Would you like to attach supporting documents (medical certificate, etc.)?")
            .setPositiveButton("Yes") { _, _ ->
                filePickerLauncher.launch("*/*")
            }
            .setNegativeButton("No") { _, _ ->
                submitLeaveApplication(reason, startDate, endDate, days, subject, null)
            }
            .show()
    }
    
    private fun handleFileSelection(uri: Uri) {
        Toast.makeText(this, "File selected: ${uri.lastPathSegment}", Toast.LENGTH_SHORT).show()
        // In a real app, you would store the file URI and submit it with the leave application
        submitLeaveApplication("", "", "", "", null, uri)
    }
    
    private fun submitLeaveApplication(reason: String, startDate: String, endDate: String, days: String, subject: String?, fileUri: Uri?) {
        // In a real app, this would submit to the server
        AlertDialog.Builder(this)
            .setTitle("Leave Application Submitted")
            .setMessage("""
                Your leave application has been submitted successfully!
                
                Reason: $reason
                From: $startDate
                To: $endDate
                Days: $days
                ${if (subject != null) "Subject: $subject" else ""}
                
                You will be notified once it's approved/rejected.
            """.trimIndent())
            .setPositiveButton("OK", null)
            .show()
    }
    
    override fun onBackPressed() {
        super.onBackPressed()
    }
}

// Adapter for attendance list
class StudentAttendanceAdapter(
    private val onAttendanceClick: (StudentSubjectAttendance) -> Unit
) : androidx.recyclerview.widget.ListAdapter<StudentSubjectAttendance, StudentAttendanceAdapter.AttendanceViewHolder>(
    AttendanceDiffCallback()
) {
    
    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): AttendanceViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student_attendance, parent, false)
        return AttendanceViewHolder(view, onAttendanceClick)
    }
    
    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class AttendanceViewHolder(
        itemView: View,
        private val onAttendanceClick: (StudentSubjectAttendance) -> Unit
    ) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        
        private val tvStudentName: TextView = itemView.findViewById(R.id.tv_student_name)
        private val tvRollNumber: TextView = itemView.findViewById(R.id.tv_roll_number)
        private val tvAttendancePercentage: TextView = itemView.findViewById(R.id.tv_attendance_percentage)
        private val tvCgpa: TextView = itemView.findViewById(R.id.tv_cgpa)
        private val tvClassesAttended: TextView = itemView.findViewById(R.id.tv_classes_attended)
        private val progressBar: android.widget.ProgressBar = itemView.findViewById(R.id.progress_bar)
        private val cardView: com.google.android.material.card.MaterialCardView = itemView.findViewById(R.id.card_attendance)
        
        fun bind(attendance: StudentSubjectAttendance) {
            // For student attendance view, we'll use the subject as student name and show attendance details
            tvStudentName.text = attendance.subject
            tvRollNumber.text = "Subject: ${attendance.subject}"
            tvAttendancePercentage.text = "${attendance.percentage}%"
            tvCgpa.text = "N/A" // CGPA not available for subject attendance
            tvClassesAttended.text = "${attendance.classesAttended}/${attendance.totalClasses} classes"
            
            // Set progress bar
            progressBar.max = 100
            progressBar.progress = attendance.percentage.toInt()
            
            // Set progress bar color based on attendance
            val colorRes = when {
                attendance.percentage >= 75.0 -> android.R.color.holo_green_light
                attendance.percentage >= 60.0 -> android.R.color.holo_orange_light
                else -> android.R.color.holo_red_light
            }
            progressBar.progressTintList = android.content.res.ColorStateList.valueOf(
                itemView.context.getColor(colorRes)
            )
            
            cardView.setOnClickListener {
                onAttendanceClick(attendance)
            }
        }
    }
    
    class AttendanceDiffCallback : androidx.recyclerview.widget.DiffUtil.ItemCallback<StudentSubjectAttendance>() {
        override fun areItemsTheSame(oldItem: StudentSubjectAttendance, newItem: StudentSubjectAttendance): Boolean {
            return oldItem.subject == newItem.subject
        }
        
        override fun areContentsTheSame(oldItem: StudentSubjectAttendance, newItem: StudentSubjectAttendance): Boolean {
            return oldItem == newItem
        }
    }
}