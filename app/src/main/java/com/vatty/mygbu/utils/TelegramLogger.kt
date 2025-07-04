package com.vatty.mygbu.utils

import android.app.Application
import android.util.Log
import kotlinx.coroutines.*
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
    private var isOfflineMode = false // Track offline mode

    // Network retry configuration
    private const val MAX_RETRIES = 3
    private const val RETRY_DELAY_MS = 4000L // 4 seconds between retries
    private const val CONNECTION_TIMEOUT = 10L // 10 seconds
    private const val READ_TIMEOUT = 10L // 10 seconds
    private const val WRITE_TIMEOUT = 10L // 10 seconds
    private const val MAX_QUEUE_SIZE = 1000 // Maximum number of messages to queue

    // Rate limiting configuration
    private const val MIN_LOG_INTERVAL = 5000L // 5 seconds between messages
    private const val TELEGRAM_RATE_LIMIT_COOLDOWN = 30000L // 30 seconds cooldown after 429 error
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

    // Coroutine scope for background operations
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    // OkHttp client with better timeouts and settings
    private val client = OkHttpClient.Builder()
        .connectTimeout(CONNECTION_TIMEOUT, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(WRITE_TIMEOUT, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(READ_TIMEOUT, java.util.concurrent.TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
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

        scope.launch {
            log("TelegramLogger initialized - Automatic error capturing enabled", "SYSTEM")
        }
    }

    /**
     * Set up automatic crash reporting
     */
    private fun setupCrashHandler() {
        originalExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()

        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            val crashInfo = "Thread: ${thread.name}\nApp crashed unexpectedly"

            // Use runBlocking to call suspend function from non-suspend context
            runBlocking(Dispatchers.IO) {
                try {
                    logCrashImmediate(crashInfo, throwable)
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to send crash report", e)
                }
            }

            // Call original handler after we've logged the crash
            originalExceptionHandler?.uncaughtException(thread, throwable)
        }
    }

    /**
     * Set up automatic log interception (monitors Android Log calls)
     */
    private fun setupLogInterception() {
        // Start background thread to process queued logs
        scope.launch {
            processLogQueue()
        }
    }

    /**
     * Start retry processor for failed messages
     */
    private fun startRetryProcessor() {
        scope.launch {
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
    /*fun logCrash(crashInfo: String, throwable: Throwable) {
        logCrashImmediate(crashInfo, throwable)
    }*/

    /**
     * Send crash report immediately (used by crash handler)
     */
    private suspend fun logCrashImmediate(message: String, throwable: Throwable? = null) {
        try {
            val fullMessage = buildCrashMessage(message, throwable)
            sendToTelegramSyncWithRetry(fullMessage)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to send crash report", e)
        }
    }

    private fun buildCrashMessage(message: String, throwable: Throwable?): String {
        val timestamp = dateFormatter.format(Date())
        return buildString {
            append("🔴 CRASH REPORT\n")
            append("⏰ Time: $timestamp\n")
            append("📱 Device: ${android.os.Build.MODEL}\n")
            append("📋 Message: $message\n")
            if (throwable != null) {
                append("❌ Exception: ${throwable.javaClass.simpleName}\n")
                append("📝 Details: ${throwable.message}\n")
                append("📚 Stack Trace:\n${throwable.stackTraceToString()}")
            }
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
     * Send message to Telegram with improved error handling
     */
    private suspend fun sendToTelegramWithRetry(message: String, isRetry: Boolean = false) {
        if (!isEnabled) return
        
        // Check queue size
        if (logQueue.size > MAX_QUEUE_SIZE) {
            Log.w(TAG, "Log queue full, dropping oldest messages")
            while (logQueue.size > MAX_QUEUE_SIZE * 0.8) { // Clear 20% of queue
                logQueue.poll()
            }
        }

        // Don't retry if we're in offline mode and this is a retry attempt
        if (isOfflineMode && isRetry) {
            Log.d(TAG, "In offline mode, queuing message for later")
            failedQueue.offer(message)
            return
        }

        var retryCount = 0
        var lastError: Exception? = null

        while (retryCount < MAX_RETRIES) {
            try {
                val json = JSONObject().apply {
                    put("chat_id", CHAT_ID)
                    put("text", message)
                    put("parse_mode", "HTML")
                }

                val request = Request.Builder()
                    .url(BASE_URL)
                    .post(json.toString().toRequestBody("application/json".toMediaTypeOrNull()))
                    .build()

                client.newCall(request).execute().use { response ->
                    when {
                        response.isSuccessful -> {
                            isOfflineMode = false // Reset offline mode on success
                            Log.d(TAG, "Message sent successfully")
                            return
                        }
                        response.code == 429 -> {
                            handleRateLimit()
                            retryCount++
                        }
                        else -> {
                            Log.w(TAG, "Failed to send message: ${response.code}")
                            retryCount++
                        }
                    }
                }
            } catch (e: IOException) {
                lastError = e
                Log.w(TAG, "Network error (attempt ${retryCount + 1}/$MAX_RETRIES): ${e.message}")
                isOfflineMode = true // Set offline mode on network error
                retryCount++
                if (retryCount < MAX_RETRIES) {
                    delay(RETRY_DELAY_MS * (retryCount + 1)) // Exponential backoff
                }
            }
        }

        // If all retries failed, queue for later
        if (retryCount >= MAX_RETRIES) {
            Log.e(TAG, "All retries failed for message", lastError)
            failedQueue.offer(message)
        }
    }

    /**
     * Handle rate limiting with exponential backoff
     */
    private suspend fun handleRateLimit() {
        consecutiveRateLimits++
        val backoffTime = TELEGRAM_RATE_LIMIT_COOLDOWN * (1 shl consecutiveRateLimits.coerceAtMost(5))
        lastRateLimitTime = System.currentTimeMillis()
        Log.w(TAG, "Rate limited, backing off for ${backoffTime}ms")
        delay(backoffTime)
    }

    /**
     * Send message to Telegram synchronously with retry logic (for crash reports)
     */
    private suspend fun sendToTelegramSyncWithRetry(message: String) {
        withContext(Dispatchers.IO) {
            repeat(MAX_RETRIES) { attempt ->
                try {
                    val json = JSONObject().apply {
                        put("chat_id", CHAT_ID)
                        put("text", message)
                        put("parse_mode", "HTML")
                    }

                    val request = Request.Builder()
                        .url(BASE_URL)
                        .post(json.toString().toRequestBody("application/json".toMediaTypeOrNull()))
                        .build()

                    client.newCall(request).execute().use { response ->
                        if (response.isSuccessful) {
                            Log.d(TAG, "✅ Crash report sent successfully on attempt ${attempt + 1}")
                            return@withContext
                        } else if (response.code == 429) {
                            // Handle rate limiting for crash reports
                            val retryAfter = response.header("Retry-After")?.toIntOrNull() ?: 15
                            Log.w(TAG, "Crash report rate limited. Waiting ${retryAfter}s before retry")
                            delay((retryAfter * 1000L))
                        } else {
                            Log.w(TAG, "Crash report API error ${response.code}: ${response.message}")
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to send crash report (Attempt ${attempt + 1}/$MAX_RETRIES)", e)
                    if (attempt < MAX_RETRIES - 1) {
                        delay(RETRY_DELAY_MS)
                    }
                }
            }
            Log.e(TAG, "Failed to send crash report after all retries")
        }
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
