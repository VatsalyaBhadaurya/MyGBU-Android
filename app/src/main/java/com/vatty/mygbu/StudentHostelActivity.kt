package com.vatty.mygbu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import com.vatty.mygbu.data.model.StudentHostel
import com.vatty.mygbu.data.repository.StudentRepository
import com.vatty.mygbu.utils.BottomNavigationHelper
import com.vatty.mygbu.viewmodel.StudentDashboardViewModel

class StudentHostelActivity : AppCompatActivity() {

    private lateinit var viewModel: StudentDashboardViewModel
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var repository: StudentRepository
    
    // UI Components
    private lateinit var tvHostelName: TextView
    private lateinit var tvRoomNumber: TextView
    private lateinit var tvFloor: TextView
    private lateinit var tvWardenName: TextView
    private lateinit var tvWardenPhone: TextView
    private lateinit var tvMessStatus: TextView
    private lateinit var tvMessPlan: TextView
    private lateinit var btnCallWarden: MaterialCardView
    private lateinit var btnViewRules: MaterialCardView
    private lateinit var btnReportIssue: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_hostel)

        // Initialize ViewModel and Repository
        viewModel = ViewModelProvider(this)[StudentDashboardViewModel::class.java]
        repository = StudentRepository()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeViews()
        setupBottomNavigation()
        loadHostelData()
        setupClickListeners()
    }

    private fun initializeViews() {
        bottomNavigation = findViewById(R.id.bottom_navigation)
        tvHostelName = findViewById(R.id.tv_hostel_name)
        tvRoomNumber = findViewById(R.id.tv_room_number)
        tvFloor = findViewById(R.id.tv_floor)
        tvWardenName = findViewById(R.id.tv_warden_name)
        tvWardenPhone = findViewById(R.id.tv_warden_phone)
        tvMessStatus = findViewById(R.id.tv_mess_status)
        tvMessPlan = findViewById(R.id.tv_mess_plan)
        btnCallWarden = findViewById(R.id.btn_call_warden)
        btnViewRules = findViewById(R.id.btn_view_rules)
        btnReportIssue = findViewById(R.id.btn_report_issue)
        
        // Back button
        findViewById<View>(R.id.iv_back).setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupBottomNavigation() {
        BottomNavigationHelper.setupBottomNavigation(this, bottomNavigation, StudentHostelActivity::class.java)
    }

    private fun loadHostelData() {
        try {
            val hostel = repository.getStudentHostel("STU001")
            updateHostelDisplay(hostel)
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to load hostel data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateHostelDisplay(hostel: StudentHostel) {
        tvHostelName.text = hostel.hostelName
        tvRoomNumber.text = hostel.roomNumber
        tvFloor.text = hostel.floor
        tvWardenName.text = hostel.wardenName
        tvWardenPhone.text = hostel.wardenPhone
        
        // Update mess status
        if (hostel.messAllotted) {
            tvMessStatus.text = "Mess Allotted"
            tvMessStatus.setTextColor(getColor(R.color.success_green))
            tvMessPlan.text = "Plan: ${hostel.messPlan ?: "Not specified"}"
            tvMessPlan.visibility = View.VISIBLE
        } else {
            tvMessStatus.text = "Mess Not Allotted"
            tvMessStatus.setTextColor(getColor(R.color.warning_orange))
            tvMessPlan.visibility = View.GONE
        }
    }

    private fun setupClickListeners() {
        btnCallWarden.setOnClickListener {
            callWarden()
        }
        
        btnViewRules.setOnClickListener {
            showHostelRules()
        }
        
        btnReportIssue.setOnClickListener {
            reportHostelIssue()
        }
    }

    private fun callWarden() {
        val hostel = repository.getStudentHostel("STU001")
        val phoneNumber = hostel.wardenPhone.replace("+91-", "")
        
        try {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phoneNumber")
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Unable to make call", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showHostelRules() {
        val rules = """
            Hostel Rules and Regulations:
            
            1. Curfew Time: 10:00 PM (Sunday-Thursday), 11:00 PM (Friday-Saturday)
            2. Visitors are allowed only in the common area during visiting hours
            3. No cooking in rooms - use designated kitchen areas
            4. Maintain cleanliness and report maintenance issues promptly
            5. No loud music or disturbances after 9:00 PM
            6. Electrical appliances must be approved by hostel administration
            7. Regular room inspections will be conducted
            8. Follow COVID-19 protocols and maintain social distancing
            9. Report any security concerns immediately to the warden
            10. Respect fellow residents and maintain harmony
            
            Violation of rules may result in disciplinary action.
        """.trimIndent()
        
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Hostel Rules")
            .setMessage(rules)
            .setPositiveButton("I Understand", null)
            .show()
    }

    private fun reportHostelIssue() {
        val issueTypes = arrayOf(
            "Electrical Problem",
            "Plumbing Issue", 
            "Cleaning Request",
            "Security Concern",
            "Mess Related",
            "Other"
        )
        
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Report Issue")
            .setItems(issueTypes) { _, which ->
                val selectedIssue = issueTypes[which]
                showIssueDetailsDialog(selectedIssue)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showIssueDetailsDialog(issueType: String) {
        val hostel = repository.getStudentHostel("STU001")
        val message = """
            Issue Type: $issueType
            Hostel: ${hostel.hostelName}
            Room: ${hostel.roomNumber}
            Warden: ${hostel.wardenName}
            Contact: ${hostel.wardenPhone}
            
            Your issue has been reported to the hostel administration.
            You will be contacted shortly for further assistance.
        """.trimIndent()
        
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Issue Reported")
            .setMessage(message)
            .setPositiveButton("OK") { _, _ ->
                Toast.makeText(this, "Issue reported successfully", Toast.LENGTH_LONG).show()
            }
            .show()
    }
}