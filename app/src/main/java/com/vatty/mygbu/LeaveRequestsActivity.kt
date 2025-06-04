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
        val pendingRequests = mutableListOf(
            LeaveRequest("Vatsalya", "Medical Emergency", "pending", R.drawable.ic_profile, "Dec 21-23, 2024"),
            LeaveRequest("Yaduraj", "Family Function", "pending", R.drawable.ic_profile, "Dec 25-27, 2024"),
            LeaveRequest("Mayank", "Personal Work", "pending", R.drawable.ic_profile, "Dec 28-30, 2024")
        )
        
        // Sample data for approved requests
        val approvedRequests = listOf(
            LeaveRequest("Yuvraj", "Vacation", "approved", R.drawable.ic_profile, "Dec 15-17, 2024"),
            LeaveRequest("Anshul", "Medical", "approved", R.drawable.ic_profile, "Dec 18-19, 2024"),
            LeaveRequest("Sunahi", "Personal", "approved", R.drawable.ic_profile, "Dec 20, 2024")
        )
        
        rvPendingRequests.adapter = LeaveRequestAdapter(pendingRequests, 
            onItemClick = { request ->
                Toast.makeText(this, "View ${request.studentName} request details", Toast.LENGTH_SHORT).show()
            },
            onApprovalChange = { request, isApproved ->
                if (isApproved) {
                    Toast.makeText(this, "${request.studentName}'s leave request approved", Toast.LENGTH_SHORT).show()
                    // Here you would typically move the request to approved list
                }
            }
        )
        
        rvApprovedRequests.adapter = LeaveRequestAdapter(approvedRequests,
            onItemClick = { request ->
                Toast.makeText(this, "View ${request.studentName} request details", Toast.LENGTH_SHORT).show()
            }
        )
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
    val avatarRes: Int,
    val dates: String? = null
) 