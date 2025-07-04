package com.vatty.mygbu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vatty.mygbu.data.model.Notification
import com.vatty.mygbu.databinding.ItemNotificationBinding
import com.vatty.mygbu.enums.NotificationPriority
import com.vatty.mygbu.enums.NotificationType

class NotificationsAdapter : RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder>() {
    private var notifications = mutableListOf<Notification>()

    fun updateNotifications(newNotifications: List<Notification>) {
        notifications.clear()
        notifications.addAll(newNotifications)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = ItemNotificationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(notifications[position])
    }

    override fun getItemCount() = notifications.size

    inner class NotificationViewHolder(
        private val binding: ItemNotificationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(notification: Notification) {
            binding.tvNotificationTitle.text = notification.title
            binding.tvNotificationMessage.text = notification.message
            binding.tvNotificationTimestamp.text = notification.timestamp

            // Set icon based on notification type
            val iconResId = when (notification.type) {
                NotificationType.ASSIGNMENT -> R.drawable.ic_assignment
                NotificationType.ATTENDANCE -> R.drawable.ic_attendance
                NotificationType.COURSE -> R.drawable.ic_courses
                NotificationType.LEAVE -> R.drawable.ic_leave
                NotificationType.REMINDER -> R.drawable.ic_alarm
                NotificationType.RESEARCH -> R.drawable.ic_research
                NotificationType.EMERGENCY -> R.drawable.ic_emergency
                NotificationType.ANNOUNCEMENT -> R.drawable.ic_announcement
            }
            binding.ivNotificationIcon.setImageResource(iconResId)

            // Set priority color
            val priorityColor = when (notification.priority) {
                NotificationPriority.HIGH -> R.color.error_red
                NotificationPriority.MEDIUM -> R.color.warning_orange
                NotificationPriority.LOW -> R.color.success_green
            }
            binding.viewPriorityIndicator.setBackgroundColor(
                binding.root.context.getColor(priorityColor)
            )
        }
    }
} 