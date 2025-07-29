package com.vatty.mygbu

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AdminFileTrackingActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_file_tracking)
        
        setupToolbar()
        setupPlaceholderContent()
    }
    
    private fun setupToolbar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "File Tracking"
        }
    }
    
    private fun setupPlaceholderContent() {
        findViewById<TextView>(R.id.tv_placeholder_title).text = "File Tracking & Approval Transparency (E-Office)"
        findViewById<TextView>(R.id.tv_placeholder_description).text = """
            This module will allow you to:
            
            • Inward and outward file entry, tracking by subject/type/date
            • File movement log between offices/personnel
            • View/print file summary with approval comments
            • Smart search and status filters
            
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