package com.vatty.mygbu.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vatty.mygbu.data.model.Message
import com.vatty.mygbu.data.model.MessageType
import kotlinx.coroutines.launch

class MessagesViewModel : ViewModel() {

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> = _messages

    private val _announcements = MutableLiveData<List<Message>>()
    val announcements: LiveData<List<Message>> = _announcements

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        loadMessages()
    }

    private fun loadMessages() {
        viewModelScope.launch {
            _isLoading.value = true
            
            try {
                // Sample messages data
                val messagesList = listOf(
                    Message(
                        id = "MSG001",
                        title = "Department Meeting",
                        content = "Weekly department meeting scheduled for tomorrow at 2 PM",
                        senderId = "ADMIN",
                        recipientIds = listOf("FAC001"),
                        timestamp = "2 hours ago",
                        isRead = false,
                        type = MessageType.PERSONAL
                    ),
                    Message(
                        id = "MSG002",
                        title = "Course Update",
                        content = "New syllabus for Data Structures course has been uploaded",
                        senderId = "ACADEMIC",
                        recipientIds = listOf("FAC001"),
                        timestamp = "1 day ago",
                        isRead = true,
                        type = MessageType.PERSONAL
                    )
                )
                
                val announcementsList = listOf(
                    Message(
                        id = "ANN001",
                        title = "Holiday Notice",
                        content = "University will remain closed on 15th August for Independence Day celebration",
                        senderId = "ADMIN",
                        recipientIds = listOf("ALL"),
                        timestamp = "1 week ago",
                        isRead = true,
                        type = MessageType.ANNOUNCEMENT
                    ),
                    Message(
                        id = "ANN002",
                        title = "Exam Schedule",
                        content = "Mid-semester examination schedule has been released. Check your course portal for details.",
                        senderId = "ACADEMIC_OFFICE",
                        recipientIds = listOf("ALL"),
                        timestamp = "2 weeks ago",
                        isRead = true,
                        type = MessageType.ANNOUNCEMENT
                    )
                )
                
                _messages.value = messagesList
                _announcements.value = announcementsList
                
            } catch (e: Exception) {
                _error.value = e.message ?: "An unexpected error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onComposeMessageClicked() {
        _error.value = "Compose Message feature - Coming Soon!"
    }

    fun onBroadcastAnnouncementClicked() {
        _error.value = "Broadcast Announcement feature - Coming Soon!"
    }

    fun refreshMessages() {
        loadMessages()
    }
} 