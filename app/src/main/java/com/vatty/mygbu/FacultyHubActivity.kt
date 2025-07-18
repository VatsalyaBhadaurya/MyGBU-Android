package com.vatty.mygbu

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.util.Log
import com.vatty.mygbu.utils.BottomNavigationHelper

class FacultyHubActivity : AppCompatActivity() {
    
    private lateinit var ivMenu: ImageView
    private lateinit var bottomNavigation: BottomNavigationView
    
    companion object {
        private const val TAG = "FacultyHubActivity"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_faculty_hub)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        // Log activity startup
        Log.i(TAG, "FacultyHubActivity started - faculty profile hub active")
        
        initializeViews()
        setupClickListeners()
        setupBottomNavigation()
    }
    
    private fun initializeViews() {
        ivMenu = findViewById(R.id.iv_menu)
        bottomNavigation = findViewById(R.id.bottom_navigation)
    }
    
    private fun setupClickListeners() {
        ivMenu.setOnClickListener {
            // TODO: Implement drawer menu
            // For now, navigate back to dashboard
            startActivity(Intent(this, FacultyDashboardActivity::class.java))
            finish()
        }
    }
    
    private fun setupBottomNavigation() {
        BottomNavigationHelper.setupBottomNavigation(this, bottomNavigation, FacultyHubActivity::class.java)
    }
}

data class ResearchUpdate(
    val title: String,
    val description: String,
    val iconRes: Int
) 