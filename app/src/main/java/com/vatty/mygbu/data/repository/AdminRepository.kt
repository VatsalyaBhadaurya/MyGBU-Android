package com.vatty.mygbu.data.repository

import com.vatty.mygbu.data.model.*
import java.util.*

class AdminRepository {
    
    // Mock data for admin dashboard
    fun getAdminDashboard(): AdminDashboard {
        return AdminDashboard(
            pendingApprovals = 15,
            pendingOnboarding = 8,
            pendingOffboarding = 3,
            activeFiles = 25,
            escalatedItems = 5,
            todayTasks = getTodayTasks(),
            recentActivities = getRecentActivities()
        )
    }
    
    // Mock data for onboarding requests
    fun getOnboardingRequests(): List<OnboardingRequest> {
        return listOf(
            OnboardingRequest(
                id = "ONB001",
                personId = "STU001",
                personType = "STUDENT",
                requestType = "ONBOARDING",
                status = OnboardingStatus.PENDING,
                submittedDate = Date(),
                processedDate = null,
                documents = listOf(
                    Document("DOC001", "Joining Letter", "PDF", "/documents/joining_letter.pdf", Date(), false, null, null),
                    Document("DOC002", "ID Card", "IMAGE", "/documents/id_card.jpg", Date(), false, null, null)
                ),
                remarks = null
            ),
            OnboardingRequest(
                id = "ONB002",
                personId = "FAC001",
                personType = "FACULTY",
                requestType = "ONBOARDING",
                status = OnboardingStatus.IN_PROGRESS,
                submittedDate = Date(System.currentTimeMillis() - 86400000), // 1 day ago
                processedDate = null,
                documents = listOf(
                    Document("DOC003", "Appointment Letter", "PDF", "/documents/appointment.pdf", Date(), false, null, null),
                    Document("DOC004", "Educational Certificates", "PDF", "/documents/certificates.pdf", Date(), false, null, null)
                ),
                remarks = "Documents under verification"
            )
        )
    }
    
    // Mock data for course mappings
    fun getCourseMappings(): List<CourseMapping> {
        return listOf(
            CourseMapping(
                id = "CM001",
                courseId = "CS101",
                courseName = "Introduction to Computer Science",
                programId = "B.Tech_CS",
                programName = "B.Tech Computer Science",
                semester = 1,
                courseType = CourseType.CORE,
                credits = 4,
                isActive = true,
                syllabusPath = "/syllabus/cs101.pdf",
                prerequisites = emptyList()
            ),
            CourseMapping(
                id = "CM002",
                courseId = "CS201",
                courseName = "Data Structures and Algorithms",
                programId = "B.Tech_CS",
                programName = "B.Tech Computer Science",
                semester = 2,
                courseType = CourseType.CORE,
                credits = 3,
                isActive = true,
                syllabusPath = "/syllabus/cs201.pdf",
                prerequisites = listOf("CS101")
            ),
            CourseMapping(
                id = "CM003",
                courseId = "CS301",
                courseName = "Database Management Systems",
                programId = "B.Tech_CS",
                programName = "B.Tech Computer Science",
                semester = 3,
                courseType = CourseType.CORE,
                credits = 4,
                isActive = true,
                syllabusPath = "/syllabus/cs301.pdf",
                prerequisites = listOf("CS201")
            ),
            CourseMapping(
                id = "CM004",
                courseId = "CS401",
                courseName = "Machine Learning",
                programId = "B.Tech_CS",
                programName = "B.Tech Computer Science",
                semester = 4,
                courseType = CourseType.ELECTIVE,
                credits = 3,
                isActive = true,
                syllabusPath = "/syllabus/cs401.pdf",
                prerequisites = listOf("CS301")
            )
        )
    }

    // Mock data for programs
    fun getPrograms(): List<Program> {
        return listOf(
            Program(
                id = "B.Tech_CS",
                name = "B.Tech Computer Science",
                department = "Computer Science",
                duration = 8,
                totalCredits = 160,
                isActive = true
            ),
            Program(
                id = "B.Tech_IT",
                name = "B.Tech Information Technology",
                department = "Information Technology",
                duration = 8,
                totalCredits = 160,
                isActive = true
            ),
            Program(
                id = "MCA",
                name = "Master of Computer Applications",
                department = "Computer Science",
                duration = 6,
                totalCredits = 120,
                isActive = true
            )
        )
    }

