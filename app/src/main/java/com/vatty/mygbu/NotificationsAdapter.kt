package com.vatty.mygbu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class NotificationsAdapter(
    private val notifications: List<Notification>,
    private val onNotificationClick: (Notification) -> Unit
) : RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        holder.bind(notification)
    }

    override fun getItemCount(): Int = notifications.size

    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardView: MaterialCardView = itemView.findViewById(R.id.card_notification)
        private val iconView: ImageView = itemView.findViewById(R.id.iv_notification_icon)
        private val titleView: TextView = itemView.findViewById(R.id.tv_notification_title)
        private val messageView: TextView = itemView.findViewById(R.id.tv_notification_message)
        private val timestampView: TextView = itemView.findViewById(R.id.tv_notification_timestamp)
        private val priorityIndicator: View = itemView.findViewById(R.id.view_priority_indicator)
        private val unreadIndicator: View = itemView.findViewById(R.id.view_unread_indicator)

        fun bind(notification: Notification) {
            titleView.text = notification.title
            messageView.text = notification.message
            timestampView.text = notification.timestamp

            // Set icon based on notification type
            val iconResource = when (notification.type) {
                NotificationType.ASSIGNMENT -> R.drawable.ic_assignment
                NotificationType.LEAVE -> R.drawable.ic_calendar
                NotificationType.REMINDER -> R.drawable.ic_alarm
                NotificationType.RESEARCH -> R.drawable.ic_document
                NotificationType.GENERAL -> R.drawable.ic_info
            }
            iconView.setImageResource(iconResource)

            // Set priority indicator color
            val priorityColor = when (notification.priority) {
                NotificationPriority.HIGH -> R.color.priority_high
                NotificationPriority.MEDIUM -> R.color.priority_medium
                NotificationPriority.LOW -> R.color.priority_low
            }
            priorityIndicator.setBackgroundColor(
                itemView.context.getColor(priorityColor)
            )

            // Show/hide unread indicator
            unreadIndicator.visibility = if (notification.isRead) View.GONE else View.VISIBLE

            // Set card appearance based on read status
            cardView.alpha = if (notification.isRead) 0.7f else 1.0f

            // Handle click
            cardView.setOnClickListener {
                onNotificationClick(notification)
            }
        }
    }
} 