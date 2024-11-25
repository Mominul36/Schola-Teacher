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



        val assignCourseId = intent.getStringExtra("assignCourseId").toString()
        val initialFragmentPosition:Int =intent.getIntExtra("initialFragmentPosition",0)


        // Set up ViewPager2 adapter
        val adapter = LabCoursePagerAdapter(this,assignCourseId)
        binding.viewPager.adapter = adapter

        // Link TabLayout with ViewPager2
        val tabTitles = listOf("Announcement","Attendence", "Lab Report","Lab Exam","Study Material")
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()


         binding.viewPager.setCurrentItem(initialFragmentPosition, false)


        binding.back.setOnClickListener{
            finish()
        }





    }
}