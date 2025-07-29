package com.vatty.mygbu

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AdminProjectTrackingActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_project_tracking)
        
        setupToolbar()
        setupPlaceholderContent()
    }
    
    private fun setupToolbar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Project Tracking"
        }
    }
    
    private fun setupPlaceholderContent() {
        findViewById<TextView>(R.id.tv_placeholder_title).text = "Project/Dissertation Tracking"
        findViewById<TextView>(R.id.tv_placeholder_description).text = """
            This module will allow you to:
            
            • Student project registration portal with supervisor mapping
            • Mid-review scheduling, progress tracking, and grade entry
            • Final evaluation report submission (with plagiarism link)
            • Record of publications or patents linked to dissertations
            
            Coming soon...
        """.trimIndent()
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
} 