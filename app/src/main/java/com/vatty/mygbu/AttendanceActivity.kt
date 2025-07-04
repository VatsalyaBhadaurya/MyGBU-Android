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
import com.vatty.mygbu.utils.ErrorHandler
import com.vatty.mygbu.utils.LogWrapper as Log

class AttendanceActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityAttendanceBinding
    private lateinit var adapter: StudentsAttendanceAdapter
    private lateinit var errorHandler: ErrorHandler
    private val studentsList = mutableListOf<StudentAttendance>()
    
    companion object {
        private const val TAG = "AttendanceActivity"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        errorHandler = ErrorHandler(this)
        
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        // Test LogWrapper - this will now be sent to Telegram automatically!
        Log.i(TAG, "AttendanceActivity started - monitoring enabled across the app!")
        
        setupViews()
        setupListeners()
        loadAttendanceData()
    }

    private fun setupViews() {
        setupToolbar()
        setupCurrentDate()
        setupStudentsList()
        setupRecyclerView()
        updateStats()
    }
    
    private fun setupToolbar() {
        binding.ivBack?.setOnClickListener {
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
            StudentAttendance("ST015", "Zara Khan", rollNumber = "2021015", status = AttendanceStatus.ABSENT)
        ))
    }
    
    private fun setupRecyclerView() {
        adapter = StudentsAttendanceAdapter(studentsList) { position, isChecked ->
            if (position >= 0 && position < studentsList.size) {
                val student = studentsList[position]
                student.updateAttendance(isChecked)
                
                Log.d(TAG, "Attendance toggled for ${student.name}: ${student.status}")
                
                binding.rvStudents.post {
                    adapter.notifyItemChanged(position)
                    updateStats()
                }
            }
        }
        binding.rvStudents.layoutManager = LinearLayoutManager(this)
        binding.rvStudents.adapter = adapter
    }
    
    private fun setupListeners() {
        binding.btnMarkAllPresent.setOnClickListener {
            markAllStudents(true)
        }
        
        binding.btnMarkAllAbsent.setOnClickListener {
            markAllStudents(false)
        }
        
        binding.btnSubmitAttendance.setOnClickListener {
            saveAttendance()
        }
    }
    
    private fun markAllStudents(isPresent: Boolean) {
        studentsList.forEach { it.updateAttendance(isPresent) }
        
        // Post the update to avoid RecyclerView layout issues
        binding.rvStudents.post {
            adapter.notifyDataSetChanged()
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
        val presentCount = studentsList.count { it.isPresent }
        val absentCount = totalStudents - presentCount
        
        binding.tvTotalStudents.text = getString(R.string.total_students, totalStudents)
        binding.tvPresentCount.text = getString(R.string.present_count, presentCount)
        binding.tvAbsentCount.text = getString(R.string.absent_count, absentCount)
    }
    
    private fun loadAttendanceData() {
        try {
            errorHandler.showLoadingState()
            // TODO: Load actual student data
            adapter.submitList(studentsList)
            updateStats()
            errorHandler.hideLoadingState()
        } catch (e: Exception) {
            errorHandler.showError(e)
        }
    }
    
    private fun saveAttendance() {
        try {
            val topicsCovered = binding.etTopicsCovered.text.toString()
            val remarks = binding.etRemarks.text.toString()

            if (topicsCovered.isEmpty()) {
                binding.etTopicsCovered.error = "Please enter topics covered"
                return
            }

            // TODO: Save attendance data to backend
            errorHandler.showLoadingState()
            // Simulate API call
            android.os.Handler().postDelayed({
                errorHandler.hideLoadingState()
                finish()
            }, 1000)

        } catch (e: Exception) {
            errorHandler.showError(e)
        }
    }
} 