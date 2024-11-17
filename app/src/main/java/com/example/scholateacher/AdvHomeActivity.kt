package com.example.scholateacher

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.scholateacher.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.scholateacher.Fragments.AdvCourseFragment
import com.example.scholateacher.Fragments.AdvHomeFragment
import com.example.scholateacher.Fragments.AdvScheduleFragment
import com.example.scholateacher.Fragments.AdvStudentFragment
import com.example.scholateacher.Fragments.HomeFragment
import com.example.scholateacher.Fragments.MessageFragment
import com.example.scholateacher.Fragments.ProfileFragment
import com.example.scholateacher.Fragments.TimeTableFragment
import com.example.scholateacher.Model.Teacher
import com.example.scholateacher.databinding.ActivityAdvHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdvHomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var binding : ActivityAdvHomeBinding
    lateinit var headerView: View

    lateinit var largeProfilePic : ImageView
    lateinit var name: TextView
    lateinit var designation: TextView
    lateinit var department: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()




        binding = ActivityAdvHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        headerView = binding.navView.getHeaderView(0)
        largeProfilePic = headerView.findViewById(R.id.largeProfilePic)
        name = headerView.findViewById(R.id.name)
        designation = headerView.findViewById(R.id.designation)
        department = headerView.findViewById(R.id.department)

        setUpForNavigationDrawer()
        setProfileCircle()
        handleMenuIconClick()
        setupBottomNavigation()





    }

    private fun set2() {



    }

    private fun setupBottomNavigation() {
        val bottomNavigationView: BottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setOnItemSelectedListener { item ->
            val selectedFragment: Fragment = when (item.itemId) {
                R.id.nav_home -> AdvHomeFragment()
                R.id.nav_student -> AdvStudentFragment()
                R.id.nav_course -> AdvCourseFragment()
                R.id.nav_schedule -> AdvScheduleFragment()
                else -> HomeFragment()
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame, selectedFragment)
                .commit()
            true
        }
    }

    private fun handleMenuIconClick() {
        binding.menuicon.setOnClickListener {
            if (!binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }    }

    private fun setUpForNavigationDrawer() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        binding.navView.setNavigationItemSelectedListener(this)
    }

    private fun setProfileCircle() {
        Glide.with(this)
            .load(R.drawable.stuprofile)
            .apply(RequestOptions.circleCropTransform())
            .into(binding.profilePic)



        Glide.with(this)
            .load(R.drawable.stuprofile)
            .apply(RequestOptions.circleCropTransform())
            .into(largeProfilePic)

    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_class_routine -> {





                // Handle Home click
                //supportFragmentManager.beginTransaction().replace(R.id.content_frame, HomeFragment()).commit()
            }
            R.id.nav_result -> {


               // startActivity(Intent(this,AdvHomeActivity::class.java))


                // Handle Profile click
                //supportFragmentManager.beginTransaction().replace(R.id.content_frame, ProfileFragment()).commit()
            }

        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}


