package com.example.scholateacher

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.scholateacher.Adapters.ThoeryCoursePagerAdapter
import com.example.scholateacher.databinding.ActivityTheoryCourseBinding
import com.google.android.material.tabs.TabLayoutMediator

class TheoryCourseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTheoryCourseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTheoryCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val assignCourseId = intent.getStringExtra("assignCourseId").toString()
        val initialFragmentPosition:Int =intent.getIntExtra("initialFragmentPosition",0)

        val adapter = ThoeryCoursePagerAdapter(this, assignCourseId)
        binding.viewPager.adapter = adapter

        val tabTitles = listOf("Announcement", "Attendance", "Class Test", "Assignment", "Study Material")
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()


        binding.viewPager.setCurrentItem(initialFragmentPosition, false)
    }
}
