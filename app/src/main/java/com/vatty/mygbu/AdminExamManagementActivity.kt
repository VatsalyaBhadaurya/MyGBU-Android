package com.vatty.mygbu

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AdminExamManagementActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_exam_management)
        
        setupToolbar()
        setupPlaceholderContent()
    }
    
    private fun setupToolbar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Exam Management"
        }
    }
    
    private fun setupPlaceholderContent() {
        findViewById<TextView>(R.id.tv_placeholder_title).text = "Admit Card & Exam Sheet Automation"
        findViewById<TextView>(R.id.tv_placeholder_description).text = """
            This module will allow you to:
            
            • View eligible candidates list (attendance, dues, registration)
            • Auto-generate admit cards with roll number, QR code, seat plan
            • Download & print exam sheets and attendance forms
            • Digital stamping/sign-off by Dean/HoD before release
            
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