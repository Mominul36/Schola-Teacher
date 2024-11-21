package com.example.scholateacher.Adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.scholateacher.Fragments.Lab.LAnnouncementFragment
import com.example.scholateacher.Fragments.Lab.LAttendenceFragment
import com.example.scholateacher.Fragments.Lab.LLabExamFragment
import com.example.scholateacher.Fragments.Lab.LLabReportFragment
import com.example.scholateacher.Fragments.Lab.LResultFragment



class LabCoursePagerAdapter(
    fragmentActivity: FragmentActivity,
    private val assignCourseId: String
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 5 // Number of tabs
    }

    override fun createFragment(position: Int): Fragment {
        val fragment= when (position) {
            0 -> LAnnouncementFragment()
            1 -> LAttendenceFragment()
            2 -> LLabReportFragment()
            3 -> LLabExamFragment()
            4 -> LResultFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }

        // Attach data to the fragment via arguments
        fragment.arguments = Bundle().apply {
            putString("assignCourseId", assignCourseId) // Add any key-value pairs
        }

        return fragment



    }




}