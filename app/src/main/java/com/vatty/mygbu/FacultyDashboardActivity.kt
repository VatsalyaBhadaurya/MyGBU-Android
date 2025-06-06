package com.vatty.mygbu

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView
import de.hdodenhof.circleimageview.CircleImageView

class FacultyDashboardActivity : AppCompatActivity() {
    
    private lateinit var tvFacultyName: TextView
    private lateinit var tvGreeting: TextView
    private lateinit var tvTodaysClasses: TextView
    private lateinit var tvPendingTasks: TextView
    private lateinit var tvTotalStudents: TextView
    private lateinit var ivNotification: ImageView
    private lateinit var ivProfileMini: CircleImageView
    
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
        loadDashboardData()
    }
    
    private fun initializeViews() {
        tvFacultyName = findViewById(R.id.tv_faculty_name)
        tvGreeting = findViewById(R.id.tv_greeting)
        tvTodaysClasses = findViewById(R.id.tv_todays_classes)
        tvPendingTasks = findViewById(R.id.tv_pending_tasks) 
        tvTotalStudents = findViewById(R.id.tv_total_students)
        ivNotification = findViewById(R.id.iv_notification)
        ivProfileMini = findViewById(R.id.iv_profile_mini)
    }
    
    private fun loadDashboardData() {
        // Load static data for now - in production this would come from API/database
        tvFacultyName.text = "Dr. Ethan Carter"
        tvGreeting.text = "Good afternoon"
        tvTodaysClasses.text = "3"
        tvPendingTasks.text = "7"
        tvTotalStudents.text = "128"
    }
    
    private fun setupHeaderActions() {
        ivNotification.setOnClickListener {
            Toast.makeText(this, "Notifications", Toast.LENGTH_SHORT).show()
        }
        
        ivProfileMini.setOnClickListener {
            startActivity(Intent(this, FacultyHubActivity::class.java))
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
} 