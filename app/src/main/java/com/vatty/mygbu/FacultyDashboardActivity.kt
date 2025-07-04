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
import com.vatty.mygbu.utils.BottomNavigationHelper
import com.vatty.mygbu.databinding.ActivityFacultyDashboardBinding
import com.vatty.mygbu.utils.ErrorHandler
import com.vatty.mygbu.utils.ValidationError

class FacultyDashboardActivity : AppCompatActivity() {
    
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
    private lateinit var binding: ActivityFacultyDashboardBinding
    private lateinit var viewModel: FacultyDashboardViewModel
    private lateinit var errorHandler: ErrorHandler
    private lateinit var recentMessagesAdapter: RecentMessagesAdapter

    private val timeHandler = Handler(Looper.getMainLooper())
    private val timeUpdateRunnable = object : Runnable {
        override fun run() {
            updateCurrentTime()
            timeHandler.postDelayed(this, 60000) // Update every minute
        }
    }
    
    private val TAG = "FacultyDashboardActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFacultyDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        errorHandler = ErrorHandler(this)
        enableEdgeToEdge()
        
        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[FacultyDashboardViewModel::class.java]
        
        // Setup views and observers
        setupViews()
        setupObservers()
        
        // Load faculty data
        viewModel.loadFacultyList()
        
        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
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
    
    private fun setupViews() {
        setupRecentMessages()
        updateGreeting()
        updateCurrentTime()
    }
    
    private fun setupRecentMessages() {
        recentMessagesAdapter = RecentMessagesAdapter { message ->
            // TODO: Navigate to message detail or chat screen
            Toast.makeText(this, "Opening message from ${message.senderName}", Toast.LENGTH_SHORT).show()
        }
        
        binding.rvRecentMessages.apply {
            layoutManager = LinearLayoutManager(this@FacultyDashboardActivity)
            adapter = recentMessagesAdapter
            setHasFixedSize(true)
        }
        
        binding.btnViewAllMessages.setOnClickListener {
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
        binding.tvNoMessages.visibility = if (sampleMessages.isEmpty()) View.VISIBLE else View.GONE
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
            in 0..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            in 17..20 -> "Good Evening"
            else -> "Good Night"
        }
        
        binding.tvGreeting.text = greeting
    }
    
    private fun updateCurrentTime() {
        val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
        binding.tvCurrentTime.text = sdf.format(Date())
    }
    
    private fun startTimeUpdates() {
        timeHandler.post(timeUpdateRunnable)
    }
    
    private fun updateDashboardStats() {
        // TODO: Replace with real data from API
        with(binding) {
            tvTodaysClasses.text = "4"
            tvPendingTasks.text = "7"
            tvTotalStudents.text = "120"
        }
    }
    
    private fun setupDashboardCards() {
        with(binding) {
            cardCourses.setOnClickListener {
                startActivity(Intent(this@FacultyDashboardActivity, CoursesActivity::class.java))
            }
            
            cardGrades.setOnClickListener {
                startActivity(Intent(this@FacultyDashboardActivity, AssignmentManagementActivity::class.java))
            }
            
            cardAttendance.setOnClickListener {
                startActivity(Intent(this@FacultyDashboardActivity, AttendanceActivity::class.java))
            }
            
            cardStudents.setOnClickListener {
                startActivity(Intent(this@FacultyDashboardActivity, StudentPerformanceActivity::class.java))
            }
        }
    }
    
    private fun setupHeaderActions() {
        binding.ivProfile.setOnClickListener {
            // TODO: Navigate to profile screen
            Toast.makeText(this, "Opening profile", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun setupBottomNavigation() {
        BottomNavigationHelper.setupBottomNavigation(
            activity = this,
            bottomNav = binding.bottomNavigation,
            currentItemId = R.id.nav_home
        )
    }
    
    private fun setupDynamicContent() {
        // TODO: Load dynamic content like announcements, schedule, etc.
    }
    
    private fun setupObservers() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.loadingIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { error ->
            if (error != null) {
                errorHandler.showError(ValidationError(error))
            }
        }

        viewModel.facultyList.observe(this) { facultyList ->
            if (facultyList.isNotEmpty()) {
                val faculty = facultyList[0] // Get the first faculty member
                with(binding) {
                    tvFacultyName.text = faculty.name
                    tvFacultyDesignation.text = faculty.designation
                    tvJoiningDate.text = "Joined: ${SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(faculty.joiningDate)}"
                }
            }
        }
    }
    
    private fun testTelegramLogger() {
        TelegramLogger.log(TAG, "Testing Telegram Logger from FacultyDashboardActivity")
    }
    

} 