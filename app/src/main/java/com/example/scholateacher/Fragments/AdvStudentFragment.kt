package com.example.scholateacher.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.scholateacher.Adapters.AdvStudentPagerAdapter
import com.example.scholateacher.R
import com.example.scholateacher.databinding.FragmentAdvStudentBinding
import com.google.android.material.tabs.TabLayoutMediator

class AdvStudentFragment : Fragment() {

    private lateinit var binding: FragmentAdvStudentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdvStudentBinding.inflate(inflater, container, false)

        // Set up ViewPager2 adapter
        val adapter = AdvStudentPagerAdapter(this)
        binding.viewPager.adapter = adapter

        // Link TabLayout with ViewPager2
        val tabTitles = listOf("Student", "Student Request")
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        return binding.root
    }
}
