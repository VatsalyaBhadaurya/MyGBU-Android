package com.vatty.mygbu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.vatty.mygbu.databinding.ActivityNotificationsBinding
import com.vatty.mygbu.data.model.Notification
import com.vatty.mygbu.enums.NotificationPriority
import com.vatty.mygbu.enums.NotificationType
import com.vatty.mygbu.utils.ErrorHandler

class NotificationsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationsBinding
    private lateinit var adapter: NotificationsAdapter
    private lateinit var errorHandler: ErrorHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        errorHandler = ErrorHandler(this)
        setupViews()
        loadNotifications()
    }

    private fun setupViews() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        adapter = NotificationsAdapter()
        binding.rvNotifications.apply {
            layoutManager = LinearLayoutManager(this@NotificationsActivity)
            adapter = this@NotificationsActivity.adapter
        }
    }

    private fun loadNotifications() {
        try {
            errorHandler.showLoadingState()
            // TODO: Load actual notifications from backend
            val dummyNotifications = createDummyNotifications()
            adapter.updateNotifications(dummyNotifications)
            errorHandler.hideLoadingState()
        } catch (e: Exception) {
            errorHandler.showError(e)
        }
    }

    private fun createDummyNotifications(): List<Notification> {
        return listOf(
            Notification(
                "1",
                "New Assignment Submission",
                "John Doe submitted Assignment 1 for CS101",
                "2 minutes ago",
                NotificationType.ASSIGNMENT,
                NotificationPriority.HIGH
            ),
            Notification(
                "2",
                "Attendance Updated",
                "Attendance for CS102 has been updated",
                "1 hour ago",
                NotificationType.ATTENDANCE,
                NotificationPriority.MEDIUM
            ),
            Notification(
                "3",
                "Course Schedule Change",
                "CS103 class rescheduled to 2:00 PM",
                "2 hours ago",
                NotificationType.COURSE,
                NotificationPriority.LOW
            )
        )
    }
} 