package com.vatty.mygbu.utils

import android.app.Application
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * SystemErrorMonitor - Monitors system logs and reports critical errors to Telegram
 * 
 * This monitor watches for:
 * - NotificationService errors (like too many toasts)
 * - System_server crashes
 * - Package manager errors
 * - Critical Android system issues
 */
object SystemErrorMonitor {
    
    private const val TAG = "SystemErrorMonitor"
    private var isMonitoring = false
    
    // System error patterns to watch for
    private val criticalErrorPatterns = listOf(
        "NotificationService.*Package has already queued.*toasts",
        "ActivityManager.*ANR in",
        "system_server.*FATAL EXCEPTION",
        "PackageManager.*INSTALL_FAILED",
        "WindowManager.*Window leaked",
        "MediaPlayer.*Error",
        "Camera.*Error",
        "Bluetooth.*Error.*failed",
        "WiFi.*Error.*connection",
        "LocationManager.*Error",
        "BatteryManager.*Critical.*low"
    )
    
    /**
     * Start monitoring system logs for critical errors
     */
    fun startMonitoring(application: Application? = null) {
        if (isMonitoring) {
            Log.d(TAG, "System error monitoring already started")
            return
        }
        
        isMonitoring = true
        Log.i(TAG, "Starting system error monitoring...")
        
        // Monitor in background
        CoroutineScope(Dispatchers.IO).launch {
            monitorSystemLogs()
        }
        
        // Setup periodic app-level error checks
        CoroutineScope(Dispatchers.IO).launch {
            monitorAppErrors()
        }
        
        TelegramLogger.log("🔍 System Error Monitor started - Watching for critical system issues", "SystemErrorMonitor")
    }
    
    /**
     * Stop monitoring
     */
    fun stopMonitoring() {
        isMonitoring = false
        Log.i(TAG, "System error monitoring stopped")
    }
    
    /**
     * Monitor system logs using logcat (if permissions allow)
     */
    private suspend fun monitorSystemLogs() {
        try {
            // Note: This requires READ_LOGS permission which is not available to regular apps on newer Android versions
            // This is more for demonstration - in production, you'd focus on app-level error monitoring
            
            while (isMonitoring) {
                try {
                    // Try to read system logs (may not work on newer Android versions)
                    val process = Runtime.getRuntime().exec("logcat -d -v time *:E")
                    val reader = BufferedReader(InputStreamReader(process.inputStream))
                    
                    var line: String?
                    while (reader.readLine().also { line = it } != null && isMonitoring) {
                        line?.let { logLine ->
                            checkForCriticalErrors(logLine)
                        }
                    }
                    
                    reader.close()
                    process.destroy()
                    
                } catch (e: SecurityException) {
                    Log.w(TAG, "Cannot read system logs due to security restrictions - focusing on app-level monitoring")
                    break // Exit system log monitoring, continue with app-level monitoring
                } catch (e: Exception) {
                    Log.e(TAG, "Error reading system logs: ${e.message}")
                }
                
                delay(10000) // Check every 10 seconds
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "System log monitoring failed: ${e.message}")
        }
    }
    
    /**
     * Monitor app-level errors and system issues we can detect
     */
    private suspend fun monitorAppErrors() {
        var toastCount = 0
        var lastToastWarning = 0L
        
        while (isMonitoring) {
            try {
                // Monitor memory usage
                val runtime = Runtime.getRuntime()
                val usedMemory = runtime.totalMemory() - runtime.freeMemory()
                val maxMemory = runtime.maxMemory()
                val memoryPercent = (usedMemory * 100 / maxMemory).toInt()
                
                if (memoryPercent > 85) {
                    reportSystemIssue("HIGH_MEMORY_USAGE", "Memory usage at $memoryPercent% ($usedMemory/$maxMemory bytes)")
                }
                
                // Monitor for excessive UI operations (simplified detection)
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastToastWarning > 30000) { // Check every 30 seconds
                    // Reset toast counter periodically
                    if (toastCount > 3) {
                        reportSystemIssue("EXCESSIVE_TOASTS", "Detected potential toast overflow - $toastCount toasts in recent period")
                    }
                    toastCount = 0
                    lastToastWarning = currentTime
                }
                
                delay(15000) // Check every 15 seconds
                
            } catch (e: Exception) {
                Log.e(TAG, "Error in app monitoring: ${e.message}")
            }
        }
    }
    
    /**
     * Check log line for critical error patterns
     */
    private fun checkForCriticalErrors(logLine: String) {
        for (pattern in criticalErrorPatterns) {
            if (logLine.matches(Regex(".*$pattern.*", RegexOption.IGNORE_CASE))) {
                reportCriticalSystemError(logLine, pattern)
                break
            }
        }
    }
    
    /**
     * Report critical system error to Telegram
     */
    private fun reportCriticalSystemError(logLine: String, pattern: String) {
        val message = """
            🚨 CRITICAL SYSTEM ERROR DETECTED 🚨
            
            Pattern: $pattern
            
            Log Entry:
            $logLine
            
            Time: ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())}
        """.trimIndent()
        
        Log.e(TAG, "Critical system error detected: $logLine")
        TelegramLogger.logSystemError(message, "CriticalSystemError")
    }
    
    /**
     * Report general system issues
     */
    fun reportSystemIssue(issueType: String, details: String) {
        val message = """
            ⚠️ SYSTEM ISSUE: $issueType
            
            Details: $details
            
            App: MyGBU
            Time: ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())}
        """.trimIndent()
        
        Log.w(TAG, "System issue detected: $issueType - $details")
        TelegramLogger.logSystemError(message, "SystemIssue")
    }
    
    /**
     * Manually report a detected notification service error (like the one you showed)
     */
    fun reportNotificationServiceError(packageName: String, queueCount: Int) {
        val message = """
            🔔 NOTIFICATION SERVICE ERROR
            
            Package: $packageName
            Issue: Already queued $queueCount toasts. Not showing more.
            
            This indicates the app is trying to show too many toast messages.
            Consider implementing toast rate limiting.
        """.trimIndent()
        
        Log.e(TAG, "NotificationService error for package: $packageName")
        TelegramLogger.logSystemError(message, "NotificationService")
    }
    
    /**
     * Report when the app detects it's showing too many toasts
     */
    fun reportToastOverflow(toastCount: Int) {
        reportNotificationServiceError("com.vatty.mygbu", toastCount)
    }
    
    /**
     * Test the system error monitor
     */
    fun testSystemErrorReporting() {
        Log.i(TAG, "Testing system error reporting...")
        
        // Simulate the exact error you showed
        reportNotificationServiceError("com.vatty.mygbu", 5)
        
        // Simulate other system issues
        reportSystemIssue("HIGH_MEMORY_USAGE", "Memory usage at 87% (2.1GB/2.4GB)")
        reportSystemIssue("NETWORK_ERROR", "Failed to connect to server after 3 retries")
        
        TelegramLogger.log("🧪 System error monitoring test completed", "SystemErrorMonitorTest")
    }
} 