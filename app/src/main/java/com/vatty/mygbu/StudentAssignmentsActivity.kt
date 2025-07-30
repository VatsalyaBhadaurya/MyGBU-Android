package com.vatty.mygbu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vatty.mygbu.data.model.StudentAssignment
import com.vatty.mygbu.enums.AssignmentStatus
import com.vatty.mygbu.utils.BottomNavigationHelper
import com.vatty.mygbu.viewmodel.StudentDashboardViewModel

class StudentAssignmentsActivity : AppCompatActivity() {
    
    private lateinit var viewModel: StudentDashboardViewModel
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvNoAssignments: TextView
    private lateinit var fabSubmit: FloatingActionButton
    
    private lateinit var assignmentsAdapter: StudentAssignmentsAdapter
    
    // File picker for assignment submission
    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedFile ->
            handleFileSelection(selectedFile)
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_assignments)
        
        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[StudentDashboardViewModel::class.java]
        
        initializeViews()
        setupClickListeners()
        setupBottomNavigation()
        setupRecyclerView()
        setupObservers()
        
        // Load assignments data
        viewModel.loadStudentDashboardData()
    }
    
    private fun initializeViews() {
        bottomNavigation = findViewById(R.id.bottom_navigation)
        recyclerView = findViewById(R.id.recycler_view)
        tvNoAssignments = findViewById(R.id.tv_no_assignments)
        fabSubmit = findViewById(R.id.fab_submit)
        
        // Back button
        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            onBackPressed()
        }
    }
    
    private fun setupClickListeners() {
        fabSubmit.setOnClickListener {
            showAssignmentSubmissionDialog()
        }
    }
    
    private fun setupBottomNavigation() {
        BottomNavigationHelper.setupBottomNavigation(this, bottomNavigation, StudentAssignmentsActivity::class.java)
    }
    
    private fun setupRecyclerView() {
        assignmentsAdapter = StudentAssignmentsAdapter { assignment ->
            onAssignmentClicked(assignment)
        }
        
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@StudentAssignmentsActivity)
            adapter = assignmentsAdapter
        }
    }
    
    private fun setupObservers() {
        viewModel.recentAssignments.observe(this) { assignments ->
            if (assignments.isEmpty()) {
                tvNoAssignments.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                tvNoAssignments.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                assignmentsAdapter.submitList(assignments)
            }
        }
        
        viewModel.isLoading.observe(this) { isLoading ->
            // Show/hide loading indicator if needed
        }
        
        viewModel.errorMessage.observe(this) { error ->
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show()
            }
        }
    }
    
    private fun onAssignmentClicked(assignment: StudentAssignment) {
        when (assignment.status) {
            AssignmentStatus.PENDING -> {
                showAssignmentDetailsDialog(assignment)
            }
            AssignmentStatus.SUBMITTED -> {
                showSubmissionDetailsDialog(assignment)
            }
            AssignmentStatus.GRADED -> {
                showGradeDetailsDialog(assignment)
            }
            else -> {
                showAssignmentDetailsDialog(assignment)
            }
        }
    }
    
    private fun showAssignmentDetailsDialog(assignment: StudentAssignment) {
        val statusIcon = when (assignment.status) {
            AssignmentStatus.PENDING -> "⏳"
            AssignmentStatus.SUBMITTED -> "📤"
            AssignmentStatus.GRADED -> "✅"
            else -> "📋"
        }
        
        AlertDialog.Builder(this)
            .setTitle("📚 Assignment Details")
            .setMessage("""
                $statusIcon Title: ${assignment.title}
                📝 Description: ${assignment.description}
                📅 Due Date: ${assignment.dueDate}
                📊 Status: ${assignment.status}
                
                ${if (assignment.status == AssignmentStatus.PENDING) "💡 Click Submit to upload your assignment file." else ""}
            """.trimIndent())
            .setPositiveButton("Submit") { _, _ ->
                if (assignment.status == AssignmentStatus.PENDING) {
                    openFilePicker(assignment)
                }
            }
            .setNegativeButton("Close", null)
            .show()
    }
    
    private fun showSubmissionDetailsDialog(assignment: StudentAssignment) {
        AlertDialog.Builder(this)
            .setTitle("📤 Submission Details")
            .setMessage("""
                📚 Title: ${assignment.title}
                📅 Submitted On: ${assignment.submittedDate ?: "N/A"}
                📊 Status: ${assignment.status}
                
                ✅ Your assignment has been submitted successfully!
            """.trimIndent())
            .setPositiveButton("OK", null)
            .show()
    }
    
    private fun showGradeDetailsDialog(assignment: StudentAssignment) {
        AlertDialog.Builder(this)
            .setTitle("📊 Grade Details")
            .setMessage("""
                📚 Title: ${assignment.title}
                🎯 Score: ${assignment.score ?: "N/A"}
                💬 Feedback: ${assignment.feedback ?: "No feedback available"}
            """.trimIndent())
            .setPositiveButton("OK", null)
            .show()
    }
    
    private fun showAssignmentSubmissionDialog() {
        val subjects = listOf("Data Structures", "Algorithms", "Database Systems", "Computer Networks", "Software Engineering")
        
        AlertDialog.Builder(this)
            .setTitle("Submit Assignment")
            .setMessage("Select a subject to submit assignment for:")
            .setItems(subjects.toTypedArray()) { _, which ->
                val selectedSubject = subjects[which]
                // Create a mock assignment for submission
                val mockAssignment = StudentAssignment(
                    id = "ASG_${System.currentTimeMillis()}",
                    assignmentId = "ASS_${System.currentTimeMillis()}",
                    studentId = "STU001",
                    title = "Assignment for $selectedSubject",
                    description = "Please submit your assignment for $selectedSubject",
                    dueDate = "2024-02-15",
                    submittedDate = null,
                    status = AssignmentStatus.PENDING,
                    filePath = null,
                    score = null,
                    feedback = null
                )
                openFilePicker(mockAssignment)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun openFilePicker(assignment: StudentAssignment) {
        filePickerLauncher.launch("*/*")
    }
    
    private fun handleFileSelection(uri: Uri) {
        // In a real app, this would upload the file to the server
        Toast.makeText(this, "File selected: ${uri.lastPathSegment}", Toast.LENGTH_SHORT).show()
        
        // Simulate successful submission
        AlertDialog.Builder(this)
            .setTitle("Submission Successful")
            .setMessage("Your assignment has been submitted successfully!")
            .setPositiveButton("OK") { _, _ ->
                // Refresh the assignments list
                viewModel.loadStudentDashboardData()
            }
            .show()
    }
    
    override fun onBackPressed() {
        super.onBackPressed()
    }
}

// Adapter for assignments list
class StudentAssignmentsAdapter(
    private val onAssignmentClick: (StudentAssignment) -> Unit
) : androidx.recyclerview.widget.ListAdapter<StudentAssignment, StudentAssignmentsAdapter.AssignmentViewHolder>(
    AssignmentDiffCallback()
) {
    
    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): AssignmentViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student_assignment, parent, false)
        return AssignmentViewHolder(view, onAssignmentClick)
    }
    
    override fun onBindViewHolder(holder: AssignmentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class AssignmentViewHolder(
        itemView: View,
        private val onAssignmentClick: (StudentAssignment) -> Unit
    ) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        
        private val tvSubject: TextView = itemView.findViewById(R.id.tv_subject)
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        private val tvDueDate: TextView = itemView.findViewById(R.id.tv_due_date)
        private val tvStatus: TextView = itemView.findViewById(R.id.tv_status)
        private val cardView: com.google.android.material.card.MaterialCardView = itemView.findViewById(R.id.card_assignment)
        
        fun bind(assignment: StudentAssignment) {
            tvSubject.text = "Assignment" // Since there's no subject property
            tvTitle.text = assignment.title
            tvDueDate.text = "Due: ${assignment.dueDate}"
            tvStatus.text = assignment.status.toString()
            
            // Set status color based on status
            val colorRes = when (assignment.status) {
                AssignmentStatus.PENDING -> R.color.warning_orange
                AssignmentStatus.SUBMITTED -> R.color.primary
                AssignmentStatus.GRADED -> R.color.success_green
                else -> R.color.text_secondary
            }
            tvStatus.setTextColor(android.content.res.ColorStateList.valueOf(
                itemView.context.getColor(colorRes)
            ))
            
            cardView.setOnClickListener {
                onAssignmentClick(assignment)
            }
        }
    }
    
    class AssignmentDiffCallback : androidx.recyclerview.widget.DiffUtil.ItemCallback<StudentAssignment>() {
        override fun areItemsTheSame(oldItem: StudentAssignment, newItem: StudentAssignment): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: StudentAssignment, newItem: StudentAssignment): Boolean {
            return oldItem == newItem
        }
    }
}