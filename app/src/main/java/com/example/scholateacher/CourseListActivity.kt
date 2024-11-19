package com.example.scholateacher

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scholateacher.Adapters.CourseAdapter
import com.example.scholateacher.Class.MyClass
import com.example.scholateacher.Model.AssignCourse
import com.example.scholateacher.Model.Course
import com.example.scholateacher.Model.CourseItem
import com.example.scholateacher.Model.Dept
import com.example.scholateacher.Model.Section
import com.example.scholateacher.databinding.ActivityCourseListBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CourseListActivity : AppCompatActivity() {
    lateinit var binding: ActivityCourseListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCourseListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val courseList = mutableListOf<CourseItem>()
        val adapter = CourseAdapter(this,courseList)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        MyClass().getCurrentTeacher { teacher ->
            val teacherId = teacher?.id ?: return@getCurrentTeacher

            val assignCourseRef = FirebaseDatabase.getInstance().getReference("AssignCourse")

            assignCourseRef.orderByChild("teacher_id").equalTo(teacherId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (courseSnapshot in snapshot.children) {
                                val course = courseSnapshot.getValue(AssignCourse::class.java)
                                if (course != null) {
                                    val courseId = course.course_id
                                    val sectionId = course.section_id



                                    // Fetch the course details from the "Course" table
                                    val courseRef = FirebaseDatabase.getInstance().getReference("Course").child(courseId!!)
                                    courseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(courseSnapshot: DataSnapshot) {
                                            val courseData = courseSnapshot.getValue(Course::class.java)
                                            if (courseData != null) {
                                                // Fetch the section details from the "Section" table
                                                val sectionRef = FirebaseDatabase.getInstance().getReference("Section").child(sectionId!!)
                                                sectionRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                                    override fun onDataChange(sectionSnapshot: DataSnapshot) {
                                                        val sectionData = sectionSnapshot.getValue(Section::class.java)
                                                        if (sectionData != null) {

                                                            var deptId = sectionData.dept

                                                            val deptRef = FirebaseDatabase.getInstance().getReference("Department").child(deptId!!)
                                                            deptRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                                                override fun onDataChange(deptSnapshot: DataSnapshot) {
                                                                    val deptData = deptSnapshot.getValue(Dept::class.java)
                                                                    if (deptData != null) {

                                                                        val courseItem = CourseItem(
                                                                            courseCode = courseData.courseCode ?: "",
                                                                            courseName = courseData.courseName ?: "",
                                                                            department = deptData.dept_short_name ?: "",
                                                                            levelTerm = sectionData.level_term ?: "",
                                                                            icon = courseData.icon ?: "",
                                                                            type = courseData.type ?: "",
                                                                            assigncourseId = course.id?:""
                                                                        )
                                                                        // Add to the courseList
                                                                        courseList.add(courseItem)
                                                                        // Notify the adapter
                                                                        adapter.notifyDataSetChanged()





                                                                    }
                                                                }

                                                                override fun onCancelled(error: DatabaseError) {
                                                                    Log.e("FirebaseError", "Error fetching section data: ${error.message}")
                                                                }
                                                            })






                                                        }
                                                    }

                                                    override fun onCancelled(error: DatabaseError) {
                                                        Log.e("FirebaseError", "Error fetching section data: ${error.message}")
                                                    }
                                                })
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            Log.e("FirebaseError", "Error fetching course data: ${error.message}")
                                        }
                                    })





                                }
                            }
                        } else {
                            Log.d("CourseData", "No courses found for teacher ID: $teacherId")
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("FirebaseError", "Error fetching data: ${error.message}")
                    }
                })
        }


        binding.back.setOnClickListener{
            finish()
        }
    }
}
