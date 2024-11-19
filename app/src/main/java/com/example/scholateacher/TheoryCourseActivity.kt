package com.example.scholateacher

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.scholateacher.Adapters.AdvStudentPagerAdapter
import com.example.scholateacher.Adapters.ThoeryCoursePagerAdapter
import com.example.scholateacher.databinding.ActivityTheoryCourseBinding
import com.google.android.material.tabs.TabLayoutMediator

class TheoryCourseActivity : AppCompatActivity() {
    lateinit var binding : ActivityTheoryCourseBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTheoryCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Set up ViewPager2 adapter
        val adapter = ThoeryCoursePagerAdapter(this)
        binding.viewPager.adapter = adapter

        // Link TabLayout with ViewPager2
        val tabTitles = listOf("Announcement", "Attendence","Class Test","Assignment","Result")
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()





    }
}