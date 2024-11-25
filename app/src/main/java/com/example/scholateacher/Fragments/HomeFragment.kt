package com.example.scholateacher.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import com.example.scholateacher.Class.ControlImage
import com.example.scholateacher.CourseListActivity
import com.example.scholateacher.Model.Course
import com.example.scholateacher.Model.Dept
import com.example.scholateacher.Model.Section
import com.example.scholateacher.Model.Teacher
import com.example.scholateacher.R
import com.example.scholateacher.databinding.FragmentHomeBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var database : DatabaseReference
    private lateinit var controlImage: ControlImage


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)



    binding.see.setOnClickListener{
        startActivity(Intent(requireContext(),CourseListActivity::class.java))
    }










        return binding.root

    }









}