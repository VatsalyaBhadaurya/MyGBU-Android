package com.vatty.mygbu

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import android.util.Log
import com.vatty.mygbu.data.model.StudentPerformance
import com.vatty.mygbu.data.repository.StudentRepository
import com.vatty.mygbu.utils.BottomNavigationHelper
import com.vatty.mygbu.viewmodel.StudentDashboardViewModel
import java.text.DecimalFormat
import androidx.lifecycle.ViewModelProvider

class StudentPerformanceActivity : AppCompatActivity() {
    
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var viewModel: StudentDashboardViewModel
    private lateinit var repository: StudentRepository
    
    // UI Components
    private lateinit var tvOverallCGPA: TextView
    private lateinit var tvAttendancePercentage: TextView
    private lateinit var tvTotalCourses: TextView
    private lateinit var tvAverageScore: TextView
    private lateinit var btnViewDetailedReport: MaterialCardView
    private lateinit var btnViewGrades: MaterialCardView
    private lateinit var btnViewAttendance: MaterialCardView
    private lateinit var btnViewAssignments: MaterialCardView
    private lateinit var btnViewExams: MaterialCardView
    private lateinit var btnFeedback: MaterialCardView
    private lateinit var btnCommunication: MaterialCardView
    
    companion object {
        private const val TAG = "StudentPerformanceActivity"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_performance)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        // Log activity startup
        Log.i(TAG, "StudentPerformanceActivity started - student analytics and performance tracking active")
        
        // Initialize ViewModel and Repository
        viewModel = ViewModelProvider(this)[StudentDashboardViewModel::class.java]
        repository = StudentRepository()
        
