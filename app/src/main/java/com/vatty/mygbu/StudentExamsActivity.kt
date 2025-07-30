package com.vatty.mygbu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.vatty.mygbu.data.model.StudentExam
import com.vatty.mygbu.data.model.StudentExamResult
import com.vatty.mygbu.enums.ExamStatus
import com.vatty.mygbu.utils.BottomNavigationHelper
import com.vatty.mygbu.viewmodel.StudentDashboardViewModel

class StudentExamsActivity : AppCompatActivity() {
    
    private lateinit var viewModel: StudentDashboardViewModel
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var recyclerView: RecyclerView
    private lateinit var tabLayout: TabLayout
    private lateinit var btnDownloadHallTicket: MaterialButton
    private lateinit var btnDownloadGradeCard: MaterialButton
    
    private lateinit var examsAdapter: StudentExamsAdapter
    
    private var currentTab = 0 // 0: Upcoming, 1: Completed
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_exams)
        
        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[StudentDashboardViewModel::class.java]
        
        initializeViews()
        setupClickListeners()
        setupBottomNavigation()
        setupRecyclerView()
        setupTabLayout()
        setupObservers()
        
        // Load exams data
        viewModel.loadStudentDashboardData()
    }
    
    private fun initializeViews() {
        bottomNavigation = findViewById(R.id.bottom_navigation)
        recyclerView = findViewById(R.id.recycler_view)
        tabLayout = findViewById(R.id.tab_layout)
        btnDownloadHallTicket = findViewById(R.id.btn_download_hall_ticket)
        btnDownloadGradeCard = findViewById(R.id.btn_download_grade_card)
        
        // Back button
        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            onBackPressed()
        }
    }
    
    private fun setupClickListeners() {
        btnDownloadHallTicket.setOnClickListener {
            downloadHallTicket()
        }
        
        btnDownloadGradeCard.setOnClickListener {
            downloadGradeCard()
        }
    }
    
    private fun setupBottomNavigation() {
        BottomNavigationHelper.setupBottomNavigation(this, bottomNavigation, StudentExamsActivity::class.java)
    }
    
    private fun setupRecyclerView() {
        examsAdapter = StudentExamsAdapter { exam ->
            onExamClicked(exam)
        }
        
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@StudentExamsActivity)
            adapter = examsAdapter
        }
    }
    
    private fun setupTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("Upcoming Exams"))
        tabLayout.addTab(tabLayout.newTab().setText("Completed Exams"))
        
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                currentTab = tab?.position ?: 0
                loadExamsForTab(currentTab)
            }
            
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
    
    private fun setupObservers() {
        // Load initial data for upcoming exams
        loadExamsForTab(0)
    }
    
    private fun loadExamsForTab(tabIndex: Int) {
        val exams = when (tabIndex) {
            0 -> getUpcomingExams()
            1 -> getCompletedExams()
            else -> emptyList()
        }
        examsAdapter.submitList(exams)
    }
    
    private fun getUpcomingExams(): List<StudentExamResult> {
        return listOf(
            StudentExamResult(
                id = "EXAM_001",
                courseId = "CS101",
                courseName = "Data Structures",
                examType = "Mid Semester",
                date = "2024-02-20",
                time = "10:00 AM - 12:00 PM",
                venue = "Room 101, Block A",
                status = ExamStatus.UPCOMING,
                grade = null,
                marks = null,
                totalMarks = null
            ),
            StudentExamResult(
                id = "EXAM_002",
                courseId = "CS102",
                courseName = "Algorithms",
                examType = "Mid Semester",
                date = "2024-02-25",
                time = "02:00 PM - 04:00 PM",
                venue = "Room 102, Block A",
                status = ExamStatus.UPCOMING,
                grade = null,
                marks = null,
                totalMarks = null
            )
        )
    }
    
    private fun getCompletedExams(): List<StudentExamResult> {
        return listOf(
            StudentExamResult(
                id = "EXAM_003",
                courseId = "CS103",
                courseName = "Database Systems",
                examType = "End Semester",
                date = "2024-01-15",
                time = "10:00 AM - 12:00 PM",
                venue = "Room 103, Block A",
                status = ExamStatus.COMPLETED,
                grade = "A",
                marks = 85,
                totalMarks = 100
            ),
            StudentExamResult(
                id = "EXAM_004",
                courseId = "CS104",
                courseName = "Computer Networks",
                examType = "End Semester",
                date = "2024-01-20",
                time = "02:00 PM - 04:00 PM",
                venue = "Room 104, Block A",
                status = ExamStatus.COMPLETED,
                grade = "B+",
                marks = 78,
                totalMarks = 100
            )
        )
    }
    
    private fun onExamClicked(exam: StudentExamResult) {
        when (exam.status) {
            ExamStatus.UPCOMING -> {
                showUpcomingExamDetails(exam)
            }
            ExamStatus.COMPLETED -> {
                showCompletedExamDetails(exam)
            }
            else -> {
                showExamDetails(exam)
            }
        }
    }
    
    private fun showUpcomingExamDetails(exam: StudentExamResult) {
        AlertDialog.Builder(this)
            .setTitle("📝 ${exam.courseName} - ${exam.examType}")
            .setMessage("""
                📅 Date: ${exam.date}
                ⏰ Time: ${exam.time}
                🏢 Venue: ${exam.venue}
                📊 Status: ${exam.status}
                
                💡 Please download your hall ticket before the exam.
            """.trimIndent())
            .setPositiveButton("Download Hall Ticket") { _, _ ->
                downloadHallTicket(exam)
            }
            .setNegativeButton("Close", null)
            .show()
    }
    
    private fun showCompletedExamDetails(exam: StudentExamResult) {
        AlertDialog.Builder(this)
            .setTitle("📝 ${exam.courseName} - ${exam.examType}")
            .setMessage("""
                📅 Date: ${exam.date}
                ⏰ Time: ${exam.time}
                🏢 Venue: ${exam.venue}
                📊 Status: ${exam.status}
                🎯 Grade: ${exam.grade}
                📊 Marks: ${exam.marks}/${exam.totalMarks}
            """.trimIndent())
            .setPositiveButton("View Result", null)
            .setNegativeButton("Close", null)
            .show()
    }
    
    private fun showExamDetails(exam: StudentExamResult) {
        AlertDialog.Builder(this)
            .setTitle("📝 ${exam.courseName} - ${exam.examType}")
            .setMessage("""
                📅 Date: ${exam.date}
                ⏰ Time: ${exam.time}
                🏢 Venue: ${exam.venue}
                📊 Status: ${exam.status}
            """.trimIndent())
            .setPositiveButton("OK", null)
            .show()
    }
    
    private fun downloadHallTicket(exam: StudentExamResult? = null) {
        val examName = exam?.courseName ?: "Current Semester"
        
        // In a real app, this would download the actual hall ticket
        Toast.makeText(this, "Downloading hall ticket for $examName...", Toast.LENGTH_SHORT).show()
        
        // Simulate download
        AlertDialog.Builder(this)
            .setTitle("Hall Ticket Downloaded")
            .setMessage("Hall ticket for $examName has been downloaded successfully!")
            .setPositiveButton("OK", null)
            .show()
    }
    
    private fun downloadGradeCard(exam: StudentExamResult? = null) {
        val examName = exam?.courseName ?: "Current Semester"
        
        // In a real app, this would download the actual grade card
        Toast.makeText(this, "Downloading grade card for $examName...", Toast.LENGTH_SHORT).show()
        
        // Simulate download
        AlertDialog.Builder(this)
            .setTitle("Grade Card Downloaded")
            .setMessage("Grade card for $examName has been downloaded successfully!")
            .setPositiveButton("OK", null)
            .show()
    }
    
    override fun onBackPressed() {
        super.onBackPressed()
    }
}

