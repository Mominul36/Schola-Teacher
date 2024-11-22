package com.example.scholateacher

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.scholateacher.Model.Student
import com.example.scholateacher.Model.TheoryAttendance
import com.example.scholateacher.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        var user = auth.currentUser

        if(user!=null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }






        binding.loginbtn.setOnClickListener{

            var id = binding.editId.text.toString()
            var pass = binding.editPassword.text.toString()

            if(id.isEmpty() || pass.isEmpty()){
                Toast.makeText(this,"Please enter ID  and Password",Toast.LENGTH_SHORT).show()
            }else{
                var email = id+"@gmail.com"
                signIn(email,pass)
            }

        }





      //  resetCourseData()


        //createAttendanceForSectionStudents("-OBuCQKNB-X1J6LdwwGe", "-OC-FI-WQulEcr77RVBc")










    }

    private fun signIn(email: String, pass: String) {
     auth.signInWithEmailAndPassword(email,pass)
         .addOnSuccessListener {
             Toast.makeText(this,"Login Successfull",Toast.LENGTH_SHORT).show()
             startActivity(Intent(this,MainActivity::class.java))
             finish()

         }
         .addOnFailureListener{ error->
             Toast.makeText(this,"Error: ${error.message}",Toast.LENGTH_SHORT).show()
         }
    }



    private fun createAttendanceForSectionStudents(sectionId: String, assignCourseId: String) {
        val studentDatabase = FirebaseDatabase.getInstance().getReference("Student")
        val attendanceDatabase = FirebaseDatabase.getInstance().getReference("TheoryAttendance")

        // Fetch students of the given section
        studentDatabase.orderByChild("sectionId").equalTo(sectionId).addListenerForSingleValueEvent(object :
            ValueEventListener {
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
                                            //Toast.makeText(this@AddCourseActivity, "Failed to create attendance: ${e.message}", Toast.LENGTH_SHORT).show()
                                        }
                                }
                            }


                        }
                    }
                } else {
                    //Toast.makeText(this@AddCourseActivity, "No students found in this section", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
               // Toast.makeText(this@AddCourseActivity, "Error fetching students: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }




    fun resetCourseData() {
        // Reference to the "AssignCourse" table in your Firebase Realtime Database
        val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("AssignCourse")

        // Retrieve all courses and update them
        databaseRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                for (child in snapshot.children) {
                    // Get the course ID (key)
                    val courseId = child.key

                    // Create an update map
                    val updates = mutableMapOf<String, Any>()
                    updates["total_class"] = 0
                    for (i in 1..42) {
                        updates["date$i"] = "" // Set all date fields to empty
                    }

                    // Update the course
                    if (courseId != null) {
                        databaseRef.child(courseId).updateChildren(updates)
                            .addOnSuccessListener {
                                println("Successfully reset course: $courseId")
                            }
                            .addOnFailureListener { exception ->
                                println("Failed to reset course $courseId: ${exception.message}")
                            }
                    }
                }
            } else {
                println("No courses found in the database.")
            }
        }.addOnFailureListener { exception ->
            println("Error retrieving courses: ${exception.message}")
        }
    }

}