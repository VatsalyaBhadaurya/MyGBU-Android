package com.vatty.mygbu

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.card.MaterialCardView
import java.util.*

class AssignmentManagementActivity : AppCompatActivity() {
    
    private lateinit var etAssignmentTitle: TextInputEditText
    private lateinit var etAssignmentDescription: TextInputEditText
    private lateinit var etDueDate: TextInputEditText
    private lateinit var btnAttachFile: MaterialButton
    private lateinit var btnSaveAssignment: MaterialButton
    private lateinit var cardStudentSubmissions: MaterialCardView
    private lateinit var cardGradedAssignments: MaterialCardView
    private lateinit var ivBack: ImageView
    private lateinit var tvAttachedFile: TextView
    private lateinit var rvSavedAssignments: RecyclerView
    
    private var attachedFileUri: Uri? = null
    private var attachedFileName: String? = null
    private val savedAssignments = mutableListOf<SavedAssignment>()
    private lateinit var assignmentsAdapter: SavedAssignmentsAdapter
    
    // File picker launcher
    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            attachedFileUri = it
            attachedFileName = getFileName(it)
            tvAttachedFile.text = "📎 ${attachedFileName ?: "Unknown file"}"
            tvAttachedFile.visibility = android.view.View.VISIBLE
            Toast.makeText(this, "File attached successfully", Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_assignment_management)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        initializeViews()
        setupBackButton()
        setupClickListeners()
        setupRecyclerView()
        loadSampleAssignments()
    }
    
    private fun setupBackButton() {
        ivBack.setOnClickListener {
            finish()
        }
    }
    
    private fun initializeViews() {
        etAssignmentTitle = findViewById(R.id.et_assignment_title)
        etAssignmentDescription = findViewById(R.id.et_assignment_description)
        etDueDate = findViewById(R.id.et_due_date)
        btnAttachFile = findViewById(R.id.btn_attach_file)
        btnSaveAssignment = findViewById(R.id.btn_save_assignment)
        cardStudentSubmissions = findViewById(R.id.card_student_submissions)
        cardGradedAssignments = findViewById(R.id.card_graded_assignments)
        ivBack = findViewById(R.id.iv_back)
        tvAttachedFile = findViewById(R.id.tv_attached_file)
        rvSavedAssignments = findViewById(R.id.rv_saved_assignments)
    }
    
    private fun setupClickListeners() {
        etDueDate.setOnClickListener {
            showDatePicker()
        }
        
        btnAttachFile.setOnClickListener {
            // Launch file picker for documents
            filePickerLauncher.launch("*/*")
        }
        
        btnSaveAssignment.setOnClickListener {
            saveAssignment()
        }
        
        cardStudentSubmissions.setOnClickListener {
            // Navigate to student submissions
            Toast.makeText(this, "View Student Submissions - 15 submitted, 8 pending", Toast.LENGTH_SHORT).show()
        }
        
        cardGradedAssignments.setOnClickListener {
            // Navigate to graded assignments
            Toast.makeText(this, "View Graded Assignments - 12 graded", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun setupRecyclerView() {
        assignmentsAdapter = SavedAssignmentsAdapter(savedAssignments)
        rvSavedAssignments.layoutManager = LinearLayoutManager(this)
        rvSavedAssignments.adapter = assignmentsAdapter
    }
    
    private fun loadSampleAssignments() {
        savedAssignments.addAll(listOf(
            SavedAssignment(
                "Data Structures Assignment 1",
                "Implement Binary Search Tree with insertion and deletion operations",
                "25/12/2024",
                "assignment1.pdf",
                "Published",
                "CS201"
            ),
            SavedAssignment(
                "Programming Lab Exercise",
                "Create a GUI application using Java Swing",
                "20/12/2024",
                "lab_exercise.docx",
                "Draft",
                "CS101"
            )
        ))
        assignmentsAdapter.notifyDataSetChanged()
    }
    
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                etDueDate.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
    
    private fun saveAssignment() {
        val title = etAssignmentTitle.text.toString().trim()
        val description = etAssignmentDescription.text.toString().trim()
        val dueDate = etDueDate.text.toString().trim()
        
        if (title.isEmpty() || description.isEmpty() || dueDate.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Create new assignment
        val newAssignment = SavedAssignment(
            title = title,
            description = description,
            dueDate = dueDate,
            attachedFile = attachedFileName ?: "No file attached",
            status = "Draft",
            courseCode = "CS101" // Default course
        )
        
        // Add to saved assignments
        savedAssignments.add(0, newAssignment) // Add at top
        assignmentsAdapter.notifyItemInserted(0)
        
        Toast.makeText(this, "Assignment saved successfully!", Toast.LENGTH_SHORT).show()
        
        // Clear form
        clearForm()
    }
    
    private fun clearForm() {
        etAssignmentTitle.setText("")
        etAssignmentDescription.setText("")
        etDueDate.setText("")
        attachedFileUri = null
        attachedFileName = null
        tvAttachedFile.visibility = android.view.View.GONE
    }
    
    private fun getFileName(uri: Uri): String? {
        return try {
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val nameIndex = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                    if (nameIndex >= 0) it.getString(nameIndex) else "Unknown file"
                } else "Unknown file"
            }
        } catch (e: Exception) {
            "Unknown file"
        }
    }
}

data class SavedAssignment(
    val title: String,
    val description: String,
    val dueDate: String,
    val attachedFile: String,
    val status: String,
    val courseCode: String
) 