package com.vatty.mygbu

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vatty.mygbu.utils.LogWrapper as Log

class AnalyticsActivity : AppCompatActivity() {

    private lateinit var studentPerformanceChart: LineChart
    private lateinit var assignmentStatsChart: BarChart
    private lateinit var courseDistributionChart: PieChart
    private lateinit var bottomNavigation: BottomNavigationView
    
    companion object {
        private const val TAG = "AnalyticsActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analytics)

        // Log activity startup - this will be sent to Telegram!
        Log.i(TAG, "AnalyticsActivity started - faculty analytics dashboard active")

        setupViews()
        setupCharts()
        setupBottomNavigation()
    }

    private fun setupViews() {
        val backButton = findViewById<ImageView>(R.id.iv_back)
        backButton.setOnClickListener { finish() }

        studentPerformanceChart = findViewById(R.id.chart_student_performance)
        assignmentStatsChart = findViewById(R.id.chart_assignment_stats)
        courseDistributionChart = findViewById(R.id.chart_course_distribution)
    }

    private fun setupCharts() {
        setupStudentPerformanceChart()
        setupAssignmentStatsChart()
        setupCourseDistributionChart()
    }

    private fun setupStudentPerformanceChart() {
        val entries = listOf(
            Entry(0f, 85f), Entry(1f, 78f), Entry(2f, 82f),
            Entry(3f, 88f), Entry(4f, 91f), Entry(5f, 87f)
        )

        val dataSet = LineDataSet(entries, "Class Average")
        dataSet.color = Color.BLUE
        dataSet.setCircleColor(Color.BLUE)
        dataSet.lineWidth = 2f
        dataSet.circleRadius = 4f
        dataSet.setDrawFilled(true)
        dataSet.fillAlpha = 30

        val lineData = LineData(dataSet)
        studentPerformanceChart.data = lineData

        studentPerformanceChart.xAxis.valueFormatter = IndexAxisValueFormatter(
            arrayOf("Sep", "Oct", "Nov", "Dec", "Jan", "Feb")
        )
        studentPerformanceChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        studentPerformanceChart.description.isEnabled = false
        studentPerformanceChart.legend.isEnabled = false
        studentPerformanceChart.invalidate()
    }

    private fun setupAssignmentStatsChart() {
        val entries = listOf(
            BarEntry(0f, 25f), BarEntry(1f, 18f), BarEntry(2f, 7f)
        )

        val dataSet = BarDataSet(entries, "Assignments")
        dataSet.colors = listOf(
            Color.parseColor("#4CAF50"), // Submitted
            Color.parseColor("#FF9800"), // Pending
            Color.parseColor("#F44336")  // Overdue
        )

        val barData = BarData(dataSet)
        assignmentStatsChart.data = barData

        assignmentStatsChart.xAxis.valueFormatter = IndexAxisValueFormatter(
            arrayOf("Submitted", "Pending", "Overdue")
        )
        assignmentStatsChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        assignmentStatsChart.description.isEnabled = false
        assignmentStatsChart.legend.isEnabled = false
        assignmentStatsChart.invalidate()
    }

    private fun setupCourseDistributionChart() {
        val entries = listOf(
            PieEntry(35f, "CS101"),
            PieEntry(28f, "CS201"),
            PieEntry(22f, "CS301"),
            PieEntry(15f, "CS401")
        )

        val dataSet = PieDataSet(entries, "Course Distribution")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        dataSet.valueTextSize = 12f
        dataSet.valueTextColor = Color.WHITE

        val pieData = PieData(dataSet)
        courseDistributionChart.data = pieData
        courseDistributionChart.description.isEnabled = false
        courseDistributionChart.centerText = "Course\nEnrollment"
        courseDistributionChart.setCenterTextSize(14f)
        courseDistributionChart.invalidate()
    }

    private fun setupBottomNavigation() {
        bottomNavigation = findViewById(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.nav_analytics
        
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Navigate to dashboard
                    true
                }
                R.id.nav_analytics -> true
                R.id.nav_reports -> {
                    // Navigate to reports
                    true
                }
                R.id.nav_profile -> {
                    // Navigate to profile
                    true
                }
                else -> false
            }
        }
    }
} 