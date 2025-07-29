package com.vatty.mygbu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vatty.mygbu.R
import com.vatty.mygbu.data.model.CurriculumNotification
import java.text.SimpleDateFormat
import java.util.*

class NotificationHistoryAdapter(
    private val onViewDetailsClick: (CurriculumNotification) -> Unit
) : ListAdapter<CurriculumNotification, NotificationHistoryAdapter.NotificationHistoryViewHolder>(NotificationHistoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification_history, parent, false)
        return NotificationHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationHistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NotificationHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNotificationTitle: TextView = itemView.findViewById(R.id.tv_notification_title)
        private val tvProgramSemester: TextView = itemView.findViewById(R.id.tv_program_semester)
        private val tvNotificationMessage: TextView = itemView.findViewById(R.id.tv_notification_message)
        private val tvSentDate: TextView = itemView.findViewById(R.id.tv_sent_date)
        private val tvSentTime: TextView = itemView.findViewById(R.id.tv_sent_time)
        private val tvRecipientCount: TextView = itemView.findViewById(R.id.tv_recipient_count)
        private val tvRecipients: TextView = itemView.findViewById(R.id.tv_recipients)
        private val btnViewDetails: Button = itemView.findViewById(R.id.btn_view_details)

        fun bind(notification: CurriculumNotification) {
            tvNotificationTitle.text = notification.title
            
            val semesterText = if (notification.semester != null) {
                "${notification.programName} - Semester ${notification.semester}"
            } else {
                "${notification.programName} - All Semesters"
            }
            tvProgramSemester.text = semesterText
            
            tvNotificationMessage.text = notification.message
            
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            tvSentDate.text = dateFormat.format(notification.sentDate)
            tvSentTime.text = timeFormat.format(notification.sentDate)
            
            tvRecipientCount.text = "${notification.recipientCount} Recipients"
            tvRecipients.text = notification.recipients.joinToString(", ")

            btnViewDetails.setOnClickListener {
                onViewDetailsClick(notification)
            }
        }
    }

    private class NotificationHistoryDiffCallback : DiffUtil.ItemCallback<CurriculumNotification>() {
        override fun areItemsTheSame(oldItem: CurriculumNotification, newItem: CurriculumNotification): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CurriculumNotification, newItem: CurriculumNotification): Boolean {
            return oldItem == newItem
        }
    }
} 