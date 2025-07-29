package com.vatty.mygbu.enums

enum class GrievanceStatus(val displayName: String, val colorRes: Int) {
    PENDING("Pending", android.R.color.holo_orange_light),
    IN_PROGRESS("In Progress", android.R.color.holo_blue_light),
    RESOLVED("Resolved", android.R.color.holo_green_light),
    CLOSED("Closed", android.R.color.holo_blue_dark),
    REJECTED("Rejected", android.R.color.holo_red_light)
}