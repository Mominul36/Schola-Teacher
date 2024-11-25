package com.example.scholateacher.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.scholateacher.Adapters.CourseAdapter2
import com.example.scholateacher.AddCourseActivity
import com.example.scholateacher.Class.MyClass
import com.example.scholateacher.Model.AssignCourse
import com.example.scholateacher.Model.Course
import com.example.scholateacher.Model.CourseItem2
import com.example.scholateacher.Model.Teacher
import com.example.scholateacher.R
import com.example.scholateacher.databinding.FragmentAdvCourseBinding
import com.google.firebase.database.FirebaseDatabase

class AdvCourseFragment : Fragment() {

    private lateinit var binding: FragmentAdvCourseBinding
    private lateinit var courseAdapter2: CourseAdapter2
    private val courseList: MutableList<CourseItem2> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdvCourseBinding.inflate(inflater, container, false)

        // Initialize RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        courseAdapter2 = CourseAdapter2(requireContext(), courseList)
        binding.recyclerView.adapter = courseAdapter2

        // Set FAB click listener
        binding.fab.setOnClickListener {
            startActivity(Intent(requireContext(), AddCourseActivity::class.java))
        }

        // Fetch and display courses
        setCourse()

        return binding.root
    }

    private fun setCourse() {
        MyClass().getCurrentSection { section ->
            val sectionId = section?.id ?: return@getCurrentSection

            // Step 1: Fetch AssignCourse data for the given section
            FirebaseDatabase.getInstance().getReference("AssignCourse")
                .orderByChild("section_id")
                .equalTo(sectionId)
                .get()
                .addOnSuccessListener { assignCourseSnapshot ->
                    if (assignCourseSnapshot.exists()) {
                        courseList.clear() // Clear previous data
                        assignCourseSnapshot.children.forEach { assignCourseChild ->
                            val assignCourse = assignCourseChild.getValue(AssignCourse::class.java)
                            assignCourse?.let { fetchCourseAndTeacher(it) }
                        }
                    }
                }
        }
    }

    private fun fetchCourseAndTeacher(assignCourse: AssignCourse) {
        val courseRef = FirebaseDatabase.getInstance().getReference("Course")
        val teacherRef = FirebaseDatabase.getInstance().getReference("Teacher")

        // Fetch course details
        courseRef.child(assignCourse.course_id ?: "").get().addOnSuccessListener { courseSnapshot ->
            val course = courseSnapshot.getValue(Course::class.java)

            // Fetch teacher details
            teacherRef.child(assignCourse.teacher_id ?: "").get().addOnSuccessListener { teacherSnapshot ->
                val teacher = teacherSnapshot.getValue(Teacher::class.java)

                if (course != null && teacher != null) {
                    // Map data to CourseItem2 and update RecyclerView
                    val courseItem = CourseItem2(
                        courseCode = course.courseCode,
                        courseName = course.courseName,
                        courseIcon = course.icon,
                        teacherName = teacher.name,
                        teacherPic = teacher.profilePic
                    )
                    courseList.add(courseItem)
                    courseAdapter2.notifyDataSetChanged()
                }
            }
        }
    }
}
