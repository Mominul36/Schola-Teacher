package com.example.scholateacher

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scholateacher.Adapters.TakeAttendenceAdapter
import com.example.scholateacher.Class.MyClass
import com.example.scholateacher.Model.AssignCourse
import com.example.scholateacher.Model.Student
import com.example.scholateacher.Model.TheoryAttendance
import com.example.scholateacher.databinding.ActivityTakeAttendenceBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TakeAttendanceActivity : AppCompatActivity() {
    lateinit var binding: ActivityTakeAttendenceBinding
    lateinit var assignCourseId : String

    lateinit var atnAdapter: TakeAttendenceAdapter
    lateinit var studentList: MutableList<Student>
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTakeAttendenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        assignCourseId = intent.getStringExtra("assignCourseId").toString()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        studentList = mutableListOf()
        atnAdapter = TakeAttendenceAdapter(this, studentList)
        binding.recyclerView.adapter = atnAdapter


        fetchStudentData()


        binding.date.setOnClickListener {
          showDatePicker()
        }

        binding.back.setOnClickListener{
            finish()
        }
        binding.submitbtn.setOnClickListener{
            saveValueToDatabase()
        }


    }

    private fun saveValueToDatabase() {
        val date = binding.date.text.toString()
        var totalClass: Int

        val acRef = FirebaseDatabase.getInstance().getReference("AssignCourse")
        val tatnRef = FirebaseDatabase.getInstance().getReference("TheoryAttendance")

        acRef.child(assignCourseId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val ac = snapshot.getValue(AssignCourse::class.java)
                if (ac != null) {
                    totalClass = ac.total_class?.toInt() ?: 0
                    totalClass += 1


                    val updateTotalClass = mapOf("total_class" to totalClass)
                    acRef.child(assignCourseId).updateChildren(updateTotalClass)


                    val (className, dateName) = MyClass().getClassAndDate(totalClass)


                    val updateDate = mapOf(dateName to date)
                    acRef.child(assignCourseId).updateChildren(updateDate)


                    val buttonStates = atnAdapter.getButtonStates()


                    for ((position, state) in buttonStates) {


                        val studentId = studentList[position].id ?: continue

                        tatnRef.orderByChild("studentId").equalTo(studentId)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {
                                        for (attendanceSnapshot in snapshot.children) {
                                            val attendance =
                                                attendanceSnapshot.getValue(TheoryAttendance::class.java)

                                            // Ensure the attendance matches the current assignCourseId
                                            if (attendance != null && attendance.assignCourseId == assignCourseId) {
                                                // Prepare updates
                                                val stateValue = when (state) {
                                                    1 -> "1" // Present
                                                    2 -> "2" // Late
                                                    else -> "3" // Absent
                                                }
                                                val updates = mapOf(
                                                    className to stateValue
                                                )

                                                // Update the attendance record
                                                tatnRef.child(attendanceSnapshot.key!!)
                                                    .updateChildren(updates)

                                                finish()
                                            }
                                        }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(
                                        this@TakeAttendanceActivity,
                                        "Failed to update attendance: ${error.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@TakeAttendanceActivity,
                    "Failed to fetch AssignCourse: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)

                val dateFormat = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
                binding.date.text = dateFormat.format(selectedDate.time)
            },
            year, month, day
        )

        datePickerDialog.show()
    }

    private fun fetchStudentData() {
        val assignCourseRef = FirebaseDatabase.getInstance().getReference("AssignCourse")
        val studentRef = FirebaseDatabase.getInstance().getReference("Student")

        assignCourseRef.child(assignCourseId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val ac = snapshot.getValue(AssignCourse::class.java)
                if (ac != null) {
                    val sectionId = ac.section_id
                    studentRef.orderByChild("sectionId").equalTo(sectionId)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val students = mutableListOf<Student>()
                                if (snapshot.exists()) {
                                    for (dataSnapshot in snapshot.children) {
                                        val student = dataSnapshot.getValue(Student::class.java)
                                        if (student?.isverify == true) {
                                            students.add(student)
                                        }
                                    }
                                    // Update the adapter with the new student list
                                    atnAdapter.updateStudentList(students)
                                } else {
                                    Toast.makeText(this@TakeAttendanceActivity, "No Student found.", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.e("FetchError", "Failed to fetch students: ${error.message}")
                            }
                        })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FetchError", "Failed to fetch AssignCourse: ${error.message}")
            }
        })
    }

}