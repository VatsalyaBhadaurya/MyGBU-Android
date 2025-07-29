package com.vatty.mygbu.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vatty.mygbu.data.model.*
import com.vatty.mygbu.data.repository.StudentRepository

class StudentDashboardViewModel : ViewModel() {
    
    private val repository = StudentRepository()
    
    private val _studentProfile = MutableLiveData<Student>()
    val studentProfile: LiveData<Student> = _studentProfile
    
    private val _attendancePercentage = MutableLiveData<Double>()
    val attendancePercentage: LiveData<Double> = _attendancePercentage
    
    private val _overallCGPA = MutableLiveData<Double>()
    val overallCGPA: LiveData<Double> = _overallCGPA
    
    private val _pendingAssignments = MutableLiveData<Int>()
    val pendingAssignments: LiveData<Int> = _pendingAssignments
    
    private val _upcomingExams = MutableLiveData<Int>()
    val upcomingExams: LiveData<Int> = _upcomingExams
    
    private val _recentAssignments = MutableLiveData<List<StudentAssignment>>()
    val recentAssignments: LiveData<List<StudentAssignment>> = _recentAssignments
    
    private val _recentExams = MutableLiveData<List<StudentExam>>()
    val recentExams: LiveData<List<StudentExam>> = _recentExams
    
    private val _feeStatus = MutableLiveData<StudentFee>()
    val feeStatus: LiveData<StudentFee> = _feeStatus
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage
    
    init {
        loadStudentDashboardData()
    }
    
    fun loadStudentDashboardData() {
        _isLoading.value = true
        
        try {
            // Load student profile
            val student = repository.getStudentProfile("STU001")
            _studentProfile.value = student
            
            // Load dashboard metrics
            _attendancePercentage.value = repository.getAttendancePercentage("STU001")
            _overallCGPA.value = repository.getOverallCGPA("STU001")
            _pendingAssignments.value = repository.getPendingAssignmentsCount("STU001")
            _upcomingExams.value = repository.getUpcomingExamsCount("STU001")
            
            // Load recent assignments (limit to 3)
            val assignments = repository.getStudentAssignments("STU001").take(3)
            _recentAssignments.value = assignments
            
            // Load recent exams (limit to 3)
            val exams = repository.getStudentExams("STU001").take(3)
            _recentExams.value = exams
            
            // Load fee status
            val fees = repository.getStudentFees("STU001")
            if (fees.isNotEmpty()) {
                _feeStatus.value = fees.first()
            }
            
            _isLoading.value = false
            
        } catch (e: Exception) {
            _isLoading.value = false
            _errorMessage.value = "Failed to load dashboard data: ${e.message}"
        }
    }
    
    fun refreshData() {
        loadStudentDashboardData()
    }
    
    fun updateStudentProfile(updatedStudent: Student) {
        _studentProfile.value = updatedStudent
        // In a real app, this would also save to the repository/API
    }
    
    fun getGreeting(): String {
        val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        return when {
            hour < 12 -> "Good Morning"
            hour < 17 -> "Good Afternoon"
            else -> "Good Evening"
        }
    }
    
    fun getCurrentTime(): String {
        val sdf = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
        return sdf.format(java.util.Date())
    }
    
    fun getCurrentDate(): String {
        val sdf = java.text.SimpleDateFormat("EEEE, dd MMMM yyyy", java.util.Locale.getDefault())
        return sdf.format(java.util.Date())
    }
}