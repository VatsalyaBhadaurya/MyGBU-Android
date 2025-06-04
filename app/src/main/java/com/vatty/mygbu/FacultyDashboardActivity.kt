package com.vatty.mygbu

import android.content.Intent
import android.os.Bundle
import android.widget.GridLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textview.MaterialTextView

class FacultyDashboardActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_faculty_dashboard)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        setupToolbar()
        setupDashboardCards()
    }
    
    private fun setupToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Faculty Dashboard"
    }
    
    private fun setupDashboardCards() {
        // Course Management Card
        findViewById<CardView>(R.id.card_courses).setOnClickListener {
            startActivity(Intent(this, CoursesActivity::class.java))
        }
        
        // Attendance Card
        findViewById<CardView>(R.id.card_attendance).setOnClickListener {
            startActivity(Intent(this, AttendanceActivity::class.java))
        }
        
        // Students Card
        findViewById<CardView>(R.id.card_students).setOnClickListener {
            startActivity(Intent(this, StudentPerformanceActivity::class.java))
        }
        
        // Grades Card
        findViewById<CardView>(R.id.card_grades).setOnClickListener {
            startActivity(Intent(this, AssignmentManagementActivity::class.java))
        }
        
        // Schedule Card
        findViewById<CardView>(R.id.card_schedule).setOnClickListener {
            startActivity(Intent(this, CoursesActivity::class.java))
        }
        
        // Announcements Card
        findViewById<CardView>(R.id.card_announcements).setOnClickListener {
            startActivity(Intent(this, AssignmentManagementActivity::class.java))
        }
        
        // Reports Card
        findViewById<CardView>(R.id.card_reports).setOnClickListener {
            startActivity(Intent(this, LeaveRequestsActivity::class.java))
        }
        
        // Profile Card
        findViewById<CardView>(R.id.card_profile).setOnClickListener {
            startActivity(Intent(this, FacultyHubActivity::class.java))
        }
    }
} 