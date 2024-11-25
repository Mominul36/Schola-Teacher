package com.example.scholateacher.Fragments

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.TextView

import androidx.fragment.app.Fragment
import com.example.scholateacher.Class.MyClass
import com.example.scholateacher.Model.ClassRoutine
import com.example.scholateacher.SetRoutineActivity

import com.example.scholateacher.databinding.FragmentAdvScheduleBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class AdvScheduleFragment : Fragment() {

    lateinit var binding: FragmentAdvScheduleBinding



    lateinit var sunText: List<TextView>
    lateinit var monText: List<TextView>
    lateinit var tueText: List<TextView>
    lateinit var wedText: List<TextView>
    lateinit var thuText: List<TextView>


    lateinit var sunRoomText: List<TextView>
    lateinit var monRoomText: List<TextView>
    lateinit var tueRoomText: List<TextView>
    lateinit var wedRoomText: List<TextView>
    lateinit var thuRoomText: List<TextView>



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdvScheduleBinding.inflate(inflater, container, false)



        initVariable()

        setRoutine("Sunday",sunText,sunRoomText)
        setRoutine("Monday",monText,monRoomText)
        setRoutine("Tuesday",tueText,tueRoomText)
        setRoutine("Wednesday",wedText,wedRoomText)
        setRoutine("Thursday",thuText,thuRoomText)

        binding.setbtn.setOnClickListener{
            startActivity(Intent(requireContext(),SetRoutineActivity::class.java))
        }









        return binding.root
    }

    private fun setRoutine(day: String, courseText: List<TextView>, roomText: List<TextView>) {

        MyClass().getCurrentSection { section->
            var sectionId = section!!.id

            var database = FirebaseDatabase.getInstance().getReference("ClassRoutine")


            database.child(sectionId!!).addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                   if(snapshot.exists()){
                       for(routineSnapShot in snapshot.children){
                           var routine = routineSnapShot.getValue(ClassRoutine::class.java)
                           if(routine!=null && routine.day==day){


                               val myArr = listOf(
                                   routine.course1, routine.course2, routine.course3, routine.course4,
                                   routine.course5, routine.course6, routine.course7, routine.course8,
                                   routine.course9
                               )



                               for (i in myArr.indices){
                                   var database2 = FirebaseDatabase.getInstance().getReference("AssignCourse")
                                   database2.child(myArr[i]!!).get().addOnSuccessListener {
                                       if(it.exists()){
                                           var txt = it.child("course_id").value.toString()
                                           if(txt!="null")
                                           courseText[i].text = txt
                                       }
                                   }
                               }


                               roomText[0].text = routine.room1
                               roomText[1].text = routine.room2
                               roomText[2].text = routine.room3
                               roomText[3].text = routine.room4
                               roomText[4].text = routine.room5
                               roomText[5].text = routine.room6
                               roomText[6].text = routine.room7
                               roomText[7].text = routine.room8
                               roomText[8].text = routine.room9



                           }
                       }
                   }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })






        }









    }

    private fun initVariable() {

        sunText = listOf(
            binding.vsunc1, binding.vsunc2, binding.vsunc3, binding.vsunc4, binding.vsunc5, binding.vsunc6,
            binding.vsunc7, binding.vsunc8, binding.vsunc9
        )
        monText = listOf(
            binding.vmonc1, binding.vmonc2, binding.vmonc3, binding.vmonc4, binding.vmonc5, binding.vmonc6,
            binding.vmonc7, binding.vmonc8, binding.vmonc9
        )
        tueText = listOf(
            binding.vtuec1, binding.vtuec2, binding.vtuec3, binding.vtuec4, binding.vtuec5, binding.vtuec6,
            binding.vtuec7, binding.vtuec8, binding.vtuec9
        )
        wedText = listOf(
            binding.vwedc1, binding.vwedc2, binding.vwedc3, binding.vwedc4, binding.vwedc5, binding.vwedc6,
            binding.vwedc7, binding.vwedc8, binding.vwedc9
        )
        thuText = listOf(
            binding.vthuc1, binding.vthuc2, binding.vthuc3, binding.vthuc4, binding.vthuc5, binding.vthuc6,
            binding.vthuc7, binding.vthuc8, binding.vthuc9
        )






        sunRoomText = listOf(
            binding.vsunr1, binding.vsunr2, binding.vsunr3, binding.vsunr4, binding.vsunr5, binding.vsunr6,
            binding.vsunr7, binding.vsunr8, binding.vsunr9
        )
        monRoomText = listOf(
            binding.vmonr1, binding.vmonr2, binding.vmonr3, binding.vmonr4, binding.vmonr5, binding.vmonr6,
            binding.vmonr7, binding.vmonr8, binding.vmonr9
        )
        tueRoomText = listOf(
            binding.vtuer1, binding.vtuer2, binding.vtuer3, binding.vtuer4, binding.vtuer5, binding.vtuer6,
            binding.vtuer7, binding.vtuer8, binding.vtuer9
        )
        wedRoomText = listOf(
            binding.vwedr1, binding.vwedr2, binding.vwedr3,
            binding.vwedr4, binding.vwedr5, binding.vwedr6, binding.vwedr7, binding.vwedr8, binding.vwedr9
        )
        thuRoomText = listOf(
            binding.vthur1, binding.vthur2, binding.vthur3, binding.vthur4, binding.vthur5, binding.vthur6,
            binding.vthur7, binding.vthur8, binding.vthur9
        )








    }


}

