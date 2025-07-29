package com.vatty.mygbu.enums

enum class LeaveStatus(val displayName: String, val colorRes: Int) {
    PENDING("Pending", android.R.color.holo_orange_light),
    APPROVED("Approved", android.R.color.holo_green_light),
    REJECTED("Rejected", android.R.color.holo_red_light),
    CANCELLED("Cancelled", android.R.color.holo_blue_light)
}