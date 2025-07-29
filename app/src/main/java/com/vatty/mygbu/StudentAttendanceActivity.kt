package com.vatty.mygbu

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vatty.mygbu.utils.BottomNavigationHelper
import com.vatty.mygbu.viewmodel.StudentDashboardViewModel

class StudentAttendanceActivity : AppCompatActivity() {

    private lateinit var viewModel: StudentDashboardViewModel
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_attendance)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[StudentDashboardViewModel::class.java]

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeViews()
        setupBottomNavigation()
        setupObservers()
    }

    private fun initializeViews() {
        bottomNavigation = findViewById(R.id.bottom_navigation)
        
        // Back button
        findViewById<View>(R.id.iv_back).setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupBottomNavigation() {
        BottomNavigationHelper.setupBottomNavigation(this, bottomNavigation, StudentAttendanceActivity::class.java)
    }

    private fun setupObservers() {
        viewModel.attendancePercentage.observe(this) { percentage ->
            // TODO: Update attendance display
        }
    }
}