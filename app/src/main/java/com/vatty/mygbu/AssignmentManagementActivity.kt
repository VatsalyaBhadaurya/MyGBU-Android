package com.vatty.mygbu

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.vatty.mygbu.data.model.SavedAssignment
import com.vatty.mygbu.databinding.ActivityAssignmentManagementBinding
import com.vatty.mygbu.enums.AssignmentStatus
import com.vatty.mygbu.utils.DateUtils
import com.vatty.mygbu.utils.ErrorHandler
import com.vatty.mygbu.utils.LogWrapper as Log
import java.util.*

class AssignmentManagementActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityAssignmentManagementBinding
    private var attachedFileUri: Uri? = null
    private var attachedFileName: String? = null
    private val savedAssignments = mutableListOf<SavedAssignment>()
    private lateinit var assignmentsAdapter: SavedAssignmentsAdapter
    private lateinit var errorHandler: ErrorHandler
    
    companion object {
        private const val TAG = "AssignmentManagement"
    }
    
    // File picker launcher
    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            attachedFileUri = it
            attachedFileName = getFileName(it)
            binding.tvAttachedFile.text = getString(R.string.file_attached, attachedFileName ?: getString(R.string.unknown_file))
            binding.tvAttachedFile.isVisible = true
            Toast.makeText(this, getString(R.string.file_attached, attachedFileName ?: getString(R.string.unknown_file)), Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssignmentManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        // Test LogWrapper - this will now be sent to Telegram automatically!
        Log.i(TAG, "AssignmentManagementActivity started - app-wide monitoring active!")
        
        errorHandler = ErrorHandler(this)
        setupViews()
        setupRecyclerView()
        loadSampleAssignments()
    }
    
    private fun setupViews() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnCreateAssignment.setOnClickListener {
            createAssignment()
        }

        binding.btnAttachFile.setOnClickListener {
            // Launch file picker for documents
            filePickerLauncher.launch("*/*")
        }

        binding.etDueDate.setOnClickListener {
            showDatePicker()
        }

        binding.fabAddAssignment.setOnClickListener {
            // TODO: Show assignment creation form
        }
    }
    
    private fun setupRecyclerView() {
        assignmentsAdapter = SavedAssignmentsAdapter(savedAssignments)
        binding.rvSavedAssignments.layoutManager = LinearLayoutManager(this)
        binding.rvSavedAssignments.adapter = assignmentsAdapter
    }
    
    private fun loadSampleAssignments() {
        savedAssignments.addAll(listOf(
            SavedAssignment(
                title = "Data Structures Assignment 1",
                courseCode = "CS201",
                dueDate = "25/12/2024",
                status = AssignmentStatus.ACTIVE,
                attachmentName = "assignment1.pdf"
            ),
            SavedAssignment(
                title = "Programming Lab Exercise",
                courseCode = "CS101",
                dueDate = "20/12/2024",
                status = AssignmentStatus.DRAFT,
                attachmentName = "lab_exercise.docx"
            )
        ))
        assignmentsAdapter.notifyDataSetChanged()
    }
    
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = DateUtils.formatDate(
                    Calendar.getInstance().apply {
                        set(year, month, dayOfMonth)
                    }.time
                )
                binding.etDueDate.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
    
    private fun createAssignment() {
        try {
            errorHandler.showLoadingState()
            val title = binding.etAssignmentTitle.text.toString().trim()
            val description = binding.etAssignmentDescription.text.toString().trim()
            val dueDate = binding.etDueDate.text.toString().trim()
            
            if (validateInput(title, description, dueDate)) {
                // Create new assignment
                val newAssignment = SavedAssignment(
                    title = title,
                    courseCode = "CS101", // Default course
                    dueDate = dueDate,
                    status = AssignmentStatus.DRAFT,
                    attachmentName = attachedFileName
                )
                
                // Add to saved assignments
                savedAssignments.add(0, newAssignment) // Add at top
                assignmentsAdapter.notifyItemInserted(0)
                
                Toast.makeText(this, getString(R.string.assignment_created_successfully), Toast.LENGTH_SHORT).show()
                Log.d(TAG, "Assignment created: $title")
                
                // Clear form
                clearForm()
                
                // TODO: Save assignment to backend
                android.os.Handler().postDelayed({
                    errorHandler.hideLoadingState()
                    finish()
                }, 1000)
            }
        } catch (e: Exception) {
            errorHandler.showError(e)
        }
    }
    
    private fun validateInput(title: String, description: String, dueDate: String): Boolean {
        if (title.isEmpty()) {
            binding.etAssignmentTitle.error = "Title is required"
            return false
        }
        if (description.isEmpty()) {
            binding.etAssignmentDescription.error = "Description is required"
            return false
        }
        if (dueDate.isEmpty()) {
            binding.etDueDate.error = "Due date is required"
            return false
        }
        return true
    }
    
    private fun clearForm() {
        binding.etAssignmentTitle.setText("")
        binding.etAssignmentDescription.setText("")
        binding.etDueDate.setText("")
        attachedFileUri = null
        attachedFileName = null
        binding.tvAttachedFile.isVisible = false
    }
    
    private fun getFileName(uri: Uri): String? {
        return try {
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val nameIndex = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                    if (nameIndex >= 0) it.getString(nameIndex) else getString(R.string.unknown_file)
                } else getString(R.string.unknown_file)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting file name: ${e.message}")
            getString(R.string.unknown_file)
        }
    }
} 