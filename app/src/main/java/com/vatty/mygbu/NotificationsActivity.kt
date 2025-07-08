package com.vatty.mygbu

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.util.Log
import com.vatty.mygbu.utils.BottomNavigationHelper

class NotificationsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var notificationsAdapter: NotificationsAdapter
    private lateinit var bottomNavigation: BottomNavigationView
    
    companion object {
        private const val TAG = "NotificationsActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        // Log activity startup
        Log.i(TAG, "NotificationsActivity started - faculty notifications active")

        setupViews()
        setupRecyclerView()
        setupBottomNavigation()
        loadNotifications()
    }

    private fun setupViews() {
        val backButton = findViewById<ImageView>(R.id.iv_back)
        backButton.setOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.rv_notifications)
        notificationsAdapter = NotificationsAdapter(getNotifications()) { notification ->
            // Handle notification click
            markAsRead(notification)
        }
        recyclerView.adapter = notificationsAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupBottomNavigation() {
        bottomNavigation = findViewById(R.id.bottom_navigation)
        BottomNavigationHelper.setupBottomNavigation(this, bottomNavigation, NotificationsActivity::class.java)
    }

    private fun loadNotifications() {
        // Load notifications from server or local database
    }

    private fun getNotifications(): List<Notification> {
        return listOf(
            Notification(
                id = "1",
                title = "New Assignment Submission",
                message = "Student John Doe submitted Assignment 1 for CS101",
                timestamp = "2 minutes ago",
                type = NotificationType.ASSIGNMENT,
                isRead = false,
                priority = NotificationPriority.HIGH
            ),
            Notification(
                id = "2",
                title = "Leave Request Approved",
                message = "Your leave request for Dec 15-16 has been approved",
                timestamp = "1 hour ago",
                type = NotificationType.LEAVE,
                isRead = false,
                priority = NotificationPriority.MEDIUM
            ),
            Notification(
                id = "3",
                title = "Class Reminder",
                message = "CS201 class starts in 30 minutes - Room 101",
                timestamp = "30 minutes ago",
                type = NotificationType.REMINDER,
                isRead = true,
                priority = NotificationPriority.LOW
            ),
            Notification(
                id = "4",
                title = "Research Paper Update",
                message = "Your paper 'Advanced Algorithms' has been accepted",
                timestamp = "3 hours ago",
                type = NotificationType.RESEARCH,
                isRead = false,
                priority = NotificationPriority.HIGH
            )
        )
    }

    private fun markAsRead(notification: Notification) {
        notification.isRead = true
        notificationsAdapter.notifyDataSetChanged()
    }
}

// Data classes for notifications
data class Notification(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val type: NotificationType,
    var isRead: Boolean,
    val priority: NotificationPriority
)

enum class NotificationType {
    ASSIGNMENT, LEAVE, REMINDER, RESEARCH, GENERAL
}

enum class NotificationPriority {
    HIGH, MEDIUM, LOW
} 