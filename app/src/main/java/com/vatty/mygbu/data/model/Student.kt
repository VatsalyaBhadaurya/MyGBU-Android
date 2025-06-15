package com.vatty.mygbu.data.model

import com.vatty.mygbu.enums.AttendanceStatus

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