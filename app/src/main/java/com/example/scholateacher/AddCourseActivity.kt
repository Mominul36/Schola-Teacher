package com.example.scholateacher

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.scholateacher.Class.MyClass
import com.example.scholateacher.Model.AssignCourse
import com.example.scholateacher.Model.Course
import com.example.scholateacher.Model.Student
import com.example.scholateacher.Model.Teacher
import com.example.scholateacher.Model.TheoryAttendance
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
                                credit = credit,
                                total_class = 0, // Initialize total_class to 0
                                date1 = "",
                                date2 = "",
                                date3 = "",
                                date4 = "",
                                date5 = "",
                                date6 = "",
                                date7 = "",
                                date8 = "",
                                date9 = "",
                                date10 = "",
                                date11 = "",
                                date12 = "",
                                date13 = "",
                                date14 = "",
                                date15 = "",
                                date16 = "",
                                date17 = "",
                                date18 = "",
                                date19 = "",
                                date20 = "",
                                date21 = "",
                                date22 = "",
                                date23 = "",
                                date24 = "",
                                date25 = "",
                                date26 = "",
                                date27 = "",
                                date28 = "",
                                date29 = "",
                                date30 = "",
                                date31 = "",
                                date32 = "",
                                date33 = "",
                                date34 = "",
                                date35 = "",
                                date36 = "",
                                date37 = "",
                                date38 = "",
                                date39 = "",
                                date40 = "",
                                date41 = "",
                                date42 = ""
                            )


                            assignCourse.id?.let { id ->
                                database.child(id).setValue(assignCourse)
                                    .addOnSuccessListener {
                                        Toast.makeText(this@AddCourseActivity, "Course assigned successfully", Toast.LENGTH_SHORT).show()
                                        createAttendanceForSectionStudents(section.id!!, id)
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


    private fun createAttendanceForSectionStudents(sectionId: String, assignCourseId: String) {
        val studentDatabase = FirebaseDatabase.getInstance().getReference("Student")
        val attendanceDatabase = FirebaseDatabase.getInstance().getReference("TheoryAttendance")

        // Fetch students of the given section
        studentDatabase.orderByChild("sectionId").equalTo(sectionId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (studentSnapshot in snapshot.children) {
                        val student = studentSnapshot.getValue(Student::class.java)

                        student?.let {

                            if(student.isverify==true){
                                // Create attendance record for each student with class1 to class42 set to 0
                                val attendance = TheoryAttendance(
                                    id = attendanceDatabase.push().key,
                                    assignCourseId = assignCourseId,
                                    studentId = it.id,
                                    class1 = "0",
                                    class2 = "0",
                                    class3 = "0",
                                    class4 = "0",
                                    class5 = "0",
                                    class6 = "0",
                                    class7 = "0",
                                    class8 = "0",
                                    class9 = "0",
                                    class10 = "0",
                                    class11 = "0",
                                    class12 = "0",
                                    class13 = "0",
                                    class14 = "0",
                                    class15 = "0",
                                    class16 = "0",
                                    class17 = "0",
                                    class18 = "0",
                                    class19 = "0",
                                    class20 = "0",
                                    class21 = "0",
                                    class22 = "0",
                                    class23 = "0",
                                    class24 = "0",
                                    class25 = "0",
                                    class26 = "0",
                                    class27 = "0",
                                    class28 = "0",
                                    class29 = "0",
                                    class30 = "0",
                                    class31 = "0",
                                    class32 = "0",
                                    class33 = "0",
                                    class34 = "0",
                                    class35 = "0",
                                    class36 = "0",
                                    class37 = "0",
                                    class38 = "0",
                                    class39 = "0",
                                    class40 = "0",
                                    class41 = "0",
                                    class42 = "0"
                                )

                                // Save attendance record to Firebase
                                attendance.id?.let { id ->
                                    attendanceDatabase.child(id).setValue(attendance)
                                        .addOnSuccessListener {
                                            // Optional: Log success if needed
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(this@AddCourseActivity, "Failed to create attendance: ${e.message}", Toast.LENGTH_SHORT).show()
                                        }
                                }
                            }


                        }
                    }
                } else {
                    Toast.makeText(this@AddCourseActivity, "No students found in this section", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AddCourseActivity, "Error fetching students: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }




}
