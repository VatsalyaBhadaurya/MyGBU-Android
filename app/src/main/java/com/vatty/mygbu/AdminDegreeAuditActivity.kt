package com.vatty.mygbu

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AdminDegreeAuditActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_degree_audit)
        
        setupToolbar()
        setupPlaceholderContent()
    }
    
    private fun setupToolbar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Degree Audit"
        }
    }
    
    private fun setupPlaceholderContent() {
        findViewById<TextView>(R.id.tv_placeholder_title).text = "Degree Audit & Eligibility Status"
        findViewById<TextView>(R.id.tv_placeholder_description).text = """
            This module will allow you to:
            
            • View degree audit dashboard for final year students
            • Check attendance %, CGPA, project completion, dues clearance
            • Real-time status view for mentors and program chairs
            • Automated eligibility flagging + alerts to students/faculty
            
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