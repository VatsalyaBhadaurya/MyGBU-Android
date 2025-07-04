package com.vatty.mygbu

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.vatty.mygbu.databinding.ActivityQuickActionsBinding
import com.vatty.mygbu.utils.BottomNavigationHelper
import com.vatty.mygbu.utils.LogWrapper as Log

class QuickActionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuickActionsBinding
    private lateinit var quickActionsAdapter: QuickActionsAdapter
    
    companion object {
        private const val TAG = "QuickActionsActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuickActionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Log activity startup - this will be sent to Telegram!
        Log.i(TAG, "QuickActionsActivity started - faculty quick actions and shortcuts active")

        setupViews()
        setupRecyclerView()
        setupBottomNavigation()
    }

    private fun setupViews() {
        binding.ivBack.setOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        quickActionsAdapter = QuickActionsAdapter(getQuickActions()) { action ->
            handleQuickAction(action)
        }
        binding.rvQuickActions.apply {
            adapter = quickActionsAdapter
            layoutManager = GridLayoutManager(this@QuickActionsActivity, 2)
        }
    }

    private fun setupBottomNavigation() {
        BottomNavigationHelper.setupBottomNavigation(
            activity = this,
            bottomNav = binding.bottomNavigation,
            currentItemId = R.id.nav_quick_actions
        )
    }

    private fun getQuickActions(): List<QuickAction> {
        return listOf(
            QuickAction(
                id = "mark_attendance",
                title = "Mark Attendance",
                subtitle = "Quick attendance marking",
                icon = R.drawable.ic_check_circle,
                color = R.color.card_attendance,
                actionType = QuickActionType.ATTENDANCE
            ),
            QuickAction(
                id = "create_assignment",
                title = "Create Assignment",
                subtitle = "New assignment upload",
                icon = R.drawable.ic_assignment,
                color = R.color.card_assignments,
                actionType = QuickActionType.ASSIGNMENT
            ),
            QuickAction(
                id = "send_announcement",
                title = "Send Announcement",
                subtitle = "Class announcements",
                icon = R.drawable.ic_announcement,
                color = R.color.card_announcements,
                actionType = QuickActionType.ANNOUNCEMENT
            ),
            QuickAction(
                id = "view_submissions",
                title = "View Submissions",
                subtitle = "Pending submissions",
                icon = R.drawable.ic_submissions,
                color = R.color.card_performance,
                actionType = QuickActionType.SUBMISSIONS
            ),
            QuickAction(
                id = "schedule_meeting",
                title = "Schedule Meeting",
                subtitle = "Book a meeting room",
                icon = R.drawable.ic_meeting,
                color = R.color.card_courses,
                actionType = QuickActionType.MEETING
            ),
            QuickAction(
                id = "grade_papers",
                title = "Grade Papers",
                subtitle = "Grade assignments",
                icon = R.drawable.ic_grade,
                color = R.color.card_reports,
                actionType = QuickActionType.GRADING
            ),
            QuickAction(
                id = "emergency_alert",
                title = "Emergency Alert",
                subtitle = "Send urgent notice",
                icon = R.drawable.ic_emergency,
                color = R.color.priority_high,
                actionType = QuickActionType.EMERGENCY
            ),
            QuickAction(
                id = "class_materials",
                title = "Upload Materials",
                subtitle = "Course resources",
                icon = R.drawable.ic_upload,
                color = R.color.card_leave,
                actionType = QuickActionType.MATERIALS
            )
        )
    }

    private fun handleQuickAction(action: QuickAction) {
        when (action.actionType) {
            QuickActionType.ATTENDANCE -> {
                startActivity(Intent(this, AttendanceActivity::class.java))
            }
            QuickActionType.ASSIGNMENT -> {
                startActivity(Intent(this, AssignmentManagementActivity::class.java))
            }
            QuickActionType.ANNOUNCEMENT -> {
                // Open announcement dialog or activity
            }
            QuickActionType.SUBMISSIONS -> {
                // Open submissions view
            }
            QuickActionType.MEETING -> {
                // Open meeting scheduler
            }
            QuickActionType.GRADING -> {
                // Open grading interface
            }
            QuickActionType.EMERGENCY -> {
                // Open emergency alert dialog
            }
            QuickActionType.MATERIALS -> {
                // Open materials upload
            }
        }
    }
}

// Data classes for quick actions
data class QuickAction(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: Int,
    val color: Int,
    val actionType: QuickActionType
)

enum class QuickActionType {
    ATTENDANCE, ASSIGNMENT, ANNOUNCEMENT, SUBMISSIONS, 
    MEETING, GRADING, EMERGENCY, MATERIALS
} 