// Adapter for exams list
class StudentExamsAdapter(
    private val onExamClick: (StudentExamResult) -> Unit
) : androidx.recyclerview.widget.ListAdapter<StudentExamResult, StudentExamsAdapter.ExamViewHolder>(
    ExamDiffCallback()
) {
    
    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ExamViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student_exam, parent, false)
        return ExamViewHolder(view, onExamClick)
    }
    
    override fun onBindViewHolder(holder: ExamViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class ExamViewHolder(
        itemView: View,
        private val onExamClick: (StudentExamResult) -> Unit
    ) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        
        private val tvSubject: TextView = itemView.findViewById(R.id.tv_subject)
        private val tvExamType: TextView = itemView.findViewById(R.id.tv_exam_type)
        private val tvDate: TextView = itemView.findViewById(R.id.tv_date)
        private val tvTime: TextView = itemView.findViewById(R.id.tv_time)
        private val tvVenue: TextView = itemView.findViewById(R.id.tv_venue)
        private val tvStatus: TextView = itemView.findViewById(R.id.tv_status)
        private val tvGrade: TextView = itemView.findViewById(R.id.tv_grade)
        private val cardView: com.google.android.material.card.MaterialCardView = itemView.findViewById(R.id.card_exam)
        
        fun bind(exam: StudentExamResult) {
            tvSubject.text = exam.courseName
            tvExamType.text = exam.examType
            tvDate.text = exam.date
            tvTime.text = exam.time
            tvVenue.text = exam.venue
            tvStatus.text = exam.status.toString()
            
            // Set status color based on status
            val colorRes = when (exam.status) {
                ExamStatus.UPCOMING -> R.color.warning_orange
                ExamStatus.COMPLETED -> R.color.success_green
                ExamStatus.ONGOING -> R.color.primary
                else -> R.color.text_secondary
            }
            tvStatus.setTextColor(android.content.res.ColorStateList.valueOf(
                itemView.context.getColor(colorRes)
            ))
            
            // Show grade if available
            if (exam.grade != null) {
                tvGrade.visibility = View.VISIBLE
                tvGrade.text = "Grade: ${exam.grade}"
            } else {
                tvGrade.visibility = View.GONE
            }
            
            cardView.setOnClickListener {
                onExamClick(exam)
            }
        }
    }
    
    class ExamDiffCallback : androidx.recyclerview.widget.DiffUtil.ItemCallback<StudentExamResult>() {
        override fun areItemsTheSame(oldItem: StudentExamResult, newItem: StudentExamResult): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: StudentExamResult, newItem: StudentExamResult): Boolean {
            return oldItem == newItem
        }
    }
}