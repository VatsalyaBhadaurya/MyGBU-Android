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
     * Setup bottom navigation with unified menu and navigation logic
     */
    fun setupBottomNavigation(
        context: Context,
        bottomNavigation: BottomNavigationView,
        currentActivity: Class<*>
    ) {
        // Set the unified menu
        bottomNavigation.menu.clear()
        bottomNavigation.inflateMenu(R.menu.unified_bottom_navigation_menu)
        
        // Set the current item based on the activity
        val currentItemId = getCurrentItemId(currentActivity)
        bottomNavigation.selectedItemId = currentItemId
        
        // Setup navigation listener
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
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
                else -> false
            }
        }
    }
    
    /**
     * Get the current navigation item ID based on the activity
     */
    private fun getCurrentItemId(currentActivity: Class<*>): Int {
        return when (currentActivity) {
            FacultyDashboardActivity::class.java -> R.id.nav_home
            CoursesActivity::class.java -> R.id.nav_courses
            QuickActionsActivity::class.java -> R.id.nav_quick_actions
            AnalyticsActivity::class.java -> R.id.nav_analytics
            FacultyHubActivity::class.java -> R.id.nav_profile
            // Map secondary activities to closest navigation items
            StudentPerformanceActivity::class.java -> R.id.nav_analytics
            NotificationsActivity::class.java -> R.id.nav_home
            LeaveRequestsActivity::class.java -> R.id.nav_analytics
            AttendanceActivity::class.java -> R.id.nav_home
            AssignmentManagementActivity::class.java -> R.id.nav_courses
            MessagesActivity::class.java -> R.id.nav_home
            ScheduleActivity::class.java -> R.id.nav_quick_actions
            else -> R.id.nav_home
        }
    }
} 