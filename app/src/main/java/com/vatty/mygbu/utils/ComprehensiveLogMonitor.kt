package com.vatty.mygbu.utils

import android.app.Application
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.atomic.AtomicBoolean

/**
 * ComprehensiveLogMonitor - Monitors BOTH app logs AND Android system logs
 * 
 * Captures:
 * 1. App-specific error logs
 * 2. Android system error logs (NotificationService, ActivityManager, etc.)
 * 3. System_server crashes and errors
 * 4. Package manager issues
 * 5. ANR (Application Not Responding) events
 * 6. Memory and performance warnings
 */
object ComprehensiveLogMonitor {
    
    private const val TAG = "ComprehensiveLogMonitor"
    private val isMonitoring = AtomicBoolean(false)
    private var packageName = "com.vatty.mygbu"
    
    // System error patterns to monitor
    private val systemErrorPatterns = mapOf(
        "NotificationService" to listOf(
            "Package has already queued.*toasts",
            "Enqueue.*notification.*failed",
            "Too many notifications"
        ),
        "ActivityManager" to listOf(
            "ANR in.*$packageName",
            "Process.*$packageName.*has died",
            "Activity.*$packageName.*not responding"
        ),
        "system_server" to listOf(
            "FATAL EXCEPTION",
            "Process crashed",
            "OutOfMemoryError"
        ),
        "PackageManager" to listOf(
            "INSTALL_FAILED.*$packageName",
            "Package.*$packageName.*stopped"
        ),
        "WindowManager" to listOf(
            "Window leaked.*$packageName",
            "View.*$packageName.*not attached"
        ),
        "InputManager" to listOf(
            "Input event injection.*failed",
            "Touch event.*$packageName.*dropped"
        )
    )
    
    // App-specific error keywords
    private val appErrorKeywords = setOf(
        "error", "exception", "crash", "fail", "timeout", "network", 
        "null pointer", "out of memory", "security", "permission",
        "sqlite", "database", "network", "connection", "auth"
    )
    
    /**
     * Start comprehensive monitoring of both app and system logs
     */
    fun startMonitoring(application: Application) {
        if (isMonitoring.getAndSet(true)) {
            Log.d(TAG, "Comprehensive monitoring already started")
            return
        }
        
        packageName = application.packageName
        Log.i(TAG, "Starting comprehensive log monitoring for $packageName...")
        
        // Start multiple monitoring threads
        startSystemLogMonitoring()
        startAppLogMonitoring()
        startPerformanceMonitoring()
        
        // Send initialization message
        TelegramLogger.log("🔍 Comprehensive Log Monitor started - Watching both app and system logs", "ComprehensiveLogMonitor")
    }
    
    /**
     * Stop all monitoring
     */
    fun stopMonitoring() {
        isMonitoring.set(false)
        Log.i(TAG, "Comprehensive log monitoring stopped")
    }
    
