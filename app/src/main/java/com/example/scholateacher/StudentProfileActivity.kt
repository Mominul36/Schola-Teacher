package com.example.scholateacher

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.scholateacher.databinding.ActivityStudentProfileBinding
import com.example.scholateacher.Model.Dept
import com.example.scholateacher.Model.Student
import com.google.firebase.database.*

class StudentProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the studentId passed from the previous activity
        val studentId = intent.getStringExtra("student_id")
        if (!studentId.isNullOrEmpty()) {
            fetchStudentById(studentId)
        } else {
            Log.e("StudentProfile", "Student ID is null or empty.")
        }


        binding.back.setOnClickListener{
            finish()
        }


    }

    // Fetch student by studentId
    private fun fetchStudentById(studentId: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Student")

        databaseReference.child(studentId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val student = snapshot.getValue(Student::class.java)
                    if (student != null) {
                        displayStudentDetails(student)
                        fetchDepartmentByCode(student.dept ?: "")
                    }
                } else {
                    Log.e("StudentProfile", "Student not found.")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("StudentProfile", "Error fetching student: ${error.message}")
            }
        })
    }

    // Fetch department by dept_code
    private fun fetchDepartmentByCode(deptCode: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Department")

        databaseReference.child(deptCode).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val department = snapshot.getValue(Dept::class.java)
                    if (department != null) {
                        displayDepartmentDetails(department)
                    }
                } else {
                    Log.e("StudentProfile", "Department not found.")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("StudentProfile", "Error fetching department: ${error.message}")
            }
        })
    }

    // Display student details in the UI
    private fun displayStudentDetails(student: Student) {
        binding.name.text = student.name
        binding.id.text = student.id
        binding.batchSection.text = "Batch - ${student.batch}               Section - ${student.section}"
        binding.email.text = student.email
        binding.phone.text = student.phone
        binding.code.text = "Verification Code: ${student.verificationCode ?: "N/A"}"

        if(student.isverify==true){
            binding.code.visibility =  View.GONE
        }


        // Set the profile picture (if available)
        if (student.profilePic.isNullOrEmpty()) {
            binding.profile.setImageResource(R.drawable.student_profile) // Default image
        } else {
            // Load profile picture using any image loading library (Glide/Picasso)
            Glide.with(this)
                .load(student.profilePic)
                .placeholder(R.drawable.student_profile)
                .into(binding.profile)
        }
    }

    // Display department details in the UI
    private fun displayDepartmentDetails(department: Dept) {
        binding.department.text = department.dept_name ?: "N/A"
    }
}
