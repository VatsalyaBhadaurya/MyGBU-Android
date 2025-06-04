package com.vatty.mygbu

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.bottomnavigation.BottomNavigationView

class AttendanceActivity : AppCompatActivity() {
    
    private lateinit var btnMarkAttendance: MaterialButton
    private lateinit var btnViewAttendance: MaterialButton
    private lateinit var etTopicsCovered: TextInputEditText
    private lateinit var etRemarks: TextInputEditText
    private lateinit var btnSubmit: MaterialButton
    private lateinit var bottomNavigation: BottomNavigationView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_attendance)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        setupToolbar()
        initializeViews()
        setupClickListeners()
        setupBottomNavigation()
    }
    
    private fun setupToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Attendance & Class Summary"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
    
    private fun initializeViews() {
        btnMarkAttendance = findViewById(R.id.btn_mark_attendance)
        btnViewAttendance = findViewById(R.id.btn_view_attendance)
        etTopicsCovered = findViewById(R.id.et_topics_covered)
        etRemarks = findViewById(R.id.et_remarks)
        btnSubmit = findViewById(R.id.btn_submit)
        bottomNavigation = findViewById(R.id.bottom_navigation)
    }
    
    private fun setupClickListeners() {
        btnMarkAttendance.setOnClickListener {
            Toast.makeText(this, "Mark Attendance", Toast.LENGTH_SHORT).show()
        }
        
        btnViewAttendance.setOnClickListener {
            Toast.makeText(this, "View Cumulative Attendance", Toast.LENGTH_SHORT).show()
        }
        
        btnSubmit.setOnClickListener {
            val topics = etTopicsCovered.text.toString().trim()
            val remarks = etRemarks.text.toString().trim()
            
            if (topics.isNotEmpty()) {
                Toast.makeText(this, "Class summary submitted!", Toast.LENGTH_SHORT).show()
                etTopicsCovered.setText("")
                etRemarks.setText("")
            } else {
                Toast.makeText(this, "Please add topics covered", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun setupBottomNavigation() {
        bottomNavigation.selectedItemId = R.id.nav_attendance
        
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, FacultyDashboardActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_courses -> {
                    startActivity(Intent(this, CoursesActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_attendance -> true
                R.id.nav_profile -> {
                    startActivity(Intent(this, FacultyHubActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }
} 