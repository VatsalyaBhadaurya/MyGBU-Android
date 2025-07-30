package com.vatty.mygbu

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.tabs.TabLayout
import com.vatty.mygbu.data.model.StudentExam
import com.vatty.mygbu.data.model.StudentTimetableItem
import com.vatty.mygbu.data.repository.StudentRepository
import com.vatty.mygbu.utils.BottomNavigationHelper
import com.vatty.mygbu.viewmodel.StudentDashboardViewModel
import java.text.SimpleDateFormat
import java.util.*

class StudentScheduleActivity : AppCompatActivity() {

    private lateinit var viewModel: StudentDashboardViewModel
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var repository: StudentRepository
    
    // UI Components
    private lateinit var tabLayout: TabLayout
    private lateinit var rvTimetable: RecyclerView
    private lateinit var rvExams: RecyclerView
    private lateinit var tvCurrentDay: TextView
    private lateinit var tvCurrentTime: TextView
    private lateinit var btnToday: MaterialCardView
    private lateinit var btnTomorrow: MaterialCardView
    private lateinit var btnWeek: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_schedule)

        // Initialize ViewModel and Repository
        viewModel = ViewModelProvider(this)[StudentDashboardViewModel::class.java]
        repository = StudentRepository()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeViews()
        setupBottomNavigation()
        setupTabLayout()
        setupClickListeners()
        loadScheduleData()
        updateCurrentTime()
    }

    private fun initializeViews() {
        bottomNavigation = findViewById(R.id.bottom_navigation)
        tabLayout = findViewById(R.id.tab_layout)
        rvTimetable = findViewById(R.id.rv_timetable)
        rvExams = findViewById(R.id.rv_exams)
        tvCurrentDay = findViewById(R.id.tv_current_day)
        tvCurrentTime = findViewById(R.id.tv_current_time)
        btnToday = findViewById(R.id.btn_today)
        btnTomorrow = findViewById(R.id.btn_tomorrow)
        btnWeek = findViewById(R.id.btn_week)
        
        // Back button
        findViewById<View>(R.id.iv_back).setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupBottomNavigation() {
        BottomNavigationHelper.setupBottomNavigation(this, bottomNavigation, StudentScheduleActivity::class.java)
    }

    private fun setupTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("Timetable"))
        tabLayout.addTab(tabLayout.newTab().setText("Exams"))
        
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> showTimetable()
                    1 -> showExams()
                }
            }
            
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupClickListeners() {
        btnToday.setOnClickListener {
            showTodaySchedule()
        }
        
        btnTomorrow.setOnClickListener {
            showTomorrowSchedule()
        }
        
        btnWeek.setOnClickListener {
            showWeekSchedule()
        }
    }

    private fun loadScheduleData() {
        try {
            // Load timetable data
            val timetable = getMockTimetable()
            displayTimetable(timetable)
            
            // Load exam data
            val exams = repository.getStudentExams("STU001")
            displayExams(exams)
            
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to load schedule data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getMockTimetable(): List<StudentTimetableItem> {
        return listOf(
            StudentTimetableItem(
                id = "TT001",
                day = "Monday",
                time = "09:00 - 10:00",
                subject = "Data Structures",
                teacher = "Dr. Amit Kumar",
                room = "A-101",
                type = "Lecture"
            ),
            StudentTimetableItem(
                id = "TT002",
                day = "Monday",
                time = "10:00 - 11:00",
                subject = "Database Systems",
                teacher = "Dr. Priya Sharma",
                room = "B-205",
                type = "Lecture"
            ),
            StudentTimetableItem(
                id = "TT003",
                day = "Monday",
                time = "11:15 - 12:15",
                subject = "Computer Networks",
                teacher = "Dr. Rajesh Singh",
                room = "C-301",
                type = "Lecture"
            ),
            StudentTimetableItem(
                id = "TT004",
                day = "Monday",
                time = "14:00 - 16:00",
                subject = "Data Structures Lab",
                teacher = "Dr. Amit Kumar",
                room = "Lab-1",
                type = "Practical"
            ),
            StudentTimetableItem(
                id = "TT005",
                day = "Tuesday",
                time = "09:00 - 10:00",
                subject = "Operating Systems",
                teacher = "Dr. Meera Patel",
                room = "A-102",
                type = "Lecture"
            ),
            StudentTimetableItem(
                id = "TT006",
                day = "Tuesday",
                time = "10:00 - 11:00",
                subject = "Software Engineering",
                teacher = "Dr. Sanjay Verma",
                room = "B-206",
                type = "Lecture"
            ),
            StudentTimetableItem(
                id = "TT007",
                day = "Tuesday",
                time = "11:15 - 12:15",
                subject = "Web Technologies",
                teacher = "Dr. Anjali Gupta",
                room = "C-302",
                type = "Lecture"
            ),
            StudentTimetableItem(
                id = "TT008",
                day = "Tuesday",
                time = "14:00 - 16:00",
                subject = "Database Lab",
                teacher = "Dr. Priya Sharma",
                room = "Lab-2",
                type = "Practical"
            ),
            StudentTimetableItem(
                id = "TT009",
                day = "Wednesday",
                time = "09:00 - 10:00",
                subject = "Data Structures",
                teacher = "Dr. Amit Kumar",
                room = "A-101",
                type = "Lecture"
            ),
            StudentTimetableItem(
                id = "TT010",
                day = "Wednesday",
                time = "10:00 - 11:00",
                subject = "Database Systems",
                teacher = "Dr. Priya Sharma",
                room = "B-205",
                type = "Lecture"
            ),
            StudentTimetableItem(
                id = "TT011",
                day = "Wednesday",
                time = "11:15 - 12:15",
                subject = "Computer Networks",
                teacher = "Dr. Rajesh Singh",
                room = "C-301",
                type = "Lecture"
            ),
            StudentTimetableItem(
                id = "TT012",
                day = "Wednesday",
                time = "14:00 - 16:00",
                subject = "Networks Lab",
                teacher = "Dr. Rajesh Singh",
                room = "Lab-3",
                type = "Practical"
            ),
            StudentTimetableItem(
                id = "TT013",
                day = "Thursday",
                time = "09:00 - 10:00",
                subject = "Operating Systems",
                teacher = "Dr. Meera Patel",
                room = "A-102",
                type = "Lecture"
            ),
            StudentTimetableItem(
                id = "TT014",
                day = "Thursday",
                time = "10:00 - 11:00",
                subject = "Software Engineering",
                teacher = "Dr. Sanjay Verma",
                room = "B-206",
                type = "Lecture"
            ),
            StudentTimetableItem(
                id = "TT015",
                day = "Thursday",
                time = "11:15 - 12:15",
                subject = "Web Technologies",
                teacher = "Dr. Anjali Gupta",
                room = "C-302",
                type = "Lecture"
            ),
            StudentTimetableItem(
                id = "TT016",
                day = "Thursday",
                time = "14:00 - 16:00",
                subject = "Web Development Lab",
                teacher = "Dr. Anjali Gupta",
                room = "Lab-1",
                type = "Practical"
            ),
            StudentTimetableItem(
                id = "TT017",
                day = "Friday",
                time = "09:00 - 10:00",
                subject = "Data Structures",
                teacher = "Dr. Amit Kumar",
                room = "A-101",
                type = "Lecture"
            ),
            StudentTimetableItem(
                id = "TT018",
                day = "Friday",
                time = "10:00 - 11:00",
                subject = "Database Systems",
                teacher = "Dr. Priya Sharma",
                room = "B-205",
                type = "Lecture"
            ),
            StudentTimetableItem(
                id = "TT019",
                day = "Friday",
                time = "11:15 - 12:15",
                subject = "Computer Networks",
                teacher = "Dr. Rajesh Singh",
                room = "C-301",
                type = "Lecture"
            ),
            StudentTimetableItem(
                id = "TT020",
                day = "Friday",
                time = "14:00 - 16:00",
                subject = "Project Work",
                teacher = "Dr. Sanjay Verma",
                room = "Lab-2",
                type = "Project"
            )
        )
    }

    private fun displayTimetable(timetable: List<StudentTimetableItem>) {
        // For now, we'll show in a dialog
        // In a real app, you would use a RecyclerView adapter
        val timetableText = timetable.joinToString("\n\n") { item ->
            val typeIcon = when (item.type) {
                "Lecture" -> "📚"
                "Practical" -> "🔬"
                "Project" -> "💻"
                else -> "📖"
            }
            
            """
            $typeIcon ${item.subject}
            ⏰ Time: ${item.time}
            👨‍🏫 Teacher: ${item.teacher}
            🏢 Room: ${item.room}
            📋 Type: ${item.type}
            """.trimIndent()
        }

        // Show in a simple dialog for now
        if (tabLayout.selectedTabPosition == 0) {
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("📅 Today's Timetable")
                .setMessage(timetableText)
                .setPositiveButton("OK", null)
                .show()
        }
    }

    private fun displayExams(exams: List<StudentExam>) {
        if (exams.isNotEmpty()) {
            val examText = exams.joinToString("\n\n") { exam ->
                """
                📝 ${exam.courseName}
                🏷️ Type: ${exam.examType}
                📅 Date: ${exam.date}
                ⏰ Time: ${exam.time}
                ⏱️ Duration: ${exam.duration}
                🏢 Hall: ${exam.hallNumber}
                🪑 Seat: ${exam.seatNumber}
                """.trimIndent()
            }

            if (tabLayout.selectedTabPosition == 1) {
                androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("📋 Upcoming Exams")
                    .setMessage(examText)
                    .setPositiveButton("OK", null)
                    .show()
            }
        } else {
            if (tabLayout.selectedTabPosition == 1) {
                Toast.makeText(this, "No upcoming exams", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showTimetable() {
        val timetable = getMockTimetable()
        displayTimetable(timetable)
    }

    private fun showExams() {
        val exams = repository.getStudentExams("STU001")
        displayExams(exams)
    }

    private fun showTodaySchedule() {
        val today = SimpleDateFormat("EEEE", Locale.getDefault()).format(Date())
        tvCurrentDay.text = today
        
        val timetable = getMockTimetable().filter { it.day.equals(today, ignoreCase = true) }
        if (timetable.isNotEmpty()) {
            displayTimetable(timetable)
        } else {
            Toast.makeText(this, "No classes scheduled for today", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showTomorrowSchedule() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val tomorrow = SimpleDateFormat("EEEE", Locale.getDefault()).format(calendar.time)
        tvCurrentDay.text = tomorrow
        
        val timetable = getMockTimetable().filter { it.day.equals(tomorrow, ignoreCase = true) }
        if (timetable.isNotEmpty()) {
            displayTimetable(timetable)
        } else {
            Toast.makeText(this, "No classes scheduled for tomorrow", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showWeekSchedule() {
        val weekSchedule = """
            Weekly Schedule:
            
            Monday:
            - 09:00-10:00: Data Structures (A-101)
            - 10:00-11:00: Database Systems (B-205)
            - 11:15-12:15: Computer Networks (C-301)
            - 14:00-16:00: Data Structures Lab (Lab-1)
            
            Tuesday:
            - 09:00-10:00: Operating Systems (A-102)
            - 10:00-11:00: Software Engineering (B-206)
            - 11:15-12:15: Web Technologies (C-302)
            - 14:00-16:00: Database Lab (Lab-2)
            
            Wednesday:
            - 09:00-10:00: Data Structures (A-101)
            - 10:00-11:00: Database Systems (B-205)
            - 11:15-12:15: Computer Networks (C-301)
            - 14:00-16:00: Networks Lab (Lab-3)
            
            Thursday:
            - 09:00-10:00: Operating Systems (A-102)
            - 10:00-11:00: Software Engineering (B-206)
            - 11:15-12:15: Web Technologies (C-302)
            - 14:00-16:00: Web Development Lab (Lab-1)
            
            Friday:
            - 09:00-10:00: Data Structures (A-101)
            - 10:00-11:00: Database Systems (B-205)
            - 11:15-12:15: Computer Networks (C-301)
            - 14:00-16:00: Project Work (Lab-2)
        """.trimIndent()
        
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Weekly Schedule")
            .setMessage(weekSchedule)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun updateCurrentTime() {
        val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        tvCurrentTime.text = currentTime
    }
}