package com.vatty.mygbu

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vatty.mygbu.data.repository.AdminRepository
import com.vatty.mygbu.data.model.*

class AdminDashboardActivity : AppCompatActivity() {
    
    private lateinit var adminRepository: AdminRepository
    private lateinit var tvPendingApprovals: TextView
    private lateinit var tvPendingOnboarding: TextView
    private lateinit var tvPendingOffboarding: TextView
    private lateinit var tvActiveFiles: TextView
    private lateinit var tvEscalatedItems: TextView
    private lateinit var rvTodayTasks: RecyclerView
    private lateinit var rvRecentActivities: RecyclerView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)
        
        adminRepository = AdminRepository()
        
        initializeViews()
        setupNavigation()
        loadDashboardData()
    }
    
    private fun initializeViews() {
        // Initialize dashboard statistics
        tvPendingApprovals = findViewById(R.id.tv_pending_approvals)
        tvPendingOnboarding = findViewById(R.id.tv_pending_onboarding)
        tvPendingOffboarding = findViewById(R.id.tv_pending_offboarding)
        tvActiveFiles = findViewById(R.id.tv_active_files)
        tvEscalatedItems = findViewById(R.id.tv_escalated_items)
        
        // Initialize RecyclerViews
        rvTodayTasks = findViewById(R.id.rv_today_tasks)
        rvRecentActivities = findViewById(R.id.rv_recent_activities)
        
        // Setup RecyclerViews
        rvTodayTasks.layoutManager = LinearLayoutManager(this)
        rvRecentActivities.layoutManager = LinearLayoutManager(this)
        
        // Setup click listeners for quick action cards
        setupQuickActionCards()
    }
    
    private fun setupQuickActionCards() {
        // Onboarding/Offboarding
        findViewById<CardView>(R.id.card_onboarding).setOnClickListener {
            startActivity(Intent(this, AdminOnboardingActivity::class.java))
        }
        
        // Course Mapping
        findViewById<CardView>(R.id.card_course_mapping).setOnClickListener {
            startActivity(Intent(this, AdminCourseMappingActivity::class.java))
        }
        
        // Exam Management
        findViewById<CardView>(R.id.card_exam_management).setOnClickListener {
            startActivity(Intent(this, AdminExamManagementActivity::class.java))
        }
        
        // Degree Audit
        findViewById<CardView>(R.id.card_degree_audit).setOnClickListener {
            startActivity(Intent(this, AdminDegreeAuditActivity::class.java))
        }
        
        // Approvals
        findViewById<CardView>(R.id.card_approvals).setOnClickListener {
            startActivity(Intent(this, AdminApprovalsActivity::class.java))
        }
        
        // Project Tracking
        findViewById<CardView>(R.id.card_project_tracking).setOnClickListener {
            startActivity(Intent(this, AdminProjectTrackingActivity::class.java))
        }
        
        // File Tracking
        findViewById<CardView>(R.id.card_file_tracking).setOnClickListener {
            startActivity(Intent(this, AdminFileTrackingActivity::class.java))
        }
        
        // Operations Dashboard
        findViewById<CardView>(R.id.card_operations).setOnClickListener {
            startActivity(Intent(this, AdminOperationsActivity::class.java))
        }
    }
    
    private fun setupNavigation() {
        // Setup bottom navigation if needed
        // This can be implemented similar to student/faculty navigation
    }
    
    private fun loadDashboardData() {
        val dashboard = adminRepository.getAdminDashboard()
        
        // Update statistics
        tvPendingApprovals.text = dashboard.pendingApprovals.toString()
        tvPendingOnboarding.text = dashboard.pendingOnboarding.toString()
        tvPendingOffboarding.text = dashboard.pendingOffboarding.toString()
        tvActiveFiles.text = dashboard.activeFiles.toString()
        tvEscalatedItems.text = dashboard.escalatedItems.toString()
        
        // Setup adapters for RecyclerViews
        rvTodayTasks.adapter = AdminTaskAdapter(dashboard.todayTasks) { task ->
            // Handle task click
            handleTaskClick(task)
        }
        
        rvRecentActivities.adapter = AdminActivityAdapter(dashboard.recentActivities)
    }
    
    private fun handleTaskClick(task: AdminTask) {
        when (task.type) {
            TaskType.ONBOARDING -> {
                startActivity(Intent(this, AdminOnboardingActivity::class.java))
            }
            TaskType.APPROVAL -> {
                startActivity(Intent(this, AdminApprovalsActivity::class.java))
            }
            TaskType.EXAM_MANAGEMENT -> {
                startActivity(Intent(this, AdminExamManagementActivity::class.java))
            }
            TaskType.FILE_TRACKING -> {
                startActivity(Intent(this, AdminFileTrackingActivity::class.java))
            }
            TaskType.OFFBOARDING -> {
                startActivity(Intent(this, AdminOnboardingActivity::class.java))
            }
        }
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

// Adapter for Today's Tasks
class AdminTaskAdapter(
    private val tasks: List<AdminTask>,
    private val onTaskClick: (AdminTask) -> Unit
) : RecyclerView.Adapter<AdminTaskAdapter.TaskViewHolder>() {
    
    class TaskViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tv_task_title)
        val tvType: TextView = view.findViewById(R.id.tv_task_type)
        val tvPriority: TextView = view.findViewById(R.id.tv_task_priority)
        val tvStatus: TextView = view.findViewById(R.id.tv_task_status)
        val tvDueDate: TextView = view.findViewById(R.id.tv_task_due_date)
    }
    
    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): TaskViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_task, parent, false)
        return TaskViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        
        holder.tvTitle.text = task.title
        holder.tvType.text = task.type.name.replace("_", " ")
        holder.tvPriority.text = task.priority.name
        holder.tvStatus.text = task.status.name.replace("_", " ")
        holder.tvDueDate.text = android.text.format.DateFormat.getDateFormat(holder.itemView.context).format(task.dueDate)
        
        // Set priority color
        val priorityColor = when (task.priority) {
            Priority.LOW -> android.graphics.Color.GREEN
            Priority.MEDIUM -> android.graphics.Color.YELLOW
            Priority.HIGH -> android.graphics.Color.RED
            Priority.URGENT -> android.graphics.Color.RED
        }
        holder.tvPriority.setTextColor(priorityColor)
        
        holder.itemView.setOnClickListener {
            onTaskClick(task)
        }
    }
    
    override fun getItemCount() = tasks.size
}

// Adapter for Recent Activities
class AdminActivityAdapter(
    private val activities: List<AdminActivity>
) : RecyclerView.Adapter<AdminActivityAdapter.ActivityViewHolder>() {
    
    class ActivityViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val tvAction: TextView = view.findViewById(R.id.tv_activity_action)
        val tvDescription: TextView = view.findViewById(R.id.tv_activity_description)
        val tvTimestamp: TextView = view.findViewById(R.id.tv_activity_timestamp)
        val tvUserName: TextView = view.findViewById(R.id.tv_activity_user)
    }
    
    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ActivityViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_activity, parent, false)
        return ActivityViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activity = activities[position]
        
        holder.tvAction.text = activity.action
        holder.tvDescription.text = activity.description
        holder.tvTimestamp.text = java.text.SimpleDateFormat("MMM dd, yyyy HH:mm", java.util.Locale.getDefault()).format(activity.timestamp)
        holder.tvUserName.text = activity.userName
    }
    
    override fun getItemCount() = activities.size
} 