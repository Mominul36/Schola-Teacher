package com.example.scholateacher.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.scholateacher.Fragments.StudentFragment
import com.example.scholateacher.Fragments.StudentRequestFragment
import com.example.scholateacher.Fragments.Thoery.TAnnouncementFragment
import com.example.scholateacher.Fragments.Thoery.TAssignmentFragment
import com.example.scholateacher.Fragments.Thoery.TAttendenceFragment
import com.example.scholateacher.Fragments.Thoery.TClassTestFragment
import com.example.scholateacher.Fragments.Thoery.TResultFragment


class ThoeryCoursePagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 5 // Number of tabs
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TAnnouncementFragment() // First tab
            1 -> TAttendenceFragment()
            2 -> TClassTestFragment()
            3 -> TAssignmentFragment()
            4 -> TResultFragment()// Second tab
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}