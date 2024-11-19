package com.example.scholateacher

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.scholateacher.Class.MyClass
import com.example.scholateacher.Model.AssignCourse
import com.example.scholateacher.Model.Course
import com.example.scholateacher.Model.Teacher
import com.example.scholateacher.databinding.ActivityAddCourseBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AddCourseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddCourseBinding
    private lateinit var auth: FirebaseAuth
    private val teacherMap = mutableMapOf<String, String>() // Map teacher name to teacher ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        loadCourses()
        loadTeachers()
        loadCredits()

        binding.addbtn.setOnClickListener {
            addCourseToSection()
        }


        binding.back.setOnClickListener {
           finish()
        }




    }

    // Load all courses into the course spinner
    private fun loadCourses() {
        val database = FirebaseDatabase.getInstance().getReference("Course")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val courseList = ArrayList<String>()
                for (courseSnapshot in snapshot.children) {
                    val course = courseSnapshot.getValue(Course::class.java)
                    course?.courseCode?.let { courseList.add(it) }
                }
                val adapter = ArrayAdapter(this@AddCourseActivity, android.R.layout.simple_spinner_item, courseList)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spncourse.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AddCourseActivity, "Failed to load courses: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Load all teachers into the teacher spinner (name displayed, but ID stored)
    private fun loadTeachers() {
        val database = FirebaseDatabase.getInstance().getReference("Teacher")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val teacherNames = ArrayList<String>()
                for (teacherSnapshot in snapshot.children) {
                    val teacher = teacherSnapshot.getValue(Teacher::class.java)
                    if (teacher != null && teacher.id != null) {
                        teacherMap[teacher.name ?: ""] = teacher.id.toString() // Map name to ID
                        teacher.name?.let { teacherNames.add(it) }
                    }
                }
                val adapter = ArrayAdapter(this@AddCourseActivity, android.R.layout.simple_spinner_item, teacherNames)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spnteacher.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AddCourseActivity, "Failed to load teachers: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Load static credit values into the credit spinner
    private fun loadCredits() {
        val credits = listOf("0.75", "1.50", "2.00", "3.00", "4.00", "5.00")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, credits)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spncredit.adapter = adapter
    }

    // Add the selected course to the current teacher's section
    private fun addCourseToSection() {
        val course = binding.spncourse.selectedItem?.toString()
        val teacherName = binding.spnteacher.selectedItem?.toString()
        val credit = binding.spncredit.selectedItem?.toString()

        // Retrieve teacher ID from the map
        val teacherId = teacherMap[teacherName]

        if (course.isNullOrEmpty() || teacherId.isNullOrEmpty() || credit.isNullOrEmpty()) {
            Toast.makeText(this, "Please select all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val myClass = MyClass()
        myClass.getCurrentSection { section ->
            if (section != null) {
                val database = FirebaseDatabase.getInstance().getReference("AssignCourse")

                // Check for duplicate course for the section
                database.orderByChild("section_id").equalTo(section.id).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var isDuplicate = false

                        for (assignSnapshot in snapshot.children) {
                            val assignCourse = assignSnapshot.getValue(AssignCourse::class.java)
                            if (assignCourse?.course_id == course) {
                                isDuplicate = true
                                break
                            }
                        }

                        if (isDuplicate) {
                            Toast.makeText(this@AddCourseActivity, "This course is already assigned to this section", Toast.LENGTH_SHORT).show()
                        } else {
                            // Proceed to add the course if no duplicate found
                            val assignCourse = AssignCourse(
                                id = database.push().key,
                                section_id = section.id,
                                course_id = course,
                                teacher_id = teacherId, // Use teacher ID
                                credit = credit
                            )

                            assignCourse.id?.let { id ->
                                database.child(id).setValue(assignCourse)
                                    .addOnSuccessListener {
                                        Toast.makeText(this@AddCourseActivity, "Course assigned successfully", Toast.LENGTH_SHORT).show()
                                        finish()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this@AddCourseActivity, "Failed to assign course: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@AddCourseActivity, "Error checking for duplicate: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "Failed to fetch section", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
