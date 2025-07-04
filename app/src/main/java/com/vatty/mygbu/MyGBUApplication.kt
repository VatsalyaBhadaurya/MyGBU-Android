package com.vatty.mygbu

import android.app.Application
import android.util.Log

/**
 * Custom Application class for MyGBU
 * Initializes comprehensive monitoring for both app and system logs
 */
class MyGBUApplication : Application() {
    
    companion object {
        private const val TAG = "MyGBUApplication"
    }
    
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "MyGBU Application starting...")
        Log.i(TAG, "MyGBU Application initialized")
    }
}