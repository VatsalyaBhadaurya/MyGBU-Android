package com.vatty.mygbu.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vatty.mygbu.data.model.DashboardStats
import com.vatty.mygbu.data.model.Faculty
import com.vatty.mygbu.data.repository.FacultyRepository
import com.vatty.mygbu.data.repository.FacultyRepositoryImpl
import kotlinx.coroutines.launch

class FacultyDashboardViewModel(
    private val facultyRepository: FacultyRepository = FacultyRepositoryImpl()
) : ViewModel() {

    private val _faculty = MutableLiveData<Faculty>()
    val faculty: LiveData<Faculty> = _faculty

    private val _dashboardStats = MutableLiveData<DashboardStats>()
    val dashboardStats: LiveData<DashboardStats> = _dashboardStats

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _greeting = MutableLiveData<String>()
    val greeting: LiveData<String> = _greeting

    init {
        loadDashboardData()
        updateGreeting()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            _isLoading.value = true
            
            try {
                // Load faculty profile
                val facultyResult = facultyRepository.getFacultyProfile()
                if (facultyResult.isSuccess) {
                    facultyResult.getOrNull()?.let { faculty ->
                        _faculty.value = faculty
                    }
                } else {
                    _error.value = "Failed to load faculty profile"
                }

                // Load dashboard stats
                val statsResult = facultyRepository.getDashboardStats()
                if (statsResult.isSuccess) {
                    statsResult.getOrNull()?.let { stats ->
                        _dashboardStats.value = stats
                    }
                } else {
                    _error.value = "Failed to load dashboard statistics"
                }
                
            } catch (e: Exception) {
                _error.value = e.message ?: "An unexpected error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun updateGreeting() {
        val currentHour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        val greetingText = when (currentHour) {
            in 0..11 -> "Good Morning,"
            in 12..16 -> "Good Afternoon,"
            else -> "Good Evening,"
        }
        _greeting.value = greetingText
    }

    fun refreshData() {
        loadDashboardData()
    }

    fun onNotificationClicked() {
        // Handle notification click
        val unreadCount = _dashboardStats.value?.unreadMessages ?: 0
        _error.value = "Notifications: $unreadCount new updates"
    }
} 