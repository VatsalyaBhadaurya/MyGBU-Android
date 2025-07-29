package com.vatty.mygbu.enums

enum class PlacementStatus(val displayName: String, val colorRes: Int) {
    APPLIED("Applied", android.R.color.holo_blue_light),
    SHORTLISTED("Shortlisted", android.R.color.holo_orange_light),
    INTERVIEW_SCHEDULED("Interview Scheduled", android.R.color.holo_green_light),
    SELECTED("Selected", android.R.color.holo_green_dark),
    REJECTED("Rejected", android.R.color.holo_red_light),
    WITHDRAWN("Withdrawn", android.R.color.holo_blue_dark)
}