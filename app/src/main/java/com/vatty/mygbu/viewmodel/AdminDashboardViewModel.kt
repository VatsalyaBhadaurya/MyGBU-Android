package com.vatty.mygbu.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vatty.mygbu.data.model.*
import com.vatty.mygbu.data.repository.AdminRepository

class AdminDashboardViewModel : ViewModel() {
    
    private val adminRepository = AdminRepository()
    
    private val _dashboardData = MutableLiveData<AdminDashboard>()
    val dashboardData: LiveData<AdminDashboard> = _dashboardData
    
    private val _onboardingRequests = MutableLiveData<List<OnboardingRequest>>()
    val onboardingRequests: LiveData<List<OnboardingRequest>> = _onboardingRequests
    
    private val _approvalRequests = MutableLiveData<List<ApprovalRequest>>()
    val approvalRequests: LiveData<List<ApprovalRequest>> = _approvalRequests
    
    private val _examSchedules = MutableLiveData<List<ExamSchedule>>()
    val examSchedules: LiveData<List<ExamSchedule>> = _examSchedules
    
    private val _degreeAudits = MutableLiveData<List<DegreeAudit>>()
    val degreeAudits: LiveData<List<DegreeAudit>> = _degreeAudits
    
    private val _projects = MutableLiveData<List<Project>>()
    val projects: LiveData<List<Project>> = _projects
    
    private val _fileRecords = MutableLiveData<List<FileRecord>>()
    val fileRecords: LiveData<List<FileRecord>> = _fileRecords

    // Course Mapping related LiveData
    private val _courseMappings = MutableLiveData<List<CourseMapping>>()
    val courseMappings: LiveData<List<CourseMapping>> = _courseMappings

    private val _programs = MutableLiveData<List<Program>>()
    val programs: LiveData<List<Program>> = _programs

    private val _syllabusDocuments = MutableLiveData<List<SyllabusDocument>>()
    val syllabusDocuments: LiveData<List<SyllabusDocument>> = _syllabusDocuments

    private val _electivePools = MutableLiveData<List<ElectivePool>>()
    val electivePools: LiveData<List<ElectivePool>> = _electivePools

    private val _creditRules = MutableLiveData<List<CreditRule>>()
    val creditRules: LiveData<List<CreditRule>> = _creditRules

    private val _curriculumNotifications = MutableLiveData<List<CurriculumNotification>>()
    val curriculumNotifications: LiveData<List<CurriculumNotification>> = _curriculumNotifications

    private val _creditValidationResult = MutableLiveData<CreditValidationResult>()
    val creditValidationResult: LiveData<CreditValidationResult> = _creditValidationResult
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage
    
    init {
        loadDashboardData()
    }
    
    fun loadDashboardData() {
        _isLoading.value = true
        try {
            _dashboardData.value = adminRepository.getAdminDashboard()
            _isLoading.value = false
        } catch (e: Exception) {
            _errorMessage.value = "Failed to load dashboard data: ${e.message}"
            _isLoading.value = false
        }
    }
    
    fun loadOnboardingRequests() {
        _isLoading.value = true
        try {
            _onboardingRequests.value = adminRepository.getOnboardingRequests()
            _isLoading.value = false
        } catch (e: Exception) {
            _errorMessage.value = "Failed to load onboarding requests: ${e.message}"
            _isLoading.value = false
        }
    }
    
    fun loadApprovalRequests() {
        _isLoading.value = true
        try {
            _approvalRequests.value = adminRepository.getApprovalRequests()
            _isLoading.value = false
        } catch (e: Exception) {
            _errorMessage.value = "Failed to load approval requests: ${e.message}"
            _isLoading.value = false
        }
    }
    
    fun loadExamSchedules() {
        _isLoading.value = true
        try {
            _examSchedules.value = adminRepository.getExamSchedules()
            _isLoading.value = false
        } catch (e: Exception) {
            _errorMessage.value = "Failed to load exam schedules: ${e.message}"
            _isLoading.value = false
        }
    }
    
    fun loadDegreeAudits() {
        _isLoading.value = true
        try {
            _degreeAudits.value = adminRepository.getDegreeAudits()
            _isLoading.value = false
        } catch (e: Exception) {
            _errorMessage.value = "Failed to load degree audits: ${e.message}"
            _isLoading.value = false
        }
    }
    
    fun loadProjects() {
        _isLoading.value = true
        try {
            _projects.value = adminRepository.getProjects()
            _isLoading.value = false
        } catch (e: Exception) {
            _errorMessage.value = "Failed to load projects: ${e.message}"
            _isLoading.value = false
        }
    }
    
