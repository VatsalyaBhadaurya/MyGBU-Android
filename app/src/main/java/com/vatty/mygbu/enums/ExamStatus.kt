package com.vatty.mygbu.enums

enum class ExamStatus(val displayName: String, val colorRes: Int) {
    UPCOMING("Upcoming", android.R.color.holo_orange_light),
    ONGOING("Ongoing", android.R.color.holo_blue_light),
    COMPLETED("Completed", android.R.color.holo_green_light),
    CANCELLED("Cancelled", android.R.color.holo_red_light)
}