    // Mock data for syllabus documents
    fun getSyllabusDocuments(): List<SyllabusDocument> {
        return listOf(
            SyllabusDocument(
                id = "SYL001",
                courseId = "CS101",
                courseName = "Introduction to Computer Science",
                title = "CS101 Syllabus 2024",
                description = "Complete syllabus for Introduction to Computer Science course",
                version = "1.0",
                filePath = "/syllabus/cs101_2024.pdf",
                fileSize = "2.5 MB",
                fileType = "PDF",
                uploadedDate = Date(),
                uploadedBy = "Admin User",
                isActive = true
            ),
            SyllabusDocument(
                id = "SYL002",
                courseId = "CS201",
                courseName = "Data Structures and Algorithms",
                title = "CS201 Syllabus 2024",
                description = "Complete syllabus for Data Structures and Algorithms course",
                version = "1.0",
                filePath = "/syllabus/cs201_2024.pdf",
                fileSize = "3.2 MB",
                fileType = "PDF",
                uploadedDate = Date(System.currentTimeMillis() - 86400000), // 1 day ago
                uploadedBy = "Admin User",
                isActive = true
            )
        )
    }

    // Mock data for elective pools
    fun getElectivePools(): List<ElectivePool> {
        return listOf(
            ElectivePool(
                id = "EP001",
                name = "Artificial Intelligence Pool",
                type = "INTERDISCIPLINARY",
                description = "Collection of AI and ML related courses for interdisciplinary learning",
                targetPrograms = listOf("B.Tech Computer Science", "B.Tech Information Technology"),
                courses = listOf(
                    ElectivePoolCourse("EPC001", "CS401", "Machine Learning", 3, Date()),
                    ElectivePoolCourse("EPC002", "CS402", "Deep Learning", 3, Date()),
                    ElectivePoolCourse("EPC003", "CS403", "Natural Language Processing", 3, Date())
                ),
                totalCredits = 9,
                isActive = true,
                createdDate = Date()
            ),
            ElectivePool(
                id = "EP002",
                name = "Open Elective Pool",
                type = "OPEN_ELECTIVE",
                description = "General elective courses for all programs",
                targetPrograms = listOf("B.Tech Computer Science", "B.Tech Information Technology", "MCA"),
                courses = listOf(
                    ElectivePoolCourse("EPC004", "GE001", "Business Communication", 2, Date()),
                    ElectivePoolCourse("EPC005", "GE002", "Environmental Science", 2, Date()),
                    ElectivePoolCourse("EPC006", "GE003", "Digital Marketing", 2, Date())
                ),
                totalCredits = 6,
                isActive = true,
                createdDate = Date(System.currentTimeMillis() - 172800000) // 2 days ago
            )
        )
    }

    // Mock data for credit rules
    fun getCreditRules(): List<CreditRule> {
        return listOf(
            CreditRule(
                id = "CR001",
                programId = "B.Tech_CS",
                programName = "B.Tech Computer Science",
                batchYear = "2024",
                totalCreditsRequired = 160,
                coreCreditsRequired = 120,
                electiveCreditsRequired = 30,
                labCreditsRequired = 10,
                isActive = true,
                createdDate = Date(),
                updatedDate = Date()
            ),
            CreditRule(
                id = "CR002",
                programId = "B.Tech_IT",
                programName = "B.Tech Information Technology",
                batchYear = "2024",
                totalCreditsRequired = 160,
                coreCreditsRequired = 115,
                electiveCreditsRequired = 35,
                labCreditsRequired = 10,
                isActive = true,
                createdDate = Date(),
                updatedDate = Date()
            )
        )
    }