    fun loadFileRecords() {
        _isLoading.value = true
        try {
            _fileRecords.value = adminRepository.getFileRecords()
            _isLoading.value = false
        } catch (e: Exception) {
            _errorMessage.value = "Failed to load file records: ${e.message}"
            _isLoading.value = false
        }
    }

    // Course Mapping related load methods
    fun loadCourseMappings() {
        _isLoading.value = true
        try {
            _courseMappings.value = adminRepository.getCourseMappings()
            _isLoading.value = false
        } catch (e: Exception) {
            _errorMessage.value = "Failed to load course mappings: ${e.message}"
            _isLoading.value = false
        }
    }

    fun loadPrograms() {
        _isLoading.value = true
        try {
            _programs.value = adminRepository.getPrograms()
            _isLoading.value = false
        } catch (e: Exception) {
            _errorMessage.value = "Failed to load programs: ${e.message}"
            _isLoading.value = false
        }
    }

    fun loadSyllabusDocuments() {
        _isLoading.value = true
        try {
            _syllabusDocuments.value = adminRepository.getSyllabusDocuments()
            _isLoading.value = false
        } catch (e: Exception) {
            _errorMessage.value = "Failed to load syllabus documents: ${e.message}"
            _isLoading.value = false
        }
    }

    fun loadElectivePools() {
        _isLoading.value = true
        try {
            _electivePools.value = adminRepository.getElectivePools()
            _isLoading.value = false
        } catch (e: Exception) {
            _errorMessage.value = "Failed to load elective pools: ${e.message}"
            _isLoading.value = false
        }
    }

    fun loadCreditRules() {
        _isLoading.value = true
        try {
            _creditRules.value = adminRepository.getCreditRules()
            _isLoading.value = false
        } catch (e: Exception) {
            _errorMessage.value = "Failed to load credit rules: ${e.message}"
            _isLoading.value = false
        }
    }

    fun loadCurriculumNotifications() {
        _isLoading.value = true
        try {
            _curriculumNotifications.value = adminRepository.getCurriculumNotifications()
            _isLoading.value = false
        } catch (e: Exception) {
            _errorMessage.value = "Failed to load curriculum notifications: ${e.message}"
            _isLoading.value = false
        }
    }
    
    fun approveOnboardingRequest(requestId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        try {
            val success = adminRepository.approveOnboardingRequest(requestId)
            if (success) {
                onSuccess()
                loadOnboardingRequests() // Refresh the list
            } else {
                onError("Failed to approve request")
            }
        } catch (e: Exception) {
            onError("Error approving request: ${e.message}")
        }
    }
    
    fun rejectOnboardingRequest(requestId: String, reason: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        try {
            val success = adminRepository.rejectOnboardingRequest(requestId, reason)
            if (success) {
                onSuccess()
                loadOnboardingRequests() // Refresh the list
            } else {
                onError("Failed to reject request")
            }
        } catch (e: Exception) {
            onError("Error rejecting request: ${e.message}")
        }
    }
    
    fun updateApprovalStatus(requestId: String, status: ApprovalStatus, remarks: String?, onSuccess: () -> Unit, onError: (String) -> Unit) {
        try {
            val success = adminRepository.updateApprovalStatus(requestId, status, remarks)
            if (success) {
                onSuccess()
                loadApprovalRequests() // Refresh the list
            } else {
                onError("Failed to update approval status")
            }
        } catch (e: Exception) {
            onError("Error updating approval status: ${e.message}")
        }
    }
    
    fun escalateRequest(requestId: String, reason: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        try {
            val success = adminRepository.escalateRequest(requestId, reason)
            if (success) {
                onSuccess()
                loadApprovalRequests() // Refresh the list
            } else {
                onError("Failed to escalate request")
            }
        } catch (e: Exception) {
            onError("Error escalating request: ${e.message}")
        }
    }
    
    fun generateAdmitCards(examId: String, onSuccess: (List<AdmitCard>) -> Unit, onError: (String) -> Unit) {
        try {
            val admitCards = adminRepository.generateAdmitCards(examId)
            onSuccess(admitCards)
        } catch (e: Exception) {
            onError("Error generating admit cards: ${e.message}")
        }
    }
    
    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    // Course Mapping CRUD Operations
    fun addCourseMapping(courseMapping: CourseMapping, onSuccess: () -> Unit, onError: (String) -> Unit) {
        try {
            val success = adminRepository.addCourseMapping(courseMapping)
            if (success) {
                onSuccess()
                loadCourseMappings() // Refresh the list
            } else {
                onError("Failed to add course mapping")
            }
        } catch (e: Exception) {
            onError("Error adding course mapping: ${e.message}")
        }
    }

