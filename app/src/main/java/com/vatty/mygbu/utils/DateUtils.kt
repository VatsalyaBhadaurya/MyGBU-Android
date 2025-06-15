package com.vatty.mygbu.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    
    private const val DEFAULT_DATE_FORMAT = "MMM dd, yyyy"
    private const val TIME_FORMAT = "HH:mm"
    private const val DATETIME_FORMAT = "MMM dd, yyyy HH:mm"
    
    fun getCurrentDateFormatted(pattern: String = DEFAULT_DATE_FORMAT): String {
        val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        return dateFormat.format(Date())
    }
    
    fun getCurrentTime(): String {
        val timeFormat = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())
        return timeFormat.format(Date())
    }
    
    fun getCurrentDateTime(): String {
        val dateTimeFormat = SimpleDateFormat(DATETIME_FORMAT, Locale.getDefault())
        return dateTimeFormat.format(Date())
    }
    
    fun formatDate(date: Date, pattern: String = DEFAULT_DATE_FORMAT): String {
        val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        return dateFormat.format(date)
    }
    
    fun getGreeting(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 5..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon" 
            in 17..21 -> "Good Evening"
            else -> "Good Night"
        }
    }
} 