package com.example.scholateacher.Fragments

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.scholateacher.Class.MyClass
import com.example.scholateacher.Model.AssignCourse
import com.example.scholateacher.Model.ClassRoutine
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

    val rooms = listOf("","2002", "2003", "2004", "2005", "2006", "2007A", "2007B", "2008", "3002", "3003", "3004", "3005", "3006", "3007A", "3007B", "3008", "4002", "4003", "4004", "4005", "4006", "4007A", "4007B", "4008", "5002", "5003", "5004", "5005", "5006", "5007A", "5007B", "5008", "6002", "6003", "6004", "6005", "6006", "6007A", "6007B", "6008")
    lateinit var spinners: List<Spinner>
    lateinit var sunSpinners: List<Spinner>
    lateinit var monSpinners: List<Spinner>
    lateinit var tueSpinners: List<Spinner>
    lateinit var wedSpinners: List<Spinner>
    lateinit var thuSpinners: List<Spinner>

    lateinit var roomSpinners: List<Spinner>
    lateinit var sunRoomSpinners: List<Spinner>
    lateinit var monRoomSpinners: List<Spinner>
    lateinit var tueRoomSpinners: List<Spinner>
    lateinit var wedRoomSpinners: List<Spinner>
    lateinit var thuRoomSpinners: List<Spinner>

    var classRoutineRef = FirebaseDatabase.getInstance().getReference("ClassRoutine")
    var acRef = FirebaseDatabase.getInstance().getReference("AssignCourse")



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdvScheduleBinding.inflate(inflater, container, false)

        initspinners()
        setRoutine()
        setSpinners()


        binding.updatebtn.setOnClickListener{
            updateRoutine("Sunday")
            updateRoutine("Monday")
            updateRoutine("Tuesday")
            updateRoutine("Wednesday")
            updateRoutine("Thursday")


        }






        return binding.root
    }



    private fun updateRoutine(day: String) {

        var spinnerList: List<Spinner> = listOf()

        when(day){
            "Sunday"->{
                spinnerList = sunSpinners
            }
            "Monday"->{
                spinnerList = monSpinners
            }
            "Tuesday"->{
                spinnerList = tueSpinners
            }
            "Wednesday"->{
                spinnerList = wedSpinners
            }
            "Thursday"->{
                spinnerList = thuSpinners
            }
        }


        MyClass().getCurrentSection { section->
            var sectionId = section!!.id.toString()


             var code1 = spinnerList[0].selectedItem.toString()

            if(code1==""){
                func1(sectionId,day,spinnerList,code1)
                return@getCurrentSection
            }

            //Search course1
            acRef.orderByChild("course_id").equalTo(code1)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (childSnapshot in snapshot.children) {
                                val acCourse = childSnapshot.getValue(AssignCourse::class.java)
                                if (acCourse != null && acCourse.section_id==sectionId) {
                                    var course1 = acCourse.id.toString()

                                    func1(sectionId,day,spinnerList,course1)


                                }
                            }
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        // Log or handle the error
                        Log.e("FirebaseError", "Error: ${error.message}")
                    }
                })








        }

    }

    private fun func1(sectionId: String, day: String, spinnerList: List<Spinner>, course1: String) {

        var code2 = spinnerList[1].selectedItem.toString()

        if(code2==""){
            func2(sectionId,day,spinnerList,course1,code2)
            return
        }

        //Search course1
        acRef.orderByChild("course_id").equalTo(code2)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (childSnapshot in snapshot.children) {
                            val acCourse = childSnapshot.getValue(AssignCourse::class.java)
                            if (acCourse != null && acCourse.section_id==sectionId) {
                                var course2 = acCourse.id.toString()

                                func2(sectionId,day,spinnerList,course1,course2)

                            }
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    // Log or handle the error
                    Log.e("FirebaseError", "Error: ${error.message}")
                }
            })




    }

    private fun func2(sectionId: String, day: String, spinnerList: List<Spinner>, course1: String, course2: String) {

        var code3 = spinnerList[2].selectedItem.toString()

        if(code3==""){
            func3(sectionId,day,spinnerList,course1,course2,code3)
            return
        }


        //Search course1
        acRef.orderByChild("course_id").equalTo(code3)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (childSnapshot in snapshot.children) {
                            val acCourse = childSnapshot.getValue(AssignCourse::class.java)
                            if (acCourse != null && acCourse.section_id==sectionId) {
                                var course3 = acCourse.id.toString()

                                func3(sectionId,day,spinnerList,course1,course2,course3)

                            }
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    // Log or handle the error
                    Log.e("FirebaseError", "Error: ${error.message}")
                }
            })




    }

    private fun func3(sectionId: String, day: String, spinnerList: List<Spinner>, course1: String, course2: String, course3: String) {


        var code4 = spinnerList[3].selectedItem.toString()

        if(code4==""){
            func4(sectionId,day,spinnerList,course1,course2,course3,code4)
            return
        }

        //Search course1
        acRef.orderByChild("course_id").equalTo(code4)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (childSnapshot in snapshot.children) {
                            val acCourse = childSnapshot.getValue(AssignCourse::class.java)
                            if (acCourse != null && acCourse.section_id==sectionId) {
                                var course4 = acCourse.id.toString()

                                func4(sectionId,day,spinnerList,course1,course2,course3,course4)

                            }
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    // Log or handle the error
                    Log.e("FirebaseError", "Error: ${error.message}")
                }
            })







    }

    private fun func4(sectionId: String, day: String, spinnerList: List<Spinner>, course1: String, course2: String, course3: String, course4: String) {

        var code5 = spinnerList[4].selectedItem.toString()

        if(code5==""){
            func5(sectionId,day,spinnerList,course1,course2,course3,course4,code5)
            return
        }

        //Search course1
        acRef.orderByChild("course_id").equalTo(code5)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (childSnapshot in snapshot.children) {
                            val acCourse = childSnapshot.getValue(AssignCourse::class.java)
                            if (acCourse != null && acCourse.section_id==sectionId) {
                                var course5 = acCourse.id.toString()

                                func5(sectionId,day,spinnerList,course1,course2,course3,course4,course5)

                            }
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    // Log or handle the error
                    Log.e("FirebaseError", "Error: ${error.message}")
                }
            })

    }

    private fun func5(sectionId: String, day: String, spinnerList: List<Spinner>, course1: String, course2: String, course3: String, course4: String, course5: String) {
        var code6 = spinnerList[5].selectedItem.toString()

        if(code6==""){
            func6(sectionId,day,spinnerList,course1,course2,course3,course4,course5,code6)
            return
        }

        //Search course1
        acRef.orderByChild("course_id").equalTo(code6)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (childSnapshot in snapshot.children) {
                            val acCourse = childSnapshot.getValue(AssignCourse::class.java)
                            if (acCourse != null && acCourse.section_id==sectionId) {
                                var course6 = acCourse.id.toString()

                                func6(sectionId,day,spinnerList,course1,course2,course3,course4,course5,course6)

                            }
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    // Log or handle the error
                    Log.e("FirebaseError", "Error: ${error.message}")
                }
            })
    }

    private fun func6(
        sectionId: String,
        day: String,
        spinnerList: List<Spinner>,
        course1: String,
        course2: String,
        course3: String,
        course4: String,
        course5: String,
        course6: String
    ) {
        var code7 = spinnerList[6].selectedItem.toString()

        if(code7==""){
            func7(sectionId,day,spinnerList,course1,course2,course3,course4,course5,course6,code7)
            return
        }

        //Search course1
        acRef.orderByChild("course_id").equalTo(code7)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (childSnapshot in snapshot.children) {
                            val acCourse = childSnapshot.getValue(AssignCourse::class.java)
                            if (acCourse != null && acCourse.section_id==sectionId) {
                                var course7 = acCourse.id.toString()

                                func7(sectionId,day,spinnerList,course1,course2,course3,course4,course5,course6,course7)

                            }
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    // Log or handle the error
                    Log.e("FirebaseError", "Error: ${error.message}")
                }
            })


    }

    private fun func7(
        sectionId: String,
        day: String,
        spinnerList: List<Spinner>,
        course1: String,
        course2: String,
        course3: String,
        course4: String,
        course5: String,
        course6: String,
        course7: String
    ) {

        var code8 = spinnerList[7].selectedItem.toString()

        if(code8==""){
            func8(sectionId,day,spinnerList,course1,course2,course3,course4,course5,course6,course7,code8)
            return
        }

        //Search course1
        acRef.orderByChild("course_id").equalTo(code8)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (childSnapshot in snapshot.children) {
                            val acCourse = childSnapshot.getValue(AssignCourse::class.java)
                            if (acCourse != null && acCourse.section_id==sectionId) {
                                var course8 = acCourse.id.toString()

                                func8(sectionId,day,spinnerList,course1,course2,course3,course4,course5,course6,course7,course8)

                            }
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    // Log or handle the error
                    Log.e("FirebaseError", "Error: ${error.message}")
                }
            })

    }

    private fun func8(
        sectionId: String,
        day: String,
        spinnerList: List<Spinner>,
        course1: String,
        course2: String,
        course3: String,
        course4: String,
        course5: String,
        course6: String,
        course7: String,
        course8: String
    ) {

        var code9 = spinnerList[8].selectedItem.toString()

        if(code9==""){
            func9(sectionId,day,spinnerList,course1,course2,course3,course4,course5,course6,course7,course8,code9)
            return
        }

        //Search course1
        acRef.orderByChild("course_id").equalTo(code9)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (childSnapshot in snapshot.children) {
                            val acCourse = childSnapshot.getValue(AssignCourse::class.java)
                            if (acCourse != null && acCourse.section_id==sectionId) {
                                var course9 = acCourse.id.toString()

                                func9(sectionId,day,spinnerList,course1,course2,course3,course4,course5,course6,course7,course8,course9)

                            }
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    // Log or handle the error
                    Log.e("FirebaseError", "Error: ${error.message}")
                }
            })



    }

    private fun func9(
        sectionId: String,
        day: String,
        spinnerList: List<Spinner>,
        course1: String,
        course2: String,
        course3: String,
        course4: String,
        course5: String,
        course6: String,
        course7: String,
        course8: String,
        course9: String
    ) {

        var room1: String
        var room2: String
        var room3: String
        var room4: String
        var room5: String
        var room6: String
        var room7: String
        var room8: String
        var room9: String



        var roomSpinnerList: List<Spinner> = listOf()

        when(day){
            "Sunday"->{
                roomSpinnerList = sunRoomSpinners
            }
            "Monday"->{
                roomSpinnerList = monRoomSpinners
            }
            "Tuesday"->{
                roomSpinnerList = tueRoomSpinners
            }
            "Wednesday"->{
                roomSpinnerList = wedRoomSpinners
            }
            "Thursday"->{
                roomSpinnerList = thuRoomSpinners
            }
        }




        if(course1==""){
            room1=""
        }else{
            room1 = roomSpinnerList[0].selectedItem.toString()
        }

        if(course2==""){
            room2=""
        }else{
            room2 = roomSpinnerList[1].selectedItem.toString()
        }


        if(course3==""){
            room3=""
        }else{
            room3 = roomSpinnerList[2].selectedItem.toString()
        }


        if(course4==""){
            room4=""
        }else{
            room4 = roomSpinnerList[3].selectedItem.toString()
        }


        if(course5==""){
            room5=""
        }else{
            room5 = roomSpinnerList[4].selectedItem.toString()
        }


        if(course6==""){
            room6=""
        }else{
            room6 = roomSpinnerList[5].selectedItem.toString()
        }


        if(course7==""){
            room7=""
        }else{
            room7 = roomSpinnerList[6].selectedItem.toString()
        }


        if(course8==""){
            room8=""
        }else{
            room8 = roomSpinnerList[7].selectedItem.toString()
        }


        if(course9==""){
            room9=""
        }else{
            room9 = roomSpinnerList[8].selectedItem.toString()
        }


        var classRoutine = ClassRoutine(sectionId,day,course1, course2, course3, course4, course5, course6, course7, course8, course9,room1, room2, room3, room4, room5, room6, room7, room8, room9)

        classRoutineRef.child(sectionId).child(day).setValue(classRoutine)




    }

    private fun setSpinners() {
        MyClass().getCurrentSection { section ->
            if (section != null) {
                var sectionId = section.id.toString()
                val assignCourseRef = FirebaseDatabase.getInstance().getReference("AssignCourse")
                assignCourseRef.orderByChild("section_id").equalTo(sectionId).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            // List to hold the courses fetched for the section
                            val courses = mutableListOf<String>()


                            courses.add("")

                            // Iterate through the AssignCourse records and get the associated course code
                            for (assignCourseSnapshot in snapshot.children) {
                                val assignCourse = assignCourseSnapshot.getValue(AssignCourse::class.java)
                                assignCourse?.course_id?.let { courses.add(it) }
                            }


                            // Set each spinner's adapter with the course codes
                            spinners.forEach { spinner ->
                                val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, courses)
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                spinner.adapter = adapter
                                spinner.setSelection(-1)
                            }
                            roomSpinners.forEach { spinner ->
                                val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, rooms)
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                spinner.adapter = adapter
                                spinner.setSelection(-1)
                            }

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle error fetching AssignCourse
                    }
                })
            }
        }
    }

    private fun initspinners() {
         spinners = listOf(
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


        sunSpinners = listOf(
            binding.sunc1, binding.sunc2, binding.sunc3, binding.sunc4, binding.sunc5, binding.sunc6,
            binding.sunc7, binding.sunc8, binding.sunc9
        )
        monSpinners = listOf(
            binding.monc1, binding.monc2, binding.monc3, binding.monc4, binding.monc5, binding.monc6,
            binding.monc7, binding.monc8, binding.monc9
        )
        tueSpinners = listOf(
            binding.tuec1, binding.tuec2, binding.tuec3, binding.tuec4, binding.tuec5, binding.tuec6,
            binding.tuec7, binding.tuec8, binding.tuec9
        )
        wedSpinners = listOf(
            binding.wedc1, binding.wedc2, binding.wedc3, binding.wedc4, binding.wedc5, binding.wedc6,
            binding.wedc7, binding.wedc8, binding.wedc9
        )
        thuSpinners = listOf(
            binding.thuc1, binding.thuc2, binding.thuc3, binding.thuc4, binding.thuc5, binding.thuc6,
            binding.thuc7, binding.thuc8, binding.thuc9
        )



         roomSpinners = listOf(
            binding.sunr1, binding.sunr2, binding.sunr3, binding.sunr4, binding.sunr5, binding.sunr6,
            binding.sunr7, binding.sunr8, binding.sunr9,
             binding.monr1, binding.monr2, binding.monr3, binding.monr4, binding.monr5, binding.monr6,
             binding.monr7, binding.monr8, binding.monr9,
            binding.tuer1, binding.tuer2, binding.tuer3, binding.tuer4, binding.tuer5, binding.tuer6,
            binding.tuer7, binding.tuer8, binding.tuer9, binding.wedr1, binding.wedr2, binding.wedr3,
            binding.wedr4, binding.wedr5, binding.wedr6, binding.wedr7, binding.wedr8, binding.wedr9,
            binding.thur1, binding.thur2, binding.thur3, binding.thur4, binding.thur5, binding.thur6,
            binding.thur7, binding.thur8, binding.thur9
        )






       sunRoomSpinners = listOf(
           binding.sunr1, binding.sunr2, binding.sunr3, binding.sunr4, binding.sunr5, binding.sunr6,
           binding.sunr7, binding.sunr8, binding.sunr9
       )
       monRoomSpinners = listOf(
           binding.monr1, binding.monr2, binding.monr3, binding.monr4, binding.monr5, binding.monr6,
           binding.monr7, binding.monr8, binding.monr9
       )
       tueRoomSpinners = listOf(
           binding.tuer1, binding.tuer2, binding.tuer3, binding.tuer4, binding.tuer5, binding.tuer6,
           binding.tuer7, binding.tuer8, binding.tuer9
       )
       wedRoomSpinners = listOf(
           binding.wedr1, binding.wedr2, binding.wedr3,
           binding.wedr4, binding.wedr5, binding.wedr6, binding.wedr7, binding.wedr8, binding.wedr9
       )
        thuRoomSpinners = listOf(
            binding.thur1, binding.thur2, binding.thur3, binding.thur4, binding.thur5, binding.thur6,
            binding.thur7, binding.thur8, binding.thur9
        )







    }

    private fun setRoutine() {
        var database= FirebaseDatabase.getInstance().getReference("ClassRoutine")

        MyClass().getCurrentSection { section->
            var sectionId= section!!.id.toString()

            database.child(sectionId).addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                   if(!snapshot.exists()){

                       var routine1 = ClassRoutine(sectionId,"Sunday","","","","","","","","","","","","","","","","","","",)
                       var routine2 = ClassRoutine(sectionId,"Monday","","","","","","","","","","","","","","","","","","",)
                       var routine3 = ClassRoutine(sectionId,"Tuesday","","","","","","","","","","","","","","","","","","",)
                       var routine4 = ClassRoutine(sectionId,"Wednesday","","","","","","","","","","","","","","","","","","",)
                       var routine5 = ClassRoutine(sectionId,"Thursday","","","","","","","","","","","","","","","","","","",)

                       database.child(sectionId).child("Sunday").setValue(routine1)
                       database.child(sectionId).child("Monday").setValue(routine2)
                       database.child(sectionId).child("Tuesday").setValue(routine3)
                       database.child(sectionId).child("Wednesday").setValue(routine4)
                       database.child(sectionId).child("Thursday").setValue(routine5)

                   }
                }
                override fun onCancelled(error: DatabaseError) {

                }

            })




        }


    }


}

