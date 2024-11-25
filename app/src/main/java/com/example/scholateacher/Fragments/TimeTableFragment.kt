package com.example.scholateacher.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.scholateacher.Model.Teacher
import com.example.scholateacher.databinding.FragmentTimeTableBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.*

class TimeTableFragment : Fragment() {

    private lateinit var binding: FragmentTimeTableBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimeTableBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Teacher")

        // Fetch and process all Teacher objects
        fetchAndCreateAuthUsers()




        return binding.root
    }

    private fun fetchAndCreateAuthUsers() {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (teacherSnapshot in snapshot.children) {
                        val teacher = teacherSnapshot.getValue(Teacher::class.java)
                        if (teacher != null && !teacher.id.isNullOrEmpty() && !teacher.password.isNullOrEmpty()) {
                            val email = "${teacher.id}@gmail.com"
                            val password = teacher.password.toString()
                            createFirebaseUser(email, password)
                        }
                    }
                } else {
                    Log.e("TimeTableFragment", "No teachers found in the database.")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TimeTableFragment", "Database error: ${error.message}")
            }
        })
    }

    private fun createFirebaseUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("TimeTableFragment", "User created: $email")
                } else {
                    if (task.exception is FirebaseAuthUserCollisionException) {
                        Log.w("TimeTableFragment", "User already exists: $email")
                    } else {
                        Log.e("TimeTableFragment", "Error creating user: ${task.exception?.message}")
                    }
                }
            }
    }
}
