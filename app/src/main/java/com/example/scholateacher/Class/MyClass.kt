package com.example.scholateacher.Class

import com.example.scholateacher.Model.Section
import com.example.scholateacher.Model.Teacher
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MyClass {

    private lateinit var auth: FirebaseAuth
    private val dates = mutableListOf<String>()
    private val classes = mutableListOf<String>()


    fun getClassAndDate(index: Int): Pair<String, String> {

        for (i in 1..42) {
            dates.add("date$i")
            classes.add("class$i")
        }

        if (index < 1 || index > dates.size) {
            throw IllegalArgumentException("Index out of range. Must be between 1 and 42.")
        }

        return Pair(classes[index - 1], dates[index - 1])
    }





    // Get the current logged-in teacher
    fun getCurrentTeacher(onComplete: (Teacher?) -> Unit) {
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if (user == null) {
            onComplete(null) // User not logged in
            return
        }

        val teacherId = user.email?.substringBefore("@")
        if (teacherId.isNullOrEmpty()) {
            onComplete(null) // Invalid teacher ID
            return
        }

        // Reference to the Teacher node in Firebase
        val database = FirebaseDatabase.getInstance().getReference("Teacher")

        database.child(teacherId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val teacher = snapshot.getValue(Teacher::class.java)
                    onComplete(teacher) // Pass the teacher object
                } else {
                    onComplete(null) // Teacher not found
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onComplete(null) // Handle error by passing null
            }
        })
    }

    // Function to fetch the section of the current teacher based on advisor_id
    fun getCurrentSection(onComplete: (Section?) -> Unit) {
        getCurrentTeacher { teacher ->
            if (teacher != null) {
                // Once teacher is fetched, use the teacher's id (as advisor_id) to query the Section
                val database = FirebaseDatabase.getInstance().getReference("Section")

                // Query Section where advisor_id is the current teacher's id
                database.orderByChild("advisor_id").equalTo(teacher.id).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            // Fetch the section(s) and return the first match
                            val section = snapshot.children.firstOrNull()?.getValue(Section::class.java)
                            onComplete(section) // Pass the Section object
                        } else {
                            onComplete(null) // No section found for this teacher
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        onComplete(null) // Handle error by passing null
                    }
                })
            } else {
                onComplete(null) // Teacher not found
            }
        }
    }
}
