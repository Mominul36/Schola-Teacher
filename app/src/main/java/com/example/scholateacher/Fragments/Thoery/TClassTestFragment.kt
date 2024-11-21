package com.example.scholateacher.Fragments.Thoery

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scholateacher.Adapters.ClassTestAdapter
import com.example.scholateacher.AddClassTestActivity
import com.example.scholateacher.Model.ClassTest
import com.example.scholateacher.R
import com.example.scholateacher.databinding.FragmentTClassTestBinding
import com.google.firebase.database.*

class TClassTestFragment : Fragment() {

    lateinit var binding: FragmentTClassTestBinding
    lateinit var classTestAdapter: ClassTestAdapter
    lateinit var classTestList: MutableList<ClassTest>
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTClassTestBinding.inflate(inflater, container, false)

        val assignCourseId = arguments?.getString("assignCourseId") ?: ""

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        classTestList = mutableListOf()
        classTestAdapter = ClassTestAdapter(requireContext(), classTestList)
        binding.recyclerView.adapter = classTestAdapter

        databaseReference = FirebaseDatabase.getInstance().getReference("ClassTest")

        fetchClassTests(assignCourseId)

        binding.fab.setOnClickListener {
            val intent = Intent(requireContext(), AddClassTestActivity::class.java)
            intent.putExtra("assignCourseId", assignCourseId)
            startActivity(intent)
        }

        return binding.root
    }


    private fun fetchClassTests(assignCourseId: String) {

        databaseReference.orderByChild("assignCourseId").equalTo(assignCourseId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                classTestList.clear()
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val classTest = dataSnapshot.getValue(ClassTest::class.java)
                        classTest?.let { classTestList.add(it) }
                    }
                    classTestAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(requireContext(), "No class tests found.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error fetching data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
