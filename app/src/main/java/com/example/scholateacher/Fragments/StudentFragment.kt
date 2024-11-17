package com.example.scholateacher.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scholateacher.Adapters.StudentAdapter
import com.example.scholateacher.Class.ControlImage
import com.example.scholateacher.Class.MyClass
import com.example.scholateacher.Model.Student
import com.example.scholateacher.R
import com.example.scholateacher.databinding.FragmentStudentBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class StudentFragment : Fragment() {

    lateinit var binding: FragmentStudentBinding
    private lateinit var controlImage: ControlImage
    private lateinit var adapter: StudentAdapter
    private val studentList = mutableListOf<Student>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=  FragmentStudentBinding.inflate(inflater, container, false)
        controlImage = ControlImage(requireContext(), requireActivity().activityResultRegistry, "imagePickerKey")

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = StudentAdapter(requireContext(), studentList, controlImage)
        binding.recyclerView.adapter = adapter


        MyClass().getCurrentSection { section ->
            if (section != null) {
                getStudentsBySectionAndVerification(section.id!!) { students ->
                    adapter.updateList(students) // Update RecyclerView with the filtered list
                }
            } else {
                // Handle the case when no section is found
                Log.d("Section Info", "No section found for the current teacher")
            }
        }







        return  binding.root
    }


    // Function to get students filtered by sectionId and isverify equal to false
    fun getStudentsBySectionAndVerification(sectionId: String, callback: (List<Student>) -> Unit) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Student")

        // Query to filter students where sectionId equals given sectionId and isverify equals false
        val query = databaseReference.orderByChild("sectionId").equalTo(sectionId)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val studentsList = mutableListOf<Student>()

                if (snapshot.exists()) {
                    // Iterate through the students
                    for (dataSnapshot in snapshot.children) {
                        val student = dataSnapshot.getValue(Student::class.java)

                        // Check if the student is verified
                        if (student?.isverify == false) {
                            studentsList.add(student)
                        }
                    }
                }

                // Return the filtered list of students via callback
                callback(studentsList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("StudentRepository", "Error fetching students: ${error.message}")
            }
        })
    }




}