    fun updateCourseMapping(courseMapping: CourseMapping, onSuccess: () -> Unit, onError: (String) -> Unit) {
        try {
            val success = adminRepository.updateCourseMapping(courseMapping)
            if (success) {
                onSuccess()
                loadCourseMappings() // Refresh the list
            } else {
                onError("Failed to update course mapping")
            }
        } catch (e: Exception) {
            onError("Error updating course mapping: ${e.message}")
        }
    }

    fun deleteCourseMapping(courseMappingId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        try {
            val success = adminRepository.deleteCourseMapping(courseMappingId)
            if (success) {
                onSuccess()
                loadCourseMappings() // Refresh the list
            } else {
                onError("Failed to delete course mapping")
            }
        } catch (e: Exception) {
            onError("Error deleting course mapping: ${e.message}")
        }
    }

    // Syllabus Management CRUD Operations
    fun uploadSyllabus(syllabusDocument: SyllabusDocument, onSuccess: () -> Unit, onError: (String) -> Unit) {
        try {
            val success = adminRepository.uploadSyllabus(syllabusDocument)
            if (success) {
                onSuccess()
                loadSyllabusDocuments() // Refresh the list
            } else {
                onError("Failed to upload syllabus")
            }
        } catch (e: Exception) {
            onError("Error uploading syllabus: ${e.message}")
        }
    }

    fun deleteSyllabus(syllabusId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        try {
            val success = adminRepository.deleteSyllabus(syllabusId)
            if (success) {
                onSuccess()
                loadSyllabusDocuments() // Refresh the list
            } else {
                onError("Failed to delete syllabus")
            }
        } catch (e: Exception) {
            onError("Error deleting syllabus: ${e.message}")
        }
    }

    // Elective Pool CRUD Operations
    fun addElectivePool(electivePool: ElectivePool, onSuccess: () -> Unit, onError: (String) -> Unit) {
        try {
            val success = adminRepository.addElectivePool(electivePool)
            if (success) {
                onSuccess()
                loadElectivePools() // Refresh the list
            } else {
                onError("Failed to add elective pool")
            }
        } catch (e: Exception) {
            onError("Error adding elective pool: ${e.message}")
        }
    }

    fun deleteElectivePool(poolId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        try {
            val success = adminRepository.deleteElectivePool(poolId)
            if (success) {
                onSuccess()
                loadElectivePools() // Refresh the list
            } else {
                onError("Failed to delete elective pool")
            }
        } catch (e: Exception) {
            onError("Error deleting elective pool: ${e.message}")
        }
    }

    fun addCourseToPool(poolId: String, course: ElectivePoolCourse, onSuccess: () -> Unit, onError: (String) -> Unit) {
        try {
            val success = adminRepository.addCourseToPool(poolId, course)
            if (success) {
                onSuccess()
                loadElectivePools() // Refresh the list
            } else {
                onError("Failed to add course to pool")
            }
        } catch (e: Exception) {
            onError("Error adding course to pool: ${e.message}")
        }
    }

    // Credit Validation CRUD Operations
    fun addCreditRule(creditRule: CreditRule, onSuccess: () -> Unit, onError: (String) -> Unit) {
        try {
            val success = adminRepository.addCreditRule(creditRule)
            if (success) {
                onSuccess()
                loadCreditRules() // Refresh the list
            } else {
                onError("Failed to add credit rule")
            }
        } catch (e: Exception) {
            onError("Error adding credit rule: ${e.message}")
        }
    }

    fun deleteCreditRule(ruleId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        try {
            val success = adminRepository.deleteCreditRule(ruleId)
            if (success) {
                onSuccess()
                loadCreditRules() // Refresh the list
            } else {
                onError("Failed to delete credit rule")
            }
        } catch (e: Exception) {
            onError("Error deleting credit rule: ${e.message}")
        }
    }

    fun validateCredits(programId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        try {
            val result = adminRepository.validateCredits(programId)
            _creditValidationResult.value = result
            onSuccess()
        } catch (e: Exception) {
            onError("Error validating credits: ${e.message}")
        }
    }

    // Curriculum Notification Operations
    fun sendCurriculumNotification(notification: CurriculumNotification, onSuccess: () -> Unit, onError: (String) -> Unit) {
        try {
            val success = adminRepository.sendCurriculumNotification(notification)
            if (success) {
                onSuccess()
                loadCurriculumNotifications() // Refresh the list
            } else {
                onError("Failed to send notification")
            }
        } catch (e: Exception) {
            onError("Error sending notification: ${e.message}")
        }
    }
} 