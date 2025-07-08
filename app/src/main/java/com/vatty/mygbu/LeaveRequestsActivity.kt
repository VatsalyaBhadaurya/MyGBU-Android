package com.vatty.mygbu

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.util.Log
import com.vatty.mygbu.utils.BottomNavigationHelper

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
        setupBackPressedHandler()
    }
    
    private fun setupToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Leave Requests"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }
    
    private fun setupBackPressedHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
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
            LeaveRequest("Alex Turner", "Medical Emergency", "pending", R.drawable.ic_profile, "Dec 21-23, 2024"),
            LeaveRequest("Olivia Bennett", "Family Function", "pending", R.drawable.ic_profile, "Dec 25-27, 2024"),
            LeaveRequest("Marcus Chen", "Personal Work", "pending", R.drawable.ic_profile, "Dec 28-30, 2024")
        )
        
        // Sample data for approved requests
        val approvedRequests = listOf(
            LeaveRequest("Emma Wilson", "Vacation", "approved", R.drawable.ic_profile, "Dec 15-17, 2024"),
            LeaveRequest("James Rodriguez", "Medical", "approved", R.drawable.ic_profile, "Dec 18-19, 2024"),
            LeaveRequest("Sophie Anderson", "Personal", "approved", R.drawable.ic_profile, "Dec 20, 2024")
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
        BottomNavigationHelper.setupBottomNavigation(this, bottomNavigation, LeaveRequestsActivity::class.java)
    }
}

data class LeaveRequest(
    val studentName: String,
    val reason: String,
    val status: String,
    val avatarRes: Int,
    val dates: String? = null
) 