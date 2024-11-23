package com.example.scholateacher.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.scholateacher.Class.ControlImage
import com.example.scholateacher.Class.MyClass
import com.example.scholateacher.Model.Dept
import com.example.scholateacher.R
import com.example.scholateacher.databinding.FragmentProfileBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var controlImage: ControlImage
    private var isImageSelected = false // Track if a new image is selected

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        controlImage = ControlImage(
            requireContext(),
            requireActivity().activityResultRegistry,
            "imagePickerKey"
        )

        setState()

        MyClass().getCurrentTeacher { teacher ->
            val deptId = teacher!!.dept.toString()

            binding.name.setText(teacher.name)

            val database = FirebaseDatabase.getInstance().getReference("Department")
            database.child(deptId!!).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val dept = snapshot.getValue(Dept::class.java)
                    if (dept != null) {
                        binding.name.setText(teacher.name)
                        binding.des.setText(teacher.des)
                        binding.ql.setText(teacher.ql)
                        binding.id.setText(teacher.id)
                        binding.dept.setText(dept.dept_name)
                        binding.email.setText(teacher.email)
                        binding.phone.setText(teacher.phone)

                        if (teacher.profilePic.isNullOrEmpty()) {
                            binding.pic.setImageResource(R.drawable.profile)
                        } else {
                            controlImage.setImageByURl(teacher.profilePic!!, binding.pic)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Failed to fetch department details", Toast.LENGTH_SHORT).show()
                }
            })
        }

        binding.pic.setOnClickListener {
            controlImage.setImageView(binding.pic)
            controlImage.selectImage()
            isImageSelected = true
        }

        binding.updatebtn.setOnClickListener {
            if (isImageSelected) {
                controlImage.uploadImageToFirebaseStorage { isSuccess, message ->
                    if (isSuccess) {
                        updateTeacherProfile(message)
                    } else {
                        Toast.makeText(requireContext(), "Failed to upload image: $message", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                MyClass().getCurrentTeacher { teacher ->
                    updateTeacherProfile(teacher!!.profilePic ?: "")
                }
            }
        }

        binding.details.setOnClickListener {
            showDetailsLayout()
        }

        binding.password.setOnClickListener {
            showPasswordLayout()
        }


        binding.passchange.setOnClickListener {
            val oldPassword = binding.oldpass.text.toString().trim()
            val newPassword = binding.newpass.text.toString().trim()

            if (oldPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter the old password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (newPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a new password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (newPassword.length < 8) {
                Toast.makeText(requireContext(), "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            MyClass().getCurrentTeacher { teacher ->
                if (teacher != null) {
                    val database = FirebaseDatabase.getInstance().getReference("Teacher")
                    val teacherId = teacher.id!!

                    database.child(teacherId).child("password").addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val currentPassword = snapshot.value.toString()
                            if (currentPassword == oldPassword) {
                                database.child(teacherId).child("password").setValue(newPassword)
                                    .addOnSuccessListener {
                                        Toast.makeText(requireContext(), "Password changed successfully", Toast.LENGTH_SHORT).show()
                                        binding.oldpass.text.clear()
                                        binding.newpass.text.clear()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(requireContext(), "Failed to change password", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                Toast.makeText(requireContext(), "Old password is incorrect", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(requireContext(), "Failed to validate password: ${error.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                } else {
                    Toast.makeText(requireContext(), "Failed to get teacher details", Toast.LENGTH_SHORT).show()
                }
            }
        }





        return binding.root
    }

    private fun updateTeacherProfile(profilePicUrl: String) {
        val database = FirebaseDatabase.getInstance().getReference("Teacher")

        val user = mapOf(
            "name" to binding.name.text.toString(),
            "des" to binding.des.text.toString(),
            "ql" to binding.ql.text.toString(),
            "email" to binding.email.text.toString(),
            "phone" to binding.phone.text.toString(),
            "profilePic" to profilePicUrl // Include the profile picture URL
        )

        MyClass().getCurrentTeacher { teacher ->
            val teacherId = teacher!!.id
            database.child(teacherId!!).updateChildren(user).addOnSuccessListener {
                Toast.makeText(requireContext(), "Successfully Updated", Toast.LENGTH_SHORT).show()
                isImageSelected = false // Reset the flag after updating
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to Update", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDetailsLayout() {
        binding.details.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.base_color))
        binding.details.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        binding.password.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.temp_color))
        binding.password.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))

        binding.mainLayout.visibility = View.VISIBLE
        binding.passwordLayout.visibility = View.GONE
    }

    private fun showPasswordLayout() {
        binding.password.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.base_color))
        binding.password.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        binding.details.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.temp_color))
        binding.details.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))

        binding.mainLayout.visibility = View.GONE
        binding.passwordLayout.visibility = View.VISIBLE
    }

    private fun setState() {
        showDetailsLayout()
    }
}
