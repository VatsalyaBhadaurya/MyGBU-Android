package com.vatty.mygbu

import android.app.Application
import com.vatty.mygbu.utils.TelegramLogger
import com.vatty.mygbu.utils.SystemErrorMonitor
import com.vatty.mygbu.utils.ComprehensiveLogMonitor
import com.vatty.mygbu.utils.LogWrapper as Log

/**
 * Custom Application class for MyGBU
 * Initializes comprehensive monitoring for both app and system logs
 */
class MyGBUApplication : Application() {
    
    companion object {
        private const val TAG = "MyGBUApplication"
        
        /**
         * Custom Log wrapper methods that automatically send errors to Telegram
         * Use these instead of Android's Log.e() for automatic Telegram logging
         */
        @JvmStatic
        fun logError(tag: String, message: String, throwable: Throwable? = null) {
            // Log to Android system
            if (throwable != null) {
                Log.e(tag, message, throwable)
            } else {
                Log.e(tag, message)
            }
            
            // Automatically send to Telegram
            TelegramLogger.interceptLog("E", tag, message, throwable)
        }
        
        @JvmStatic
        fun logWarning(tag: String, message: String) {
            Log.w(tag, message)
            TelegramLogger.interceptLog("W", tag, message)
        }
        
        @JvmStatic
        fun logInfo(tag: String, message: String) {
            Log.i(tag, message)
            TelegramLogger.interceptLog("I", tag, message)
        }
        
        /**
         * Report system errors that you detect
         */
        @JvmStatic
        fun reportSystemError(errorMessage: String, source: String = "SYSTEM") {
            TelegramLogger.logSystemError(errorMessage, source)
        }
    }
    
    override fun onCreate() {
        super.onCreate()
        
        Log.d(TAG, "MyGBU Application starting...")
        
        // Initialize TelegramLogger with automatic error capturing
        TelegramLogger.initialize(this)
        
        // Initialize Basic System Error Monitor
        SystemErrorMonitor.startMonitoring(this)
        
        // Initialize Comprehensive Log Monitor (monitors BOTH app and system logs)
        ComprehensiveLogMonitor.startMonitoring(this)
        
        Log.i(TAG, "MyGBU Application initialized with comprehensive log monitoring (app + system)")
        
        // Send startup notification
        TelegramLogger.log("🚀 MyGBU App started with COMPREHENSIVE monitoring (app + system logs)", "AppStartup")
    }
    
    override fun onTerminate() {
        super.onTerminate()
        
        // Stop all monitoring when app terminates
        SystemErrorMonitor.stopMonitoring()
        ComprehensiveLogMonitor.stopMonitoring()
        
        Log.i(TAG, "MyGBU Application terminated")
    }
}