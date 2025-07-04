package com.vatty.mygbu.utils

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vatty.mygbu.*

/**
 * Helper class to standardize bottom navigation behavior across activities
 */
object BottomNavigationHelper {

    /**
     * Setup bottom navigation with standard configuration
     */
    fun setupBottomNavigation(activity: AppCompatActivity, bottomNav: BottomNavigationView, currentItemId: Int) {
        // Set the current selected item
        bottomNav.selectedItemId = currentItemId

        // Setup item selection listener
        bottomNav.setOnItemSelectedListener { item ->
            if (item.itemId == currentItemId) {
                return@setOnItemSelectedListener true
            }

            val targetActivity = when (item.itemId) {
                R.id.nav_home -> FacultyDashboardActivity::class.java
                R.id.nav_courses -> CoursesActivity::class.java
                R.id.nav_quick_actions -> QuickActionsActivity::class.java
                R.id.nav_notifications -> NotificationsActivity::class.java
                R.id.nav_profile -> FacultyHubActivity::class.java
                else -> null
            }

            if (targetActivity != null) {
                activity.startActivity(Intent(activity, targetActivity))
                activity.finish()
            }

            true
        }
    }

    /**
     * Navigate to a new activity, clearing the back stack if going home
     */
    private fun navigateToActivity(currentActivity: Activity, targetActivityClass: Class<out Activity>) {
        val intent = Intent(currentActivity, targetActivityClass)
        
        // If navigating to home, clear the back stack
        if (targetActivityClass == FacultyDashboardActivity::class.java) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        currentActivity.startActivity(intent)
        
        // Only finish the current activity if not going home (since home clears the stack anyway)
        if (targetActivityClass != FacultyDashboardActivity::class.java) {
            currentActivity.finish()
        }
    }
} 