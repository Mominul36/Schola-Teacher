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
import com.example.scholateacher.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class AdvHomeFragment : Fragment() {
    lateinit var database : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

      val  myClass = MyClass()



//        myClass.getCurrentSection { section ->
//            if (section != null) {
//            database= FirebaseDatabase.getInstance().getReference("ClassRoutine")
//                var id = database.push().key
//
//                var classRoutine = ClassRoutine(id,section.id,"THURSDAY","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","")
//
//                database.child(section.id!!).child("THURSDAY").setValue(classRoutine)
//
//
//
//            }
//        }














        return inflater.inflate(R.layout.fragment_adv_home, container, false)
    }


}