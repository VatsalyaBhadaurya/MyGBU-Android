package com.vatty.mygbu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vatty.mygbu.data.repository.*

class ViewModelFactory(
    private val facultyRepository: FacultyRepository = FacultyRepositoryImpl(),
    private val courseRepository: CourseRepository = CourseRepositoryImpl()
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(FacultyDashboardViewModel::class.java) -> {
                FacultyDashboardViewModel(facultyRepository) as T
            }
            modelClass.isAssignableFrom(CoursesViewModel::class.java) -> {
                CoursesViewModel(courseRepository) as T
            }
            modelClass.isAssignableFrom(MessagesViewModel::class.java) -> {
                MessagesViewModel() as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
} 