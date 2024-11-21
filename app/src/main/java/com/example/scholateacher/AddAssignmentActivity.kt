package com.example.scholateacher

import android.R
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.scholateacher.Model.AssignCourse
import com.example.scholateacher.Model.Assignment
import com.example.scholateacher.Model.ClassTest
import com.example.scholateacher.Model.Course
import com.example.scholateacher.Model.Dept
import com.example.scholateacher.Model.Section
import com.example.scholateacher.databinding.ActivityAddAssignmentBinding
import com.example.scholateacher.databinding.ActivityAddClassTestBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar

class AddAssignmentActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddAssignmentBinding
    val calendar = Calendar.getInstance()
    lateinit var assignCourseId : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddAssignmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        assignCourseId = intent.getStringExtra("assignCourseId").toString()

        showDetails()


        val assignmentOptions = listOf("Assignment - 1", "Assignment - 2")
        val spinnerAdapter = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, assignmentOptions)
        binding.assignmentNo.adapter = spinnerAdapter




        binding.date.setOnClickListener {
            setDatePicker()
        }


        binding.back.setOnClickListener{
            finish()
        }

        binding.add.setOnClickListener {
            val assignmentNo = binding.assignmentNo.selectedItem.toString()
            val date = binding.date.text.toString().trim()
            val topic = binding.topic.text.toString().trim()
            val description = binding.description.text.toString().trim()

            if (date == "Select Submission Date" || topic.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            addClassTestToDatabase(assignCourseId,assignmentNo,date,topic,description)
        }



    }

    private fun addClassTestToDatabase(assignCourseId: String, ctNo: String, date: String, topic: String, description: String) {
        val database = FirebaseDatabase.getInstance().getReference("Assignment")

        val id = database.push().key

        if (id != null) {
            val assignment = Assignment(id, assignCourseId, ctNo, date, topic, description)

            database.child(id).setValue(assignment)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Assignment added successfully!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Failed to add Assignment. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Failed to generate unique ID for Class Test.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun showDetails() {
        var acRef = FirebaseDatabase.getInstance().getReference("AssignCourse")
        var deptRef = FirebaseDatabase.getInstance().getReference("Department")
        var courseRef = FirebaseDatabase.getInstance().getReference("Course")
        var sectionRef = FirebaseDatabase.getInstance().getReference("Section")

        acRef.child(assignCourseId!!).addListenerForSingleValueEvent(object  : ValueEventListener{
            override fun onDataChange(acsnapshot: DataSnapshot) {
                var ac = acsnapshot.getValue(AssignCourse::class.java)
                if(ac!=null){
                    var sectionId  = ac.section_id
                    var courseId = ac.course_id

                    courseRef.child(courseId!!).addListenerForSingleValueEvent(object  : ValueEventListener{
                        override fun onDataChange(coursesnapshot: DataSnapshot) {
                            var course = coursesnapshot.getValue(Course::class.java)
                            if(course!=null){

                                sectionRef.child(sectionId!!).addListenerForSingleValueEvent(object  : ValueEventListener{
                                    override fun onDataChange(sectionsnapshot: DataSnapshot) {
                                        var section = sectionsnapshot.getValue(Section::class.java)
                                        if(section!=null){

                                            deptRef.child(section.dept!!).addListenerForSingleValueEvent(object  : ValueEventListener{
                                                override fun onDataChange(deptsnapshot: DataSnapshot) {
                                                    var dept = deptsnapshot.getValue(Dept::class.java)
                                                    if(dept!=null){

                                                        var lt = section.level_term.toString()
                                                        val parts = lt.split("-")
                                                        val level = parts[0]
                                                        val term = parts[1]

                                                        binding.courseName.text = course.courseName
                                                        binding.courseCode.text = course.courseCode
                                                        binding.dept.text ="Dept of "+ dept.dept_short_name
                                                        binding.lt.text ="Level - "+level+"    Term - "+term+"    Sec - "+section.section

                                                    }
                                                }
                                                override fun onCancelled(error: DatabaseError) {
                                                }
                                            })
                                        }
                                    }
                                    override fun onCancelled(error: DatabaseError) {
                                    }
                                })

                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }


    private fun setDatePicker() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                binding.date.text = "$dayOfMonth-${month + 1}-$year"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.minDate = calendar.timeInMillis
        datePickerDialog.show()
    }
}