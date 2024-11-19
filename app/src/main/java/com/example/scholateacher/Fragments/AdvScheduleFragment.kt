package com.example.scholateacher.Fragments

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.scholateacher.Class.MyClass
import com.example.scholateacher.Model.AssignCourse
import com.example.scholateacher.Model.Course
import com.example.scholateacher.Model.Teacher
import com.example.scholateacher.databinding.FragmentAdvScheduleBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class AdvScheduleFragment : Fragment() {

    lateinit var binding: FragmentAdvScheduleBinding
    lateinit var auth: FirebaseAuth
    lateinit var myClass: MyClass

    val rooms = listOf("2002", "2003", "2004", "2005", "2006", "2007A", "2007B", "2008", "3002", "3003", "3004", "3005", "3006", "3007A", "3007B", "3008", "4002", "4003", "4004", "4005", "4006", "4007A", "4007B", "4008", "5002", "5003", "5004", "5005", "5006", "5007A", "5007B", "5008", "6002", "6003", "6004", "6005", "6006", "6007A", "6007B", "6008")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdvScheduleBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        myClass = MyClass()

        val currentUser = auth.currentUser
        val currentTeacherId = currentUser?.email?.substringBefore("@")

        // Fetch the current section using teacher's ID
        myClass.getCurrentSection { section ->
            if (section != null) {
                // Fetch all AssignCourse records where section_id matches the current section's id
                val assignCourseRef = FirebaseDatabase.getInstance().getReference("AssignCourse")
                assignCourseRef.orderByChild("section_id").equalTo(section.id).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            // List to hold the courses fetched for the section
                            val courses = mutableListOf<String>()

                            //courses.add("")

                            // Iterate through the AssignCourse records and get the associated course code
                            for (assignCourseSnapshot in snapshot.children) {
                                val assignCourse = assignCourseSnapshot.getValue(AssignCourse::class.java)
                                assignCourse?.course_id?.let { courses.add(it) }
                            }

                            // Now set the course codes in the COURSE Spinners
                            val spinners = listOf(
                                binding.sunc1, binding.sunc2, binding.sunc3, binding.sunc4, binding.sunc5, binding.sunc6,
                                binding.sunc7, binding.sunc8, binding.sunc9,
                                binding.monc1, binding.monc2, binding.monc3, binding.monc4, binding.monc5, binding.monc6,
                                binding.monc7, binding.monc8, binding.monc9,
                                binding.tuec1, binding.tuec2, binding.tuec3, binding.tuec4, binding.tuec5, binding.tuec6,
                                binding.tuec7, binding.tuec8, binding.tuec9,
                                binding.wedc1, binding.wedc2, binding.wedc3, binding.wedc4, binding.wedc5, binding.wedc6,
                                binding.wedc7, binding.wedc8, binding.wedc9,
                                binding.thuc1, binding.thuc2, binding.thuc3, binding.thuc4, binding.thuc5, binding.thuc6,
                                binding.thuc7, binding.thuc8, binding.thuc9
                            )

                            // Set each spinner's adapter with the course codes
                            spinners.forEach { spinner ->
                                val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, courses)
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                spinner.adapter = adapter
                                spinner.setSelection(-1) // Set selection to null (no item selected)
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle error fetching AssignCourse
                    }
                })
            }
        }

        // Set predefined room numbers for ROOM Spinners
        val roomSpinners = listOf(
            binding.sunr1, binding.sunr2, binding.sunr3, binding.sunr4, binding.sunr5, binding.sunr6,
            binding.sunr7, binding.sunr8, binding.sunr9, binding.monr1, binding.monr2, binding.monr3,
            binding.monr4, binding.monr5, binding.monr6, binding.monr7, binding.monr8, binding.monr9,
            binding.tuer1, binding.tuer2, binding.tuer3, binding.tuer4, binding.tuer5, binding.tuer6,
            binding.tuer7, binding.tuer8, binding.tuer9, binding.wedr1, binding.wedr2, binding.wedr3,
            binding.wedr4, binding.wedr5, binding.wedr6, binding.wedr7, binding.wedr8, binding.wedr9,
            binding.thur1, binding.thur2, binding.thur3, binding.thur4, binding.thur5, binding.thur6,
            binding.thur7, binding.thur8, binding.thur9
        )

        // Set room numbers in the Room spinners and set them to null initially
        roomSpinners.forEach { spinner ->
            val roomAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, rooms)
            roomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = roomAdapter
            spinner.setSelection(-1) // Set selection to null (no item selected)
        }



        binding.addbtn.setOnClickListener {
         Log.d("bal","asdfasdfadf")


            // List to hold selected course codes and room numbers
            val selectedCourses = mutableListOf<String?>()
            val selectedRooms = mutableListOf<String?>()

            val courseColumn = listOf(
                "course1","course2","course3","course4","course5","course6","course7","course8","course9"
            )

            val roomColumn = listOf(
                "room1","room2","room3","room4","room5","room6","room7","room8","room9"
            )

            val teacherNameColumn = listOf(
                "teacher_name1", "teacher_name2", "teacher_name3", "teacher_name4", "teacher_name5", "teacher_name6", "teacher_name7", "teacher_name8", "teacher_name9"
            )

            val picColumn = listOf(
                "pic1","pic2","pic3","pic4","pic5","pic6","pic7","pic8","pic9"
            )

            // Fetch selected course codes
            val courseSpinners = listOf(
                binding.sunc1, binding.sunc2, binding.sunc3, binding.sunc4, binding.sunc5, binding.sunc6,
                binding.sunc7, binding.sunc8, binding.sunc9, binding.monc1, binding.monc2, binding.monc3,
                binding.monc4, binding.monc5, binding.monc6, binding.monc7, binding.monc8, binding.monc9,
                binding.tuec1, binding.tuec2, binding.tuec3, binding.tuec4, binding.tuec5, binding.tuec6,
                binding.tuec7, binding.tuec8, binding.tuec9, binding.wedc1, binding.wedc2, binding.wedc3,
                binding.wedc4, binding.wedc5, binding.wedc6, binding.wedc7, binding.wedc8, binding.wedc9,
                binding.thuc1, binding.thuc2, binding.thuc3, binding.thuc4, binding.thuc5, binding.thuc6,
                binding.thuc7, binding.thuc8, binding.thuc9
            )
            courseSpinners.forEach { spinner ->
                selectedCourses.add(spinner.selectedItem?.toString())
            }


            // Fetch selected room numbers
            val roomSpinners = listOf(
                binding.sunr1, binding.sunr2, binding.sunr3, binding.sunr4, binding.sunr5, binding.sunr6,
                binding.sunr7, binding.sunr8, binding.sunr9, binding.monr1, binding.monr2, binding.monr3,
                binding.monr4, binding.monr5, binding.monr6, binding.monr7, binding.monr8, binding.monr9,
                binding.tuer1, binding.tuer2, binding.tuer3, binding.tuer4, binding.tuer5, binding.tuer6,
                binding.tuer7, binding.tuer8, binding.tuer9, binding.wedr1, binding.wedr2, binding.wedr3,
                binding.wedr4, binding.wedr5, binding.wedr6, binding.wedr7, binding.wedr8, binding.wedr9,
                binding.thur1, binding.thur2, binding.thur3, binding.thur4, binding.thur5, binding.thur6,
                binding.thur7, binding.thur8, binding.thur9
            )
            roomSpinners.forEach { spinner ->
                selectedRooms.add(spinner.selectedItem?.toString())
            }



            for (i in 0..44) {
                val remainder:Int = i % 9
                val r:Int = i/9



                if(r==0){
                    myfun("SUNDAY",selectedCourses[i],selectedRooms[i],courseColumn[remainder], roomColumn[remainder],  teacherNameColumn[remainder],   picColumn[remainder])
                }
                else if(r==1){
                    myfun("MONDAY",selectedCourses[i],selectedRooms[i],courseColumn[remainder], roomColumn[remainder],  teacherNameColumn[remainder],   picColumn[remainder])
                }
                else if(r==2){
                    myfun("TUESDAY",selectedCourses[i],selectedRooms[i],courseColumn[remainder], roomColumn[remainder],  teacherNameColumn[remainder],   picColumn[remainder])
                }
                else if(r==3){
                    myfun("WEDNESDAY",selectedCourses[i],selectedRooms[i],courseColumn[remainder], roomColumn[remainder],  teacherNameColumn[remainder],   picColumn[remainder])
                }
                else if(r==4){
                    myfun("THURSDAY",selectedCourses[i],selectedRooms[i],courseColumn[remainder], roomColumn[remainder],  teacherNameColumn[remainder],   picColumn[remainder])
                }




            }




        }




        return binding.root
    }

    private fun myfun(day: String, courseId: String?, room: String?, courseColumn: String, roomColumn: String, teacherNameColumn: String, picColumn: String) {
       // Log.d("bal", "${day}")
        val currentUser = auth.currentUser
        val currentTeacherId = currentUser?.email?.substringBefore("@")

        // Fetch the current section using teacher's ID
        myClass.getCurrentSection { section ->
            if (section != null) {
               val sectionId = section.id

                val database = FirebaseDatabase.getInstance()
                val assignCourseRef = database.getReference("AssignCourse")
                val teacherRef = database.getReference("Teacher")
                val classRoutineRef = database.getReference("ClassRoutine")

                // Step 1: Query AssignCourse with section_id and course_id
                assignCourseRef.orderByChild("section_id").equalTo(sectionId).get().addOnSuccessListener { snapshot ->
                    var teacherId: String? = null

                    for (child in snapshot.children) {
                        val assignCourse = child.getValue(AssignCourse::class.java)
                        if (assignCourse != null && assignCourse.course_id == courseId) {
                            teacherId = assignCourse.teacher_id
                            break
                        }
                    }

                    // Step 2: If teacherId is found, query Teacher table
                    if (teacherId != null) {
                        teacherRef.child(teacherId).get().addOnSuccessListener { teacherSnapshot ->
                            val teacher = teacherSnapshot.getValue(Teacher::class.java)
                            if (teacher != null) {


                                val user = mapOf<String,String>(
                                    courseColumn to courseId.toString(),
                                    roomColumn to room.toString(),
                                    teacherNameColumn to teacher.name.toString(),
                                    picColumn to teacher.profilePic.toString()
                                )

                                classRoutineRef.child(sectionId!!).child(day).updateChildren(user)
                                Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show()


                            } else {
                                Toast.makeText(context, "No teacher found with ID: $teacherId", Toast.LENGTH_SHORT).show()
                            }
                        }.addOnFailureListener { error ->
                            Toast.makeText(context, "Error fetching teacher details: ${error.message}", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "No AssignCourse record found for section_id: $sectionId and course_id: $courseId", Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener { error ->
                    println("Error fetching AssignCourse: ${error.message}")
                }





            }
        }





    }

}
