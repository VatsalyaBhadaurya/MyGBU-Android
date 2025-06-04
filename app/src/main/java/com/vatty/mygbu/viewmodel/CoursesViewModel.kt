package com.vatty.mygbu.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vatty.mygbu.data.model.Course
import com.vatty.mygbu.data.repository.CourseRepository
import com.vatty.mygbu.data.repository.CourseRepositoryImpl
import kotlinx.coroutines.launch

class CoursesViewModel(
    private val courseRepository: CourseRepository = CourseRepositoryImpl()
) : ViewModel() {

    private val _courses = MutableLiveData<List<Course>>()
    val courses: LiveData<List<Course>> = _courses

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        loadCourses()
    }

    private fun loadCourses() {
        viewModelScope.launch {
            _isLoading.value = true
            
            try {
                val result = courseRepository.getCourses()
                if (result.isSuccess) {
                    _courses.value = result.getOrNull() ?: emptyList()
                } else {
                    _error.value = "Failed to load courses"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "An unexpected error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refreshCourses() {
        loadCourses()
    }

    fun onCourseSelected(course: Course) {
        // Handle course selection logic
        _error.value = "Selected: ${course.title}"
    }
} 