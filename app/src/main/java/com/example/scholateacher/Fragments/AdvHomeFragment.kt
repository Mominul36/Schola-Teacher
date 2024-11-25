package com.example.scholateacher.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.scholateacher.Class.MyClass
import com.example.scholateacher.Model.AssignCourse
import com.example.scholateacher.Model.ClassRoutine
import com.example.scholateacher.Model.Dept
import com.example.scholateacher.Model.Section
import com.example.scholateacher.Model.Student
import com.example.scholateacher.R
import com.example.scholateacher.databinding.FragmentAdvHomeBinding
import com.example.scholateacher.databinding.FragmentHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class AdvHomeFragment : Fragment() {
    lateinit var binding: FragmentAdvHomeBinding
    lateinit var database : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdvHomeBinding.inflate(inflater, container, false)


        fetchSectionDetails()




















        return binding.root
    }

    private fun fetchSectionDetails() {

        MyClass().getCurrentSection { section ->
            val sectionId = section?.id ?: return@getCurrentSection

          var   secRef = FirebaseDatabase.getInstance().getReference("Section")

            secRef.
                addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            for (sectionSnapshot in snapshot.children){
                                val section = sectionSnapshot.getValue(Section::class.java)


                                if(section!=null && section.id==sectionId){
                                    fetchDeptName(section.dept)
                                    updateUI(section)


                                }

                            }
                        }


                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle errors
                        showError(error.message)
                    }
                })


            var   stuRef = FirebaseDatabase.getInstance().getReference("Student")


            // Fetch total students in this section
            stuRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            var cnt: Int = 0
                            for (stuSnapshot in snapshot.children){
                                val student = stuSnapshot.getValue(Student::class.java)
                                if(student!=null && student.sectionId==sectionId){
                                    cnt++
                                }

                            }
                           binding.totalstudent.text = cnt.toString()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        showError(error.message)
                    }
                })

            var   courseRef = FirebaseDatabase.getInstance().getReference("AssignCourse")


            // Fetch total course in this section
            courseRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        var cnt: Int = 0
                        for (stuSnapshot in snapshot.children){
                            val student = stuSnapshot.getValue(AssignCourse::class.java)
                            if(student!=null && student.section_id==sectionId){
                                cnt++
                            }

                        }
                        binding.totalcourse.text = cnt.toString()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showError(error.message)
                }
            })
        }
    }

    private fun fetchDeptName(deptId: String?) {
        if (deptId.isNullOrEmpty()) {
            binding.dept.text = "N/A"
            return
        }

        var deptRef = FirebaseDatabase.getInstance().getReference("Department")
        // Query the Dept table to fetch the department name
        deptRef
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        for(deptSnapShot in snapshot.children){
                            val dept = deptSnapShot.getValue(Dept::class.java)
                            if(dept!=null&& dept.dept_code == deptId){
                                binding.dept.text = dept.dept_name ?: "Unknown Department"
                            }
                        }

                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    showError(error.message)
                }
            })
    }


    private fun updateUI(section: Section) {
        binding.batch.text = section.batch ?: "N/A"
        binding.section.text = section.section ?: "N/A"
        binding.lt.text = section.level_term ?: "N/A"
    }

    private fun showError(message: String) {
        // Display error (can use a Snackbar or Toast)
        // Example: Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


}