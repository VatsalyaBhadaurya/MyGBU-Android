package com.vatty.mygbu

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vatty.mygbu.utils.BottomNavigationHelper
import com.vatty.mygbu.viewmodel.StudentDashboardViewModel

class StudentAssignmentsActivity : AppCompatActivity() {

    private lateinit var viewModel: StudentDashboardViewModel
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvNoAssignments: TextView
    private lateinit var fabSubmit: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_assignments)

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
        setupClickListeners()
    }

    private fun initializeViews() {
        bottomNavigation = findViewById(R.id.bottom_navigation)
        recyclerView = findViewById(R.id.recycler_view)
        tvNoAssignments = findViewById(R.id.tv_no_assignments)
        fabSubmit = findViewById(R.id.fab_submit)
        
        // Back button
        findViewById<View>(R.id.iv_back).setOnClickListener {
            onBackPressed()
        }
        
        // Setup RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
    }

    private fun setupBottomNavigation() {
        BottomNavigationHelper.setupBottomNavigation(this, bottomNavigation, StudentAssignmentsActivity::class.java)
    }

    private fun setupObservers() {
        viewModel.recentAssignments.observe(this) { assignments ->
            if (assignments.isNotEmpty()) {
                recyclerView.visibility = View.VISIBLE
                tvNoAssignments.visibility = View.GONE
                // TODO: Set up adapter for assignments
                // recyclerView.adapter = StudentAssignmentsAdapter(assignments)
            } else {
                recyclerView.visibility = View.GONE
                tvNoAssignments.visibility = View.VISIBLE
            }
        }
    }

    private fun setupClickListeners() {
        fabSubmit.setOnClickListener {
            // TODO: Open assignment submission dialog
        }
    }
}