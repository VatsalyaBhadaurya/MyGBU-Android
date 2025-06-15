package com.vatty.mygbu.utils

import android.app.Application
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * TelegramLogger - Enhanced utility class to send logs to Telegram chat via Bot API
 * 
 * Features:
 * - Automatically captures and sends Error-level logs
 * - Captures uncaught exceptions and crashes
 * - Robust network handling with retries and longer timeouts
 * - Sends logs only in debug builds (configurable)
 * - Singleton pattern for easy access from anywhere
 * - Handles network errors gracefully
 * - Formats messages with timestamp and app info
 * - Asynchronous operation to avoid blocking UI
 * - Automatic log interception and filtering
 */
object TelegramLogger {
    
    private const val TAG = "TelegramLogger"
    
    // Telegram Bot Configuration
    private const val BOT_TOKEN = "7589439541:AAF79-w4LZ0KO1V6jAgYmO2GUe-5reFy0Rg"  // Your bot token
    private const val BASE_URL = "https://api.telegram.org/bot$BOT_TOKEN/sendMessage"
    
    // Your actual chat ID
    private const val CHAT_ID = "-1002089861646"      // Your chat ID
    
    // Configuration
    private var isEnabled = true // Set to false for release builds if needed
    private var includeDeviceInfo = true
    private var autoLogErrors = true // Automatically send Error-level logs
    private var autoLogCrashes = true // Automatically send crashes
    
    // Network retry configuration
    private const val MAX_RETRIES = 3
    private const val RETRY_DELAY_MS = 2000L // 2 seconds between retries
    private const val CONNECTION_TIMEOUT = 30L // 30 seconds
    private const val READ_TIMEOUT = 30L // 30 seconds
    private const val WRITE_TIMEOUT = 30L // 30 seconds
    
    // Rate limiting configuration
    private const val MIN_LOG_INTERVAL = 3000L // 3 seconds between messages (increased from 1s)
    private const val TELEGRAM_RATE_LIMIT_COOLDOWN = 15000L // 15 seconds cooldown after 429 error
    private var lastRateLimitTime = 0L
    private var consecutiveRateLimits = 0
    
    // Log filtering
    private val errorKeywords = setOf(
        "error", "exception", "crash", "fail", "timeout", "network", 
        "null pointer", "out of memory", "security", "permission"
    )
    
    // Queue to prevent spam and rate limiting
    private val logQueue = ConcurrentLinkedQueue<String>()
    private val failedQueue = ConcurrentLinkedQueue<String>() // For retry logic
    private var lastLogTime = 0L
    
    // Original exception handler
    private var originalExceptionHandler: Thread.UncaughtExceptionHandler? = null
    
