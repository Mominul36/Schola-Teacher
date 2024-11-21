package com.example.scholateacher.Fragments.Thoery

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scholateacher.Adapters.AssignmentAdapter
import com.example.scholateacher.Adapters.ClassTestAdapter
import com.example.scholateacher.AddAssignmentActivity
import com.example.scholateacher.AddClassTestActivity
import com.example.scholateacher.Model.Assignment
import com.example.scholateacher.Model.ClassTest
import com.example.scholateacher.R
import com.example.scholateacher.databinding.FragmentTAssignmentBinding
import com.example.scholateacher.databinding.FragmentTClassTestBinding
import com.google.firebase.database.*

class TAssignmentFragment : Fragment() {

    lateinit var binding: FragmentTAssignmentBinding
    lateinit var assignmentAdapter: AssignmentAdapter
    lateinit var assignmentList: MutableList<Assignment>
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTAssignmentBinding.inflate(inflater, container, false)

        val assignCourseId = arguments?.getString("assignCourseId") ?: ""

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        assignmentList = mutableListOf()
        assignmentAdapter = AssignmentAdapter(requireContext(), assignmentList)
        binding.recyclerView.adapter = assignmentAdapter

        databaseReference = FirebaseDatabase.getInstance().getReference("Assignment")

        fetchAssignment(assignCourseId)

        binding.fab.setOnClickListener {
            val intent = Intent(requireContext(), AddAssignmentActivity::class.java)
            intent.putExtra("assignCourseId", assignCourseId)
            startActivity(intent)
        }

        return binding.root
    }


    private fun fetchAssignment(assignCourseId: String) {

        databaseReference.orderByChild("assignCourseId").equalTo(assignCourseId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                assignmentList.clear()
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val assignment = dataSnapshot.getValue(Assignment::class.java)
                        assignment?.let { assignmentList.add(it) }
                    }
                    assignmentAdapter.notifyDataSetChanged()
                } else {
                    //Toast.makeText(requireContext(), "No class tests found.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error fetching data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
