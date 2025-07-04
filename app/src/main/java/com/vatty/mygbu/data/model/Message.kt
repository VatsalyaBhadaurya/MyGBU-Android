package com.vatty.mygbu.data.model

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("id")
    val id: String,
    @SerializedName("sender_id")
    val senderId: String,
    @SerializedName("sender_name")
    val senderName: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("timestamp")
    val timestamp: Long,
    @SerializedName("is_read")
    val isRead: Boolean = false
) 