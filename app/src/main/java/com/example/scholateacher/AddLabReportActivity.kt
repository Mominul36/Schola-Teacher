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
import com.example.scholateacher.Model.LabReport
import com.example.scholateacher.Model.Section
import com.example.scholateacher.databinding.ActivityAddAssignmentBinding
import com.example.scholateacher.databinding.ActivityAddClassTestBinding
import com.example.scholateacher.databinding.ActivityAddLabReportBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar

class AddLabReportActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddLabReportBinding
    val calendar = Calendar.getInstance()
    lateinit var assignCourseId : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddLabReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        assignCourseId = intent.getStringExtra("assignCourseId").toString()

        showDetails()


        val labReportOptions = listOf("Lab Report - 1","Lab Report - 2","Lab Report - 3","Lab Report - 4","Lab Report - 5","Lab Report - 6","Lab Report - 7","Lab Report - 8","Lab Report - 9","Lab Report - 10")
        val spinnerAdapter = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, labReportOptions)
        binding.lrNo.adapter = spinnerAdapter




        binding.date.setOnClickListener {
            setDatePicker()
        }


        binding.back.setOnClickListener{
            finish()
        }

        binding.add.setOnClickListener {
            val lrNo = binding.lrNo.selectedItem.toString()
            val date = binding.date.text.toString().trim()
            val topic = binding.topic.text.toString().trim()
            val description = binding.description.text.toString().trim()

            if (date == "Select Submission Date" || topic.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            addClassTestToDatabase(assignCourseId,lrNo,date,topic,description)
        }



    }

    private fun addClassTestToDatabase(assignCourseId: String, lrNo: String, date: String, topic: String, description: String) {
        val database = FirebaseDatabase.getInstance().getReference("LabReport")

        val id = database.push().key

        if (id != null) {
            val labReport = LabReport(id, assignCourseId, lrNo, date, topic, description)

            database.child(id).setValue(labReport)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Lab Report added successfully!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Failed to add Lab Report. Please try again.", Toast.LENGTH_SHORT).show()
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