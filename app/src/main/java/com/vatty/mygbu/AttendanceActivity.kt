package com.vatty.mygbu

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.vatty.mygbu.data.model.StudentAttendance
import com.vatty.mygbu.databinding.ActivityAttendanceBinding
import com.vatty.mygbu.enums.AttendanceStatus
import com.vatty.mygbu.utils.DateUtils
import android.util.Log

class AttendanceActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityAttendanceBinding
    private lateinit var studentsAdapter: StudentsAttendanceAdapter
    private val studentsList = mutableListOf<StudentAttendance>()
    
    companion object {
        private const val TAG = "AttendanceActivity"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        // Log activity startup
        Log.i(TAG, "AttendanceActivity started")
        
        setupToolbar()
        setupStudentsList()
        setupRecyclerView()
        setupClickListeners()
        updateStats()
        setupCurrentDate()
    }
    
    private fun setupToolbar() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }
    
    private fun setupCurrentDate() {
        binding.tvClassDate.text = getString(R.string.today_date, DateUtils.getCurrentDateFormatted())
    }
    
    private fun setupStudentsList() {
        // Sample realistic student data using the new model
        studentsList.addAll(listOf(
            StudentAttendance("ST001", "Aarav Sharma", rollNumber = "2021001"),
            StudentAttendance("ST002", "Aditi Patel", rollNumber = "2021002"),
            StudentAttendance("ST003", "Arjun Singh", rollNumber = "2021003", status = AttendanceStatus.ABSENT),
            StudentAttendance("ST004", "Diya Gupta", rollNumber = "2021004"),
            StudentAttendance("ST005", "Ishaan Kumar", rollNumber = "2021005"),
            StudentAttendance("ST006", "Kavya Reddy", rollNumber = "2021006"),
            StudentAttendance("ST007", "Manas Verma", rollNumber = "2021007", status = AttendanceStatus.ABSENT),
            StudentAttendance("ST008", "Nisha Jain", rollNumber = "2021008"),
            StudentAttendance("ST009", "Priya Iyer", rollNumber = "2021009"),
            StudentAttendance("ST010", "Rohan Mehta", rollNumber = "2021010"),
            StudentAttendance("ST011", "Saanvi Agarwal", rollNumber = "2021011", status = AttendanceStatus.ABSENT),
            StudentAttendance("ST012", "Tanishq Bansal", rollNumber = "2021012"),
            StudentAttendance("ST013", "Urvi Shah", rollNumber = "2021013"),
            StudentAttendance("ST014", "Vihaan Nair", rollNumber = "2021014"),
            StudentAttendance("ST015", "Zara Khan", rollNumber = "2021015", status = AttendanceStatus.ABSENT),
            StudentAttendance("ST016", "Aditya Saxena", rollNumber = "2021016"),
            StudentAttendance("ST017", "Bhumika Tiwari", rollNumber = "2021017"),
            StudentAttendance("ST018", "Chirag Malhotra", rollNumber = "2021018"),
            StudentAttendance("ST019", "Devika Pillai", rollNumber = "2021019"),
            StudentAttendance("ST020", "Eshan Rao", rollNumber = "2021020"),
            StudentAttendance("ST021", "Falguni Desai", rollNumber = "2021021"),
            StudentAttendance("ST022", "Gaurav Kohli", rollNumber = "2021022"),
            StudentAttendance("ST023", "Harini Srinivasan", rollNumber = "2021023"),
            StudentAttendance("ST024", "Ishan Chopra", rollNumber = "2021024"),
            StudentAttendance("ST025", "Janhvi Thakur", rollNumber = "2021025"),
            StudentAttendance("ST026", "Karthik Bose", rollNumber = "2021026"),
            StudentAttendance("ST027", "Lavanya Mishra", rollNumber = "2021027"),
            StudentAttendance("ST028", "Mridul Das", rollNumber = "2021028"),
            StudentAttendance("ST029", "Navya Kapoor", rollNumber = "2021029"),
            StudentAttendance("ST030", "Om Pandey", rollNumber = "2021030"),
            StudentAttendance("ST031", "Parineeti Bhatt", rollNumber = "2021031"),
            StudentAttendance("ST032", "Quincy D'Souza", rollNumber = "2021032"),
            StudentAttendance("ST033", "Rhea Chandra", rollNumber = "2021033"),
            StudentAttendance("ST034", "Siddharth Joshi", rollNumber = "2021034"),
            StudentAttendance("ST035", "Tanya Srivastava", rollNumber = "2021035"),
            StudentAttendance("ST036", "Ujjwal Goyal", rollNumber = "2021036"),
            StudentAttendance("ST037", "Vidhi Ahluwalia", rollNumber = "2021037"),
            StudentAttendance("ST038", "Yash Singhal", rollNumber = "2021038"),
            StudentAttendance("ST039", "Zoya Bajaj", rollNumber = "2021039"),
            StudentAttendance("ST040", "Arnav Kulkarni", rollNumber = "2021040"),
            StudentAttendance("ST041", "Bhavya Randhawa", rollNumber = "2021041"),
            StudentAttendance("ST042", "Chinmay Ghosh", rollNumber = "2021042")
        ))
    }
    
    private fun setupRecyclerView() {
        studentsAdapter = StudentsAttendanceAdapter(studentsList) { position, status ->
            if (position >= 0 && position < studentsList.size) {
                val student = studentsList[position]
                student.status = status
                student.isPresent = status == AttendanceStatus.PRESENT
                
                Log.d(TAG, "Attendance updated for ${student.name}: ${student.status}")
                
                binding.rvStudents.post {
                    studentsAdapter.notifyItemChanged(position)
                    updateStats()
                }
            }
        }
        binding.rvStudents.layoutManager = LinearLayoutManager(this)
        binding.rvStudents.adapter = studentsAdapter
    }
    
    private fun setupClickListeners() {
        binding.btnMarkAllPresent.setOnClickListener {
            markAllStudents(true)
        }
        
        binding.btnMarkAllAbsent.setOnClickListener {
            markAllStudents(false)
        }
        
        binding.btnSubmitAttendance.setOnClickListener {
            submitAttendance()
        }
    }
    
    private fun markAllStudents(isPresent: Boolean) {
        studentsList.forEach { 
            it.status = if (isPresent) AttendanceStatus.PRESENT else AttendanceStatus.ABSENT
            it.isPresent = isPresent
        }
        
        // Post the update to avoid RecyclerView layout issues
        binding.rvStudents.post {
            studentsAdapter.notifyDataSetChanged()
            updateStats()
        }
        
        val message = if (isPresent) {
            "All students marked present"
        } else {
            "All students marked absent"
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        Log.d(TAG, "Marked all students: $isPresent")
    }
    
    private fun updateStats() {
        val totalStudents = studentsList.size
        val presentCount = studentsList.count { it.status == AttendanceStatus.PRESENT }
        val lateCount = studentsList.count { it.status == AttendanceStatus.LATE }
        val absentCount = studentsList.count { it.status == AttendanceStatus.ABSENT }
        
        binding.tvTotalStudents.text = totalStudents.toString()
        binding.tvPresentCount.text = (presentCount + lateCount).toString() // Include late as present
        binding.tvAbsentCount.text = absentCount.toString()
    }
    
    private fun submitAttendance() {
        val topicsCovered = binding.etTopicsCovered.text.toString().trim()
        
        if (topicsCovered.isEmpty()) {
            // Test error logging
            Log.w(TAG, "User tried to submit attendance without entering topics covered")
            Toast.makeText(this, "Please enter topics covered", Toast.LENGTH_SHORT).show()
            return
        }
        
        val presentCount = studentsList.count { it.status == AttendanceStatus.PRESENT }
        val lateCount = studentsList.count { it.status == AttendanceStatus.LATE }
        val totalStudents = studentsList.size
        val attendancePercentage = ((presentCount + lateCount) * 100) / totalStudents
        
        // Test low attendance warning
        if (attendancePercentage < 75) {
            Log.w(TAG, "Low attendance detected: $attendancePercentage% (${presentCount + lateCount}/$totalStudents)")
        }
        
        Toast.makeText(
            this, 
            getString(R.string.attendance_marked_successfully) + 
            "\n${presentCount + lateCount}/$totalStudents present ($attendancePercentage%)", 
            Toast.LENGTH_LONG
        ).show()
        
        Log.d(TAG, "Attendance submitted: ${presentCount + lateCount}/$totalStudents present ($attendancePercentage%)")
        
        // Clear form
        binding.etTopicsCovered.setText("")
        binding.etRemarks.setText("")
        
        finish()
    }
} 