    /**
     * Monitor Android system logs using logcat
     */
    private fun startSystemLogMonitoring() {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d(TAG, "Starting system log monitoring...")
            
            while (isMonitoring.get()) {
                try {
                    // Method 1: Monitor all error-level logs
                    monitorLogcatErrors()
                    delay(5000)
                    
                    // Method 2: Monitor specific system services
                    monitorSystemServices()
                    delay(5000)
                    
                } catch (e: Exception) {
                    Log.e(TAG, "Error in system log monitoring: ${e.message}")
                    delay(10000) // Wait longer on error
                }
            }
        }
    }
    
    /**
     * Monitor logcat for error-level system logs
     */
    private suspend fun monitorLogcatErrors() {
        try {
            // Get recent error logs
            val process = Runtime.getRuntime().exec(arrayOf(
                "logcat", "-d", "-v", "time", "*:E"
            ))
            
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val recentLogs = mutableListOf<String>()
            
            // Read recent logs
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                line?.let { logLine ->
                    recentLogs.add(logLine)
                }
            }
            
            // Process recent logs (last 50 lines to avoid spam)
            recentLogs.takeLast(50).forEach { logLine ->
                checkSystemErrorPatterns(logLine)
                checkAppRelatedErrors(logLine)
            }
            
            reader.close()
            process.destroy()
            
            // Clear logcat to avoid re-processing same logs
            Runtime.getRuntime().exec("logcat -c")
            
        } catch (e: SecurityException) {
            Log.w(TAG, "Cannot access system logs - using alternative monitoring")
        } catch (e: Exception) {
            Log.e(TAG, "Error reading system logs: ${e.message}")
        }
    }
    
    /**
     * Monitor specific system services
     */
    private suspend fun monitorSystemServices() {
        try {
            // Monitor specific system components
            val services = listOf("system_server", "ActivityManager", "NotificationService")
            
            for (service in services) {
                val process = Runtime.getRuntime().exec(arrayOf(
                    "logcat", "-d", "-v", "time", "-s", service
                ))
                
                val reader = BufferedReader(InputStreamReader(process.inputStream))
                var line: String?
                
                while (reader.readLine().also { line = it } != null) {
                    line?.let { logLine ->
                        if (containsErrorIndicators(logLine)) {
                            reportSystemLog(logLine, service)
                        }
                    }
                }
                
                reader.close()
                process.destroy()
            }
            
        } catch (e: Exception) {
            Log.w(TAG, "Service-specific monitoring failed: ${e.message}")
        }
    }
    
    /**
     * Monitor app-specific logs and performance
     */
    private fun startAppLogMonitoring() {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d(TAG, "Starting app log monitoring...")
            
            while (isMonitoring.get()) {
                try {
                    // Monitor app-specific logs
                    monitorAppLogs()
                    delay(8000)
                    
                } catch (e: Exception) {
                    Log.e(TAG, "Error in app log monitoring: ${e.message}")
                    delay(15000)
                }
            }
        }
    }
    
    /**
     * Monitor app-specific logcat entries
     */
    private suspend fun monitorAppLogs() {
        try {
            // Get logs for our specific package
            val process = Runtime.getRuntime().exec(arrayOf(
                "logcat", "-d", "-v", "time", "--pid=${android.os.Process.myPid()}"
            ))
            
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var line: String?
            
            while (reader.readLine().also { line = it } != null) {
                line?.let { logLine ->
                    if (containsAppError(logLine)) {
                        reportAppLog(logLine)
                    }
                }
            }
            
            reader.close()
            process.destroy()
            
        } catch (e: Exception) {
            Log.w(TAG, "App log monitoring failed: ${e.message}")
        }
    }
    
    /**
     * Monitor performance metrics
     */
    private fun startPerformanceMonitoring() {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d(TAG, "Starting performance monitoring...")
            
            while (isMonitoring.get()) {
                try {
                    checkMemoryUsage()
                    checkCpuUsage()
                    delay(30000) // Check every 30 seconds
                    
                } catch (e: Exception) {
                    Log.e(TAG, "Error in performance monitoring: ${e.message}")
                    delay(60000)
                }
            }
        }
    }
    
    /**
     * Check system error patterns in log line
     */
    private fun checkSystemErrorPatterns(logLine: String) {
        for ((service, patterns) in systemErrorPatterns) {
            for (pattern in patterns) {
                if (logLine.contains(service, ignoreCase = true) && 
                    logLine.matches(Regex(".*$pattern.*", RegexOption.IGNORE_CASE))) {
                    reportSystemError(logLine, service, pattern)
                    return
                }
            }
        }
    }
    
    /**
     * Check for app-related errors in system logs
     */
    private fun checkAppRelatedErrors(logLine: String) {
        if (logLine.contains(packageName, ignoreCase = true)) {
            if (containsErrorIndicators(logLine)) {
                reportAppRelatedSystemError(logLine)
            }
        }
    }
    
    /**
     * Check if log line contains error indicators
     */
    private fun containsErrorIndicators(logLine: String): Boolean {
        val errorIndicators = listOf("E/", "ERROR", "FATAL", "Exception", "Error", "Failed", "Crash")
        return errorIndicators.any { indicator ->
            logLine.contains(indicator, ignoreCase = true)
        }
    }
    
    /**
     * Check if log line contains app-specific errors
     */
    private fun containsAppError(logLine: String): Boolean {
        val lowerLine = logLine.lowercase()
        return appErrorKeywords.any { keyword ->
            lowerLine.contains(keyword)
        } && containsErrorIndicators(logLine)
    }
    
    /**
     * Check memory usage
     */
    private fun checkMemoryUsage() {
        try {
            val runtime = Runtime.getRuntime()
            val usedMemory = runtime.totalMemory() - runtime.freeMemory()
            val maxMemory = runtime.maxMemory()
            val memoryPercent = (usedMemory * 100 / maxMemory).toInt()
            
            if (memoryPercent > 85) {
                val message = """
                    📊 HIGH MEMORY USAGE DETECTED
                    
                    Memory Usage: $memoryPercent%
                    Used: ${usedMemory / 1024 / 1024} MB
                    Max: ${maxMemory / 1024 / 1024} MB
                    
                    App: $packageName
                """.trimIndent()
                
                TelegramLogger.logSystemError(message, "MemoryMonitor")
                Log.w(TAG, "High memory usage: $memoryPercent%")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error checking memory usage: ${e.message}")
        }
    }
    
    /**
     * Check CPU usage (simplified)
     */
    private fun checkCpuUsage() {
        try {
            // Get CPU info from /proc/stat (simplified approach)
            val process = Runtime.getRuntime().exec("cat /proc/loadavg")
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val loadAvg = reader.readLine()
            reader.close()
            
            loadAvg?.let { load ->
                val parts = load.split(" ")
                if (parts.isNotEmpty()) {
                    val currentLoad = parts[0].toFloatOrNull()
                    if (currentLoad != null && currentLoad > 2.0f) {
                        TelegramLogger.logSystemError(
                            "⚡ HIGH CPU LOAD: $currentLoad (App: $packageName)",
                            "CPUMonitor"
                        )
                    }
                }
            }
        } catch (e: Exception) {
            // CPU monitoring may not be available on all devices
            Log.d(TAG, "CPU monitoring not available: ${e.message}")
        }
    }
    
    /**
     * Report system error to Telegram
     */
    private fun reportSystemError(logLine: String, service: String, pattern: String) {
        val message = """
            🚨 SYSTEM ERROR DETECTED
            
            Service: $service
            Pattern: $pattern
            
            Log Entry:
            $logLine
            
            Time: ${getCurrentTimestamp()}
        """.trimIndent()
        
        Log.e(TAG, "System error detected in $service: $logLine")
        TelegramLogger.logSystemError(message, "SystemError_$service")
    }
    
    /**
     * Report app-related system error
     */
    private fun reportAppRelatedSystemError(logLine: String) {
        val message = """
            ⚠️ APP-RELATED SYSTEM ERROR
            
            Package: $packageName
            
            System Log:
            $logLine
            
            Time: ${getCurrentTimestamp()}
        """.trimIndent()
        
        Log.w(TAG, "App-related system error: $logLine")
        TelegramLogger.logSystemError(message, "AppSystemError")
    }
    
    /**
     * Report system log entry
     */
    private fun reportSystemLog(logLine: String, service: String) {
        val message = """
            📋 SYSTEM LOG: $service
            
            $logLine
            
            Time: ${getCurrentTimestamp()}
        """.trimIndent()
        
        Log.i(TAG, "System log from $service: $logLine")
        TelegramLogger.logSystemError(message, "SystemLog_$service")
    }
    
    /**
     * Report app log entry
     */
    private fun reportAppLog(logLine: String) {
        val message = """
            📱 APP ERROR LOG
            
            Package: $packageName
            
            $logLine
            
            Time: ${getCurrentTimestamp()}
        """.trimIndent()
        
        Log.w(TAG, "App error log: $logLine")
        TelegramLogger.logError(message, tag = "AppErrorLog")
    }
    
    /**
     * Get current timestamp
     */
    private fun getCurrentTimestamp(): String {
        return java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
            .format(java.util.Date())
    }
    
    /**
     * Test comprehensive monitoring
     */
    fun testComprehensiveMonitoring() {
        Log.i(TAG, "Testing comprehensive monitoring...")
        
        // Test 1: Generate app error
        LogWrapper.e("TestApp", "This is a test app error that should be captured")
        
        // Test 2: Simulate system errors
        reportSystemError(
            "2025-06-11 11:40:25.961   591-706   NotificationService     system_server                        E  Package has already queued 5 toasts. Not showing more. Package=$packageName",
            "NotificationService",
            "Package has already queued.*toasts"
        )
        
        // Test 3: Simulate ANR
        reportSystemError(
            "2025-06-11 11:41:00.123   123-456   ActivityManager         system_server                        E  ANR in $packageName (MainActivity)",
            "ActivityManager", 
            "ANR in.*$packageName"
        )
        
        // Test 4: Memory warning
        checkMemoryUsage()
        
        TelegramLogger.log("🧪 Comprehensive monitoring test completed - Both app and system logs tested", "ComprehensiveTest")
    }
} 