package com.vatty.mygbu.data.repository

import com.vatty.mygbu.data.model.*
import com.vatty.mygbu.enums.AssignmentStatus
import com.vatty.mygbu.enums.LeaveStatus
import com.vatty.mygbu.enums.PlacementStatus
import com.vatty.mygbu.enums.GrievanceStatus
import java.util.*

class StudentRepository {
    
    // Mock data for demonstration
    private val mockStudent = Student(
        id = "STU001",
        name = "Rahul Kumar",
        rollNumber = "2023CS001",
        email = "rahul.kumar@student.gbu.ac.in",
        phone = "+91-9876543210",
        program = "Bachelor of Technology",
        department = "Computer Science",
        year = 2,
        semester = 3,
        address = "Room 205, Hostel Block A, GBU Campus",
        emergencyContact = "+91-9876543211",
        dateOfBirth = "15-03-2002",
        gender = "Male",
        bloodGroup = "B+",
        parentName = "Rajesh Kumar",
        parentPhone = "+91-9876543212"
    )
    
    private val mockPerformance = listOf(
        StudentPerformance(
            studentId = "STU001",
            courseId = "CS301",
            courseName = "Data Structures",
            attendancePercentage = 85.0,
            assignmentScore = 88.0,
            examScore = 92.0,
            totalScore = 90.0,
            grade = "A"
        ),
        StudentPerformance(
            studentId = "STU001",
            courseId = "CS302",
            courseName = "Database Systems",
            attendancePercentage = 78.0,
            assignmentScore = 85.0,
            examScore = 88.0,
            totalScore = 86.5,
            grade = "A-"
        )
    )
    
    private val mockAssignments = listOf(
        StudentAssignment(
            id = "ASS001",
            assignmentId = "CS301_ASG1",
            studentId = "STU001",
            title = "Array Implementation",
            description = "Implement various array operations",
            dueDate = "2024-01-15",
            submittedDate = "2024-01-14",
            status = AssignmentStatus.SUBMITTED,
            filePath = "/assignments/array_impl.pdf",
            score = 88.0,
            feedback = "Good implementation with proper documentation"
        ),
        StudentAssignment(
            id = "ASS002",
            assignmentId = "CS302_ASG1",
            studentId = "STU001",
            title = "SQL Queries",
            description = "Write complex SQL queries for given scenarios",
            dueDate = "2024-01-20",
            submittedDate = null,
            status = AssignmentStatus.PENDING,
            filePath = null,
            score = null,
            feedback = null
        )
    )
    
    private val mockFees = listOf(
        StudentFee(
            studentId = "STU001",
            semester = 3,
            totalAmount = 75000.0,
            paidAmount = 50000.0,
            pendingAmount = 25000.0,
            dueDate = "2024-02-15",
            isPaid = false
        )
    )
    
    private val mockExams = listOf(
        StudentExam(
            id = "EXAM001",
            courseId = "CS301",
            courseName = "Data Structures",
            examType = "Mid Semester",
            date = "2024-01-25",
            time = "10:00 AM",
            duration = "2 hours",
            hallNumber = "A-101",
            seatNumber = "A-15"
        ),
        StudentExam(
            id = "EXAM002",
            courseId = "CS302",
            courseName = "Database Systems",
            examType = "Mid Semester",
            date = "2024-01-28",
            time = "2:00 PM",
            duration = "2 hours",
            hallNumber = "B-205",
            seatNumber = "B-08"
        )
    )
    
    private val mockResults = listOf(
        StudentResult(
            id = "RES001",
            studentId = "STU001",
            semester = 2,
            courseId = "CS201",
            courseName = "Programming Fundamentals",
            grade = "A",
            score = 88.0,
            totalMarks = 100.0,
            cgpa = 8.8
        )
    )
    
    private val mockPlacements = listOf(
        StudentPlacement(
            id = "PLAC001",
            studentId = "STU001",
            companyName = "TechCorp Solutions",
            position = "Software Developer Intern",
            packageAmount = "6 LPA",
            status = PlacementStatus.APPLIED,
            appliedDate = "2024-01-10",
            interviewDate = null
        )
    )
    
    private val mockHostel = StudentHostel(
        studentId = "STU001",
        hostelName = "Gautam Buddha Hostel",
        roomNumber = "A-205",
        floor = "2nd Floor",
        wardenName = "Dr. Priya Sharma",
        wardenPhone = "+91-9876543213",
        messAllotted = true,
        messPlan = "Vegetarian"
    )
    
    private val mockGrievances = listOf(
        StudentGrievance(
            id = "GRV001",
            studentId = "STU001",
            category = "Academic",
            subject = "Assignment Extension Request",
            description = "Need extension for Data Structures assignment due to technical issues",
            status = GrievanceStatus.PENDING,
            submittedDate = "2024-01-12",
            resolvedDate = null,
            attachmentPath = "/grievances/tech_issue.pdf",
            response = null
        )
    )
    
    // Get student profile
    fun getStudentProfile(studentId: String): Student {
        return mockStudent
    }
    
    // Get student performance
    fun getStudentPerformance(studentId: String): List<StudentPerformance> {
        return mockPerformance
    }
    
    // Get student assignments
    fun getStudentAssignments(studentId: String): List<StudentAssignment> {
        return mockAssignments
    }
    
    // Get student fees
    fun getStudentFees(studentId: String): List<StudentFee> {
        return mockFees
    }
    
    // Get student exams
    fun getStudentExams(studentId: String): List<StudentExam> {
        return mockExams
    }
    
    // Get student results
    fun getStudentResults(studentId: String): List<StudentResult> {
        return mockResults
    }
    
    // Get student placements
    fun getStudentPlacements(studentId: String): List<StudentPlacement> {
        return mockPlacements
    }
    
    // Get student hostel details
    fun getStudentHostel(studentId: String): StudentHostel {
        return mockHostel
    }
    
    // Get student grievances
    fun getStudentGrievances(studentId: String): List<StudentGrievance> {
        return mockGrievances
    }
    
    // Submit assignment
    fun submitAssignment(assignment: StudentAssignment): Boolean {
        // Mock implementation
        return true
    }
    
    // Apply for leave
    fun applyForLeave(leaveRequest: StudentLeaveRequest): Boolean {
        // Mock implementation
        return true
    }
    
    // Submit grievance
    fun submitGrievance(grievance: StudentGrievance): Boolean {
        // Mock implementation
        return true
    }
    
    // Get attendance percentage
    fun getAttendancePercentage(studentId: String): Double {
        return 82.5
    }
    
    // Get overall CGPA
    fun getOverallCGPA(studentId: String): Double {
        return 8.7
    }
    
    // Get pending assignments count
    fun getPendingAssignmentsCount(studentId: String): Int {
        return mockAssignments.count { it.status == AssignmentStatus.PENDING }
    }
    
    // Get upcoming exams count
    fun getUpcomingExamsCount(studentId: String): Int {
        return mockExams.size
    }
}