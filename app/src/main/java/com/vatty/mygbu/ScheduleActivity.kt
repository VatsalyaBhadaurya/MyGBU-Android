package com.vatty.mygbu

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log

class ScheduleActivity : AppCompatActivity() {
    
    private lateinit var ivBack: ImageView
    private lateinit var rvSchedule: RecyclerView
    private lateinit var scheduleAdapter: ScheduleAdapter
    private val scheduleList = mutableListOf<ScheduleItem>()
    
    companion object {
        private const val TAG = "ScheduleActivity"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_schedule)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        // Log activity startup
        Log.i(TAG, "ScheduleActivity started - faculty schedule and timetable management active")
        
        initializeViews()
        setupBackButton()
        loadScheduleData()
        setupRecyclerView()
    }
    
    private fun initializeViews() {
        ivBack = findViewById(R.id.iv_back)
        rvSchedule = findViewById(R.id.rv_schedule)
    }
    
    private fun setupBackButton() {
        ivBack.setOnClickListener {
            finish()
        }
    }
    
    private fun loadScheduleData() {
        scheduleList.addAll(listOf(
            ScheduleItem(
                day = "Monday",
                timeSlots = listOf(
                    TimeSlot("9:00 - 10:30", "Data Structures (CS201)", "Room A-101", "Lecture"),
                    TimeSlot("11:00 - 12:30", "Database Management (CS301)", "Room B-205", "Lecture"),
                    TimeSlot("2:00 - 3:30", "Programming Lab (CS101)", "Lab C-1", "Practical")
                )
            ),
            ScheduleItem(
                day = "Tuesday",
                timeSlots = listOf(
                    TimeSlot("10:00 - 11:30", "Software Engineering (CS401)", "Room A-102", "Lecture"),
                    TimeSlot("2:00 - 3:30", "Data Structures (CS201)", "Room A-101", "Tutorial"),
                    TimeSlot("4:00 - 5:30", "Faculty Meeting", "Conference Room", "Meeting")
                )
            ),
            ScheduleItem(
                day = "Wednesday",
                timeSlots = listOf(
                    TimeSlot("9:00 - 10:30", "Data Structures (CS201)", "Room A-101", "Lecture"),
                    TimeSlot("11:00 - 12:30", "Programming Fundamentals (CS101)", "Room B-201", "Lecture"),
                    TimeSlot("3:00 - 4:30", "Office Hours", "Faculty Room F-12", "Consultation")
                )
            ),
            ScheduleItem(
                day = "Thursday",
                timeSlots = listOf(
                    TimeSlot("10:00 - 11:30", "Software Engineering (CS401)", "Room A-102", "Lecture"),
                    TimeSlot("1:00 - 2:30", "Database Management (CS301)", "Room B-205", "Tutorial"),
                    TimeSlot("3:00 - 4:30", "Research Work", "Faculty Room F-12", "Research")
                )
            ),
            ScheduleItem(
                day = "Friday",
                timeSlots = listOf(
                    TimeSlot("9:00 - 10:30", "Programming Fundamentals (CS101)", "Room B-201", "Lecture"),
                    TimeSlot("11:00 - 12:30", "Data Structures Lab (CS201)", "Lab C-2", "Practical"),
                    TimeSlot("2:00 - 3:30", "Department Seminar", "Seminar Hall", "Seminar")
                )
            ),
            ScheduleItem(
                day = "Saturday",
                timeSlots = listOf(
                    TimeSlot("10:00 - 11:30", "Extra Classes (CS201)", "Room A-101", "Lecture"),
                    TimeSlot("12:00 - 1:00", "Student Counseling", "Faculty Room F-12", "Consultation")
                )
            )
        ))
    }
    
    private fun setupRecyclerView() {
        scheduleAdapter = ScheduleAdapter(scheduleList)
        rvSchedule.layoutManager = LinearLayoutManager(this)
        rvSchedule.adapter = scheduleAdapter
    }
}

data class ScheduleItem(
    val day: String,
    val timeSlots: List<TimeSlot>
)

data class TimeSlot(
    val time: String,
    val subject: String,
    val location: String,
    val type: String
) 