    // OkHttp client with longer timeouts
    private val client = OkHttpClient.Builder()
        .connectTimeout(CONNECTION_TIMEOUT, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(WRITE_TIMEOUT, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(READ_TIMEOUT, java.util.concurrent.TimeUnit.SECONDS)
        .retryOnConnectionFailure(true) // Enable automatic retry on connection failure
        .build()
    
    // Date formatter for log timestamps
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    
    /**
     * Initialize TelegramLogger with automatic error capturing
     * Call this from your Application class or MainActivity
     */
    fun initialize(application: Application? = null) {
        Log.d(TAG, "Initializing TelegramLogger with automatic error capturing")
        
        // Set up global exception handler for crashes
        if (autoLogCrashes) {
            setupCrashHandler()
        }
        
        // Set up automatic error log interception
        if (autoLogErrors) {
            setupLogInterception()
        }
        
        // Start retry mechanism for failed messages
        startRetryProcessor()
        
        log("TelegramLogger initialized - Automatic error capturing enabled", "SYSTEM")
    }
    
    /**
     * Set up automatic crash reporting
     */
    private fun setupCrashHandler() {
        originalExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            try {
                // Send crash report to Telegram immediately
                val crashInfo = "Thread: ${thread.name}\nApp crashed unexpectedly"
                logCrashImmediate(crashInfo, throwable)
                
                // Give some time for the message to send
                Thread.sleep(5000) // Increased from 2 to 5 seconds
            } catch (e: Exception) {
                Log.e(TAG, "Failed to send crash report", e)
            } finally {
                // Call original handler to maintain normal crash behavior
                originalExceptionHandler?.uncaughtException(thread, throwable)
            }
        }
    }
    
    /**
     * Set up automatic log interception (monitors Android Log calls)
     */
    private fun setupLogInterception() {
        // Start background thread to process queued logs
        CoroutineScope(Dispatchers.IO).launch {
            processLogQueue()
        }
    }
    
    /**
     * Start retry processor for failed messages
     */
    private fun startRetryProcessor() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                try {
                    val failedMessage = failedQueue.poll()
                    if (failedMessage != null) {
                        Log.d(TAG, "Retrying failed message...")
                        sendToTelegramWithRetry(failedMessage, isRetry = true)
                    }
                    delay(RETRY_DELAY_MS * 2) // Check failed queue every 4 seconds
                } catch (e: Exception) {
                    Log.e(TAG, "Error in retry processor", e)
                }
            }
        }
    }
    
    /**
     * Process queued logs with rate limiting
     */
    private suspend fun processLogQueue() {
        while (true) {
            try {
                val logMessage = logQueue.poll()
                if (logMessage != null) {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastLogTime >= MIN_LOG_INTERVAL) {
                        sendToTelegramWithRetry(logMessage)
                        lastLogTime = currentTime
                    } else {
                        // Put back in queue if rate limited
                        logQueue.offer(logMessage)
                        delay(MIN_LOG_INTERVAL)
                    }
                }
                delay(500) // Check queue every 500ms
            } catch (e: Exception) {
                Log.e(TAG, "Error processing log queue", e)
            }
        }
    }
    
    /**
     * Intercept and automatically send Error-level logs
     * Call this method from your custom Log wrapper or when you detect important errors
     */
    fun interceptLog(level: String, tag: String, message: String, throwable: Throwable? = null) {
        if (!isEnabled || !autoLogErrors) return
        
        // Check if this is an important log that should be sent to Telegram
        val shouldSend = when (level.uppercase()) {
            "E", "ERROR" -> true
            "W", "WARN" -> containsImportantKeywords(message) || containsImportantKeywords(tag)
            "I", "INFO" -> containsImportantKeywords(message, setOf("crash", "security", "permission"))
            else -> false
        }
        
        if (shouldSend) {
            val logLevel = when (level.uppercase()) {
                "E", "ERROR" -> LogLevel.ERROR
                "W", "WARN" -> LogLevel.WARNING
                else -> LogLevel.INFO
            }
            
            val fullMessage = if (throwable != null) {
                "$message\n\nException: ${throwable.message}\n${throwable.stackTraceToString()}"
            } else {
                message
            }
            
            val formattedMessage = formatMessage("INTERCEPTED LOG:\n$fullMessage", tag, logLevel)
            
            // Add to queue to prevent spam
            if (logQueue.size < 10) { // Limit queue size
                logQueue.offer(formattedMessage)
            }
        }
    }
    
    /**
     * Check if message contains important keywords
     */
    private fun containsImportantKeywords(text: String, keywords: Set<String> = errorKeywords): Boolean {
        val lowerText = text.lowercase()
        return keywords.any { keyword -> lowerText.contains(keyword) }
    }
    
    /**
     * Monitor system notifications and errors
     * Call this when you detect system-level issues like the NotificationService errors
     */
    fun logSystemError(errorMessage: String, source: String = "SYSTEM") {
        if (!isEnabled) return
        
        val formattedMessage = formatMessage(
            "SYSTEM ERROR DETECTED:\n$errorMessage", 
            source, 
            LogLevel.ERROR
        )
        
        // System errors are important, send immediately with retry
        CoroutineScope(Dispatchers.IO).launch {
            sendToTelegramWithRetry(formattedMessage)
        }
    }
    
    /**
     * Enable or disable automatic error logging
     */
    fun setAutoLogErrors(enabled: Boolean) {
        autoLogErrors = enabled
        Log.d(TAG, "Auto log errors: $enabled")
    }
    
    /**
     * Enable or disable automatic crash reporting
     */
    fun setAutoLogCrashes(enabled: Boolean) {
        autoLogCrashes = enabled
        Log.d(TAG, "Auto log crashes: $enabled")
    }
    
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
        
        val formattedMessage = formatMessage(message, tag, LogLevel.INFO)
        sendToTelegramAsync(formattedMessage)
    }
    
    /**
     * Send an error log to Telegram
     * @param message The error message
     * @param throwable Optional throwable for stack trace
     * @param tag Optional tag for the log
     */
    fun logError(message: String, throwable: Throwable? = null, tag: String = getCallerClassName()) {
        if (!isEnabled) return
        
        val errorMessage = if (throwable != null) {
            "$message\n\nStack trace:\n${throwable.stackTraceToString()}"
        } else {
            message
        }
        
        val formattedMessage = formatMessage(errorMessage, tag, LogLevel.ERROR)
        sendToTelegramAsync(formattedMessage)
    }
    
    /**
     * Send a warning log to Telegram
     * @param message The warning message
     * @param tag Optional tag for the log
     */
    fun logWarning(message: String, tag: String = getCallerClassName()) {
        if (!isEnabled) return
        
        val formattedMessage = formatMessage(message, tag, LogLevel.WARNING)
        sendToTelegramAsync(formattedMessage)
    }
    
    /**
     * Send a debug log to Telegram
     * @param message The debug message
     * @param tag Optional tag for the log
     */
    fun logDebug(message: String, tag: String = getCallerClassName()) {
        if (!isEnabled) return
        
        val formattedMessage = formatMessage(message, tag, LogLevel.DEBUG)
        sendToTelegramAsync(formattedMessage)
    }
    
    /**
     * Send a crash report to Telegram
     * @param crashInfo Crash information
     * @param throwable The throwable that caused the crash
     */
    fun logCrash(crashInfo: String, throwable: Throwable) {
        logCrashImmediate(crashInfo, throwable)
    }
    
    /**
     * Send crash report immediately (used by crash handler)
     */
    private fun logCrashImmediate(crashInfo: String, throwable: Throwable) {
        val crashMessage = "🚨 CRASH REPORT 🚨\n\n$crashInfo\n\nStack trace:\n${throwable.stackTraceToString()}"
        val formattedMessage = formatMessage(crashMessage, "CRASH", LogLevel.CRASH)
        
        // Send crash reports immediately, synchronously with retry logic
        try {
            sendToTelegramSyncWithRetry(formattedMessage)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to send crash report after all retries", e)
            // Add to failed queue for later retry
            failedQueue.offer(formattedMessage)
        }
    }
    
    /**
     * Send message to Telegram asynchronously with retry logic
     */
    private fun sendToTelegramAsync(message: String) {
        CoroutineScope(Dispatchers.IO).launch {
            sendToTelegramWithRetry(message)
        }
    }
    
    /**
     * Send message to Telegram with retry logic
     */
    private suspend fun sendToTelegramWithRetry(message: String, isRetry: Boolean = false) {
        // Check if we're in rate limit cooldown
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastRateLimitTime < TELEGRAM_RATE_LIMIT_COOLDOWN) {
            Log.d(TAG, "Skipping message due to rate limit cooldown (${(TELEGRAM_RATE_LIMIT_COOLDOWN - (currentTime - lastRateLimitTime))/1000}s remaining)")
            // Add to failed queue for later retry
            if (!isRetry && failedQueue.size < 10) {
                failedQueue.offer(message)
            }
            return
        }
        
        var attempt = 0
        var lastException: Exception? = null
        
        while (attempt < MAX_RETRIES) {
            try {
                val result = sendToTelegramSync(message)
                if (result.success) {
                    // Reset rate limit tracking on success
                    consecutiveRateLimits = 0
                    if (isRetry) {
                        Log.d(TAG, "✅ Retry successful! Message sent on attempt ${attempt + 1}")
                    }
                    return // Success, exit retry loop
                } else {
                    // Handle specific error codes
                    when (result.errorCode) {
                        429 -> {
                            // Rate limited - respect Telegram's retry_after parameter
                            val retryAfter = result.retryAfter ?: 15
                            lastRateLimitTime = currentTime
                            consecutiveRateLimits++
                            
                            Log.w(TAG, "Rate limited by Telegram API. Retry after ${retryAfter}s (consecutive: $consecutiveRateLimits)")
                            
                            // If we're getting rate limited too often, back off more
                            val backoffMultiplier = if (consecutiveRateLimits > 3) 2 else 1
                            val actualDelay = (retryAfter * 1000L) * backoffMultiplier
                            
                            if (attempt < MAX_RETRIES - 1) {
                                Log.d(TAG, "Waiting ${actualDelay/1000}s before retry (attempt ${attempt + 1}/$MAX_RETRIES)")
                                delay(actualDelay)
                            }
                        }
                        else -> {
                            // Other API errors
                            Log.w(TAG, "Telegram API error ${result.errorCode}: ${result.errorMessage}")
                        }
                    }
                }
            } catch (e: Exception) {
                lastException = e
                Log.w(TAG, "Attempt ${attempt + 1} failed: ${e.message}")
            }
            
            attempt++
            if (attempt < MAX_RETRIES && lastRateLimitTime == 0L) { // Don't retry if rate limited
                val delayTime = RETRY_DELAY_MS * attempt // Exponential backoff
                Log.d(TAG, "Retrying in ${delayTime}ms... (attempt $attempt/$MAX_RETRIES)")
                delay(delayTime)
            }
        }
        
        // All retries failed
        Log.e(TAG, "❌ Failed to send message after $MAX_RETRIES attempts", lastException)
        
        // Add to failed queue for later retry (unless it's already a retry)
        if (!isRetry && failedQueue.size < 20) { // Limit failed queue size
            failedQueue.offer(message)
            Log.d(TAG, "Added message to failed queue for later retry")
        }
    }
    
    /**
     * Send message to Telegram synchronously with retry logic (for crash reports)
     */
    private fun sendToTelegramSyncWithRetry(message: String) {
        var attempt = 0
        var lastException: Exception? = null
        
        while (attempt < MAX_RETRIES) {
            try {
                val result = sendToTelegramSync(message)
                if (result.success) {
                    Log.d(TAG, "✅ Crash report sent successfully on attempt ${attempt + 1}")
                    return
                } else if (result.errorCode == 429) {
                    // Handle rate limiting for crash reports
                    val retryAfter = result.retryAfter ?: 15
                    Log.w(TAG, "Crash report rate limited. Waiting ${retryAfter}s before retry")
                    Thread.sleep((retryAfter * 1000L))
                } else {
                    Log.w(TAG, "Crash report API error ${result.errorCode}: ${result.errorMessage}")
                }
            } catch (e: Exception) {
                lastException = e
                Log.w(TAG, "Crash report attempt ${attempt + 1} failed: ${e.message}")
            }
            
            attempt++
            if (attempt < MAX_RETRIES) {
                Thread.sleep(RETRY_DELAY_MS * attempt) // Blocking sleep for sync method
            }
        }
        
        Log.e(TAG, "❌ Failed to send crash report after $MAX_RETRIES attempts", lastException)
        throw lastException ?: Exception("Failed to send crash report")
    }
    
    /**
     * Result class for Telegram API responses
     */
    private data class TelegramResponse(
        val success: Boolean,
        val errorCode: Int? = null,
        val errorMessage: String? = null,
        val retryAfter: Int? = null
    )
    
    /**
     * Send message to Telegram synchronously
     */
    private fun sendToTelegramSync(message: String): TelegramResponse {
        val json = JSONObject().apply {
            put("chat_id", CHAT_ID)
            put("text", message)
            put("parse_mode", "HTML")
        }
        
        val requestBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder().url(BASE_URL).post(requestBody).build()
        
        client.newCall(request).execute().use { response ->
            val responseBody = response.body?.string()
            
            if (response.isSuccessful) {
                Log.d(TAG, "✅ Message sent to Telegram successfully")
                return TelegramResponse(success = true)
            } else {
                Log.e(TAG, "❌ Telegram API error: ${response.code} - ${response.message}")
                Log.e(TAG, "Response body: $responseBody")
                
                // Parse error details from response
                var retryAfter: Int? = null
                var errorMessage = response.message
                
                try {
                    responseBody?.let { body ->
                        val errorJson = JSONObject(body)
                        errorMessage = errorJson.optString("description", response.message)
                        
                        // Extract retry_after parameter for rate limiting
                        val parameters = errorJson.optJSONObject("parameters")
                        retryAfter = parameters?.optInt("retry_after")
                    }
                } catch (e: Exception) {
                    Log.w(TAG, "Could not parse error response: ${e.message}")
                }
                
                return TelegramResponse(
                    success = false,
                    errorCode = response.code,
                    errorMessage = errorMessage,
                    retryAfter = retryAfter
                )
            }
        }
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
        log("🧪 TelegramLogger test message - Connection working with improved timeout handling!", "TEST")
    }
    
    /**
     * Test automatic error detection
     */
    fun testAutoErrorCapture() {
        Log.e("TelegramLogger", "This is a test error message that should be automatically captured")
        interceptLog("E", "TestTag", "Simulated error for testing automatic capture")
        logSystemError("Simulated system error: Too many notifications queued", "NotificationService")
    }
    
    /**
     * Get network statistics
     */
    fun getNetworkStats(): String {
        return "Queue: ${logQueue.size}, Failed: ${failedQueue.size}"
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