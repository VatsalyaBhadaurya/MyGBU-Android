package com.vatty.mygbu.utils

import android.util.Log

/**
 * LogWrapper - Custom Log wrapper that automatically captures important logs
 * and sends them to Telegram via TelegramLogger
 * 
 * Usage: Replace android.util.Log calls with LogWrapper calls:
 * LogWrapper.e(tag, message) instead of Log.e(tag, message)
 * 
 * This wrapper will:
 * 1. Call the original Android Log method
 * 2. Automatically send Error/Warning logs to Telegram
 * 3. Filter system-level errors for monitoring
 */
object LogWrapper {
    
    private const val TAG = "LogWrapper"
    
    /**
     * Debug log - calls original Log.d and optionally sends to Telegram
     */
    fun d(tag: String, message: String): Int {
        val result = Log.d(tag, message)
        
        // Only send debug logs if they contain important keywords
        if (containsImportantKeywords(message, setOf("crash", "error", "exception"))) {
            TelegramLogger.interceptLog("D", tag, message)
        }
        
        return result
    }
    
    /**
     * Info log - calls original Log.i and optionally sends to Telegram
     */
    fun i(tag: String, message: String): Int {
        val result = Log.i(tag, message)
        
        // Send info logs that contain important keywords
        if (containsImportantKeywords(message, setOf("crash", "security", "permission", "notification"))) {
            TelegramLogger.interceptLog("I", tag, message)
        }
        
        return result
    }
    
    /**
     * Warning log - calls original Log.w and sends to Telegram
     */
    fun w(tag: String, message: String): Int {
        val result = Log.w(tag, message)
        
        // Automatically send all warnings to Telegram
        TelegramLogger.interceptLog("W", tag, message)
        
        return result
    }
    
    /**
     * Warning log with throwable - calls original Log.w and sends to Telegram
     */
    fun w(tag: String, message: String, throwable: Throwable): Int {
        val result = Log.w(tag, message, throwable)
        
        // Send warning with exception details
        TelegramLogger.interceptLog("W", tag, message, throwable)
        
        return result
    }
    
    /**
     * Error log - calls original Log.e and sends to Telegram
     */
    fun e(tag: String, message: String): Int {
        val result = Log.e(tag, message)
        
        // Automatically send all errors to Telegram
        TelegramLogger.interceptLog("E", tag, message)
        
        return result
    }
    
    /**
     * Error log with throwable - calls original Log.e and sends to Telegram
     */
    fun e(tag: String, message: String, throwable: Throwable): Int {
        val result = Log.e(tag, message, throwable)
        
        // Send error with exception details
        TelegramLogger.interceptLog("E", tag, message, throwable)
        
        return result
    }
    
    /**
     * Verbose log - calls original Log.v
     */
    fun v(tag: String, message: String): Int {
        return Log.v(tag, message)
        // Don't send verbose logs to Telegram (too much noise)
    }
    
    /**
     * What a Terrible Failure log - calls original Log.wtf and sends to Telegram
     */
    fun wtf(tag: String, message: String): Int {
        val result = Log.wtf(tag, message)
        
        // WTF logs are always important
        TelegramLogger.interceptLog("WTF", tag, "TERRIBLE FAILURE: $message")
        
        return result
    }
    
    /**
     * What a Terrible Failure log with throwable
     */
    fun wtf(tag: String, message: String, throwable: Throwable): Int {
        val result = Log.wtf(tag, message, throwable)
        
        // WTF logs with exceptions are critical
        TelegramLogger.interceptLog("WTF", tag, "TERRIBLE FAILURE: $message", throwable)
        
        return result
    }
    
    /**
     * Check if message contains important keywords
     */
    private fun containsImportantKeywords(text: String, keywords: Set<String>): Boolean {
        val lowerText = text.lowercase()
        return keywords.any { keyword -> lowerText.contains(keyword) }
    }
    
    /**
     * Manually log system errors that we detect
     */
    fun logSystemError(message: String, source: String = "SYSTEM") {
        Log.e(source, message)
        TelegramLogger.logSystemError(message, source)
    }
    
    /**
     * Monitor system logs for specific patterns
     * Call this to report system-level issues you detect
     */
    fun reportSystemIssue(issue: String, details: String = "") {
        val fullMessage = if (details.isNotEmpty()) "$issue\n$details" else issue
        Log.w("SystemMonitor", fullMessage)
        TelegramLogger.logSystemError(fullMessage, "SystemMonitor")
    }
} 