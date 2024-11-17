package com.example.scholateacher.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.scholateacher.Class.ControlImage
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


        controlImage = ControlImage(requireContext(), requireActivity().activityResultRegistry,
            "imagePickerKey")


        binding.select.setOnClickListener{
            controlImage.setImageView(binding.image)
            controlImage.selectImage()
        }


        binding.upload.setOnClickListener{
            controlImage.uploadImageToFirebaseStorage { isSuccess, message ->
                if (isSuccess) {
                   var imageURL : String = message //This URl can be save in database
                    saveImageUriToDatabase(imageURL) //Function to set url to realtime database

                    Toast.makeText(requireContext(), "Image uploaded successfully",
                        Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }



        }

       setSection()



        return binding.root

    }

    private fun setSection() {
        database = FirebaseDatabase.getInstance().getReference("Section")
        var id:String = database.push()!!.key!!
        var section = Section(id,"01","17","C","2-1","58796590")

        database.child(id!!).setValue(section)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Success",
                    Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(requireContext(), "Faield",
                    Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveImageUriToDatabase(imageURL: String) {
        database = FirebaseDatabase.getInstance().getReference("Course")
        var code:String = "CSE 3107"
        var course = Course(code,"Data Communication and Networking","01",imageURL,"Theory")

        database.child(code!!).setValue(course)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Success",
                    Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(requireContext(), "Faield",
                    Toast.LENGTH_SHORT).show()
            }


    }


}