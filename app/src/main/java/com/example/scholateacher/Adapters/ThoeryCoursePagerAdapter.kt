package com.example.scholateacher.Adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.scholateacher.Fragments.Thoery.TAnnouncementFragment
import com.example.scholateacher.Fragments.Thoery.TAssignmentFragment
import com.example.scholateacher.Fragments.Thoery.TAttendanceFragment
import com.example.scholateacher.Fragments.Thoery.TClassTestFragment
import com.example.scholateacher.Fragments.Thoery.TResultFragment
import com.example.scholateacher.Fragments.Thoery.TStudyMaterialFragment


class ThoeryCoursePagerAdapter(
    fragmentActivity: FragmentActivity,
    private val assignCourseId: String // Data to pass
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 5 // Number of tabs
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = when (position) {
            0 -> TAnnouncementFragment()
            1 -> TAttendanceFragment()
            2 -> TClassTestFragment()
            3 -> TAssignmentFragment()
            4 -> TStudyMaterialFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }

        // Attach data to the fragment via arguments
        fragment.arguments = Bundle().apply {
            putString("assignCourseId", assignCourseId) // Add any key-value pairs
        }

        return fragment
    }
}

