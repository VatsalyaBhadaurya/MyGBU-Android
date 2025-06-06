package com.vatty.mygbu

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.vatty.mygbu.data.model.Course
import com.vatty.mygbu.data.model.TimetableItem
import com.vatty.mygbu.databinding.ActivityCoursesBinding

class CoursesActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityCoursesBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoursesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        setupToolbar()
        setupRecyclerViews()
        setupBottomNavigation()
    }
    
    private fun setupToolbar() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }
    
    private fun setupRecyclerViews() {
        binding.rvAssignedCourses.layoutManager = LinearLayoutManager(this)
        binding.rvTimetable.layoutManager = LinearLayoutManager(this)
        
        // Sample data for assigned courses using the new model
        val assignedCourses = listOf(
            Course(
                courseCode = "CS101",
                courseName = "Introduction to Programming",
                credits = 4,
                enrolledStudents = 45,
                schedule = "Mon, Wed, Fri 9:00-10:00 AM",
                room = "Lab-201"
            ),
            Course(
                courseCode = "CS201",
                courseName = "Data Structures and Algorithms",
                credits = 4,
                enrolledStudents = 38,
                schedule = "Tue, Thu 11:00 AM-12:30 PM",
                room = "Room-302"
            ),
            Course(
                courseCode = "CS301",
                courseName = "Database Management Systems",
                credits = 3,
                enrolledStudents = 42,
                schedule = "Wed, Fri 2:00-3:30 PM",
                room = "Lab-105"
            )
        )
        
        // Sample data for timetable
        val timetableItems = listOf(
            TimetableItem("Introduction to Programming", "Monday, 9:00 AM - 10:00 AM", R.drawable.ic_schedule),
            TimetableItem("Data Structures and Algorithms", "Tuesday, 11:00 AM - 12:00 PM", R.drawable.ic_schedule),
            TimetableItem("Database Management Systems", "Wednesday, 2:00 PM - 3:00 PM", R.drawable.ic_schedule)
        )
        
        binding.rvAssignedCourses.adapter = CourseAdapter(assignedCourses) { course ->
            Toast.makeText(this, getString(R.string.course_details, course.courseName), Toast.LENGTH_SHORT).show()
        }
        
        binding.rvTimetable.adapter = TimetableAdapter(timetableItems) { item ->
            Toast.makeText(this, "Selected: ${item.title}", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun setupBottomNavigation() {
        binding.bottomNavigation.selectedItemId = R.id.nav_courses
        
        binding.bottomNavigation.setOnItemSelectedListener { item ->
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