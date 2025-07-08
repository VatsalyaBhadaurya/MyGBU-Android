package com.vatty.mygbu

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView
import com.google.android.material.bottomnavigation.BottomNavigationView
import de.hdodenhof.circleimageview.CircleImageView


import java.text.SimpleDateFormat
import java.util.*
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import android.widget.ProgressBar
import com.vatty.mygbu.utils.BottomNavigationHelper
import com.vatty.mygbu.viewmodel.FacultyDashboardViewModel

class FacultyDashboardActivity : AppCompatActivity() {

    private lateinit var tvFacultyName: TextView
    private lateinit var tvGreeting: TextView
    private lateinit var tvCurrentTime: TextView
    private lateinit var tvTodaysClasses: TextView
    private lateinit var tvPendingTasks: TextView
    private lateinit var tvTotalStudents: TextView
    private lateinit var bottomNavigation: BottomNavigationView

    // Dynamic content containers
    private lateinit var quickStatsContainer: View

    private val timeHandler = Handler(Looper.getMainLooper())
    private val timeUpdateRunnable = object : Runnable {
        override fun run() {
            updateCurrentTime()
            timeHandler.postDelayed(this, 60000) // Update every minute
        }
    }

    private val TAG = "FacultyDashboardActivity"
    private lateinit var viewModel: FacultyDashboardViewModel

    private lateinit var loadingIndicator: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_faculty_dashboard)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[FacultyDashboardViewModel::class.java]

        // Setup observers
        setupObservers()

        // Load faculty data
        viewModel.loadFacultyList()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeViews()
        setupDashboardCards()
        setupHeaderActions()
        setupBottomNavigation()
        loadDashboardData()
        startTimeUpdates()
        setupDynamicContent()


    }

    override fun onDestroy() {
        super.onDestroy()
        timeHandler.removeCallbacks(timeUpdateRunnable)
    }

    private fun initializeViews() {
        loadingIndicator = findViewById(R.id.loadingIndicator)
        tvFacultyName = findViewById(R.id.tv_faculty_name)
        tvGreeting = findViewById(R.id.tv_greeting)
        tvCurrentTime = findViewById(R.id.tv_current_time)
        tvTodaysClasses = findViewById(R.id.tv_todays_classes)
        tvPendingTasks = findViewById(R.id.tv_pending_tasks)
        tvTotalStudents = findViewById(R.id.tv_total_students)
        bottomNavigation = findViewById(R.id.bottom_navigation)

        quickStatsContainer = findViewById(R.id.quick_stats_container)
    }

    private fun loadDashboardData() {
        // Dynamic greeting based on time
        updateGreeting()

        // Real-time stats - these could be updated from a different API endpoint
        updateDashboardStats()
    }

    private fun updateGreeting() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        val greeting = when (hour) {
            in 5..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            in 17..20 -> "Good Evening"
            else -> "Good Night"
        }

        tvGreeting.text = greeting
    }

    private fun updateCurrentTime() {
        val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        val currentDate = SimpleDateFormat("EEEE, MMM dd", Locale.getDefault()).format(Date())
        tvCurrentTime.text = "$currentTime • $currentDate"
    }

    private fun startTimeUpdates() {
        updateCurrentTime()
        timeHandler.post(timeUpdateRunnable)
    }

    private fun updateDashboardStats() {
        // Simulate real-time data updates
        tvTodaysClasses.text = "3"
        tvPendingTasks.text = "7"
        tvTotalStudents.text = "128"

        // Animate stats update (you can add CountUp animation here)
        animateStatsUpdate()
    }

    private fun animateStatsUpdate() {
        // Simple fade animation for stats
        quickStatsContainer.alpha = 0f
        quickStatsContainer.animate()
            .alpha(1f)
            .setDuration(500)
            .start()
    }

    private fun setupDynamicContent() {
        // Update dynamic content setup
        updateDashboardStats()
    }

    private fun setupHeaderActions() {
        // Setup notification icon click
        findViewById<ImageView>(R.id.iv_notifications).setOnClickListener {
            startActivity(Intent(this, NotificationsActivity::class.java))
        }

        // Setup "View All Messages" button click
        findViewById<LinearLayout>(R.id.btn_view_all_messages).setOnClickListener {
            startActivity(Intent(this, MessagesActivity::class.java))
        }
    }

    private fun setupBottomNavigation() {
        BottomNavigationHelper.setupBottomNavigation(this, bottomNavigation, FacultyDashboardActivity::class.java)
    }

    private fun setupDashboardCards() {
        // Primary Actions (Large Cards)
        findViewById<MaterialCardView>(R.id.card_courses).setOnClickListener {
            startActivity(Intent(this, CoursesActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.card_grades).setOnClickListener {
            startActivity(Intent(this, AssignmentManagementActivity::class.java))
        }

        // Secondary Actions
        findViewById<MaterialCardView>(R.id.card_attendance).setOnClickListener {
            startActivity(Intent(this, AttendanceActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.card_students).setOnClickListener {
            startActivity(Intent(this, StudentPerformanceActivity::class.java))
        }

        // Additional Features (Compact Cards)
        findViewById<MaterialCardView>(R.id.card_schedule).setOnClickListener {
            startActivity(Intent(this, ScheduleActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.card_reports).setOnClickListener {
            startActivity(Intent(this, LeaveRequestsActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.card_announcements).setOnClickListener {
            startActivity(Intent(this, MessagesActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.card_profile).setOnClickListener {
            startActivity(Intent(this, FacultyHubActivity::class.java))
        }
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(this) { isLoading ->
            loadingIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { error ->
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.facultyList.observe(this) { facultyList ->
            if (facultyList.isNotEmpty()) {
                val faculty = facultyList[0] // Get the first faculty member
                updateFacultyInfo(faculty)
            }
        }
    }

    private fun updateFacultyInfo(faculty: com.vatty.mygbu.data.model.Faculty) {
        // Update the faculty info card
        // Update the header section
        tvFacultyName.text = faculty.name
    }


}
