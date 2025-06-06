package com.vatty.mygbu.data.model

data class Faculty(
    val id: String,
    val name: String,
    val designation: String,
    val department: String,
    val email: String,
    val phone: String,
    val profileImageUrl: String = ""
)

data class Course(
    val id: String,
    val title: String,
    val code: String,
    val semester: String,
    val students: Int,
    val schedule: String,
    val imageRes: Int = 0
)

data class Assignment(
    val id: String,
    val title: String,
    val description: String,
    val courseId: String,
    val dueDate: String,
    val submissionsCount: Int,
    val totalStudents: Int,
    val isPublished: Boolean = false
)

data class Student(
    val id: String,
    val name: String,
    val rollNumber: String,
    val email: String,
    val courseIds: List<String>,
    val attendance: Double,
    val grade: String = ""
)

data class Attendance(
    val id: String,
    val studentId: String,
    val courseId: String,
    val date: String,
    val status: AttendanceStatus,
    val remarks: String = ""
)

enum class AttendanceStatus {
    PRESENT, ABSENT, LATE
}

data class Message(
    val id: String,
    val title: String,
    val content: String,
    val senderId: String,
    val recipientIds: List<String>,
    val timestamp: String,
    val isRead: Boolean = false,
    val type: MessageType
)

enum class MessageType {
    PERSONAL, ANNOUNCEMENT, SYSTEM
}

data class DashboardStats(
    val todaysClasses: Int,
    val pendingTasks: Int,
    val totalStudents: Int,
    val unreadMessages: Int
) 