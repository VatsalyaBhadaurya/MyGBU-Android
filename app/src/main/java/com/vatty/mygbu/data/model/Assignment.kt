package com.vatty.mygbu.data.model

import com.vatty.mygbu.enums.AssignmentStatus

data class SavedAssignment(
    val title: String,
    val courseCode: String,
    val dueDate: String,
    val status: AssignmentStatus,
    val attachmentName: String? = null
) 