package com.vatty.mygbu

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vatty.mygbu.utils.BottomNavigationHelper
import com.vatty.mygbu.viewmodel.StudentDashboardViewModel
import de.hdodenhof.circleimageview.CircleImageView

class StudentProfileActivity : AppCompatActivity() {

    private lateinit var viewModel: StudentDashboardViewModel
    private lateinit var bottomNavigation: BottomNavigationView
    
    // Profile views
    private lateinit var ivProfileImage: CircleImageView
    private lateinit var tvStudentName: TextView
    private lateinit var tvRollNumber: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvProgram: TextView
    private lateinit var tvDepartment: TextView
    private lateinit var tvYear: TextView
    private lateinit var tvSemester: TextView
    private lateinit var tvAddress: TextView
    private lateinit var tvEmergencyContact: TextView
    private lateinit var tvDateOfBirth: TextView
    private lateinit var tvGender: TextView
    private lateinit var tvBloodGroup: TextView
    private lateinit var tvParentName: TextView
    private lateinit var tvParentPhone: TextView
    
    // Editable fields
    private lateinit var etPhone: EditText
    private lateinit var etAddress: EditText
    private lateinit var etEmergencyContact: EditText
    private lateinit var etParentPhone: EditText
    
    private lateinit var btnEdit: Button
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button
    
    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_profile)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[StudentDashboardViewModel::class.java]

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeViews()
        setupBottomNavigation()
        setupObservers()
        setupClickListeners()
        loadProfileData()
    }

    private fun initializeViews() {
        bottomNavigation = findViewById(R.id.bottom_navigation)
        
        // Profile image and basic info
        ivProfileImage = findViewById(R.id.iv_profile_image)
        tvStudentName = findViewById(R.id.tv_student_name)
        tvRollNumber = findViewById(R.id.tv_roll_number)
        tvEmail = findViewById(R.id.tv_email)
        tvPhone = findViewById(R.id.tv_phone)
        tvProgram = findViewById(R.id.tv_program)
        tvDepartment = findViewById(R.id.tv_department)
        tvYear = findViewById(R.id.tv_year)
        tvSemester = findViewById(R.id.tv_semester)
        
        // Personal details
        tvAddress = findViewById(R.id.tv_address)
        tvEmergencyContact = findViewById(R.id.tv_emergency_contact)
        tvDateOfBirth = findViewById(R.id.tv_date_of_birth)
        tvGender = findViewById(R.id.tv_gender)
        tvBloodGroup = findViewById(R.id.tv_blood_group)
        tvParentName = findViewById(R.id.tv_parent_name)
        tvParentPhone = findViewById(R.id.tv_parent_phone)
        
        // Editable fields
        etPhone = findViewById(R.id.et_phone)
        etAddress = findViewById(R.id.et_address)
        etEmergencyContact = findViewById(R.id.et_emergency_contact)
        etParentPhone = findViewById(R.id.et_parent_phone)
        
        // Buttons
        btnEdit = findViewById(R.id.btn_edit)
        btnSave = findViewById(R.id.btn_save)
        btnCancel = findViewById(R.id.btn_cancel)
        
        // Back button
        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupBottomNavigation() {
        BottomNavigationHelper.setupBottomNavigation(this, bottomNavigation, StudentProfileActivity::class.java)
    }

    private fun setupObservers() {
        viewModel.studentProfile.observe(this) { student ->
            displayProfileData(student)
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

    private fun loadProfileData() {
        // Profile data is already loaded in ViewModel
    }

    private fun displayProfileData(student: com.vatty.mygbu.data.model.Student) {
        tvStudentName.text = student.name
        tvRollNumber.text = "Roll No: ${student.rollNumber}"
        tvEmail.text = student.email
        tvPhone.text = student.phone
        tvProgram.text = student.program
        tvDepartment.text = student.department
        tvYear.text = "${student.year}${getOrdinalSuffix(student.year)} Year"
        tvSemester.text = "${student.semester}${getOrdinalSuffix(student.semester)} Semester"
        tvAddress.text = student.address
        tvEmergencyContact.text = student.emergencyContact
        tvDateOfBirth.text = student.dateOfBirth
        tvGender.text = student.gender
        tvBloodGroup.text = student.bloodGroup
        tvParentName.text = student.parentName
        tvParentPhone.text = student.parentPhone
        
        // Set editable fields
        etPhone.setText(student.phone)
        etAddress.setText(student.address)
        etEmergencyContact.setText(student.emergencyContact)
        etParentPhone.setText(student.parentPhone)
    }

    private fun enableEditMode() {
        isEditMode = true
        
        // Show editable fields
        findViewById<View>(R.id.layout_editable_fields).visibility = View.VISIBLE
        
        // Hide read-only fields
        findViewById<View>(R.id.layout_readonly_fields).visibility = View.GONE
        
        // Show save/cancel buttons, hide edit button
        btnEdit.visibility = View.GONE
        btnSave.visibility = View.VISIBLE
        btnCancel.visibility = View.VISIBLE
    }

    private fun cancelEditMode() {
        isEditMode = false
        
        // Hide editable fields
        findViewById<View>(R.id.layout_editable_fields).visibility = View.GONE
        
        // Show read-only fields
        findViewById<View>(R.id.layout_readonly_fields).visibility = View.VISIBLE
        
        // Show edit button, hide save/cancel buttons
        btnEdit.visibility = View.VISIBLE
        btnSave.visibility = View.GONE
        btnCancel.visibility = View.GONE
        
        // Reset editable fields to original values
        viewModel.studentProfile.value?.let { student ->
            etPhone.setText(student.phone)
            etAddress.setText(student.address)
            etEmergencyContact.setText(student.emergencyContact)
            etParentPhone.setText(student.parentPhone)
        }
    }

    private fun saveProfileChanges() {
        // Validate input
        if (etPhone.text.isNullOrBlank() || etAddress.text.isNullOrBlank() || 
            etEmergencyContact.text.isNullOrBlank() || etParentPhone.text.isNullOrBlank()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Here you would typically save to backend
        // For now, just update the UI
        tvPhone.text = etPhone.text.toString()
        tvAddress.text = etAddress.text.toString()
        tvEmergencyContact.text = etEmergencyContact.text.toString()
        tvParentPhone.text = etParentPhone.text.toString()
        
        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
        cancelEditMode()
    }

    private fun getOrdinalSuffix(number: Int): String {
        return when {
            number % 100 in 11..13 -> "th"
            number % 10 == 1 -> "st"
            number % 10 == 2 -> "nd"
            number % 10 == 3 -> "rd"
            else -> "th"
        }
    }
}