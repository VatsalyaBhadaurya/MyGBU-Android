package com.vatty.mygbu.data.model

import com.vatty.mygbu.enums.AttendanceStatus
import com.vatty.mygbu.enums.AssignmentStatus
import com.vatty.mygbu.enums.LeaveStatus
import com.vatty.mygbu.enums.PlacementStatus
import com.vatty.mygbu.enums.GrievanceStatus

// Main Student data class
data class Student(
    val id: String,
    val name: String,
    val rollNumber: String,
    val email: String,
    val phone: String,
    val profileImage: Int = 0,
    val program: String,
    val department: String,
    val year: Int,
    val semester: Int,
    val address: String,
    val emergencyContact: String,
    val dateOfBirth: String,
    val gender: String,
    val bloodGroup: String,
    val parentName: String,
    val parentPhone: String
)

// Student Attendance for faculty view
data class StudentAttendance(
    val studentId: String,
    val name: String,
    val profileImage: Int = 0,
    var status: AttendanceStatus = AttendanceStatus.PRESENT,
    val rollNumber: String = "",
    var isPresent: Boolean = true // For backward compatibility
) {
    // Sync status with isPresent for backward compatibility
    init {
        isPresent = status == AttendanceStatus.PRESENT
    }
    
    // Method to update both status and isPresent together
    fun updateAttendance(present: Boolean) {
        isPresent = present
        status = if (present) AttendanceStatus.PRESENT else AttendanceStatus.ABSENT
    }
}

// Student Academic Performance
data class StudentPerformance(
    val studentId: String,
    val courseId: String,
    val courseName: String,
    val attendancePercentage: Double,
    val assignmentScore: Double,
    val examScore: Double,
    val totalScore: Double,
    val grade: String
)

// Student Fee Details
data class StudentFee(
    val studentId: String,
    val semester: Int,
    val totalAmount: Double,
    val paidAmount: Double,
    val pendingAmount: Double,
    val dueDate: String,
    val isPaid: Boolean = false
)

// Student Assignment Submission
data class StudentAssignment(
    val id: String,
    val assignmentId: String,
    val studentId: String,
    val title: String,
    val description: String,
    val dueDate: String,
    val submittedDate: String?,
    val status: AssignmentStatus,
    val filePath: String?,
    val score: Double?,
    val feedback: String?
)

// Student Leave Request
data class StudentLeaveRequest(
    val id: String,
    val studentId: String,
    val reason: String,
    val startDate: String,
    val endDate: String,
    val status: LeaveStatus,
    val attachmentPath: String?,
    val submittedDate: String,
    val approvedBy: String?,
    val approvedDate: String?
)

// Student Exam Schedule
data class StudentExam(
    val id: String,
    val courseId: String,
    val courseName: String,
    val examType: String,
    val date: String,
    val time: String,
    val duration: String,
    val hallNumber: String,
    val seatNumber: String
)

// Student Result
data class StudentResult(
    val id: String,
    val studentId: String,
    val semester: Int,
    val courseId: String,
    val courseName: String,
    val grade: String,
    val score: Double,
    val totalMarks: Double,
    val cgpa: Double
)

// Student Placement
data class StudentPlacement(
    val id: String,
    val studentId: String,
    val companyName: String,
    val position: String,
    val packageAmount: String,
    val status: PlacementStatus,
    val appliedDate: String,
    val interviewDate: String?
)

// Student Hostel Details
data class StudentHostel(
    val studentId: String,
    val hostelName: String,
    val roomNumber: String,
    val floor: String,
    val wardenName: String,
    val wardenPhone: String,
    val messAllotted: Boolean,
    val messPlan: String?
)

// Student Grievance
data class StudentGrievance(
    val id: String,
    val studentId: String,
    val category: String,
    val subject: String,
    val description: String,
    val status: GrievanceStatus,
    val submittedDate: String,
    val resolvedDate: String?,
    val attachmentPath: String?,
    val response: String?
) 