    // Mock data for curriculum notifications
    fun getCurriculumNotifications(): List<CurriculumNotification> {
        return listOf(
            CurriculumNotification(
                id = "CN001",
                title = "New Curriculum Published",
                message = "The updated curriculum for B.Tech Computer Science 2024 batch has been published. Please review the changes.",
                programId = "B.Tech_CS",
                programName = "B.Tech Computer Science",
                semester = null,
                recipients = listOf("STUDENTS", "FACULTY"),
                sentDate = Date(),
                sentBy = "Admin User",
                recipientCount = 150
            ),
            CurriculumNotification(
                id = "CN002",
                title = "Elective Course Registration Open",
                message = "Registration for elective courses for semester 4 is now open. Please select your preferred courses.",
                programId = "B.Tech_CS",
                programName = "B.Tech Computer Science",
                semester = 4,
                recipients = listOf("STUDENTS"),
                sentDate = Date(System.currentTimeMillis() - 86400000), // 1 day ago
                sentBy = "Admin User",
                recipientCount = 120
            )
        )
    }
    
    // Mock data for exam schedules
    fun getExamSchedules(): List<ExamSchedule> {
        return listOf(
            ExamSchedule(
                id = "EXAM001",
                examType = "Mid Semester",
                courseId = "CS101",
                courseName = "Introduction to Computer Science",
                date = Date(System.currentTimeMillis() + 604800000), // 1 week from now
                startTime = "10:00 AM",
                endTime = "12:00 PM",
                venue = "Room 101, Block A",
                totalStudents = 120,
                status = ExamScheduleStatus.PUBLISHED
            ),
            ExamSchedule(
                id = "EXAM002",
                examType = "End Semester",
                courseId = "CS201",
                courseName = "Data Structures",
                date = Date(System.currentTimeMillis() + 1209600000), // 2 weeks from now
                startTime = "02:00 PM",
                endTime = "05:00 PM",
                venue = "Room 205, Block B",
                totalStudents = 95,
                status = ExamScheduleStatus.DRAFT
            )
        )
    }
    
    // Mock data for degree audits
    fun getDegreeAudits(): List<DegreeAudit> {
        return listOf(
            DegreeAudit(
                id = "AUDIT001",
                studentId = "STU001",
                studentName = "John Doe",
                programId = "B.Tech_CS",
                currentSemester = 8,
                attendancePercentage = 85.5,
                cgpa = 8.2,
                totalCredits = 180,
                requiredCredits = 180,
                projectCompleted = true,
                duesCleared = true,
                isEligible = true,
                auditDate = Date()
            ),
            DegreeAudit(
                id = "AUDIT002",
                studentId = "STU002",
                studentName = "Jane Smith",
                programId = "B.Tech_CS",
                currentSemester = 8,
                attendancePercentage = 78.0,
                cgpa = 7.8,
                totalCredits = 175,
                requiredCredits = 180,
                projectCompleted = false,
                duesCleared = false,
                isEligible = false,
                auditDate = Date()
            )
        )
    }
    
    // Mock data for approval requests
    fun getApprovalRequests(): List<ApprovalRequest> {
        return listOf(
            ApprovalRequest(
                id = "APR001",
                requestType = ApprovalRequestType.LEAVE_REQUEST,
                requesterId = "STU001",
                requesterName = "John Doe",
                requesterType = "STUDENT",
                subject = "Medical Leave Application",
                description = "Requesting 5 days medical leave due to illness",
                status = ApprovalStatus.PENDING,
                priority = Priority.MEDIUM,
                submittedDate = Date(),
                dueDate = Date(System.currentTimeMillis() + 172800000), // 2 days from now
                currentLevel = 1,
                totalLevels = 3,
                attachments = listOf("/attachments/medical_certificate.pdf")
            ),
            ApprovalRequest(
                id = "APR002",
                requestType = ApprovalRequestType.GRIEVANCE,
                requesterId = "FAC001",
                requesterName = "Dr. Smith",
                requesterType = "FACULTY",
                subject = "Classroom Infrastructure Issue",
                description = "Projector not working in Room 205",
                status = ApprovalStatus.ESCALATED,
                priority = Priority.HIGH,
                submittedDate = Date(System.currentTimeMillis() - 86400000), // 1 day ago
                dueDate = Date(System.currentTimeMillis() + 86400000), // 1 day from now
                currentLevel = 2,
                totalLevels = 3,
                attachments = listOf("/attachments/issue_photo.jpg")
            )
        )
    }
    
