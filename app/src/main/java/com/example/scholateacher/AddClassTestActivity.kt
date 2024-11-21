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
import com.example.scholateacher.Model.ClassTest
import com.example.scholateacher.Model.Course
import com.example.scholateacher.Model.Dept
import com.example.scholateacher.Model.Section
import com.example.scholateacher.databinding.ActivityAddClassTestBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar

class AddClassTestActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddClassTestBinding
    val calendar = Calendar.getInstance()
    lateinit var assignCourseId : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddClassTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

         assignCourseId = intent.getStringExtra("assignCourseId").toString()

         showDetails()


        val classTestOptions = listOf("Class Test - 1", "Class Test - 2", "Class Test - 3")
        val spinnerAdapter = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, classTestOptions)
        binding.ctNo.adapter = spinnerAdapter




        binding.date.setOnClickListener {
            setDatePicker()
        }

        binding.time.setOnClickListener {
            setTimePicker()
        }

        binding.back.setOnClickListener{
            finish()
        }

        binding.add.setOnClickListener {
            val ctNo = binding.ctNo.selectedItem.toString()
            val date = binding.date.text.toString().trim()
            val time = binding.time.text.toString().trim()
            val topic = binding.topic.text.toString().trim()
            val description = binding.description.text.toString().trim()

            if (date == "Select Date" || time == "Select Time" || topic.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

//            if(!validateTime(time)){
//                return@setOnClickListener
//            }

            addClassTestToDatabase(assignCourseId,ctNo,date,time,topic,description)
        }



    }

    private fun addClassTestToDatabase(assignCourseId: String, ctNo: String, date: String, time: String, topic: String, description: String) {
        val database = FirebaseDatabase.getInstance().getReference("ClassTest")

        val id = database.push().key

        if (id != null) {
            val classTest = ClassTest(id, assignCourseId, ctNo, date, time, topic, description)

            database.child(id).setValue(classTest)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Class Test added successfully!", Toast.LENGTH_SHORT).show()
                         finish()
                    } else {
                        Toast.makeText(this, "Failed to add Class Test. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Failed to generate unique ID for Class Test.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateTime(time: String): Boolean {
        val timeParts = time.split(":").map { it.toIntOrNull() }
        if (timeParts.size == 2) {
            val hour = timeParts[0] ?: -1
            val minute = timeParts[1] ?: -1
            if (hour !in 7..17) {
                Toast.makeText(this, "Time must be between 7 AM and 5 PM", Toast.LENGTH_SHORT).show()
                return false
            }
            return true
        } else {
            Toast.makeText(this, "Invalid time format", Toast.LENGTH_SHORT).show()
            return false
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

    private fun setTimePicker() {
        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                val amPm = if (hourOfDay < 12) "AM" else "PM"
                val hour = if (hourOfDay > 12) hourOfDay - 12 else if (hourOfDay == 0) 12 else hourOfDay
                binding.time.text = String.format("%02d:%02d %s", hour, minute, amPm)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        )
        timePickerDialog.show()
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