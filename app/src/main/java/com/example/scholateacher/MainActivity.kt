package com.example.scholateacher

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.scholateacher.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.scholateacher.Class.ControlImage
import com.example.scholateacher.Class.MyClass
import com.example.scholateacher.Fragments.HomeFragment
import com.example.scholateacher.Fragments.MessageFragment
import com.example.scholateacher.Fragments.ProfileFragment
import com.example.scholateacher.Fragments.TimeTableFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var binding : ActivityMainBinding
    lateinit var headerView: View
    lateinit var controlImage : ControlImage
    var flag:Boolean = false

    lateinit var largeProfilePic : ImageView
    lateinit var name: TextView
    lateinit var designation: TextView
    lateinit var department: TextView

    lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controlImage = ControlImage(this, this.activityResultRegistry, "imagePickerKey")
        headerView = binding.navView.getHeaderView(0)
        largeProfilePic = headerView.findViewById(R.id.largeProfilePic)
        name = headerView.findViewById(R.id.name)
        designation = headerView.findViewById(R.id.designation)
        department = headerView.findViewById(R.id.department)
        auth = FirebaseAuth.getInstance()






        setUpForNavigationDrawer()
        setProfilePic()
        handleMenuIconClick()
        setupBottomNavigation()
        setFragment(HomeFragment())








    }




    private fun setupBottomNavigation() {
        val bottomNavigationView: BottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setOnItemSelectedListener { item ->
            val selectedFragment: Fragment = when (item.itemId) {
                R.id.nav_home -> HomeFragment()
                R.id.nav_profile -> ProfileFragment()
                R.id.nav_timetable -> TimeTableFragment()
                R.id.nav_message -> MessageFragment()
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

    private fun setProfilePic() {




        MyClass().getCurrentTeacher{ teacher ->
            if (teacher != null) {
                controlImage.setImageByURl(teacher.profilePic.toString(),binding.profilePic)
                controlImage.setImageByURl(teacher.profilePic.toString(),largeProfilePic)


                name.text = teacher.name
                designation.text = teacher.des



            } else {
            }
        }




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
            R.id.nav_faculty_members -> {

                startActivity(Intent(this,FacultyActivity::class.java))



            }
            R.id.nav_advising_batch -> {


                startActivity(Intent(this,AdvHomeActivity::class.java))


                // Handle Profile click
                //supportFragmentManager.beginTransaction().replace(R.id.content_frame, ProfileFragment()).commit()
            }
            R.id.nav_academic_calendar -> {
                startActivity(Intent(this,AcademicCalendarActivity::class.java))

            }
            R.id.nav_cgpa_calculator -> {

            }
            R.id.nav_question_bank -> {
                startActivity(Intent(this,QuestionActivity::class.java))


            }
            R.id.nav_notice -> {
                startActivity(Intent(this,NoticeActivity::class.java))


            }
            R.id.nav_settings -> {


            }
            R.id.nav_logout -> {
                auth.signOut()
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    fun setFragment(fragment: Fragment){
        val fragmentManager : FragmentManager = supportFragmentManager
        val frammentTransition : FragmentTransaction = fragmentManager.beginTransaction()

        if(!flag){
            frammentTransition.add(R.id.frame,fragment)
            flag = true
        }
        else{
            frammentTransition.replace(R.id.frame,fragment)
        }
        frammentTransition.commit()
    }

}

