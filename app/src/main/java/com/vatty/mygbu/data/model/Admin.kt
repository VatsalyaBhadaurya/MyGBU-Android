package com.vatty.mygbu.data.model

import com.vatty.mygbu.enums.*
import java.util.Date

// Main Admin data class
data class Admin(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val designation: String,
    val department: String,
    val profileImage: Int = 0,
    val joiningDate: Date,
    val permissions: List<String>,
    val isActive: Boolean = true
)

// Admin Role and Permissions
data class AdminRole(
    val id: String,
    val name: String,
    val description: String,
    val permissions: List<String>,
    val isActive: Boolean = true
)

// Onboarding/Offboarding Models
data class OnboardingRequest(
    val id: String,
    val personId: String,
    val personType: String, // "STUDENT" or "FACULTY"
    val requestType: String, // "ONBOARDING" or "OFFBOARDING"
    val status: OnboardingStatus,
    val submittedDate: Date,
    val processedDate: Date?,
    val documents: List<Document>,
    val remarks: String?
)

data class Document(
    val id: String,
    val name: String,
    val type: String,
    val filePath: String,
    val uploadedDate: Date,
    val isVerified: Boolean = false,
    val verifiedBy: String?,
    val verifiedDate: Date?
)

// Course Mapping Models
data class CourseMapping(
    val id: String,
    val courseId: String,
    val courseName: String,
    val programId: String,
    val programName: String,
    val semester: Int,
    val courseType: CourseType, // CORE, ELECTIVE, AUDIT
    val credits: Int,
    val isActive: Boolean = true,
    val syllabusPath: String?,
    val prerequisites: List<String> = emptyList()
)

data class Program(
    val id: String,
    val name: String,
    val department: String,
    val duration: Int, // in semesters
    val totalCredits: Int,
    val isActive: Boolean = true
)

// Syllabus Management Models
data class SyllabusDocument(
    val id: String,
    val courseId: String,
    val courseName: String,
    val title: String,
    val description: String,
    val version: String,
    val filePath: String,
    val fileSize: String,
    val fileType: String,
    val uploadedDate: Date,
    val uploadedBy: String,
    val isActive: Boolean = true
)

// Elective Pool Models
data class ElectivePool(
    val id: String,
    val name: String,
    val type: String, // "INTERDISCIPLINARY", "OPEN_ELECTIVE"
    val description: String,
    val targetPrograms: List<String>,
    val courses: List<ElectivePoolCourse>,
    val totalCredits: Int,
    val isActive: Boolean = true,
    val createdDate: Date
)

data class ElectivePoolCourse(
    val id: String,
    val courseId: String,
    val courseName: String,
    val credits: Int,
    val addedDate: Date
)

// Credit Validation Models
data class CreditRule(
    val id: String,
    val programId: String,
    val programName: String,
    val batchYear: String,
    val totalCreditsRequired: Int,
    val coreCreditsRequired: Int,
    val electiveCreditsRequired: Int,
    val labCreditsRequired: Int,
    val isActive: Boolean = true,
    val createdDate: Date,
    val updatedDate: Date
)

data class CreditValidationResult(
    val programId: String,
    val programName: String,
    val totalCreditsEarned: Int,
    val totalCreditsRequired: Int,
    val coreCreditsEarned: Int,
    val coreCreditsRequired: Int,
    val electiveCreditsEarned: Int,
    val electiveCreditsRequired: Int,
    val labCreditsEarned: Int,
    val labCreditsRequired: Int,
    val isValid: Boolean,
    val issues: List<String>
)

// Curriculum Notification Models
data class CurriculumNotification(
    val id: String,
    val title: String,
    val message: String,
    val programId: String,
    val programName: String,
    val semester: Int?,
    val recipients: List<String>, // "STUDENTS", "FACULTY", "MENTORS"
    val sentDate: Date,
    val sentBy: String,
    val recipientCount: Int
)

// Exam Management Models
data class ExamSchedule(
    val id: String,
    val examType: String,
    val courseId: String,
    val courseName: String,
    val date: Date,
    val startTime: String,
    val endTime: String,
    val venue: String,
    val totalStudents: Int,
    val status: ExamScheduleStatus
)

data class AdmitCard(
    val id: String,
    val studentId: String,
    val examId: String,
    val rollNumber: String,
    val qrCode: String,
    val seatNumber: String,
    val hallNumber: String,
    val isGenerated: Boolean = false,
    val generatedDate: Date?
)

