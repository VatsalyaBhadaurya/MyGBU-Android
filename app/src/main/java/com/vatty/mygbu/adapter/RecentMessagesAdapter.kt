package com.vatty.mygbu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vatty.mygbu.R
import com.vatty.mygbu.data.model.Message
import java.text.SimpleDateFormat
import java.util.Locale

class RecentMessagesAdapter(
    private var messages: List<Message> = emptyList(),
    private val onMessageClick: (Message) -> Unit
) : RecyclerView.Adapter<RecentMessagesAdapter.MessageViewHolder>() {

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val senderAvatar: ImageView = view.findViewById(R.id.ivSenderAvatar)
        val senderName: TextView = view.findViewById(R.id.tvSenderName)
        val messagePreview: TextView = view.findViewById(R.id.tvMessagePreview)
        val messageTime: TextView = view.findViewById(R.id.tvMessageTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recent_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.senderName.text = message.senderName
        holder.messagePreview.text = message.content
        holder.messageTime.text = formatTime(message.timestamp)
        
        // TODO: Load sender avatar using Glide or Coil
        
        holder.itemView.setOnClickListener { onMessageClick(message) }
    }

    override fun getItemCount() = messages.size

    fun updateMessages(newMessages: List<Message>) {
        messages = newMessages
        notifyDataSetChanged()
    }

    private fun formatTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
        return sdf.format(timestamp)
    }
} 