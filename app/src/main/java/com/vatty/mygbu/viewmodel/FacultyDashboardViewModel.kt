package com.vatty.mygbu.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vatty.mygbu.data.model.Faculty
import com.vatty.mygbu.data.repository.FacultyRepository
import android.util.Log
import kotlinx.coroutines.launch

class FacultyDashboardViewModel : ViewModel() {
    private val TAG = "FacultyDashboardViewModel"
    private val repository = FacultyRepository()
    
    private val _facultyList = MutableLiveData<List<Faculty>>()
    val facultyList: LiveData<List<Faculty>> = _facultyList
    
    private val _currentFaculty = MutableLiveData<Faculty>()
    val currentFaculty: LiveData<Faculty> = _currentFaculty
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error
    
    fun loadFacultyList() {
        viewModelScope.launch {
            try {
                Log.i(TAG, "Loading faculty list")
                _isLoading.value = true
                val faculty = repository.getFacultyList()
                _facultyList.value = faculty
                Log.i(TAG, "Successfully loaded ${faculty.size} faculty members")
            } catch (e: Exception) {
                Log.e(TAG, "Error loading faculty list: ${e.message}")
                _error.value = "Failed to load faculty list: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun loadFacultyById(facultyId: String) {
        viewModelScope.launch {
            try {
                Log.i(TAG, "Loading faculty member with ID: $facultyId")
                _isLoading.value = true
                val faculty = repository.getFacultyById(facultyId)
                if (faculty != null) {
                    _currentFaculty.value = faculty
                    Log.i(TAG, "Successfully loaded faculty member: ${faculty.name}")
                } else {
                    Log.w(TAG, "No faculty member found with ID: $facultyId")
                    _error.value = "Faculty member not found"
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading faculty member: ${e.message}")
                _error.value = "Failed to load faculty member: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
} 