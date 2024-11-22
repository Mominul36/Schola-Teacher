package com.example.scholateacher.Fragments.Thoery

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scholateacher.Adapters.TheoryAttendanceAdapter
import com.example.scholateacher.Model.AssignCourse
import com.example.scholateacher.Model.TheoryAttendance
import com.example.scholateacher.TakeAttendanceActivity
import com.example.scholateacher.databinding.FragmentTAttendenceBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class TAttendanceFragment : Fragment() {
    lateinit var binding: FragmentTAttendenceBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var theoryAttendanceList: MutableList<TheoryAttendance>
    private lateinit var theoryAttendanceAdapter: TheoryAttendanceAdapter
    lateinit var assignCourseId : String



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTAttendenceBinding.inflate(inflater, container, false)

         assignCourseId = arguments?.getString("assignCourseId") ?: ""



        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference("TheoryAttendance")


        theoryAttendanceList = mutableListOf()
        theoryAttendanceAdapter = TheoryAttendanceAdapter(theoryAttendanceList)

        // Setup RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = theoryAttendanceAdapter

        // Fetch data from Firebase
        fetchTheoryAttendanceData()


        setAllDate()
        
        

        binding.takeatn.setOnClickListener{
            var intent = Intent(requireContext(),TakeAttendanceActivity::class.java)
            intent.putExtra("assignCourseId",assignCourseId)
            startActivity(intent)
        }

















        return binding.root
    }

    private fun setAllDate() {
        val acRef = FirebaseDatabase.getInstance().getReference("AssignCourse")

        // Attach a ValueEventListener for real-time updates
        acRef.child(assignCourseId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val ac = snapshot.getValue(AssignCourse::class.java)
                if (ac != null) {
                    // Dynamically set all date fields
                    binding.d1.text = ac.date1
                    binding.d2.text = ac.date2
                    binding.d3.text = ac.date3
                    binding.d4.text = ac.date4
                    binding.d5.text = ac.date5
                    binding.d6.text = ac.date6
                    binding.d7.text = ac.date7
                    binding.d8.text = ac.date8
                    binding.d9.text = ac.date9
                    binding.d10.text = ac.date10
                    binding.d11.text = ac.date11
                    binding.d12.text = ac.date12
                    binding.d13.text = ac.date13
                    binding.d14.text = ac.date14
                    binding.d15.text = ac.date15
                    binding.d16.text = ac.date16
                    binding.d17.text = ac.date17
                    binding.d18.text = ac.date18
                    binding.d19.text = ac.date19
                    binding.d20.text = ac.date20
                    binding.d21.text = ac.date21
                    binding.d22.text = ac.date22
                    binding.d23.text = ac.date23
                    binding.d24.text = ac.date24
                    binding.d25.text = ac.date25
                    binding.d26.text = ac.date26
                    binding.d27.text = ac.date27
                    binding.d28.text = ac.date28
                    binding.d29.text = ac.date29
                    binding.d30.text = ac.date30
                    binding.d31.text = ac.date31
                    binding.d32.text = ac.date32
                    binding.d33.text = ac.date33
                    binding.d34.text = ac.date34
                    binding.d35.text = ac.date35
                    binding.d36.text = ac.date36
                    binding.d37.text = ac.date37
                    binding.d38.text = ac.date38
                    binding.d39.text = ac.date39
                    binding.d40.text = ac.date40
                    binding.d41.text = ac.date41
                    binding.d42.text = ac.date42

                    // Update total classes
                    binding.totalClass.text = "Total Class: ${ac.total_class}"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TAttendanceFragment", "Error observing data: ${error.message}")
            }
        })
    }



    private fun fetchTheoryAttendanceData() {
        // Fetch data from the TheoryAttendence table in Firebase
        databaseReference.orderByChild("assignCourseId").equalTo(assignCourseId)
            .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                theoryAttendanceList.clear()

                for (attendanceSnapshot in snapshot.children) {
                    val theoryAttendence = attendanceSnapshot.getValue(TheoryAttendance::class.java)
                    theoryAttendence?.let {
                        theoryAttendanceList.add(it)
                    }
                }

                // Notify adapter to update the RecyclerView
                theoryAttendanceAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TAttendanceFragment", "Error fetching data: ${error.message}")
            }
        })
    }





}