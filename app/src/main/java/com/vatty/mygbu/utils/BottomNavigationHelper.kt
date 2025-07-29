package com.vatty.mygbu.utils

import android.content.Context
import android.content.Intent
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vatty.mygbu.*

/**
 * Common bottom navigation helper for consistent navigation across all activities
 */
object BottomNavigationHelper {
    
    /**
     * Setup bottom navigation with appropriate menu based on activity type
     */
    fun setupBottomNavigation(
        context: Context,
        bottomNavigation: BottomNavigationView,
        currentActivity: Class<*>
    ) {
        // Clear existing menu and inflate appropriate menu based on activity type
        bottomNavigation.menu.clear()
        
        val menuRes = when {
            isStudentActivity(currentActivity) -> R.menu.student_bottom_navigation_menu
            isFacultyActivity(currentActivity) -> R.menu.faculty_bottom_navigation_menu
            else -> R.menu.faculty_bottom_navigation_menu // Default to faculty
        }
        
        bottomNavigation.inflateMenu(menuRes)
        
        // Set the current item based on the activity
        val currentItemId = getCurrentItemId(currentActivity)
        bottomNavigation.selectedItemId = currentItemId
        
        // Setup navigation listener
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                // Faculty Navigation
                R.id.nav_faculty_dashboard -> {
                    if (currentActivity != FacultyDashboardActivity::class.java) {
                        context.startActivity(Intent(context, FacultyDashboardActivity::class.java))
                        if (context is android.app.Activity) {
                            context.finish()
                        }
                    }
                    true
                }
                R.id.nav_courses -> {
                    if (currentActivity != CoursesActivity::class.java) {
                        context.startActivity(Intent(context, CoursesActivity::class.java))
                        if (context is android.app.Activity) {
                            context.finish()
                        }
                    }
                    true
                }
                R.id.nav_quick_actions -> {
                    if (currentActivity != QuickActionsActivity::class.java) {
                        context.startActivity(Intent(context, QuickActionsActivity::class.java))
                        if (context is android.app.Activity) {
                            context.finish()
                        }
                    }
                    true
                }
                R.id.nav_analytics -> {
                    if (currentActivity != AnalyticsActivity::class.java) {
                        context.startActivity(Intent(context, AnalyticsActivity::class.java))
                        if (context is android.app.Activity) {
                            context.finish()
                        }
                    }
                    true
                }
                R.id.nav_profile -> {
                    if (currentActivity != FacultyHubActivity::class.java) {
                        context.startActivity(Intent(context, FacultyHubActivity::class.java))
                        if (context is android.app.Activity) {
                            context.finish()
                        }
                    }
                    true
                }
                
                // Student Navigation
                R.id.nav_student_dashboard -> {
                    if (currentActivity != StudentDashboardActivity::class.java) {
                        context.startActivity(Intent(context, StudentDashboardActivity::class.java))
                        if (context is android.app.Activity) {
                            context.finish()
                        }
                    }
                    true
                }
                R.id.nav_student_assignments -> {
                    if (currentActivity != StudentAssignmentsActivity::class.java) {
                        context.startActivity(Intent(context, StudentAssignmentsActivity::class.java))
                        if (context is android.app.Activity) {
                            context.finish()
                        }
                    }
                    true
                }
                R.id.nav_student_exams -> {
                    if (currentActivity != StudentExamsActivity::class.java) {
                        context.startActivity(Intent(context, StudentExamsActivity::class.java))
                        if (context is android.app.Activity) {
                            context.finish()
                        }
                    }
                    true
                }
                R.id.nav_student_fees -> {
                    if (currentActivity != StudentFeesActivity::class.java) {
                        context.startActivity(Intent(context, StudentFeesActivity::class.java))
                        if (context is android.app.Activity) {
                            context.finish()
                        }
                    }
                    true
                }
                R.id.nav_student_profile -> {
                    if (currentActivity != StudentProfileActivity::class.java) {
                        context.startActivity(Intent(context, StudentProfileActivity::class.java))
                        if (context is android.app.Activity) {
                            context.finish()
                        }
                    }
                    true
                }
                else -> false
            }
        }
    }
    
    /**
     * Get the current navigation item ID based on the activity
     */
    private fun getCurrentItemId(currentActivity: Class<*>): Int {
        return when (currentActivity) {
            // Faculty Activities
            FacultyDashboardActivity::class.java -> R.id.nav_faculty_dashboard
            CoursesActivity::class.java -> R.id.nav_courses
            QuickActionsActivity::class.java -> R.id.nav_quick_actions
            AnalyticsActivity::class.java -> R.id.nav_analytics
            FacultyHubActivity::class.java -> R.id.nav_profile
            
            // Student Activities
            StudentDashboardActivity::class.java -> R.id.nav_student_dashboard
            StudentProfileActivity::class.java -> R.id.nav_student_profile
            StudentAssignmentsActivity::class.java -> R.id.nav_student_assignments
            StudentExamsActivity::class.java -> R.id.nav_student_exams
            StudentAttendanceActivity::class.java -> R.id.nav_student_dashboard
            StudentFeesActivity::class.java -> R.id.nav_student_fees
            StudentScheduleActivity::class.java -> R.id.nav_student_dashboard
            StudentPlacementActivity::class.java -> R.id.nav_student_dashboard
            StudentHostelActivity::class.java -> R.id.nav_student_dashboard
            StudentGrievanceActivity::class.java -> R.id.nav_student_dashboard
            
            // Map secondary activities to closest navigation items
            StudentPerformanceActivity::class.java -> R.id.nav_analytics
            NotificationsActivity::class.java -> R.id.nav_faculty_dashboard
            LeaveRequestsActivity::class.java -> R.id.nav_analytics
            AttendanceActivity::class.java -> R.id.nav_faculty_dashboard
            AssignmentManagementActivity::class.java -> R.id.nav_courses
            MessagesActivity::class.java -> R.id.nav_faculty_dashboard
            ScheduleActivity::class.java -> R.id.nav_quick_actions
            else -> R.id.nav_faculty_dashboard
        }
    }
    
    /**
     * Check if the activity is a student activity
     */
    private fun isStudentActivity(activity: Class<*>): Boolean {
        return activity.name.startsWith("com.vatty.mygbu.Student")
    }
    
    /**
     * Check if the activity is a faculty activity
     */
    private fun isFacultyActivity(activity: Class<*>): Boolean {
        return activity.name.startsWith("com.vatty.mygbu.Faculty") || 
               activity.name == "com.vatty.mygbu.CoursesActivity" ||
               activity.name == "com.vatty.mygbu.QuickActionsActivity" ||
               activity.name == "com.vatty.mygbu.AnalyticsActivity" ||
               activity.name == "com.vatty.mygbu.FacultyHubActivity"
    }
} 