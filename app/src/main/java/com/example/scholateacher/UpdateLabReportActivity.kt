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
import com.example.scholateacher.Model.LabReport
import com.example.scholateacher.Model.Section
import com.example.scholateacher.databinding.ActivityAddClassTestBinding
import com.example.scholateacher.databinding.ActivityUpdateAssignmentBinding
import com.example.scholateacher.databinding.ActivityUpdateClassTestBinding
import com.example.scholateacher.databinding.ActivityUpdateLabReportBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar

class UpdateLabReportActivity : AppCompatActivity() {
    lateinit var binding: ActivityUpdateLabReportBinding
    val calendar = Calendar.getInstance()
    lateinit var assignCourseId : String
    lateinit var labReportId : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUpdateLabReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        assignCourseId = intent.getStringExtra("assignCourseId").toString()
        labReportId = intent.getStringExtra("labReportId").toString()

        showDetails()
        showLabReportData()


        val labReportOptions = listOf("Lab Report - 1","Lab Report - 2","Lab Report - 3","Lab Report - 4","Lab Report - 5","Lab Report - 6","Lab Report - 7","Lab Report - 8","Lab Report - 9","Lab Report - 10")
        val spinnerAdapter = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, labReportOptions)
        binding.lrNo.adapter = spinnerAdapter




        binding.date.setOnClickListener {
            setDatePicker()
        }


        binding.back.setOnClickListener{
            var intent = Intent(this,LabCourseActivity::class.java)
            intent.putExtra("initialFragmentPosition",2)
            intent.putExtra("assignCourseId",assignCourseId)
            startActivity(intent)
            finish()
        }

        binding.update.setOnClickListener {
            val lrNo = binding.lrNo.selectedItem.toString()
            val date = binding.date.text.toString().trim()
            val topic = binding.topic.text.toString().trim()
            val description = binding.description.text.toString().trim()

            if (date == "Select Date" || topic.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }



            addClassTestToDatabase(assignCourseId,lrNo,date,topic,description)
        }


        binding.delete.setOnClickListener {
            deleteLabReport()
        }




    }

    private fun deleteLabReport() {
        val database = FirebaseDatabase.getInstance().getReference("LabReport")

        AlertDialog.Builder(this)
            .setTitle("Delete Lab Report")
            .setMessage("Are you sure you want to delete this Lab Report?")
            .setPositiveButton("Yes") { dialog, _ ->
                database.child(labReportId).removeValue()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Lab Report deleted successfully!", Toast.LENGTH_SHORT).show()
                            var intent = Intent(this,LabCourseActivity::class.java)
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

    private fun showLabReportData() {
        val ctRef = FirebaseDatabase.getInstance().getReference("LabReport")

        ctRef.child(labReportId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val labReport = snapshot.getValue(LabReport::class.java)
                if (labReport != null) {
                    binding.lrNo.setSelection(getClassTestPosition(labReport.lrNo))
                    binding.date.setText(labReport.date)
                    binding.topic.setText(labReport.topic)
                    binding.description.setText(labReport.description)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@UpdateLabReportActivity, "Failed to load Lab Report data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getClassTestPosition(lrNo: String?): Int {
        val labReportOptions = listOf("Lab Report - 1","Lab Report - 2","Lab Report - 3","Lab Report - 4","Lab Report - 5","Lab Report - 6","Lab Report - 7","Lab Report - 8","Lab Report - 9","Lab Report - 10")
        return labReportOptions.indexOf(lrNo ?: "")
    }


    private fun addClassTestToDatabase(assignCourseId: String, lrNo: String, date: String, topic: String, description: String) {
        val database = FirebaseDatabase.getInstance().getReference("LabReport")


        val labReport = LabReport(labReportId, assignCourseId, lrNo, date, topic, description)

        database.child(labReportId).setValue(labReport)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Lab Report Updated successfully!", Toast.LENGTH_SHORT).show()
                    var intent = Intent(this,LabCourseActivity::class.java)
                    intent.putExtra("initialFragmentPosition",2)
                    intent.putExtra("assignCourseId",assignCourseId)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Failed to Update Lab Report. Please try again.", Toast.LENGTH_SHORT).show()
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
        var intent = Intent(this,LabCourseActivity::class.java)
        intent.putExtra("initialFragmentPosition",2)
        intent.putExtra("assignCourseId",assignCourseId)
        startActivity(intent)
        finish()
    }
}