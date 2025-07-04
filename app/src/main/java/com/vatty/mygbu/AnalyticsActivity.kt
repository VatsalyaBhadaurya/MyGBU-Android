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
import com.vatty.mygbu.utils.BottomNavigationHelper
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

    private fun setupBottomNavigation() {
        BottomNavigationHelper.setupBottomNavigation(
            activity = this,
            bottomNav = binding.bottomNavigation,
            currentItemId = R.id.nav_home
        )
    }

    private fun setupCharts() {
        setupStudentPerformanceChart()
        setupAssignmentStatsChart()
        setupCourseDistributionChart()
    }

    private fun setupStudentPerformanceChart() {
        // Configure student performance line chart
        studentPerformanceChart.apply {
            description.isEnabled = false
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(true)
            
            // Sample data
            val entries = listOf(
                Entry(0f, 75f),
                Entry(1f, 80f),
                Entry(2f, 85f),
                Entry(3f, 78f),
                Entry(4f, 88f)
            )
            
            val dataSet = LineDataSet(entries, "Average Performance")
            dataSet.apply {
                color = Color.BLUE
                valueTextColor = Color.BLACK
                lineWidth = 2f
                setCircleColor(Color.BLUE)
                setDrawCircleHole(false)
            }
            
            data = LineData(dataSet)
            
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                valueFormatter = IndexAxisValueFormatter(arrayOf("Week 1", "Week 2", "Week 3", "Week 4", "Week 5"))
            }
            
            axisLeft.apply {
                setDrawGridLines(true)
                axisMinimum = 0f
                axisMaximum = 100f
            }
            
            axisRight.isEnabled = false
            
            invalidate()
        }
    }

    private fun setupAssignmentStatsChart() {
        // Configure assignment statistics pie chart
        assignmentStatsChart.apply {
            description.isEnabled = false
            isDrawHoleEnabled = true
            setHoleColor(Color.WHITE)
            setTransparentCircleColor(Color.WHITE)
            setTransparentCircleAlpha(110)
            holeRadius = 58f
            transparentCircleRadius = 61f
            setDrawCenterText(true)
            centerText = "Assignments"
            
            // Sample data
            val entries = listOf(
                PieEntry(30f, "Submitted"),
                PieEntry(15f, "Late"),
                PieEntry(10f, "Missing"),
                PieEntry(45f, "On Time")
            )
            
            val dataSet = PieDataSet(entries, "Assignment Status")
            dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
            
            data = PieData(dataSet)
            data.setValueTextSize(11f)
            data.setValueTextColor(Color.WHITE)
            
            invalidate()
        }
    }

    private fun setupCourseDistributionChart() {
        // Configure course distribution bar chart
        courseDistributionChart.apply {
            description.isEnabled = false
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(false)
            
            // Sample data
            val entries = listOf(
                BarEntry(0f, 45f),
                BarEntry(1f, 38f),
                BarEntry(2f, 42f),
                BarEntry(3f, 35f)
            )
            
            val dataSet = BarDataSet(entries, "Students per Course")
            dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
            
            data = BarData(dataSet)
            data.barWidth = 0.9f
            
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                valueFormatter = IndexAxisValueFormatter(arrayOf("Math", "Physics", "Chemistry", "Biology"))
            }
            
            axisLeft.apply {
                setDrawGridLines(true)
                axisMinimum = 0f
            }
            
            axisRight.isEnabled = false
            
            invalidate()
        }
    }
} 