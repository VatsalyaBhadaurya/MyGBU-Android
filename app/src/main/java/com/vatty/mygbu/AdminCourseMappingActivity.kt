package com.vatty.mygbu

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.vatty.mygbu.adapter.CourseMappingPagerAdapter

class AdminCourseMappingActivity : AppCompatActivity() {
    
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var pagerAdapter: CourseMappingPagerAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_course_mapping)
        
        setupToolbar()
        setupViewPager()
        setupTabLayout()
    }
    
    private fun setupToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Course Mapping"
        }
    }
    
    private fun setupViewPager() {
        viewPager = findViewById(R.id.viewPager)
        pagerAdapter = CourseMappingPagerAdapter(this)
        viewPager.adapter = pagerAdapter
    }
    
    private fun setupTabLayout() {
        tabLayout = findViewById(R.id.tabLayout)
        
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Course Mapping"
                1 -> "Syllabus"
                2 -> "Elective Pools"
                3 -> "Credit Validation"
                4 -> "Notifications"
                else -> "Tab $position"
            }
        }.attach()
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
} 