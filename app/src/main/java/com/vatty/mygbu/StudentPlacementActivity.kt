package com.vatty.mygbu

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
import com.vatty.mygbu.data.model.StudentPlacement
import com.vatty.mygbu.data.repository.StudentRepository
import com.vatty.mygbu.enums.PlacementStatus
import com.vatty.mygbu.utils.BottomNavigationHelper
import com.vatty.mygbu.viewmodel.StudentDashboardViewModel

class StudentPlacementActivity : AppCompatActivity() {

    private lateinit var viewModel: StudentDashboardViewModel
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var repository: StudentRepository
    
    // UI Components
    private lateinit var tvAppliedCount: TextView
    private lateinit var tvInterviewCount: TextView
    private lateinit var tvSelectedCount: TextView
    private lateinit var btnViewOpportunities: MaterialCardView
    private lateinit var btnMyApplications: MaterialCardView
    private lateinit var btnResumeBuilder: MaterialCardView
    private lateinit var btnInterviewPrep: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_placement)

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
        loadPlacementData()
        setupClickListeners()
    }

    private fun initializeViews() {
        bottomNavigation = findViewById(R.id.bottom_navigation)
        tvAppliedCount = findViewById(R.id.tv_applied_count)
        tvInterviewCount = findViewById(R.id.tv_interview_count)
        tvSelectedCount = findViewById(R.id.tv_selected_count)
        btnViewOpportunities = findViewById(R.id.btn_view_opportunities)
        btnMyApplications = findViewById(R.id.btn_my_applications)
        btnResumeBuilder = findViewById(R.id.btn_resume_builder)
        btnInterviewPrep = findViewById(R.id.btn_interview_prep)
        
        // Back button
        findViewById<View>(R.id.iv_back).setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupBottomNavigation() {
        BottomNavigationHelper.setupBottomNavigation(this, bottomNavigation, StudentPlacementActivity::class.java)
    }

    private fun loadPlacementData() {
        try {
            val placements = repository.getStudentPlacements("STU001")
            updatePlacementStats(placements)
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to load placement data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updatePlacementStats(placements: List<StudentPlacement>) {
        val appliedCount = placements.count { it.status == PlacementStatus.APPLIED }
        val interviewCount = placements.count { it.status == PlacementStatus.INTERVIEW_SCHEDULED }
        val selectedCount = placements.count { it.status == PlacementStatus.SELECTED }
        
        tvAppliedCount.text = appliedCount.toString()
        tvInterviewCount.text = interviewCount.toString()
        tvSelectedCount.text = selectedCount.toString()
    }

    private fun setupClickListeners() {
        btnViewOpportunities.setOnClickListener {
            showPlacementOpportunities()
        }
        
        btnMyApplications.setOnClickListener {
            showMyApplications()
        }
        
        btnResumeBuilder.setOnClickListener {
            showResumeBuilder()
        }
        
        btnInterviewPrep.setOnClickListener {
            showInterviewPrep()
        }
    }

    private fun showPlacementOpportunities() {
        val opportunities = """
            Current Placement Opportunities:
            
            1. TechCorp Solutions
               - Position: Software Developer Intern
               - Package: 6 LPA
               - Location: Noida
               - Deadline: 2024-02-15
            
            2. DataFlow Analytics
               - Position: Data Analyst
               - Package: 5.5 LPA
               - Location: Gurgaon
               - Deadline: 2024-02-20
            
            3. CloudTech Systems
               - Position: Cloud Engineer
               - Package: 7 LPA
               - Location: Bangalore
               - Deadline: 2024-02-25
            
            4. MobileFirst Apps
               - Position: Android Developer
               - Package: 6.5 LPA
               - Location: Mumbai
               - Deadline: 2024-03-01
        """.trimIndent()
        
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Placement Opportunities")
            .setMessage(opportunities)
            .setPositiveButton("Apply Now") { _, _ ->
                showApplicationForm()
            }
            .setNegativeButton("Close", null)
            .show()
    }

    private fun showMyApplications() {
        val placements = repository.getStudentPlacements("STU001")
        if (placements.isNotEmpty()) {
            val applicationsText = placements.joinToString("\n\n") { placement ->
                """
                Company: ${placement.companyName}
                Position: ${placement.position}
                Package: ${placement.packageAmount}
                Status: ${placement.status}
                Applied: ${placement.appliedDate}
                ${if (placement.interviewDate != null) "Interview: ${placement.interviewDate}" else ""}
                """.trimIndent()
            }
            
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("My Applications")
                .setMessage(applicationsText)
                .setPositiveButton("OK", null)
                .show()
        } else {
            Toast.makeText(this, "No applications submitted yet", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showApplicationForm() {
        val companies = arrayOf(
            "TechCorp Solutions",
            "DataFlow Analytics", 
            "CloudTech Systems",
            "MobileFirst Apps"
        )
        
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Select Company")
            .setItems(companies) { _, which ->
                val selectedCompany = companies[which]
                submitApplication(selectedCompany)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun submitApplication(companyName: String) {
        val positions = mapOf(
            "TechCorp Solutions" to "Software Developer Intern",
            "DataFlow Analytics" to "Data Analyst",
            "CloudTech Systems" to "Cloud Engineer",
            "MobileFirst Apps" to "Android Developer"
        )
        
        val packages = mapOf(
            "TechCorp Solutions" to "6 LPA",
            "DataFlow Analytics" to "5.5 LPA",
            "CloudTech Systems" to "7 LPA",
            "MobileFirst Apps" to "6.5 LPA"
        )
        
        val position = positions[companyName] ?: "Software Developer"
        val packageAmount = packages[companyName] ?: "6 LPA"
        
        val newPlacement = StudentPlacement(
            id = "PLAC${System.currentTimeMillis()}",
            studentId = "STU001",
            companyName = companyName,
            position = position,
            packageAmount = packageAmount,
            status = PlacementStatus.APPLIED,
            appliedDate = "2024-01-15",
            interviewDate = null
        )
        
        // In a real app, this would save to the repository
        Toast.makeText(this, "Application submitted to $companyName", Toast.LENGTH_LONG).show()
        
        // Refresh the stats
        loadPlacementData()
    }

    private fun showResumeBuilder() {
        val resumeTips = """
            Resume Building Tips:
            
            1. Contact Information
               - Name, Email, Phone, LinkedIn
            
            2. Education
               - Degree, Institution, CGPA, Year
            
            3. Skills
               - Technical Skills (Programming, Tools)
               - Soft Skills (Communication, Leadership)
            
            4. Projects
               - Title, Description, Technologies Used
               - GitHub Links
            
            5. Experience
               - Internships, Part-time Jobs
               - Responsibilities and Achievements
            
            6. Certifications
               - Relevant Online Courses
               - Professional Certifications
            
            7. Achievements
               - Awards, Competitions, Hackathons
            
            Tips:
            - Keep it concise (1-2 pages)
            - Use action verbs
            - Quantify achievements
            - Proofread thoroughly
            - Customize for each application
        """.trimIndent()
        
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Resume Builder")
            .setMessage(resumeTips)
            .setPositiveButton("Create Resume", null)
            .setNegativeButton("Close", null)
            .show()
    }

    private fun showInterviewPrep() {
        val interviewTips = """
            Interview Preparation Guide:
            
            Technical Interview:
            1. Review core concepts
            2. Practice coding problems
            3. Know your projects well
            4. Prepare for system design
            
            Behavioral Interview:
            1. STAR method responses
            2. Prepare for common questions
            3. Research the company
            4. Prepare questions to ask
            
            Common Questions:
            - Tell me about yourself
            - Why this company?
            - Your strengths/weaknesses
            - Handle difficult situations
            - Future goals
            
            Tips:
            - Dress professionally
            - Arrive early
            - Maintain eye contact
            - Be confident and honest
            - Follow up with thank you email
        """.trimIndent()
        
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Interview Preparation")
            .setMessage(interviewTips)
            .setPositiveButton("Practice Interview", null)
            .setNegativeButton("Close", null)
            .show()
    }
}