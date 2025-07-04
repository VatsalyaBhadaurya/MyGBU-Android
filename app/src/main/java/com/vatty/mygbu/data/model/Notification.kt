package com.vatty.mygbu.data.model

import com.vatty.mygbu.enums.NotificationPriority
import com.vatty.mygbu.enums.NotificationType

data class Notification(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val type: NotificationType,
    val priority: NotificationPriority,
    val isRead: Boolean = false
) 