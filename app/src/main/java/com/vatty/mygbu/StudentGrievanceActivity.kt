package com.vatty.mygbu

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vatty.mygbu.data.model.StudentGrievance
import com.vatty.mygbu.data.repository.StudentRepository
import com.vatty.mygbu.enums.GrievanceStatus
import com.vatty.mygbu.utils.BottomNavigationHelper
import com.vatty.mygbu.viewmodel.StudentDashboardViewModel
import java.text.SimpleDateFormat
import java.util.*

class StudentGrievanceActivity : AppCompatActivity() {

    private lateinit var viewModel: StudentDashboardViewModel
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var repository: StudentRepository
    private lateinit var fabNewGrievance: FloatingActionButton
    
    // UI Components
    private lateinit var rvGrievances: RecyclerView
    private lateinit var tvNoGrievances: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_grievance)

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
        setupClickListeners()
        loadGrievances()
    }

    private fun initializeViews() {
        bottomNavigation = findViewById(R.id.bottom_navigation)
        fabNewGrievance = findViewById(R.id.fab_new_grievance)
        rvGrievances = findViewById(R.id.rv_grievances)
        tvNoGrievances = findViewById(R.id.tv_no_grievances)
        
        // Back button
        findViewById<View>(R.id.iv_back).setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupBottomNavigation() {
        BottomNavigationHelper.setupBottomNavigation(this, bottomNavigation, StudentGrievanceActivity::class.java)
    }

    private fun setupClickListeners() {
        fabNewGrievance.setOnClickListener {
            showNewGrievanceDialog()
        }
    }

    private fun loadGrievances() {
        try {
            val grievances = repository.getStudentGrievances("STU001")
            if (grievances.isNotEmpty()) {
                tvNoGrievances.visibility = View.GONE
                rvGrievances.visibility = View.VISIBLE
                // In a real app, you would use a RecyclerView adapter here
                displayGrievances(grievances)
            } else {
                tvNoGrievances.visibility = View.VISIBLE
                rvGrievances.visibility = View.GONE
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to load grievances", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayGrievances(grievances: List<StudentGrievance>) {
        // For now, we'll show them in a dialog
        // In a real app, you would use a RecyclerView adapter
        val grievanceText = grievances.joinToString("\n\n") { grievance ->
            val statusIcon = when (grievance.status) {
                GrievanceStatus.PENDING -> "⏳"
                GrievanceStatus.IN_PROGRESS -> "🔄"
                GrievanceStatus.RESOLVED -> "✅"
                GrievanceStatus.REJECTED -> "❌"
                else -> "📋"
            }
            
            val categoryIcon = when (grievance.category) {
                "Academic" -> "📚"
                "Hostel" -> "🏠"
                "Mess" -> "🍽️"
                "Technical" -> "💻"
                else -> "📋"
            }
            
            """
            $statusIcon Grievance ID: ${grievance.id}
            $categoryIcon Category: ${grievance.category}
            📝 Subject: ${grievance.subject}
            📊 Status: ${grievance.status}
            📅 Submitted: ${grievance.submittedDate}
            ${if (grievance.response != null) "💬 Response: ${grievance.response}" else ""}
            """.trimIndent()
        }

        // Show in a simple dialog for now
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("📋 Your Grievances")
            .setMessage(grievanceText)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showNewGrievanceDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_new_grievance, null)
        
        val etCategory = dialogView.findViewById<EditText>(R.id.et_category)
        val etSubject = dialogView.findViewById<EditText>(R.id.et_subject)
        val etDescription = dialogView.findViewById<EditText>(R.id.et_description)
        
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Submit New Grievance")
            .setView(dialogView)
            .setPositiveButton("Submit") { _, _ ->
                val category = etCategory.text.toString()
                val subject = etSubject.text.toString()
                val description = etDescription.text.toString()
                
                if (category.isNotEmpty() && subject.isNotEmpty() && description.isNotEmpty()) {
                    submitGrievance(category, subject, description)
                } else {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun submitGrievance(category: String, subject: String, description: String) {
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        
        val newGrievance = StudentGrievance(
            id = "GRV${System.currentTimeMillis()}",
            studentId = "STU001",
            category = category,
            subject = subject,
            description = description,
            status = GrievanceStatus.PENDING,
            submittedDate = currentDate,
            resolvedDate = null,
            attachmentPath = null,
            response = null
        )
        
        try {
            val success = repository.submitGrievance(newGrievance)
            if (success) {
                Toast.makeText(this, "Grievance submitted successfully", Toast.LENGTH_LONG).show()
                loadGrievances() // Refresh the list
            } else {
                Toast.makeText(this, "Failed to submit grievance", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error submitting grievance: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showGrievanceDetails(grievance: StudentGrievance) {
        val details = """
            Grievance Details:
            
            ID: ${grievance.id}
            Category: ${grievance.category}
            Subject: ${grievance.subject}
            Description: ${grievance.description}
            Status: ${grievance.status}
            Submitted: ${grievance.submittedDate}
            ${if (grievance.resolvedDate != null) "Resolved: ${grievance.resolvedDate}" else ""}
            ${if (grievance.response != null) "Response: ${grievance.response}" else ""}
        """.trimIndent()
        
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Grievance Details")
            .setMessage(details)
            .setPositiveButton("OK", null)
            .show()
    }
}