package com.vatty.mygbu

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.vatty.mygbu.utils.TelegramLoggerTest
import com.vatty.mygbu.utils.SystemErrorMonitor
import com.vatty.mygbu.utils.ComprehensiveLogMonitor
import java.text.SimpleDateFormat
import java.util.*
import com.vatty.mygbu.utils.LogWrapper as Log
import androidx.lifecycle.ViewModelProvider
import android.widget.ProgressBar
import com.vatty.mygbu.viewmodel.FacultyDashboardViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vatty.mygbu.adapter.RecentMessagesAdapter
import com.vatty.mygbu.data.model.Message

class FacultyDashboardActivity : AppCompatActivity() {
    
    // Message data class
    data class Message(
        val id: String,
        val senderId: String,
        val senderName: String,
        val content: String,
        val timestamp: Long,
        val isRead: Boolean = false
    )

    // Recent Messages Adapter
    private inner class RecentMessagesAdapter(
        private var messages: List<Message> = emptyList(),
        private val onMessageClick: (Message) -> Unit
    ) : RecyclerView.Adapter<RecentMessagesAdapter.MessageViewHolder>() {

        inner class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val senderAvatar: ImageView = view.findViewById(R.id.ivSenderAvatar)
            val senderName: TextView = view.findViewById(R.id.tvSenderName)
            val messagePreview: TextView = view.findViewById(R.id.tvMessagePreview)
            val messageTime: TextView = view.findViewById(R.id.tvMessageTime)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_recent_message, parent, false)
            return MessageViewHolder(view)
        }

        override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
            val message = messages[position]
            holder.senderName.text = message.senderName
            holder.messagePreview.text = message.content
            holder.messageTime.text = formatTime(message.timestamp)
            
            holder.itemView.setOnClickListener { onMessageClick(message) }
        }

        override fun getItemCount() = messages.size

        fun updateMessages(newMessages: List<Message>) {
            messages = newMessages
            notifyDataSetChanged()
        }

        private fun formatTime(timestamp: Long): String {
            val sdf = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
            return sdf.format(timestamp)
        }
    }
    
    // View references
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var facultyName: TextView
    private lateinit var facultyDesignation: TextView
    private lateinit var facultyJoiningDate: TextView
    private lateinit var tvGreeting: TextView
    private lateinit var tvCurrentTime: TextView
    private lateinit var tvTodaysClasses: TextView
    private lateinit var tvPendingTasks: TextView
    private lateinit var tvTotalStudents: TextView
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var rvRecentMessages: RecyclerView
    private lateinit var tvNoMessages: TextView
    private lateinit var recentMessagesAdapter: RecentMessagesAdapter
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
        
        // Handle window insets
        val rootView = findViewById<View>(android.R.id.content)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
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

        // Test TelegramLogger with improved timeout handling - just call the test function
        testImprovedTelegramLogger()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        timeHandler.removeCallbacks(timeUpdateRunnable)
    }
    
    private fun initializeViews() {
        // Initialize all views
        loadingIndicator = findViewById(R.id.loadingIndicator)
        facultyName = findViewById(R.id.facultyName)
        facultyDesignation = findViewById(R.id.facultyDesignation)
        facultyJoiningDate = findViewById(R.id.facultyJoiningDate)
        tvGreeting = findViewById(R.id.tv_greeting)
        tvCurrentTime = findViewById(R.id.tv_current_time)
        tvTodaysClasses = findViewById(R.id.tv_todays_classes)
        tvPendingTasks = findViewById(R.id.tv_pending_tasks) 
        tvTotalStudents = findViewById(R.id.tv_total_students)
        bottomNavigation = findViewById(R.id.bottom_navigation)
        rvRecentMessages = findViewById(R.id.rvRecentMessages)
        tvNoMessages = findViewById(R.id.tvNoMessages)
        quickStatsContainer = findViewById(R.id.quick_stats_container)
        
        setupRecentMessages()
    }
    
    private fun setupRecentMessages() {
        recentMessagesAdapter = RecentMessagesAdapter(
            onMessageClick = { message ->
                // TODO: Navigate to message detail or chat screen
                Toast.makeText(this, "Opening message from ${message.senderName}", Toast.LENGTH_SHORT).show()
            }
        )
        
        rvRecentMessages.apply {
            layoutManager = LinearLayoutManager(this@FacultyDashboardActivity)
            adapter = recentMessagesAdapter
            setHasFixedSize(true)
        }
        
        findViewById<View>(R.id.btnViewAllMessages).setOnClickListener {
            // TODO: Navigate to all messages screen
            Toast.makeText(this, "Opening all messages", Toast.LENGTH_SHORT).show()
        }
        
        loadSampleMessages()
    }
    
    private fun loadSampleMessages() {
        val sampleMessages = listOf(
            Message(
                id = "1",
                senderId = "101",
                senderName = "Dr. Sharma",
                content = "Please review the research proposal for the upcoming conference.",
                timestamp = System.currentTimeMillis() - 3600000, // 1 hour ago
                isRead = false
            ),
            Message(
                id = "2",
                senderId = "102",
                senderName = "HOD Computer Science",
                content = "Department meeting scheduled for tomorrow at 11 AM in the conference room. Please ensure your attendance.",
                timestamp = System.currentTimeMillis() - 7200000, // 2 hours ago
                isRead = true
            ),
            Message(
                id = "3",
                senderId = "103",
                senderName = "Student Council",
                content = "Request for faculty participation in the annual tech fest. We would be honored to have you as a judge for the coding competition.",
                timestamp = System.currentTimeMillis() - 86400000, // 1 day ago
                isRead = true
            )
        )
        
        recentMessagesAdapter.updateMessages(sampleMessages)
        tvNoMessages.visibility = if (sampleMessages.isEmpty()) View.VISIBLE else View.GONE
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
        // Remove this method as the notification icon no longer exists
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
        findViewById<MaterialCardView>(R.id.card_courses)?.setOnClickListener {
            startActivity(Intent(this, CoursesActivity::class.java))
        }
        
        findViewById<MaterialCardView>(R.id.card_grades)?.setOnClickListener {
            startActivity(Intent(this, AssignmentManagementActivity::class.java))
        }
        
        // Secondary Actions
        findViewById<MaterialCardView>(R.id.card_attendance)?.setOnClickListener {
            startActivity(Intent(this, AttendanceActivity::class.java))
        }
        
        findViewById<MaterialCardView>(R.id.card_students)?.setOnClickListener {
            startActivity(Intent(this, StudentPerformanceActivity::class.java))
        }
        
        // Additional Features (Compact Cards)
        findViewById<MaterialCardView>(R.id.card_schedule)?.setOnClickListener {
            startActivity(Intent(this, ScheduleActivity::class.java))
        }
        
        findViewById<MaterialCardView>(R.id.card_reports)?.setOnClickListener {
            startActivity(Intent(this, LeaveRequestsActivity::class.java))
        }
        
        findViewById<MaterialCardView>(R.id.card_announcements)?.setOnClickListener {
            startActivity(Intent(this, MessagesActivity::class.java))
        }
        
        findViewById<MaterialCardView>(R.id.card_profile)?.setOnClickListener {
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
        facultyName.text = faculty.name
        facultyDesignation.text = faculty.designation
        facultyJoiningDate.text = "Joined: ${SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(faculty.joiningDate)}"
    }
    
    // Test function for Enhanced TelegramLogger - Remove after testing
    private fun testTelegramLogger() {
        Log.d("FacultyDashboard", "Testing TelegramLogger (rate-limited)...")
        
        // Test the enhanced TelegramLogger with a single message
        TelegramLogger.log("🧪 Faculty Dashboard started with rate-limited monitoring", "FacultyDashboard")
        
        // Don't run comprehensive tests immediately to avoid rate limiting
        Log.d("FacultyDashboard", "TelegramLogger basic test completed. Real monitoring is active.")
    }
    
    private fun testSystemErrorCapture() {
        Log.d("FacultyDashboard", "System error monitoring is active in background...")
        
        // System monitoring runs automatically - no need to spam with tests
        // Just send one test to confirm it's working
        SystemErrorMonitor.reportNotificationServiceError("com.vatty.mygbu", 5)
        
        Log.d("FacultyDashboard", "Comprehensive error monitoring is running. Real errors will be captured automatically.")
    }
    
    private fun testImprovedTelegramLogger() {
        Log.d("FacultyDashboard", "Enhanced TelegramLogger is active...")
        
        // Show queue stats instead of sending multiple messages
        val stats = TelegramLogger.getNetworkStats()
        Log.d("FacultyDashboard", "Current queue stats: $stats")
        
        Toast.makeText(this, "Enhanced monitoring is active! Real errors will be captured without overwhelming Telegram API.", Toast.LENGTH_LONG).show()
    }
} 