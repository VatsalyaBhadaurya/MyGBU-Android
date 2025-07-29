package com.vatty.mygbu.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vatty.mygbu.fragment.CourseMappingFragment
import com.vatty.mygbu.fragment.SyllabusManagementFragment
import com.vatty.mygbu.fragment.ElectivePoolFragment
import com.vatty.mygbu.fragment.CreditValidationFragment
import com.vatty.mygbu.fragment.CurriculumNotificationsFragment

class CourseMappingPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    
    override fun getItemCount(): Int = 5
    
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CourseMappingFragment()
            1 -> SyllabusManagementFragment()
            2 -> ElectivePoolFragment()
            3 -> CreditValidationFragment()
            4 -> CurriculumNotificationsFragment()
            else -> CourseMappingFragment()
        }
    }
} 