    // Mock data for projects
    fun getProjects(): List<Project> {
        return listOf(
            Project(
                id = "PROJ001",
                studentId = "STU001",
                studentName = "John Doe",
                supervisorId = "FAC001",
                supervisorName = "Dr. Johnson",
                title = "Machine Learning for Student Performance Prediction",
                description = "Developing an ML model to predict student performance based on various parameters",
                status = ProjectStatus.IN_PROGRESS,
                registrationDate = Date(System.currentTimeMillis() - 2592000000), // 30 days ago
                submissionDate = null,
                midReviewDate = Date(System.currentTimeMillis() + 604800000), // 1 week from now
                finalReviewDate = null,
                grade = null,
                plagiarismReport = null
            ),
            Project(
                id = "PROJ002",
                studentId = "STU002",
                studentName = "Jane Smith",
                supervisorId = "FAC002",
                supervisorName = "Dr. Williams",
                title = "Blockchain-based Academic Credential Verification",
                description = "Implementing a blockchain solution for secure academic credential verification",
                status = ProjectStatus.REGISTERED,
                registrationDate = Date(),
                submissionDate = null,
                midReviewDate = null,
                finalReviewDate = null,
                grade = null,
                plagiarismReport = null
            )
        )
    }
    
    // Mock data for file records
    fun getFileRecords(): List<FileRecord> {
        return listOf(
            FileRecord(
                id = "FILE001",
                fileNumber = "ADM/2024/001",
                subject = "Student Admission Approval",
                fileType = FileType.ACADEMIC,
                priority = Priority.HIGH,
                status = FileStatus.ACTIVE,
                createdDate = Date(),
                createdBy = "Admin User",
                currentLocation = "Dean's Office",
                currentHandler = "Dr. Dean",
                attachments = listOf("/files/admission_form.pdf", "/files/student_documents.pdf")
            ),
            FileRecord(
                id = "FILE002",
                fileNumber = "FIN/2024/002",
                subject = "Faculty Salary Revision",
                fileType = FileType.FINANCIAL,
                priority = Priority.MEDIUM,
                status = FileStatus.ACTIVE,
                createdDate = Date(System.currentTimeMillis() - 172800000), // 2 days ago
                createdBy = "HR Admin",
                currentLocation = "Finance Department",
                currentHandler = "Mr. Finance",
                attachments = listOf("/files/salary_proposal.pdf")
            )
        )
    }
    
    // Mock data for today's tasks
    private fun getTodayTasks(): List<AdminTask> {
        return listOf(
            AdminTask(
                id = "TASK001",
                title = "Review Student Onboarding Applications",
                type = TaskType.ONBOARDING,
                priority = Priority.HIGH,
                dueDate = Date(),
                status = TaskStatus.PENDING,
                assignedTo = "Admin User"
            ),
            AdminTask(
                id = "TASK002",
                title = "Approve Leave Requests",
                type = TaskType.APPROVAL,
                priority = Priority.MEDIUM,
                dueDate = Date(),
                status = TaskStatus.IN_PROGRESS,
                assignedTo = "Admin User"
            ),
            AdminTask(
                id = "TASK003",
                title = "Generate Exam Admit Cards",
                type = TaskType.EXAM_MANAGEMENT,
                priority = Priority.HIGH,
                dueDate = Date(),
                status = TaskStatus.PENDING,
                assignedTo = "Admin User"
            )
        )
    }
    
    // Mock data for recent activities
    private fun getRecentActivities(): List<AdminActivity> {
        return listOf(
            AdminActivity(
                id = "ACT001",
                action = "Approved Leave Request",
                description = "Approved medical leave for John Doe",
                timestamp = Date(System.currentTimeMillis() - 3600000), // 1 hour ago
                userId = "ADMIN001",
                userName = "Admin User"
            ),
            AdminActivity(
                id = "ACT002",
                action = "Generated Admit Cards",
                description = "Generated admit cards for CS101 exam",
                timestamp = Date(System.currentTimeMillis() - 7200000), // 2 hours ago
                userId = "ADMIN001",
                userName = "Admin User"
            ),
            AdminActivity(
                id = "ACT003",
                action = "Updated Course Mapping",
                description = "Updated course mapping for B.Tech CS program",
                timestamp = Date(System.currentTimeMillis() - 10800000), // 3 hours ago
                userId = "ADMIN001",
                userName = "Admin User"
            )
        )
    }
    