// Degree Audit Models
data class DegreeAudit(
    val id: String,
    val studentId: String,
    val studentName: String,
    val programId: String,
    val currentSemester: Int,
    val attendancePercentage: Double,
    val cgpa: Double,
    val totalCredits: Int,
    val requiredCredits: Int,
    val projectCompleted: Boolean,
    val duesCleared: Boolean,
    val isEligible: Boolean,
    val auditDate: Date
)

// Approval Workflow Models
data class ApprovalRequest(
    val id: String,
    val requestType: ApprovalRequestType,
    val requesterId: String,
    val requesterName: String,
    val requesterType: String, // "STUDENT" or "FACULTY"
    val subject: String,
    val description: String,
    val status: ApprovalStatus,
    val priority: Priority,
    val submittedDate: Date,
    val dueDate: Date?,
    val currentLevel: Int,
    val totalLevels: Int,
    val attachments: List<String> = emptyList()
)

data class ApprovalWorkflow(
    val id: String,
    val requestId: String,
    val level: Int,
    val approverId: String,
    val approverName: String,
    val status: ApprovalStatus,
    val remarks: String?,
    val actionDate: Date?,
    val slaHours: Int
)

// Project/Dissertation Models
data class Project(
    val id: String,
    val studentId: String,
    val studentName: String,
    val supervisorId: String,
    val supervisorName: String,
    val title: String,
    val description: String,
    val status: ProjectStatus,
    val registrationDate: Date,
    val submissionDate: Date?,
    val midReviewDate: Date?,
    val finalReviewDate: Date?,
    val grade: String?,
    val plagiarismReport: String?
)

data class ProjectReview(
    val id: String,
    val projectId: String,
    val reviewerId: String,
    val reviewerName: String,
    val reviewType: ReviewType, // MID_REVIEW, FINAL_REVIEW
    val date: Date,
    val score: Double?,
    val feedback: String,
    val status: ReviewStatus
)

// File Tracking Models
data class FileRecord(
    val id: String,
    val fileNumber: String,
    val subject: String,
    val fileType: FileType,
    val priority: Priority,
    val status: FileStatus,
    val createdDate: Date,
    val createdBy: String,
    val currentLocation: String,
    val currentHandler: String,
    val attachments: List<String> = emptyList()
)

data class FileMovement(
    val id: String,
    val fileId: String,
    val fromLocation: String,
    val toLocation: String,
    val fromPerson: String,
    val toPerson: String,
    val movementDate: Date,
    val remarks: String?,
    val actionTaken: String?
)

// Dashboard Models
data class AdminDashboard(
    val pendingApprovals: Int,
    val pendingOnboarding: Int,
    val pendingOffboarding: Int,
    val activeFiles: Int,
    val escalatedItems: Int,
    val todayTasks: List<AdminTask>,
    val recentActivities: List<AdminActivity>
)

data class AdminTask(
    val id: String,
    val title: String,
    val type: TaskType,
    val priority: Priority,
    val dueDate: Date,
    val status: TaskStatus,
    val assignedTo: String
)

data class AdminActivity(
    val id: String,
    val action: String,
    val description: String,
    val timestamp: Date,
    val userId: String,
    val userName: String
)

// Enums for Admin Module
enum class OnboardingStatus {
    PENDING, IN_PROGRESS, COMPLETED, REJECTED
}

enum class CourseType {
    CORE, ELECTIVE, AUDIT, OPEN_ELECTIVE
}

enum class ExamScheduleStatus {
    DRAFT, PUBLISHED, ONGOING, COMPLETED
}

enum class ApprovalRequestType {
    LEAVE_REQUEST, GRIEVANCE, DOCUMENT_REQUEST, EVENT_PROPOSAL, PROJECT_PROPOSAL
}

enum class ApprovalStatus {
    PENDING, APPROVED, REJECTED, ESCALATED
}

enum class Priority {
    LOW, MEDIUM, HIGH, URGENT
}

enum class ProjectStatus {
    REGISTERED, IN_PROGRESS, MID_REVIEW_COMPLETED, SUBMITTED, EVALUATED
}

enum class ReviewType {
    MID_REVIEW, FINAL_REVIEW
}

enum class ReviewStatus {
    PENDING, COMPLETED, FAILED
}

enum class FileType {
    ACADEMIC, ADMINISTRATIVE, FINANCIAL, PERSONNEL, GENERAL
}

enum class FileStatus {
    ACTIVE, CLOSED, ARCHIVED
}

enum class TaskType {
    APPROVAL, ONBOARDING, OFFBOARDING, FILE_TRACKING, EXAM_MANAGEMENT
}

enum class TaskStatus {
    PENDING, IN_PROGRESS, COMPLETED, OVERDUE
} 