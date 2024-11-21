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
import com.example.scholateacher.Model.Assignment
import com.example.scholateacher.Model.ClassTest
import com.example.scholateacher.Model.Course
import com.example.scholateacher.Model.Dept
import com.example.scholateacher.Model.Section
import com.example.scholateacher.databinding.ActivityAddClassTestBinding
import com.example.scholateacher.databinding.ActivityUpdateAssignmentBinding
import com.example.scholateacher.databinding.ActivityUpdateClassTestBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar

class UpdateAssignmentActivity : AppCompatActivity() {
    lateinit var binding: ActivityUpdateAssignmentBinding
    val calendar = Calendar.getInstance()
    lateinit var assignCourseId : String
    lateinit var assignmentId : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUpdateAssignmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        assignCourseId = intent.getStringExtra("assignCourseId").toString()
        assignmentId = intent.getStringExtra("assignmentId").toString()

        showDetails()
        showAssignmentData()


        val assignmentOptions = listOf("Assignment - 1", "Assignment - 2")
        val spinnerAdapter = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, assignmentOptions)
        binding.assignmentNo.adapter = spinnerAdapter




        binding.date.setOnClickListener {
            setDatePicker()
        }


        binding.back.setOnClickListener{
            var intent = Intent(this,TheoryCourseActivity::class.java)
            intent.putExtra("initialFragmentPosition",3)
            intent.putExtra("assignCourseId",assignCourseId)
            startActivity(intent)
            finish()
        }

        binding.update.setOnClickListener {
            val assignmentNo = binding.assignmentNo.selectedItem.toString()
            val date = binding.date.text.toString().trim()
            val topic = binding.topic.text.toString().trim()
            val description = binding.description.text.toString().trim()

            if (date == "Select Date" || topic.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }



            addClassTestToDatabase(assignCourseId,assignmentNo,date,topic,description)
        }


        binding.delete.setOnClickListener {
            deleteClassTest()
        }




    }

    private fun deleteClassTest() {
        val database = FirebaseDatabase.getInstance().getReference("Assignment")

        AlertDialog.Builder(this)
            .setTitle("Delete Assignment")
            .setMessage("Are you sure you want to delete this Assignment?")
            .setPositiveButton("Yes") { dialog, _ ->
                database.child(assignmentId).removeValue()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Assignment deleted successfully!", Toast.LENGTH_SHORT).show()
                            var intent = Intent(this,TheoryCourseActivity::class.java)
                            intent.putExtra("initialFragmentPosition",3)
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

    private fun showAssignmentData() {
        val ctRef = FirebaseDatabase.getInstance().getReference("Assignment")

        ctRef.child(assignmentId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val assignment = snapshot.getValue(Assignment::class.java)
                if (assignment != null) {
                    binding.assignmentNo.setSelection(getClassTestPosition(assignment.assignmentNo))
                    binding.date.setText(assignment.date)
                    binding.topic.setText(assignment.topic)
                    binding.description.setText(assignment.description)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@UpdateAssignmentActivity, "Failed to load Class Test data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getClassTestPosition(assignmentNo: String?): Int {
        val assignmentOptions = listOf("Assignment - 1", "Assignment - 2")
        return assignmentOptions.indexOf(assignmentNo ?: "")
    }


    private fun addClassTestToDatabase(assignCourseId: String, assignmentNo: String, date: String, topic: String, description: String) {
        val database = FirebaseDatabase.getInstance().getReference("Assignment")


        val assignment = Assignment(assignmentId, assignCourseId, assignmentNo, date, topic, description)

        database.child(assignmentId).setValue(assignment)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Assignment Updated successfully!", Toast.LENGTH_SHORT).show()
                    var intent = Intent(this,TheoryCourseActivity::class.java)
                    intent.putExtra("initialFragmentPosition",3)
                    intent.putExtra("assignCourseId",assignCourseId)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Failed to Update Assignment. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
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

    override fun onBackPressed() {
        super.onBackPressed()
        var intent = Intent(this,TheoryCourseActivity::class.java)
        intent.putExtra("initialFragmentPosition",3)
        intent.putExtra("assignCourseId",assignCourseId)
        startActivity(intent)
        finish()
    }
}