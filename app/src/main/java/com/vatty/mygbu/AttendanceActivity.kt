package com.vatty.mygbu

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class AttendanceActivity : AppCompatActivity() {
    
    private lateinit var ivBack: ImageView
    private lateinit var tvCourseName: TextView
    private lateinit var tvClassDate: TextView
    private lateinit var tvTotalStudents: TextView
    private lateinit var tvPresentCount: TextView
    private lateinit var tvAbsentCount: TextView
    private lateinit var etTopicsCovered: TextInputEditText
    private lateinit var etRemarks: TextInputEditText
    private lateinit var btnMarkAllPresent: MaterialButton
    private lateinit var btnMarkAllAbsent: MaterialButton
    private lateinit var btnSubmitAttendance: MaterialButton
    private lateinit var rvStudents: RecyclerView
    
    private lateinit var studentsAdapter: StudentsAttendanceAdapter
    private val studentsList = mutableListOf<StudentAttendance>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_attendance)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        initializeViews()
        setupBackButton()
        setupStudentsList()
        setupRecyclerView()
        setupClickListeners()
        updateStats()
        setupCurrentDate()
    }
    
    private fun initializeViews() {
        ivBack = findViewById(R.id.iv_back)
        tvCourseName = findViewById(R.id.tv_course_name)
        tvClassDate = findViewById(R.id.tv_class_date)
        tvTotalStudents = findViewById(R.id.tv_total_students)
        tvPresentCount = findViewById(R.id.tv_present_count)
        tvAbsentCount = findViewById(R.id.tv_absent_count)
        etTopicsCovered = findViewById(R.id.et_topics_covered)
        etRemarks = findViewById(R.id.et_remarks)
        btnMarkAllPresent = findViewById(R.id.btn_mark_all_present)
        btnMarkAllAbsent = findViewById(R.id.btn_mark_all_absent)
        btnSubmitAttendance = findViewById(R.id.btn_submit_attendance)
        rvStudents = findViewById(R.id.rv_students)
    }
    
    private fun setupBackButton() {
        ivBack.setOnClickListener {
            finish()
        }
    }
    
    private fun setupCurrentDate() {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        tvClassDate.text = dateFormat.format(Date())
    }
    
    private fun setupStudentsList() {
        // Sample realistic student data
        studentsList.addAll(listOf(
            StudentAttendance("2021001", "Aarav Sharma", true),
            StudentAttendance("2021002", "Aditi Patel", true),
            StudentAttendance("2021003", "Arjun Singh", false),
            StudentAttendance("2021004", "Diya Gupta", true),
            StudentAttendance("2021005", "Ishaan Kumar", true),
            StudentAttendance("2021006", "Kavya Reddy", true),
            StudentAttendance("2021007", "Manas Verma", false),
            StudentAttendance("2021008", "Nisha Jain", true),
            StudentAttendance("2021009", "Priya Iyer", true),
            StudentAttendance("2021010", "Rohan Mehta", true),
            StudentAttendance("2021011", "Saanvi Agarwal", false),
            StudentAttendance("2021012", "Tanishq Bansal", true),
            StudentAttendance("2021013", "Urvi Shah", true),
            StudentAttendance("2021014", "Vihaan Nair", true),
            StudentAttendance("2021015", "Zara Khan", false),
            StudentAttendance("2021016", "Aditya Saxena", true),
            StudentAttendance("2021017", "Bhumika Tiwari", true),
            StudentAttendance("2021018", "Chirag Malhotra", true),
            StudentAttendance("2021019", "Devika Pillai", true),
            StudentAttendance("2021020", "Eshan Rao", true),
            StudentAttendance("2021021", "Falguni Desai", true),
            StudentAttendance("2021022", "Gaurav Kohli", true),
            StudentAttendance("2021023", "Harini Srinivasan", true),
            StudentAttendance("2021024", "Ishan Chopra", true),
            StudentAttendance("2021025", "Janhvi Thakur", true),
            StudentAttendance("2021026", "Karthik Bose", true),
            StudentAttendance("2021027", "Lavanya Mishra", true),
            StudentAttendance("2021028", "Mridul Das", true),
            StudentAttendance("2021029", "Navya Kapoor", true),
            StudentAttendance("2021030", "Om Pandey", true),
            StudentAttendance("2021031", "Parineeti Bhatt", true),
            StudentAttendance("2021032", "Quincy D'Souza", true),
            StudentAttendance("2021033", "Rhea Chandra", true),
            StudentAttendance("2021034", "Siddharth Joshi", true),
            StudentAttendance("2021035", "Tanya Srivastava", true),
            StudentAttendance("2021036", "Ujjwal Goyal", true),
            StudentAttendance("2021037", "Vidhi Ahluwalia", true),
            StudentAttendance("2021038", "Yash Singhal", true),
            StudentAttendance("2021039", "Zoya Bajaj", true),
            StudentAttendance("2021040", "Arnav Kulkarni", true),
            StudentAttendance("2021041", "Bhavya Randhawa", true),
            StudentAttendance("2021042", "Chinmay Ghosh", true)
        ))
    }
    
    private fun setupRecyclerView() {
        studentsAdapter = StudentsAttendanceAdapter(studentsList) { position ->
            // Check if position is valid
            if (position >= 0 && position < studentsList.size) {
                // Toggle attendance status
                studentsList[position].isPresent = !studentsList[position].isPresent
                
                // Post the adapter update to avoid RecyclerView layout issues
                rvStudents.post {
                    studentsAdapter.notifyItemChanged(position)
                    updateStats()
                }
            }
        }
        rvStudents.layoutManager = LinearLayoutManager(this)
        rvStudents.adapter = studentsAdapter
    }
    
    private fun setupClickListeners() {
        btnMarkAllPresent.setOnClickListener {
            markAllStudents(true)
        }
        
        btnMarkAllAbsent.setOnClickListener {
            markAllStudents(false)
        }
        
        btnSubmitAttendance.setOnClickListener {
            submitAttendance()
        }
    }
    
    private fun markAllStudents(isPresent: Boolean) {
        studentsList.forEach { it.isPresent = isPresent }
        
        // Post the update to avoid RecyclerView layout issues
        rvStudents.post {
            studentsAdapter.notifyDataSetChanged()
            updateStats()
        }
        
        val message = if (isPresent) "All students marked present" else "All students marked absent"
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    
    private fun updateStats() {
        val totalStudents = studentsList.size
        val presentCount = studentsList.count { it.isPresent }
        val absentCount = totalStudents - presentCount
        
        tvTotalStudents.text = totalStudents.toString()
        tvPresentCount.text = presentCount.toString()
        tvAbsentCount.text = absentCount.toString()
    }
    
    private fun submitAttendance() {
        val topicsCovered = etTopicsCovered.text.toString().trim()
        val remarks = etRemarks.text.toString().trim()
        
        if (topicsCovered.isEmpty()) {
            Toast.makeText(this, "Please enter topics covered", Toast.LENGTH_SHORT).show()
            return
        }
        
        val presentCount = studentsList.count { it.isPresent }
        val totalStudents = studentsList.size
        val attendancePercentage = (presentCount * 100) / totalStudents
        
        Toast.makeText(
            this, 
            "Attendance submitted successfully!\n$presentCount/$totalStudents present ($attendancePercentage%)", 
            Toast.LENGTH_LONG
        ).show()
        
        // Clear form
        etTopicsCovered.setText("")
        etRemarks.setText("")
        
        finish()
    }
}

data class StudentAttendance(
    val rollNumber: String,
    val name: String,
    var isPresent: Boolean
) 