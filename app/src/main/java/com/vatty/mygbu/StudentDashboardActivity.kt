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
import com.vatty.mygbu.viewmodel.StudentDashboardViewModel

class StudentDashboardActivity : AppCompatActivity() {

    private lateinit var tvStudentName: TextView
    private lateinit var tvGreeting: TextView
    private lateinit var tvCurrentTime: TextView
    private lateinit var tvCurrentDate: TextView
    private lateinit var tvAttendancePercentage: TextView
    private lateinit var tvOverallCGPA: TextView
    private lateinit var tvPendingAssignments: TextView
    private lateinit var tvUpcomingExams: TextView
    private lateinit var bottomNavigation: BottomNavigationView

    // Dynamic content containers
    private lateinit var quickStatsContainer: View
    private lateinit var notificationBadge: View

    private val timeHandler = Handler(Looper.getMainLooper())
    private val timeUpdateRunnable = object : Runnable {
        override fun run() {
            updateCurrentTime()
            timeHandler.postDelayed(this, 60000) // Update every minute
        }
    }

    private val TAG = "StudentDashboardActivity"
    private lateinit var viewModel: StudentDashboardViewModel

    private lateinit var loadingIndicator: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_dashboard)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[StudentDashboardViewModel::class.java]

        // Setup observers
        setupObservers()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeViews()
        setupDashboardCards()
        setupHeaderActions()
        setupBottomNavigation()
        startTimeUpdates()
        setupDynamicContent()
        showNotificationBadgeAnimated()
    }

    override fun onDestroy() {
        super.onDestroy()
        timeHandler.removeCallbacks(timeUpdateRunnable)
    }

    private fun initializeViews() {
        loadingIndicator = findViewById(R.id.loadingIndicator)
        tvStudentName = findViewById(R.id.tv_student_name)
        tvGreeting = findViewById(R.id.tv_greeting)
        tvCurrentTime = findViewById(R.id.tv_current_time)
        tvCurrentDate = findViewById(R.id.tv_current_date)
        tvAttendancePercentage = findViewById(R.id.tv_attendance_percentage)
        tvOverallCGPA = findViewById(R.id.tv_overall_cgpa)
        tvPendingAssignments = findViewById(R.id.tv_pending_assignments)
        tvUpcomingExams = findViewById(R.id.tv_upcoming_exams)
        bottomNavigation = findViewById(R.id.bottom_navigation)
    }

    private fun setupObservers() {
        viewModel.studentProfile.observe(this) { student ->
            tvStudentName.text = student.name
        }

        viewModel.attendancePercentage.observe(this) { percentage ->
            tvAttendancePercentage.text = "${percentage}%"
        }

        viewModel.overallCGPA.observe(this) { cgpa ->
            tvOverallCGPA.text = cgpa.toString()
        }

        viewModel.pendingAssignments.observe(this) { count ->
            tvPendingAssignments.text = count.toString()
        }

        viewModel.upcomingExams.observe(this) { count ->
            tvUpcomingExams.text = count.toString()
        }

        viewModel.isLoading.observe(this) { isLoading ->
            loadingIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(this) { error ->
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupDashboardCards() {
        // Quick Actions Cards
        findViewById<MaterialCardView>(R.id.card_assignments).setOnClickListener {
            startActivity(Intent(this, StudentAssignmentsActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.card_attendance).setOnClickListener {
            startActivity(Intent(this, StudentAttendanceActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.card_exams).setOnClickListener {
            startActivity(Intent(this, StudentExamsActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.card_fees).setOnClickListener {
            startActivity(Intent(this, StudentFeesActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.card_profile).setOnClickListener {
            startActivity(Intent(this, StudentProfileActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.card_schedule).setOnClickListener {
            startActivity(Intent(this, StudentScheduleActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.card_placement).setOnClickListener {
            startActivity(Intent(this, StudentPlacementActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.card_hostel).setOnClickListener {
            startActivity(Intent(this, StudentHostelActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.card_grievance).setOnClickListener {
            startActivity(Intent(this, StudentGrievanceActivity::class.java))
        }
    }

    private fun setupHeaderActions() {
        // Profile image click
        findViewById<CircleImageView>(R.id.iv_profile).setOnClickListener {
            startActivity(Intent(this, StudentProfileActivity::class.java))
        }

        // Notifications
        findViewById<ImageView>(R.id.iv_notifications).setOnClickListener {
            startActivity(Intent(this, NotificationsActivity::class.java))
        }

        // Messages
        findViewById<ImageView>(R.id.iv_messages).setOnClickListener {
            startActivity(Intent(this, MessagesActivity::class.java))
        }
    }

    private fun setupBottomNavigation() {
        BottomNavigationHelper.setupBottomNavigation(this, bottomNavigation, StudentDashboardActivity::class.java)
    }

    private fun startTimeUpdates() {
        updateCurrentTime()
        timeHandler.postDelayed(timeUpdateRunnable, 60000)
    }

    private fun updateCurrentTime() {
        tvCurrentTime.text = viewModel.getCurrentTime()
        tvCurrentDate.text = viewModel.getCurrentDate()
        tvGreeting.text = viewModel.getGreeting()
    }

    private fun setupDynamicContent() {
        quickStatsContainer = findViewById(R.id.quick_stats_container)
        notificationBadge = findViewById(R.id.notification_badge)
    }

    private fun showNotificationBadgeAnimated() {
        // Show notification badge with animation
        notificationBadge.visibility = View.VISIBLE
        notificationBadge.alpha = 0f
        notificationBadge.animate()
            .alpha(1f)
            .setDuration(500)
            .start()
    }

    private fun loadDashboardData() {
        viewModel.refreshData()
    }
}