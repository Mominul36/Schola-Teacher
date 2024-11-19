package com.example.scholateacher

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.scholateacher.Adapters.LabCoursePagerAdapter
import com.example.scholateacher.Adapters.ThoeryCoursePagerAdapter
import com.example.scholateacher.databinding.ActivityLabCourseBinding
import com.example.scholateacher.databinding.ActivityTheoryCourseBinding
import com.google.android.material.tabs.TabLayoutMediator

class LabCourseActivity : AppCompatActivity() {
    lateinit var binding : ActivityLabCourseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLabCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)




        // Set up ViewPager2 adapter
        val adapter = LabCoursePagerAdapter(this)
        binding.viewPager.adapter = adapter

        // Link TabLayout with ViewPager2
        val tabTitles = listOf("Announcement", "Lab Report","Lab Exam","Result")
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()





    }
}