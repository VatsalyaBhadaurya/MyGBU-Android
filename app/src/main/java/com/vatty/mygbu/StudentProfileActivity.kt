package com.vatty.mygbu

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.vatty.mygbu.data.model.Student
import com.vatty.mygbu.utils.BottomNavigationHelper
import com.vatty.mygbu.viewmodel.StudentDashboardViewModel

class StudentProfileActivity : AppCompatActivity() {
    
    private lateinit var viewModel: StudentDashboardViewModel
    private lateinit var bottomNavigation: BottomNavigationView
    
    // UI Elements
    private lateinit var tvStudentName: TextView
    private lateinit var tvRollNumber: TextView
    private lateinit var tvDepartment: TextView
    private lateinit var tvYear: TextView
    private lateinit var tvSemester: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvDateOfBirth: TextView
    private lateinit var tvGender: TextView
    private lateinit var tvBloodGroup: TextView
    
    // Editable fields
    private lateinit var etPhone: TextInputEditText
    private lateinit var etAddress: TextInputEditText
    private lateinit var etEmergencyContact: TextInputEditText
    private lateinit var etParentPhone: TextInputEditText
    
    // Layout containers
    private lateinit var layoutReadonlyFields: LinearLayout
    private lateinit var layoutEditableFields: LinearLayout
    
    // Buttons
    private lateinit var btnEdit: Button
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button
    
    private var isEditMode = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_profile)
        
        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[StudentDashboardViewModel::class.java]
        
        initializeViews()
        setupClickListeners()
        setupBottomNavigation()
        setupObservers()
        
        // Load student data
        viewModel.loadStudentDashboardData()
    }
    
    private fun initializeViews() {
        bottomNavigation = findViewById(R.id.bottom_navigation)
        
        // Read-only fields
        tvStudentName = findViewById(R.id.tv_student_name)
        tvRollNumber = findViewById(R.id.tv_roll_number)
        tvDepartment = findViewById(R.id.tv_department)
        tvYear = findViewById(R.id.tv_year)
        tvSemester = findViewById(R.id.tv_semester)
        tvEmail = findViewById(R.id.tv_email)
        tvDateOfBirth = findViewById(R.id.tv_date_of_birth)
        tvGender = findViewById(R.id.tv_gender)
        tvBloodGroup = findViewById(R.id.tv_blood_group)
        
        // Editable fields
        etPhone = findViewById(R.id.et_phone)
        etAddress = findViewById(R.id.et_address)
        etEmergencyContact = findViewById(R.id.et_emergency_contact)
        etParentPhone = findViewById(R.id.et_parent_phone)
        
        // Layout containers
        layoutReadonlyFields = findViewById(R.id.layout_readonly_fields)
        layoutEditableFields = findViewById(R.id.layout_editable_fields)
        
        // Buttons
        btnEdit = findViewById(R.id.btn_edit)
        btnSave = findViewById(R.id.btn_save)
        btnCancel = findViewById(R.id.btn_cancel)
        
        // Back button
        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            onBackPressed()
        }
    }
    
    private fun setupClickListeners() {
        btnEdit.setOnClickListener {
            enableEditMode()
        }
        
        btnSave.setOnClickListener {
            saveProfileChanges()
        }
        
        btnCancel.setOnClickListener {
            cancelEditMode()
        }
    }
    
    private fun setupBottomNavigation() {
        BottomNavigationHelper.setupBottomNavigation(this, bottomNavigation, StudentProfileActivity::class.java)
    }
    
    private fun setupObservers() {
        viewModel.studentProfile.observe(this) { student ->
            displayStudentData(student)
        }
    }
    
    private fun displayStudentData(student: Student) {
        // Display read-only information
        tvStudentName.text = student.name
        tvRollNumber.text = student.rollNumber
        tvDepartment.text = student.department
        tvYear.text = "${student.year} Year"
        tvSemester.text = "${student.semester} Semester"
        tvEmail.text = student.email
        tvDateOfBirth.text = student.dateOfBirth
        tvGender.text = student.gender
        tvBloodGroup.text = student.bloodGroup
        
        // Set editable fields with current values
        etPhone.setText(student.phone)
        etAddress.setText(student.address)
        etEmergencyContact.setText(student.emergencyContact)
        etParentPhone.setText(student.parentPhone)
    }
    
    private fun enableEditMode() {
        isEditMode = true
        layoutEditableFields.visibility = View.VISIBLE
        layoutReadonlyFields.visibility = View.GONE
        btnEdit.visibility = View.GONE
        btnSave.visibility = View.VISIBLE
        btnCancel.visibility = View.VISIBLE
    }
    
    private fun cancelEditMode() {
        isEditMode = false
        layoutEditableFields.visibility = View.GONE
        layoutReadonlyFields.visibility = View.VISIBLE
        btnEdit.visibility = View.VISIBLE
        btnSave.visibility = View.GONE
        btnCancel.visibility = View.GONE
        
        // Reset fields to original values
        viewModel.studentProfile.value?.let { student ->
            etPhone.setText(student.phone)
            etAddress.setText(student.address)
            etEmergencyContact.setText(student.emergencyContact)
            etParentPhone.setText(student.parentPhone)
        }
    }
    
    private fun saveProfileChanges() {
        val phone = etPhone.text.toString().trim()
        val address = etAddress.text.toString().trim()
        val emergencyContact = etEmergencyContact.text.toString().trim()
        val parentPhone = etParentPhone.text.toString().trim()
        
        // Validate input
        if (phone.isEmpty() || address.isEmpty() || emergencyContact.isEmpty() || parentPhone.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }
        
        if (phone.length < 10) {
            Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Save changes (in a real app, this would call an API)
        viewModel.studentProfile.value?.let { student ->
            val updatedStudent = student.copy(
                phone = phone,
                address = address,
                emergencyContact = emergencyContact,
                parentPhone = parentPhone
            )
            
            // Update the ViewModel
            viewModel.updateStudentProfile(updatedStudent)
            
            // Exit edit mode
            isEditMode = false
            layoutEditableFields.visibility = View.GONE
            layoutReadonlyFields.visibility = View.VISIBLE
            btnEdit.visibility = View.VISIBLE
            btnSave.visibility = View.GONE
            btnCancel.visibility = View.GONE
            
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun onBackPressed() {
        if (isEditMode) {
            cancelEditMode()
        } else {
            super.onBackPressed()
        }
    }
}