package com.vatty.mygbu.utils

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

/**
 * TelegramLoggerTest - Test utility for TelegramLogger
 * 
 * Usage:
 * TelegramLoggerTest.runAllTests()
 */
object TelegramLoggerTest {
    
    private const val TAG = "TelegramLoggerTest"
    
    /**
     * Send a simple test message
     */
    fun sendTestMessage() {
        Log.d(TAG, "Sending test message...")
        TelegramLogger.log("🧪 Test message with improved network handling - ${System.currentTimeMillis()}", "TEST")
    }
    
    /**
     * Test network resilience with multiple rapid messages
     */
    fun testNetworkResilience() {
        Log.d(TAG, "Testing network resilience...")
        CoroutineScope(Dispatchers.IO).launch {
            repeat(5) { i ->
                TelegramLogger.log("📡 Network test message $i - Testing timeout improvements", "NETWORK_TEST")
                delay(1000) // 1 second between messages
            }
        }
    }
    
    /**
     * Test error handling
     */
    fun testErrorHandling() {
        Log.d(TAG, "Testing error handling...")
        TelegramLogger.logError("🚫 Test error with improved retry logic", Exception("Simulated exception for testing"), "ERROR_TEST")
    }
    
    /**
     * Test automatic error capture
     */
    fun testAutoErrorCapture() {
        Log.d(TAG, "Testing automatic error capture...")
        TelegramLogger.testAutoErrorCapture()
    }
    
    /**
     * Test system error reporting
     */
    fun testSystemError() {
        Log.d(TAG, "Testing system error reporting...")
        TelegramLogger.logSystemError("📱 System error test - Timeout handling improved", "SYSTEM_TEST")
    }
    
    /**
     * Test queue monitoring
     */
    fun testQueueMonitoring() {
        Log.d(TAG, "Testing queue monitoring...")
        val stats = TelegramLogger.getNetworkStats()
        TelegramLogger.log("📊 Queue Status: $stats", "QUEUE_TEST")
        Log.d(TAG, "Current queue stats: $stats")
    }
    
    /**
     * Test rapid message sending (rate limiting)
     */
    fun testRapidMessages() {
        Log.d(TAG, "Testing rapid message sending and rate limiting...")
        repeat(3) { i ->
            TelegramLogger.log("⚡ Rapid message $i - Testing rate limiting", "RAPID_TEST")
        }
    }
    
    /**
     * Check if bot info is accessible (test connection)
     */
    fun testBotInfo() {
        Log.d(TAG, "Testing bot connection...")
        TelegramLogger.testConnection()
    }
    
    /**
     * Run comprehensive tests
     */
    fun runAllTests() {
        Log.d(TAG, "🧪 Starting comprehensive TelegramLogger tests...")
        
        // Test 1: Basic connectivity
        sendTestMessage()
        
        // Wait a bit
        CoroutineScope(Dispatchers.IO).launch {
            delay(2000)
            
            // Test 2: Error handling
            testErrorHandling()
            
            delay(2000)
            
            // Test 3: Network resilience
            testNetworkResilience()
            
            delay(3000)
            
            // Test 4: System error
            testSystemError()
            
            delay(2000)
            
            // Test 5: Queue monitoring
            testQueueMonitoring()
            
            delay(2000)
            
            // Test 6: Rate limiting
            testRapidMessages()
            
            delay(2000)
            
            // Test 7: Auto error capture
            testAutoErrorCapture()
            
            Log.d(TAG, "✅ All TelegramLogger tests completed!")
        }
    }
} 