        setupToolbar()
        initializeViews()
        setupClickListeners()
        setupBottomNavigation()
        setupBackPressedHandler()
        loadPerformanceData()
    }
    
    private fun setupToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Student Performance"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }
    
    private fun setupBackPressedHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
    }
    
    private fun initializeViews() {
        bottomNavigation = findViewById(R.id.bottom_navigation)
        tvOverallCGPA = findViewById(R.id.tv_overall_cgpa)
        tvAttendancePercentage = findViewById(R.id.tv_attendance_percentage)
        tvTotalCourses = findViewById(R.id.tv_total_courses)
        tvAverageScore = findViewById(R.id.tv_average_score)
        btnViewDetailedReport = findViewById(R.id.btn_view_detailed_report)
        btnViewGrades = findViewById(R.id.btn_view_grades)
        btnViewAttendance = findViewById(R.id.btn_view_attendance)
        btnViewAssignments = findViewById(R.id.btn_view_assignments)
        btnViewExams = findViewById(R.id.btn_view_exams)
        btnFeedback = findViewById(R.id.btn_feedback)
        btnCommunication = findViewById(R.id.btn_communication)
    }
    
    private fun setupClickListeners() {
        btnViewDetailedReport.setOnClickListener {
            showDetailedReport()
        }
        
        btnViewGrades.setOnClickListener {
            showGradesReport()
        }
        
        btnViewAttendance.setOnClickListener {
            showAttendanceReport()
        }
        
        btnViewAssignments.setOnClickListener {
            showAssignmentsReport()
        }
        
        btnViewExams.setOnClickListener {
            showExamsReport()
        }
        
        btnFeedback.setOnClickListener {
            showFeedbackSystem()
        }
        
        btnCommunication.setOnClickListener {
            showCommunicationTools()
        }
    }
    
    private fun setupBottomNavigation() {
        BottomNavigationHelper.setupBottomNavigation(this, bottomNavigation, StudentPerformanceActivity::class.java)
    }
    
    private fun loadPerformanceData() {
        try {
            val performance = repository.getStudentPerformance("STU001")
            updatePerformanceDisplay(performance)
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to load performance data", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun updatePerformanceDisplay(performance: List<StudentPerformance>) {
        if (performance.isNotEmpty()) {
            val totalCourses = performance.size
            val overallCGPA = performance.map { it.totalScore / 10.0 }.average()
            val averageAttendance = performance.map { it.attendancePercentage }.average()
            val averageScore = performance.map { it.totalScore }.average()
            
            val df = DecimalFormat("#.##")
            
            tvOverallCGPA.text = df.format(overallCGPA)
            tvAttendancePercentage.text = "${df.format(averageAttendance)}%"
            tvTotalCourses.text = totalCourses.toString()
            tvAverageScore.text = df.format(averageScore)
        }
    }
    
    private fun showDetailedReport() {
        val performance = repository.getStudentPerformance("STU001")
        if (performance.isNotEmpty()) {
            val reportText = performance.joinToString("\n\n") { perf ->
                val gradeIcon = when (perf.grade) {
                    "A+" -> "🏆"
                    "A" -> "🥇"
                    "A-" -> "🥈"
                    "B+" -> "🥉"
                    "B" -> "📊"
                    else -> "📈"
                }
                
                """
                $gradeIcon ${perf.courseName}
                📊 Attendance: ${perf.attendancePercentage}%
                📝 Assignment Score: ${perf.assignmentScore}%
                📋 Exam Score: ${perf.examScore}%
                🎯 Total Score: ${perf.totalScore}%
                🏅 Grade: ${perf.grade}
                """.trimIndent()
            }

            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("📊 Detailed Performance Report")
                .setMessage(reportText)
                .setPositiveButton("OK", null)
                .show()
        } else {
            Toast.makeText(this, "No performance data available", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun showGradesReport() {
        val performance = repository.getStudentPerformance("STU001")
        if (performance.isNotEmpty()) {
            val gradesReport = """
                Grades Summary:
                
                ${performance.joinToString("\n") { perf ->
                    "${perf.courseName}: ${perf.grade} (${perf.totalScore})"
                }}
                
                Grade Distribution:
                A: ${performance.count { it.grade == "A" }}
                A-: ${performance.count { it.grade == "A-" }}
                B+: ${performance.count { it.grade == "B+" }}
                B: ${performance.count { it.grade == "B" }}
                B-: ${performance.count { it.grade == "B-" }}
                C+: ${performance.count { it.grade == "C+" }}
                C: ${performance.count { it.grade == "C" }}
            """.trimIndent()
            
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Grades Report")
                .setMessage(gradesReport)
                .setPositiveButton("OK", null)
                .show()
        } else {
            Toast.makeText(this, "No grades data available", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun showAttendanceReport() {
        val performance = repository.getStudentPerformance("STU001")
        if (performance.isNotEmpty()) {
            val attendanceReport = """
                Attendance Summary:
                
                ${performance.joinToString("\n") { perf ->
                    "${perf.courseName}: ${perf.attendancePercentage}%"
                }}
                
                Overall Attendance: ${performance.map { it.attendancePercentage }.average().toInt()}%
                
                Attendance Status:
                Excellent (90%+): ${performance.count { it.attendancePercentage >= 90 }}
                Good (75-89%): ${performance.count { it.attendancePercentage in 75.0..89.0 }}
                Average (60-74%): ${performance.count { it.attendancePercentage in 60.0..74.0 }}
                Below Average (<60%): ${performance.count { it.attendancePercentage < 60 }}
            """.trimIndent()
            
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Attendance Report")
                .setMessage(attendanceReport)
                .setPositiveButton("OK", null)
                .show()
        } else {
            Toast.makeText(this, "No attendance data available", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun showAssignmentsReport() {
        val assignments = repository.getStudentAssignments("STU001")
        if (assignments.isNotEmpty()) {
            val submittedCount = assignments.count { it.status.name == "SUBMITTED" }
            val pendingCount = assignments.count { it.status.name == "PENDING" }
            val averageScore = assignments.filter { it.score != null }.map { it.score!! }.average()
            
            val assignmentsReport = """
                Assignments Summary:
                
                Total Assignments: ${assignments.size}
                Submitted: $submittedCount
                Pending: $pendingCount
                Average Score: ${if (averageScore.isNaN()) "N/A" else "%.2f".format(averageScore)}
                
                Recent Assignments:
                ${assignments.take(5).joinToString("\n") { assignment ->
                    "${assignment.title}: ${assignment.status} ${if (assignment.score != null) "(${assignment.score})" else ""}"
                }}
            """.trimIndent()
            
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Assignments Report")
                .setMessage(assignmentsReport)
                .setPositiveButton("OK", null)
                .show()
        } else {
            Toast.makeText(this, "No assignments data available", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun showExamsReport() {
        val exams = repository.getStudentExams("STU001")
        if (exams.isNotEmpty()) {
            val examsReport = """
                Exams Summary:
                
                Total Exams: ${exams.size}
                
                Upcoming Exams:
                ${exams.joinToString("\n") { exam ->
                    "${exam.courseName} - ${exam.examType}\nDate: ${exam.date}, Time: ${exam.time}\nHall: ${exam.hallNumber}, Seat: ${exam.seatNumber}"
                }}
            """.trimIndent()
            
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Exams Report")
                .setMessage(examsReport)
                .setPositiveButton("OK", null)
                .show()
        } else {
            Toast.makeText(this, "No exams data available", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun showFeedbackSystem() {
        val feedbackOptions = arrayOf(
            "Course Feedback",
            "Faculty Feedback", 
            "Infrastructure Feedback",
            "General Suggestions"
        )
        
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Feedback System")
            .setItems(feedbackOptions) { _, which ->
                val selectedOption = feedbackOptions[which]
                showFeedbackForm(selectedOption)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun showFeedbackForm(feedbackType: String) {
        val message = """
            Feedback Type: $feedbackType
            
            Your feedback is important to us. Please provide detailed feedback to help us improve.
            
            Features:
            - Anonymous feedback option
            - Course-specific feedback
            - Faculty evaluation
            - Infrastructure suggestions
            - General improvement ideas
            
            Your feedback will be reviewed by the administration and appropriate actions will be taken.
        """.trimIndent()
        
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Submit Feedback")
            .setMessage(message)
            .setPositiveButton("Submit Feedback") { _, _ ->
                Toast.makeText(this, "Feedback submitted successfully", Toast.LENGTH_LONG).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun showCommunicationTools() {
        val communicationOptions = arrayOf(
            "Contact Faculty",
            "Academic Advisor",
            "Department Head",
            "Student Support"
        )
        
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Communication Tools")
            .setItems(communicationOptions) { _, which ->
                val selectedOption = communicationOptions[which]
                showCommunicationDetails(selectedOption)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun showCommunicationDetails(option: String) {
        val details = when (option) {
            "Contact Faculty" -> """
                Faculty Contact Information:
                
                Dr. Amit Kumar (Data Structures)
                Email: amit.kumar@gbu.ac.in
                Phone: +91-9876543210
                
                Dr. Priya Sharma (Database Systems)
                Email: priya.sharma@gbu.ac.in
                Phone: +91-9876543211
                
                Dr. Rajesh Singh (Computer Networks)
                Email: rajesh.singh@gbu.ac.in
                Phone: +91-9876543212
            """.trimIndent()
            
            "Academic Advisor" -> """
                Academic Advisor:
                
                Dr. Meera Patel
                Email: meera.patel@gbu.ac.in
                Phone: +91-9876543213
                Office: Block A, Room 105
                Office Hours: Mon-Fri, 10:00 AM - 2:00 PM
            """.trimIndent()
            
            "Department Head" -> """
                Department Head:
                
                Prof. Dr. Sanjay Verma
                Email: sanjay.verma@gbu.ac.in
                Phone: +91-9876543214
                Office: Block A, Room 201
                Office Hours: Mon-Fri, 9:00 AM - 5:00 PM
            """.trimIndent()
            
            else -> """
                Student Support:
                
                General Inquiries: support@gbu.ac.in
                Technical Issues: tech@gbu.ac.in
                Emergency: +91-9876543215
                
                Office Hours: Mon-Sat, 8:00 AM - 6:00 PM
            """.trimIndent()
        }
        
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle(option)
            .setMessage(details)
            .setPositiveButton("Contact", null)
            .setNegativeButton("Close", null)
            .show()
    }
} 