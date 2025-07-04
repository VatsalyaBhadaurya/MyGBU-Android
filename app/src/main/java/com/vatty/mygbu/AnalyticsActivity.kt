package com.vatty.mygbu

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.vatty.mygbu.databinding.ActivityAnalyticsBinding
import com.vatty.mygbu.utils.LogWrapper as Log

class AnalyticsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnalyticsBinding
    private lateinit var studentPerformanceChart: LineChart
    private lateinit var assignmentStatsChart: PieChart
    private lateinit var courseDistributionChart: BarChart
    
    companion object {
        private const val TAG = "AnalyticsActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalyticsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.i(TAG, "AnalyticsActivity started - faculty analytics dashboard active")
        setupViews()
        setupCharts()
        setupBottomNavigation()
    }

    private fun setupViews() {
        binding.ivBack.setOnClickListener { finish() }

        studentPerformanceChart = binding.chartStudentPerformance
        assignmentStatsChart = binding.chartAssignmentStats
        courseDistributionChart = binding.chartCourseDistribution
    }

    private fun setupCharts() {
        setupStudentPerformanceChart()
        setupAssignmentStatsChart()
        setupCourseDistributionChart()
    }

    private fun setupStudentPerformanceChart() {
        val entries = listOf(
            Entry(0f, 85f), Entry(1f, 80f), Entry(2f, 82f),
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
            PieEntry(25f, "Submitted"),
            PieEntry(18f, "Pending"),
            PieEntry(7f, "Overdue")
        )

        val dataSet = PieDataSet(entries, "Assignments")
        dataSet.colors = listOf(
            Color.parseColor("#4CAF50"), // Submitted
            Color.parseColor("#FF9800"), // Pending
            Color.parseColor("#F44336")  // Overdue
        )
        dataSet.valueTextSize = 12f
        dataSet.valueTextColor = Color.WHITE

        val pieData = PieData(dataSet)
        assignmentStatsChart.data = pieData
        assignmentStatsChart.description.isEnabled = false
        assignmentStatsChart.centerText = "Assignment\nStatus"
        assignmentStatsChart.setCenterTextSize(14f)
        assignmentStatsChart.invalidate()
    }

    private fun setupCourseDistributionChart() {
        val entries = listOf(
            BarEntry(0f, 35f),
            BarEntry(1f, 28f),
            BarEntry(2f, 22f),
            BarEntry(3f, 15f)
        )

        val dataSet = BarDataSet(entries, "Course Distribution")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()

        val barData = BarData(dataSet)
        courseDistributionChart.data = barData

        courseDistributionChart.xAxis.valueFormatter = IndexAxisValueFormatter(
            arrayOf("CS101", "CS201", "CS301", "CS401")
        )
        courseDistributionChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        courseDistributionChart.description.isEnabled = false
        courseDistributionChart.legend.isEnabled = false
        courseDistributionChart.invalidate()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.selectedItemId = R.id.nav_analytics
        
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    finish()
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