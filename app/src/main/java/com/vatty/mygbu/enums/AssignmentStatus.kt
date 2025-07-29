package com.vatty.mygbu.enums

enum class AssignmentStatus(val displayName: String, val colorRes: Int) {
    PENDING("Pending", android.R.color.holo_orange_light),
    SUBMITTED("Submitted", android.R.color.holo_green_light),
    ACTIVE("Active", android.R.color.holo_green_light),
    OVERDUE("Overdue", android.R.color.holo_red_light),
    GRADED("Graded", android.R.color.holo_blue_light),
    DRAFT("Draft", android.R.color.holo_orange_light)
} 