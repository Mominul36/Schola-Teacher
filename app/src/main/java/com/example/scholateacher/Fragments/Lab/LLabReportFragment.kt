package com.example.scholateacher.Fragments.Lab

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
import com.example.scholateacher.Adapters.LabReportAdapter
import com.example.scholateacher.AddAssignmentActivity
import com.example.scholateacher.AddClassTestActivity
import com.example.scholateacher.AddLabReportActivity
import com.example.scholateacher.Model.Assignment
import com.example.scholateacher.Model.ClassTest
import com.example.scholateacher.Model.LabReport
import com.example.scholateacher.R
import com.example.scholateacher.databinding.FragmentLLabReportBinding
import com.example.scholateacher.databinding.FragmentTAssignmentBinding
import com.example.scholateacher.databinding.FragmentTClassTestBinding
import com.google.firebase.database.*

class LLabReportFragment : Fragment() {

    lateinit var binding: FragmentLLabReportBinding
    lateinit var labReportAdapter: LabReportAdapter
    lateinit var labReportList: MutableList<LabReport>
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLLabReportBinding.inflate(inflater, container, false)

        val assignCourseId = arguments?.getString("assignCourseId") ?: ""

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        labReportList = mutableListOf()
        labReportAdapter = LabReportAdapter(requireContext(), labReportList)
        binding.recyclerView.adapter = labReportAdapter

        databaseReference = FirebaseDatabase.getInstance().getReference("LabReport")

        fetchLabReport(assignCourseId)

        binding.fab.setOnClickListener {
            val intent = Intent(requireContext(), AddLabReportActivity::class.java)
            intent.putExtra("assignCourseId", assignCourseId)
            startActivity(intent)
        }

        return binding.root
    }


    private fun fetchLabReport(assignCourseId: String) {

        databaseReference.orderByChild("assignCourseId").equalTo(assignCourseId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                labReportList.clear()
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val labReport = dataSnapshot.getValue(LabReport::class.java)
                        labReport?.let { labReportList.add(it) }
                    }
                    labReportAdapter.notifyDataSetChanged()
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
