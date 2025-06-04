package com.vatty.mygbu

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class CoursesActivity : AppCompatActivity() {
    
    private lateinit var rvAssignedCourses: RecyclerView
    private lateinit var rvTimetable: RecyclerView
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var ivBack: ImageView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_courses)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        initializeViews()
        setupBackButton()
        setupRecyclerViews()
        setupBottomNavigation()
    }
    
    private fun setupBackButton() {
        ivBack.setOnClickListener {
            finish()
        }
    }
    
    private fun initializeViews() {
        rvAssignedCourses = findViewById(R.id.rv_assigned_courses)
        rvTimetable = findViewById(R.id.rv_timetable)
        bottomNavigation = findViewById(R.id.bottom_navigation)
        ivBack = findViewById(R.id.iv_back)
    }
    
    private fun setupRecyclerViews() {
        rvAssignedCourses.layoutManager = LinearLayoutManager(this)
        rvTimetable.layoutManager = LinearLayoutManager(this)
        
        // Sample data for assigned courses
        val assignedCourses = listOf(
            Course("Introduction to Programming", "Semester 1, 2024", R.drawable.ic_courses),
            Course("Data Structures and Algorithms", "Semester 1, 2024", R.drawable.ic_courses),
            Course("Database Management Systems", "Semester 1, 2024", R.drawable.ic_courses)
        )
        
        // Sample data for timetable
        val timetableItems = listOf(
            TimetableItem("Introduction to Programming", "Monday, 9:00 AM - 10:00 AM", R.drawable.ic_schedule),
            TimetableItem("Data Structures and Algorithms", "Tuesday, 11:00 AM - 12:00 PM", R.drawable.ic_schedule),
            TimetableItem("Database Management Systems", "Wednesday, 2:00 PM - 3:00 PM", R.drawable.ic_schedule)
        )
        
        rvAssignedCourses.adapter = CourseAdapter(assignedCourses) { course ->
            Toast.makeText(this, "Selected: ${course.title}", Toast.LENGTH_SHORT).show()
        }
        
        rvTimetable.adapter = TimetableAdapter(timetableItems) { item ->
            Toast.makeText(this, "Selected: ${item.title}", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun setupBottomNavigation() {
        bottomNavigation.selectedItemId = R.id.nav_courses
        
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, FacultyDashboardActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_courses -> true
                R.id.nav_attendance -> {
                    startActivity(Intent(this, AttendanceActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, FacultyHubActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}

data class Course(
    val title: String,
    val semester: String,
    val iconRes: Int
)

data class TimetableItem(
    val title: String,
    val time: String,
    val iconRes: Int
) 