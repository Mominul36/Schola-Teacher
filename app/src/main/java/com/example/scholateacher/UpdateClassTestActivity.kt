package com.example.scholateacher

import android.R
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
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
import com.example.scholateacher.databinding.ActivityUpdateClassTestBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar

class UpdateClassTestActivity : AppCompatActivity() {
    lateinit var binding: ActivityUpdateClassTestBinding
    val calendar = Calendar.getInstance()
    lateinit var assignCourseId : String
    lateinit var classTestId : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUpdateClassTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        assignCourseId = intent.getStringExtra("assignCourseId").toString()
        classTestId = intent.getStringExtra("classTestId").toString()

        showDetails()
        showClassTestData()


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
            var intent = Intent(this,TheoryCourseActivity::class.java)
            intent.putExtra("initialFragmentPosition",2)
            intent.putExtra("assignCourseId",assignCourseId)
            startActivity(intent)
            finish()
        }

        binding.update.setOnClickListener {
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


        binding.delete.setOnClickListener {
            deleteClassTest()
        }




    }

    private fun deleteClassTest() {
        val database = FirebaseDatabase.getInstance().getReference("ClassTest")

        AlertDialog.Builder(this)
            .setTitle("Delete Class Test")
            .setMessage("Are you sure you want to delete this Class Test?")
            .setPositiveButton("Yes") { dialog, _ ->
                database.child(classTestId).removeValue()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Class Test deleted successfully!", Toast.LENGTH_SHORT).show()
                            var intent = Intent(this,TheoryCourseActivity::class.java)
                            intent.putExtra("initialFragmentPosition",2)
                            intent.putExtra("assignCourseId",assignCourseId)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Failed to delete Class Test. Please try again.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showClassTestData() {
        val ctRef = FirebaseDatabase.getInstance().getReference("ClassTest")

        ctRef.child(classTestId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val classTest = snapshot.getValue(ClassTest::class.java)
                if (classTest != null) {
                    binding.ctNo.setSelection(getClassTestPosition(classTest.ctNo))
                    binding.date.setText(classTest.date)
                    binding.time.setText(classTest.time)
                    binding.topic.setText(classTest.topic)
                    binding.description.setText(classTest.description)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@UpdateClassTestActivity, "Failed to load Class Test data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getClassTestPosition(ctNo: String?): Int {
        val classTestOptions = listOf("Class Test - 1", "Class Test - 2", "Class Test - 3")
        return classTestOptions.indexOf(ctNo ?: "")
    }


    private fun addClassTestToDatabase(assignCourseId: String, ctNo: String, date: String, time: String, topic: String, description: String) {
        val database = FirebaseDatabase.getInstance().getReference("ClassTest")


            val classTest = ClassTest(classTestId, assignCourseId, ctNo, date, time, topic, description)

            database.child(classTestId).setValue(classTest)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Class Test Updated successfully!", Toast.LENGTH_SHORT).show()
                        var intent = Intent(this,TheoryCourseActivity::class.java)
                        intent.putExtra("initialFragmentPosition",2)
                        intent.putExtra("assignCourseId",assignCourseId)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Failed to Update Class Test. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
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

    override fun onBackPressed() {
        super.onBackPressed()
        var intent = Intent(this,TheoryCourseActivity::class.java)
        intent.putExtra("initialFragmentPosition",2)
        intent.putExtra("assignCourseId",assignCourseId)
        startActivity(intent)
        finish()
    }
}