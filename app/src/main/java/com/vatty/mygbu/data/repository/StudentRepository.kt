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
            courseId = "CS101",
            courseName = "Programming Fundamentals",
            attendancePercentage = 95.0,
            assignmentScore = 92.0,
            examScore = 88.0,
            totalScore = 89.5,
            grade = "A+"
        ),
        StudentPerformance(
            studentId = "STU001",
            courseId = "CS102",
            courseName = "Digital Logic Design",
            attendancePercentage = 88.0,
            assignmentScore = 85.0,
            examScore = 90.0,
            totalScore = 87.8,
            grade = "A"
        ),
        StudentPerformance(
            studentId = "STU001",
            courseId = "CS103",
            courseName = "Computer Organization",
            attendancePercentage = 92.0,
            assignmentScore = 88.0,
            examScore = 82.0,
            totalScore = 85.2,
            grade = "A-"
        ),
        StudentPerformance(
            studentId = "STU001",
            courseId = "CS201",
            courseName = "Object Oriented Programming",
            attendancePercentage = 96.0,
            assignmentScore = 94.0,
            examScore = 95.0,
            totalScore = 94.6,
            grade = "A+"
        ),
        StudentPerformance(
            studentId = "STU001",
            courseId = "CS202",
            courseName = "Data Structures",
            attendancePercentage = 90.0,
            assignmentScore = 89.0,
            examScore = 88.0,
            totalScore = 88.7,
            grade = "A"
        ),
        StudentPerformance(
            studentId = "STU001",
            courseId = "CS203",
            courseName = "Discrete Mathematics",
            attendancePercentage = 85.0,
            assignmentScore = 80.0,
            examScore = 85.0,
            totalScore = 82.5,
            grade = "B+"
        ),
        StudentPerformance(
            studentId = "STU001",
            courseId = "CS301",
            courseName = "Advanced Data Structures",
            attendancePercentage = 93.0,
            assignmentScore = 88.0,
            examScore = 85.0,
            totalScore = 86.8,
            grade = "A-"
        ),
        StudentPerformance(
            studentId = "STU001",
            courseId = "CS302",
            courseName = "Database Systems",
            attendancePercentage = 91.0,
            assignmentScore = 85.0,
            examScore = 88.0,
            totalScore = 86.5,
            grade = "A-"
        ),
        StudentPerformance(
            studentId = "STU001",
            courseId = "CS303",
            courseName = "Computer Networks",
            attendancePercentage = 89.0,
            assignmentScore = 92.0,
            examScore = 87.0,
            totalScore = 89.2,
            grade = "A"
        ),
        StudentPerformance(
            studentId = "STU001",
            courseId = "CS304",
            courseName = "Operating Systems",
            attendancePercentage = 87.0,
            assignmentScore = 83.0,
            examScore = 86.0,
            totalScore = 84.8,
            grade = "B+"
        ),
        StudentPerformance(
            studentId = "STU001",
            courseId = "CS305",
            courseName = "Software Engineering",
            attendancePercentage = 94.0,
            assignmentScore = 85.0,
            examScore = 88.0,
            totalScore = 86.8,
            grade = "A-"
        ),
        StudentPerformance(
            studentId = "STU001",
            courseId = "CS306",
            courseName = "Web Technologies",
            attendancePercentage = 96.0,
            assignmentScore = 90.0,
            examScore = 92.0,
            totalScore = 91.2,
            grade = "A+"
        )
    )
    
    private val mockAssignments = listOf(
        StudentAssignment(
            id = "ASS001",
            assignmentId = "CS301_ASG1",
            studentId = "STU001",
            title = "Array Implementation",
            description = "Implement various array operations including sorting, searching, and manipulation algorithms",
            dueDate = "2024-01-15",
            submittedDate = "2024-01-14",
            status = AssignmentStatus.SUBMITTED,
            filePath = "/assignments/array_impl.pdf",
            score = 88.0,
            feedback = "Excellent implementation with proper documentation and efficient algorithms"
        ),
        StudentAssignment(
            id = "ASS002",
            assignmentId = "CS302_ASG1",
            studentId = "STU001",
            title = "SQL Database Design",
            description = "Design and implement a complete database system with ER diagrams and SQL queries",
            dueDate = "2024-01-20",
            submittedDate = null,
            status = AssignmentStatus.PENDING,
            filePath = null,
            score = null,
            feedback = null
        ),
        StudentAssignment(
            id = "ASS003",
            assignmentId = "CS303_ASG1",
            studentId = "STU001",
            title = "Network Protocol Analysis",
            description = "Analyze and implement basic network protocols using socket programming",
            dueDate = "2024-01-25",
            submittedDate = "2024-01-24",
            status = AssignmentStatus.SUBMITTED,
            filePath = "/assignments/network_protocol.pdf",
            score = 92.0,
            feedback = "Outstanding work! Your protocol implementation is very efficient"
        ),
        StudentAssignment(
            id = "ASS004",
            assignmentId = "CS304_ASG1",
            studentId = "STU001",
            title = "Process Scheduling Simulation",
            description = "Implement various CPU scheduling algorithms and compare their performance",
            dueDate = "2024-02-01",
            submittedDate = null,
            status = AssignmentStatus.PENDING,
            filePath = null,
            score = null,
            feedback = null
        ),
        StudentAssignment(
            id = "ASS005",
            assignmentId = "CS305_ASG1",
            studentId = "STU001",
            title = "Software Requirements Document",
            description = "Create a comprehensive SRS document for a student management system",
            dueDate = "2024-02-05",
            submittedDate = "2024-02-04",
            status = AssignmentStatus.SUBMITTED,
            filePath = "/assignments/srs_document.pdf",
            score = 85.0,
            feedback = "Good documentation but needs more detailed use case scenarios"
        ),
        StudentAssignment(
            id = "ASS006",
            assignmentId = "CS306_ASG1",
            studentId = "STU001",
            title = "Web Application Development",
            description = "Develop a responsive web application using HTML, CSS, and JavaScript",
            dueDate = "2024-02-10",
            submittedDate = null,
            status = AssignmentStatus.PENDING,
            filePath = null,
            score = null,
            feedback = null
        ),
        StudentAssignment(
            id = "ASS007",
            assignmentId = "CS301_ASG2",
            studentId = "STU001",
            title = "Linked List Implementation",
            description = "Implement singly and doubly linked lists with all basic operations",
            dueDate = "2024-02-15",
            submittedDate = "2024-02-14",
            status = AssignmentStatus.SUBMITTED,
            filePath = "/assignments/linked_list.pdf",
            score = 90.0,
            feedback = "Very good implementation with proper memory management"
        ),
        StudentAssignment(
            id = "ASS008",
            assignmentId = "CS302_ASG2",
            studentId = "STU001",
            title = "Database Normalization",
            description = "Normalize a given database schema to 3NF and implement it",
            dueDate = "2024-02-20",
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
            semester = 1,
            totalAmount = 65000.0,
            paidAmount = 65000.0,
            pendingAmount = 0.0,
            dueDate = "2023-08-15",
            isPaid = true
        ),
        StudentFee(
            studentId = "STU001",
            semester = 2,
            totalAmount = 70000.0,
            paidAmount = 70000.0,
            pendingAmount = 0.0,
            dueDate = "2023-12-15",
            isPaid = true
        ),
        StudentFee(
            studentId = "STU001",
            semester = 3,
            totalAmount = 75000.0,
            paidAmount = 50000.0,
            pendingAmount = 25000.0,
            dueDate = "2024-02-15",
            isPaid = false
        ),
        StudentFee(
            studentId = "STU001",
            semester = 4,
            totalAmount = 80000.0,
            paidAmount = 0.0,
            pendingAmount = 80000.0,
            dueDate = "2024-06-15",
            isPaid = false
        ),
        StudentFee(
            studentId = "STU001",
            semester = 5,
            totalAmount = 85000.0,
            paidAmount = 0.0,
            pendingAmount = 85000.0,
            dueDate = "2024-10-15",
            isPaid = false
        ),
        StudentFee(
            studentId = "STU001",
            semester = 6,
            totalAmount = 90000.0,
            paidAmount = 0.0,
            pendingAmount = 90000.0,
            dueDate = "2025-02-15",
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
        ),
        StudentExam(
            id = "EXAM003",
            courseId = "CS303",
            courseName = "Computer Networks",
            examType = "Mid Semester",
            date = "2024-02-01",
            time = "10:00 AM",
            duration = "2 hours",
            hallNumber = "C-301",
            seatNumber = "C-12"
        ),
        StudentExam(
            id = "EXAM004",
            courseId = "CS304",
            courseName = "Operating Systems",
            examType = "Mid Semester",
            date = "2024-02-05",
            time = "2:00 PM",
            duration = "2 hours",
            hallNumber = "D-401",
            seatNumber = "D-20"
        ),
        StudentExam(
            id = "EXAM005",
            courseId = "CS305",
            courseName = "Software Engineering",
            examType = "Mid Semester",
            date = "2024-02-10",
            time = "10:00 AM",
            duration = "2 hours",
            hallNumber = "E-501",
            seatNumber = "E-05"
        ),
        StudentExam(
            id = "EXAM006",
            courseId = "CS306",
            courseName = "Web Technologies",
            examType = "Mid Semester",
            date = "2024-02-15",
            time = "2:00 PM",
            duration = "2 hours",
            hallNumber = "F-601",
            seatNumber = "F-18"
        )
    )
    
    private val mockResults = listOf(
        StudentResult(
            id = "RES001",
            studentId = "STU001",
            semester = 1,
            courseId = "CS101",
            courseName = "Programming Fundamentals",
            grade = "A+",
            score = 92.0,
            totalMarks = 100.0,
            cgpa = 9.2
        ),
        StudentResult(
            id = "RES002",
            studentId = "STU001",
            semester = 1,
            courseId = "CS102",
            courseName = "Digital Logic Design",
            grade = "A",
            score = 88.0,
            totalMarks = 100.0,
            cgpa = 8.8
        ),
        StudentResult(
            id = "RES003",
            studentId = "STU001",
            semester = 1,
            courseId = "CS103",
            courseName = "Computer Organization",
            grade = "A-",
            score = 85.0,
            totalMarks = 100.0,
            cgpa = 8.5
        ),
        StudentResult(
            id = "RES004",
            studentId = "STU001",
            semester = 2,
            courseId = "CS201",
            courseName = "Object Oriented Programming",
            grade = "A+",
            score = 94.0,
            totalMarks = 100.0,
            cgpa = 9.4
        ),
        StudentResult(
            id = "RES005",
            studentId = "STU001",
            semester = 2,
            courseId = "CS202",
            courseName = "Data Structures",
            grade = "A",
            score = 89.0,
            totalMarks = 100.0,
            cgpa = 8.9
        ),
        StudentResult(
            id = "RES006",
            studentId = "STU001",
            semester = 2,
            courseId = "CS203",
            courseName = "Discrete Mathematics",
            grade = "B+",
            score = 82.0,
            totalMarks = 100.0,
            cgpa = 8.2
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
        ),
        StudentPlacement(
            id = "PLAC002",
            studentId = "STU001",
            companyName = "DataFlow Analytics",
            position = "Data Analyst",
            packageAmount = "5.5 LPA",
            status = PlacementStatus.INTERVIEW_SCHEDULED,
            appliedDate = "2024-01-15",
            interviewDate = "2024-02-20"
        ),
        StudentPlacement(
            id = "PLAC003",
            studentId = "STU001",
            companyName = "CloudTech Systems",
            position = "Cloud Engineer",
            packageAmount = "7 LPA",
            status = PlacementStatus.APPLIED,
            appliedDate = "2024-01-20",
            interviewDate = null
        ),
        StudentPlacement(
            id = "PLAC004",
            studentId = "STU001",
            companyName = "MobileFirst Apps",
            position = "Android Developer",
            packageAmount = "6.5 LPA",
            status = PlacementStatus.SELECTED,
            appliedDate = "2024-01-25",
            interviewDate = "2024-02-10"
        ),
        StudentPlacement(
            id = "PLAC005",
            studentId = "STU001",
            companyName = "AI Innovations",
            position = "Machine Learning Intern",
            packageAmount = "8 LPA",
            status = PlacementStatus.APPLIED,
            appliedDate = "2024-02-01",
            interviewDate = null
        ),
        StudentPlacement(
            id = "PLAC006",
            studentId = "STU001",
            companyName = "CyberSec Pro",
            position = "Security Analyst",
            packageAmount = "7.5 LPA",
            status = PlacementStatus.APPLIED,
            appliedDate = "2024-02-05",
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
        ),
        StudentGrievance(
            id = "GRV002",
            studentId = "STU001",
            category = "Hostel",
            subject = "Room Allocation Issue",
            description = "I was allocated a room with a broken window. Please help me change it.",
            status = GrievanceStatus.PENDING,
            submittedDate = "2024-01-18",
            resolvedDate = null,
            attachmentPath = null,
            response = null
        ),
        StudentGrievance(
            id = "GRV003",
            studentId = "STU001",
            category = "Academic",
            subject = "Course Material Availability",
            description = "I need the lecture notes for CS301. They are not available online.",
            status = GrievanceStatus.PENDING,
            submittedDate = "2024-01-20",
            resolvedDate = null,
            attachmentPath = null,
            response = null
        ),
        StudentGrievance(
            id = "GRV004",
            studentId = "STU001",
            category = "Hostel",
            subject = "Mess Food Quality",
            description = "The food in the hostel mess is not hygienic. I have been getting stomach issues.",
            status = GrievanceStatus.PENDING,
            submittedDate = "2024-01-25",
            resolvedDate = null,
            attachmentPath = null,
            response = null
        ),
        StudentGrievance(
            id = "GRV005",
            studentId = "STU001",
            category = "Academic",
            subject = "Exam Timings",
            description = "The exam timings for CS302 are conflicting with my other classes. Can you please adjust it?",
            status = GrievanceStatus.PENDING,
            submittedDate = "2024-02-01",
            resolvedDate = null,
            attachmentPath = null,
            response = null
        ),
        StudentGrievance(
            id = "GRV006",
            studentId = "STU001",
            category = "Hostel",
            subject = "Water Supply Issue",
            description = "The water supply in the hostel is very low. We are facing water scarcity.",
            status = GrievanceStatus.PENDING,
            submittedDate = "2024-02-05",
            resolvedDate = null,
            attachmentPath = null,
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