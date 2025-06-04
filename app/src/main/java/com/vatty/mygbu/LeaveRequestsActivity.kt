package com.vatty.mygbu

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

class LeaveRequestsActivity : AppCompatActivity() {
    
    private lateinit var rvPendingRequests: RecyclerView
    private lateinit var rvApprovedRequests: RecyclerView
    private lateinit var bottomNavigation: BottomNavigationView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_leave_requests)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        setupToolbar()
        initializeViews()
        setupRecyclerViews()
        setupBottomNavigation()
    }
    
    private fun setupToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Leave Requests"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
    
    private fun initializeViews() {
        rvPendingRequests = findViewById(R.id.rv_pending_requests)
        rvApprovedRequests = findViewById(R.id.rv_approved_requests)
        bottomNavigation = findViewById(R.id.bottom_navigation)
    }
    
    private fun setupRecyclerViews() {
        rvPendingRequests.layoutManager = LinearLayoutManager(this)
        rvApprovedRequests.layoutManager = LinearLayoutManager(this)
        
        // Sample data for pending requests
        val pendingRequests = listOf(
            LeaveRequest("Alex Turner", "Medical", "pending", R.drawable.ic_profile),
            LeaveRequest("Olivia Bennett", "Family Emergency", "pending", R.drawable.ic_profile)
        )
        
        // Sample data for approved requests
        val approvedRequests = listOf(
            LeaveRequest("Ethan Carter", "Vacation", "approved", R.drawable.ic_profile),
            LeaveRequest("Sophia Clark", "Personal", "approved", R.drawable.ic_profile)
        )
        
        rvPendingRequests.adapter = LeaveRequestAdapter(pendingRequests) { request ->
            Toast.makeText(this, "View ${request.studentName} request", Toast.LENGTH_SHORT).show()
        }
        
        rvApprovedRequests.adapter = LeaveRequestAdapter(approvedRequests) { request ->
            Toast.makeText(this, "View ${request.studentName} request", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun setupBottomNavigation() {
        bottomNavigation.selectedItemId = R.id.nav_requests
        
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
                R.id.nav_attendance -> {
                    startActivity(Intent(this, AttendanceActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_requests -> true
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

data class LeaveRequest(
    val studentName: String,
    val reason: String,
    val status: String,
    val avatarRes: Int
) 