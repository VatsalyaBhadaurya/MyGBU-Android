package com.vatty.mygbu

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import com.vatty.mygbu.utils.BottomNavigationHelper
import com.vatty.mygbu.utils.LogWrapper as Log

class StudentPerformanceActivity : AppCompatActivity() {
    
    private lateinit var bottomNavigation: BottomNavigationView
    
    companion object {
        private const val TAG = "StudentPerformanceActivity"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_performance)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        // Log activity startup - this will be sent to Telegram!
        Log.i(TAG, "StudentPerformanceActivity started - student analytics and performance tracking active")
        
        setupToolbar()
        initializeViews()
        setupClickListeners()
        setupBottomNavigation()
        setupBackPressedHandler()
    }
    
    private fun setupToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Student Performance"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }
    
    private fun setupBackPressedHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
    }
    
    private fun initializeViews() {
        bottomNavigation = findViewById(R.id.bottom_navigation)
    }
    
    private fun setupClickListeners() {
        findViewById<MaterialCardView>(R.id.card_feedback).setOnClickListener {
            Toast.makeText(this, "Student Feedback System", Toast.LENGTH_SHORT).show()
        }
        
        findViewById<MaterialCardView>(R.id.card_communication).setOnClickListener {
            Toast.makeText(this, "Communication Tools", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun setupBottomNavigation() {
        BottomNavigationHelper.setupBottomNavigation(
            activity = this,
            bottomNav = bottomNavigation,
            currentItemId = R.id.nav_quick_actions
        )
    }
} 