    // Methods for CRUD operations
    fun approveOnboardingRequest(requestId: String): Boolean {
        // Mock approval logic
        return true
    }
    
    fun rejectOnboardingRequest(requestId: String, remarks: String): Boolean {
        // Mock rejection logic
        return true
    }
    
    fun generateAdmitCards(examId: String): List<AdmitCard> {
        // Mock admit card generation
        return listOf(
            AdmitCard(
                id = "ADMIT001",
                studentId = "STU001",
                examId = examId,
                rollNumber = "2024CS001",
                qrCode = "QR_CODE_001",
                seatNumber = "A1",
                hallNumber = "101",
                isGenerated = true,
                generatedDate = Date()
            )
        )
    }
    
    fun updateApprovalStatus(requestId: String, status: ApprovalStatus, remarks: String?): Boolean {
        // Mock approval status update
        return true
    }
    
    fun escalateRequest(requestId: String, reason: String): Boolean {
        // Mock escalation logic
        return true
    }

    // Course Mapping CRUD Operations
    fun addCourseMapping(courseMapping: CourseMapping): Boolean {
        // Mock add course mapping logic
        return true
    }

    fun updateCourseMapping(courseMapping: CourseMapping): Boolean {
        // Mock update course mapping logic
        return true
    }

    fun deleteCourseMapping(courseMappingId: String): Boolean {
        // Mock delete course mapping logic
        return true
    }

    // Syllabus Management CRUD Operations
    fun uploadSyllabus(syllabusDocument: SyllabusDocument): Boolean {
        // Mock syllabus upload logic
        return true
    }

    fun updateSyllabus(syllabusDocument: SyllabusDocument): Boolean {
        // Mock syllabus update logic
        return true
    }

    fun deleteSyllabus(syllabusId: String): Boolean {
        // Mock syllabus delete logic
        return true
    }

    // Elective Pool CRUD Operations
    fun addElectivePool(electivePool: ElectivePool): Boolean {
        // Mock add elective pool logic
        return true
    }

    fun updateElectivePool(electivePool: ElectivePool): Boolean {
        // Mock update elective pool logic
        return true
    }

    fun deleteElectivePool(poolId: String): Boolean {
        // Mock delete elective pool logic
        return true
    }

    fun addCourseToPool(poolId: String, course: ElectivePoolCourse): Boolean {
        // Mock add course to pool logic
        return true
    }

    fun removeCourseFromPool(poolId: String, courseId: String): Boolean {
        // Mock remove course from pool logic
        return true
    }

    // Credit Validation CRUD Operations
    fun addCreditRule(creditRule: CreditRule): Boolean {
        // Mock add credit rule logic
        return true
    }

    fun updateCreditRule(creditRule: CreditRule): Boolean {
        // Mock update credit rule logic
        return true
    }

    fun deleteCreditRule(ruleId: String): Boolean {
        // Mock delete credit rule logic
        return true
    }

    fun validateCredits(programId: String): CreditValidationResult {
        // Mock credit validation logic
        return CreditValidationResult(
            programId = programId,
            programName = "B.Tech Computer Science",
            totalCreditsEarned = 155,
            totalCreditsRequired = 160,
            coreCreditsEarned = 118,
            coreCreditsRequired = 120,
            electiveCreditsEarned = 27,
            electiveCreditsRequired = 30,
            labCreditsEarned = 10,
            labCreditsRequired = 10,
            isValid = false,
            issues = listOf(
                "Total credits short by 5",
                "Core credits short by 2",
                "Elective credits short by 3"
            )
        )
    }

    // Curriculum Notification CRUD Operations
    fun sendCurriculumNotification(notification: CurriculumNotification): Boolean {
        // Mock send notification logic
        return true
    }

    fun getNotificationHistory(): List<CurriculumNotification> {
        return getCurriculumNotifications()
    }
} 