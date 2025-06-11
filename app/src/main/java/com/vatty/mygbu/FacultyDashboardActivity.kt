package com.vatty.mygbu

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView
import com.google.android.material.bottomnavigation.BottomNavigationView
import de.hdodenhof.circleimageview.CircleImageView
import com.vatty.mygbu.utils.TelegramLogger
import java.text.SimpleDateFormat
import java.util.*

class FacultyDashboardActivity : AppCompatActivity() {
    
    private lateinit var tvFacultyName: TextView
    private lateinit var tvGreeting: TextView
    private lateinit var tvCurrentTime: TextView
    private lateinit var tvTodaysClasses: TextView
    private lateinit var tvPendingTasks: TextView
    private lateinit var tvTotalStudents: TextView
    private lateinit var tvNotificationBadge: TextView
    private lateinit var ivNotification: ImageView
    private lateinit var ivProfileMini: CircleImageView
    private lateinit var bottomNavigation: BottomNavigationView
    
    // Dynamic content containers
    private lateinit var quickStatsContainer: View
    private lateinit var upcomingClassCard: MaterialCardView
    private lateinit var urgentTasksCard: MaterialCardView
    
    private val timeHandler = Handler(Looper.getMainLooper())
    private val timeUpdateRunnable = object : Runnable {
        override fun run() {
            updateCurrentTime()
            timeHandler.postDelayed(this, 60000) // Update every minute
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_faculty_dashboard)
        
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
        
        // Test TelegramLogger (remove this after testing)
        testTelegramLogger()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        timeHandler.removeCallbacks(timeUpdateRunnable)
    }
    
    private fun initializeViews() {
        tvFacultyName = findViewById(R.id.tv_faculty_name)
        tvGreeting = findViewById(R.id.tv_greeting)
        tvCurrentTime = findViewById(R.id.tv_current_time)
        tvTodaysClasses = findViewById(R.id.tv_todays_classes)
        tvPendingTasks = findViewById(R.id.tv_pending_tasks) 
        tvTotalStudents = findViewById(R.id.tv_total_students)
        tvNotificationBadge = findViewById(R.id.tv_notification_badge)
        ivNotification = findViewById(R.id.iv_notification)
        ivProfileMini = findViewById(R.id.iv_profile_mini)
        bottomNavigation = findViewById(R.id.bottom_navigation)
        
        quickStatsContainer = findViewById(R.id.quick_stats_container)
        upcomingClassCard = findViewById(R.id.card_upcoming_class)
        urgentTasksCard = findViewById(R.id.card_urgent_tasks)
    }
    
    private fun loadDashboardData() {
        // Dynamic greeting based on time
        updateGreeting()
        
        // Faculty info
        tvFacultyName.text = "Dr. Gaurav Kumar"
        
        // Real-time stats
        updateDashboardStats()
        
        // Notification badge
        updateNotificationBadge(5) // Example: 5 unread notifications
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
    
    private fun updateNotificationBadge(count: Int) {
        if (count > 0) {
            tvNotificationBadge.visibility = View.VISIBLE
            tvNotificationBadge.text = if (count > 99) "99+" else count.toString()
        } else {
            tvNotificationBadge.visibility = View.GONE
        }
    }
    
    private fun setupDynamicContent() {
        // Setup upcoming class card
        setupUpcomingClassCard()
        
        // Setup urgent tasks card
        setupUrgentTasksCard()
    }
    
    private fun setupUpcomingClassCard() {
        upcomingClassCard.setOnClickListener {
            startActivity(Intent(this, ScheduleActivity::class.java))
        }
        
        // You can populate with real upcoming class data
        val nextClass = getNextClass()
        if (nextClass != null) {
            upcomingClassCard.visibility = View.VISIBLE
            // Update UI with next class info
        } else {
            upcomingClassCard.visibility = View.GONE
        }
    }
    
    private fun setupUrgentTasksCard() {
        urgentTasksCard.setOnClickListener {
            // Navigate to urgent tasks or quick actions
            startActivity(Intent(this, QuickActionsActivity::class.java))
        }
    }
    
    private fun getNextClass(): String? {
        // Mock data - in production, fetch from calendar/schedule
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        
        return when {
            hour < 9 -> "CS101 - 09:00 AM"
            hour < 11 -> "CS201 - 11:00 AM" 
            hour < 14 -> "CS301 - 02:00 PM"
            else -> null
        }
    }
    
    private fun setupHeaderActions() {
        ivNotification.setOnClickListener {
            startActivity(Intent(this, NotificationsActivity::class.java))
        }
        
        ivProfileMini.setOnClickListener {
            startActivity(Intent(this, FacultyHubActivity::class.java))
        }
    }
    
    private fun setupBottomNavigation() {
        bottomNavigation.selectedItemId = R.id.nav_home
        
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_quick_actions -> {
                    startActivity(Intent(this, QuickActionsActivity::class.java))
                    true
                }
                R.id.nav_analytics -> {
                    startActivity(Intent(this, AnalyticsActivity::class.java))
                    true
                }
                R.id.nav_messages -> {
                    startActivity(Intent(this, MessagesActivity::class.java))
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, FacultyHubActivity::class.java))
                    true
                }
                else -> false
            }
        }
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
    
    // Test function for TelegramLogger - Remove after testing
    private fun testTelegramLogger() {
        // Test basic logging
        TelegramLogger.log("Faculty Dashboard Activity started successfully!")
        
        // Test error logging
        try {
            // Simulate some operation that might fail
            val result = 10 / 1 // Change to 0 to test error logging
            TelegramLogger.logDebug("Division result: $result")
        } catch (e: Exception) {
            TelegramLogger.logError("Error in calculation", e)
        }
        
        // Test warning
        TelegramLogger.logWarning("This is a test warning message")
        
        // Test connection
        TelegramLogger.testConnection()
    }
} 