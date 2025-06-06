package com.vatty.mygbu

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class StudentPerformanceActivity : AppCompatActivity() {
    
    private lateinit var etFeedback: TextInputEditText
    private lateinit var etFlagNotes: TextInputEditText
    private lateinit var etCommunication: TextInputEditText
    private lateinit var btnSubmit: MaterialButton
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_performance)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        setupToolbar()
        initializeViews()
        setupClickListeners()
    }
    
    private fun setupToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Student Performance"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
    
    private fun initializeViews() {
        etFeedback = findViewById(R.id.et_feedback)
        etFlagNotes = findViewById(R.id.et_flag_notes)
        etCommunication = findViewById(R.id.et_communication)
        btnSubmit = findViewById(R.id.btn_submit)
    }
    
    private fun setupClickListeners() {
        btnSubmit.setOnClickListener {
            val feedback = etFeedback.text.toString().trim()
            val flagNotes = etFlagNotes.text.toString().trim()
            val communication = etCommunication.text.toString().trim()
            
            if (feedback.isNotEmpty() || flagNotes.isNotEmpty() || communication.isNotEmpty()) {
                Toast.makeText(this, "Student performance data submitted!", Toast.LENGTH_SHORT).show()
                clearForm()
            } else {
                Toast.makeText(this, "Please fill at least one field", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun clearForm() {
        etFeedback.setText("")
        etFlagNotes.setText("")
        etCommunication.setText("")
    }
} 