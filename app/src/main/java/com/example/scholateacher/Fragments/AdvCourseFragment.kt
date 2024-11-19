package com.example.scholateacher.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.scholateacher.AddCourseActivity
import com.example.scholateacher.R
import com.example.scholateacher.databinding.FragmentAdvCourseBinding
import com.google.firebase.auth.FirebaseAuth


class AdvCourseFragment : Fragment() {
    lateinit var binding: FragmentAdvCourseBinding
    lateinit var auth : FirebaseAuth




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdvCourseBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        var user = auth.currentUser





    binding.fab.setOnClickListener{
        startActivity(Intent(requireContext(),AddCourseActivity::class.java))
    }









        return binding.root
    }

}