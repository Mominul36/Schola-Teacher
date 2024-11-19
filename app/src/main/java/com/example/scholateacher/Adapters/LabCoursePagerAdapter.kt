package com.example.scholateacher.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.scholateacher.Fragments.Lab.LAnnouncementFragment
import com.example.scholateacher.Fragments.Lab.LLabExamFragment
import com.example.scholateacher.Fragments.Lab.LLabReportFragment
import com.example.scholateacher.Fragments.Lab.LResultFragment
import com.example.scholateacher.Fragments.StudentFragment
import com.example.scholateacher.Fragments.StudentRequestFragment
import com.example.scholateacher.Fragments.Thoery.TAnnouncementFragment
import com.example.scholateacher.Fragments.Thoery.TAssignmentFragment
import com.example.scholateacher.Fragments.Thoery.TAttendenceFragment
import com.example.scholateacher.Fragments.Thoery.TClassTestFragment
import com.example.scholateacher.Fragments.Thoery.TResultFragment


class LabCoursePagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 4 // Number of tabs
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LAnnouncementFragment() // First tab
            1 -> LLabReportFragment()
            2 -> LLabExamFragment()
            3 -> LResultFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}