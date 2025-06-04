package com.vatty.mygbu

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
    }
    
    private fun setupClickListeners() {
        etDueDate.setOnClickListener {
            showDatePicker()
        }
        
        btnAttachFile.setOnClickListener {
            // TODO: Implement file picker
            Toast.makeText(this, "File attachment feature", Toast.LENGTH_SHORT).show()
        }
        
        btnSaveAssignment.setOnClickListener {
            saveAssignment()
        }
        
        cardStudentSubmissions.setOnClickListener {
            // Navigate to student submissions
            Toast.makeText(this, "View Student Submissions", Toast.LENGTH_SHORT).show()
        }
        
        cardGradedAssignments.setOnClickListener {
            // Navigate to graded assignments
            Toast.makeText(this, "View Graded Assignments", Toast.LENGTH_SHORT).show()
        }
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
        
        // TODO: Save assignment to database
        Toast.makeText(this, "Assignment saved successfully!", Toast.LENGTH_SHORT).show()
        
        // Clear form
        etAssignmentTitle.setText("")
        etAssignmentDescription.setText("")
        etDueDate.setText("")
    }
} 