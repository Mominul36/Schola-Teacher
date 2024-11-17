package com.example.scholateacher.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.scholateacher.Fragments.StudentFragment
import com.example.scholateacher.Fragments.StudentRequestFragment

class AdvStudentPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 2 // Number of tabs
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> StudentFragment() // First tab
            1 -> StudentRequestFragment() // Second tab
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}
