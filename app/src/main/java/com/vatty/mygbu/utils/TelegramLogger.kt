package com.vatty.mygbu.utils

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * TelegramLogger - A utility class to send logs to Telegram chat via Bot API
 * 
 * Features:
 * - Sends logs only in debug builds (configurable)
 * - Singleton pattern for easy access from anywhere
 * - Handles network errors gracefully
 * - Formats messages with timestamp and app info
 * - Asynchronous operation to avoid blocking UI
 */
object TelegramLogger {
    
    private const val TAG = "TelegramLogger"
    
    // Telegram Bot Configuration
    private const val BOT_TOKEN = "7589439541:AAF79-w4LZ0KO1V6jAgYmO2GUe-5reFy0Rg"
    private const val BASE_URL = "https://api.telegram.org/bot$BOT_TOKEN/sendMessage"
    
    // Your actual chat ID
    private const val CHAT_ID = "2124838302"
    
    // Configuration
    private var isEnabled = true // Set to false for release builds if needed
    private var includeDeviceInfo = true
    
    // OkHttp client for network requests
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
        .build()
    
    // Date formatter for log timestamps
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    
    /**
     * Enable or disable logging
     * @param enabled true to enable logging, false to disable
     */
    fun setEnabled(enabled: Boolean) {
        isEnabled = enabled
        Log.d(TAG, "TelegramLogger enabled: $enabled")
    }
    
    /**
     * Set whether to include device information in logs
     * @param include true to include device info, false to exclude
     */
    fun setIncludeDeviceInfo(include: Boolean) {
        includeDeviceInfo = include
    }
    
    /**
     * Send a simple log message to Telegram
     * @param message The message to send
     * @param tag Optional tag for the log (defaults to calling class name)
     */
    fun log(message: String, tag: String = getCallerClassName()) {
        if (!isEnabled) {
            Log.d(TAG, "Logging disabled, skipping message: $message")
            return
        }
        
        if (CHAT_ID == "YOUR_CHAT_ID_HERE") {
            Log.w(TAG, "Chat ID not set! Please update CHAT_ID in TelegramLogger")
            return
        }
        
        val formattedMessage = formatMessage(message, tag, LogLevel.INFO)
        sendToTelegram(formattedMessage)
    }
    
    /**
     * Send an error log to Telegram
     * @param message The error message
     * @param throwable Optional throwable for stack trace
     * @param tag Optional tag for the log
     */
    fun logError(message: String, throwable: Throwable? = null, tag: String = getCallerClassName()) {
        if (!isEnabled) return
        if (CHAT_ID == "YOUR_CHAT_ID_HERE") {
            Log.w(TAG, "Chat ID not set! Please update CHAT_ID in TelegramLogger")
            return
        }
        
        val errorMessage = if (throwable != null) {
            "$message\n\nStack trace:\n${throwable.stackTraceToString()}"
        } else {
            message
        }
        
        val formattedMessage = formatMessage(errorMessage, tag, LogLevel.ERROR)
        sendToTelegram(formattedMessage)
    }
    
    /**
     * Send a warning log to Telegram
     * @param message The warning message
     * @param tag Optional tag for the log
     */
    fun logWarning(message: String, tag: String = getCallerClassName()) {
        if (!isEnabled) return
        if (CHAT_ID == "YOUR_CHAT_ID_HERE") {
            Log.w(TAG, "Chat ID not set! Please update CHAT_ID in TelegramLogger")
            return
        }
        
        val formattedMessage = formatMessage(message, tag, LogLevel.WARNING)
        sendToTelegram(formattedMessage)
    }
    
    /**
     * Send a debug log to Telegram
     * @param message The debug message
     * @param tag Optional tag for the log
     */
    fun logDebug(message: String, tag: String = getCallerClassName()) {
        if (!isEnabled) return
        if (CHAT_ID == "YOUR_CHAT_ID_HERE") {
            Log.w(TAG, "Chat ID not set! Please update CHAT_ID in TelegramLogger")
            return
        }
        
        val formattedMessage = formatMessage(message, tag, LogLevel.DEBUG)
        sendToTelegram(formattedMessage)
    }
    
    /**
     * Send a crash report to Telegram
     * @param crashInfo Crash information
     * @param throwable The throwable that caused the crash
     */
    fun logCrash(crashInfo: String, throwable: Throwable) {
        // Always send crash reports, even if logging is disabled
        if (CHAT_ID == "YOUR_CHAT_ID_HERE") {
            Log.w(TAG, "Chat ID not set! Please update CHAT_ID in TelegramLogger")
            return
        }
        
        val crashMessage = "🚨 CRASH REPORT 🚨\n\n$crashInfo\n\nStack trace:\n${throwable.stackTraceToString()}"
        val formattedMessage = formatMessage(crashMessage, "CRASH", LogLevel.CRASH)
        sendToTelegram(formattedMessage)
    }
    
    /**
     * Format the log message with timestamp, app info, and device info
     */
    private fun formatMessage(message: String, tag: String, level: LogLevel): String {
        val timestamp = dateFormatter.format(Date())
        val emoji = level.emoji
        val appInfo = "MyGBU v1.0 (1)" // You can make this dynamic if needed
        
        val deviceInfo = if (includeDeviceInfo) {
            "\nDevice: ${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL} (API ${android.os.Build.VERSION.SDK_INT})"
        } else {
            ""
        }
        
        return """
            $emoji [$level] $tag
            📅 $timestamp
            📱 $appInfo$deviceInfo
            
            📝 Message:
            $message
        """.trimIndent()
    }
    
    /**
     * Send the formatted message to Telegram using OkHttp
     */
    private fun sendToTelegram(message: String) {
        // Use coroutine to avoid blocking the main thread
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val json = JSONObject().apply {
                    put("chat_id", CHAT_ID)
                    put("text", message)
                    put("parse_mode", "HTML")
                }
                
                val requestBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
                
                val request = Request.Builder()
                    .url(BASE_URL)
                    .post(requestBody)
                    .build()
                
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.e(TAG, "Failed to send message to Telegram: ${e.message}", e)
                    }
                    
                    override fun onResponse(call: Call, response: Response) {
                        if (response.isSuccessful) {
                            Log.d(TAG, "Message sent to Telegram successfully")
                        } else {
                            Log.e(TAG, "Telegram API error: ${response.code} - ${response.message}")
                            Log.e(TAG, "Response body: ${response.body?.string()}")
                        }
                        response.close()
                    }
                })
                
            } catch (e: Exception) {
                Log.e(TAG, "Error preparing Telegram message: ${e.message}", e)
            }
        }
    }
    
    /**
     * Get the calling class name for automatic tagging
     */
    private fun getCallerClassName(): String {
        return try {
            val stackTrace = Thread.currentThread().stackTrace
            // Find the first stack trace element that's not from this class or Thread class
            stackTrace.firstOrNull { element ->
                !element.className.contains("TelegramLogger") && 
                !element.className.contains("Thread") &&
                !element.className.contains("VMStack")
            }?.className?.substringAfterLast('.') ?: "Unknown"
        } catch (e: Exception) {
            "Unknown"
        }
    }
    
    /**
     * Test the Telegram logger with a simple message
     */
    fun testConnection() {
        log("🧪 TelegramLogger test message - Connection working!", "TEST")
    }
    
    /**
     * Log levels with emojis for better visual distinction
     */
    private enum class LogLevel(val emoji: String) {
        DEBUG("🐛"),
        INFO("ℹ️"),
        WARNING("⚠️"),
        ERROR("❌"),
        CRASH("🚨")
    }
} 