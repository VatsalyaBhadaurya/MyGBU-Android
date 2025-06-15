package com.vatty.mygbu

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.vatty.mygbu.utils.LogWrapper as Log

class MainActivity : AppCompatActivity() {
    
    companion object {
        private const val TAG = "MainActivity"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        // Log main activity startup - this will be sent to Telegram!
        Log.i(TAG, "MainActivity started - main entry point active")
        
        // Navigate to Faculty Dashboard
        findViewById<Button>(R.id.btn_faculty_dashboard).setOnClickListener {
            Log.i(TAG, "User clicked Faculty Dashboard button")
            val intent = Intent(this, FacultyDashboardActivity::class.java)
            startActivity(intent)
        }
    }
}