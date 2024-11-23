package com.example.scholateacher

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.scholateacher.databinding.ActivityDriveSetBinding
import com.google.firebase.database.FirebaseDatabase

class DriveSetActivity : AppCompatActivity() {

    lateinit var binding: ActivityDriveSetBinding
    lateinit var assignCourseId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDriveSetBinding.inflate(layoutInflater)
        setContentView(binding.root)


         assignCourseId = intent.getStringExtra("assignCourseId").toString()


         fetchDriveLink()

        binding.updatebtn.setOnClickListener{
            updateDriveLink()

        }


        binding.back.setOnClickListener{
             finish()
        }




    }

    private fun updateDriveLink() {
        val database = FirebaseDatabase.getInstance().getReference("StudyMaterial")
        val link = binding.edtlink.text.toString().trim()

        // Validation for the link
        if (link.isEmpty()) {
            Toast.makeText(this, "Link cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if (!android.util.Patterns.WEB_URL.matcher(link).matches()) {
            Toast.makeText(this, "Invalid URL format", Toast.LENGTH_SHORT).show()
            return
        }

        if (!link.contains("drive.google.com")) {
            Toast.makeText(this, "Link must be a valid Google Drive link", Toast.LENGTH_SHORT).show()
            return
        }

        // Proceed to update the drive link in Firebase
        val user = mapOf<String, String>(
            "driveLink" to link
        )
        database.child(assignCourseId).updateChildren(user).addOnSuccessListener {
            Toast.makeText(this, "Successfully Updated", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to Update", Toast.LENGTH_SHORT).show()
        }
    }


    @SuppressLint("SuspiciousIndentation")
    private fun fetchDriveLink() {
     var database = FirebaseDatabase.getInstance().getReference("StudyMaterial")

        database.child(assignCourseId).get().addOnSuccessListener {
            if (it.exists()){
                binding.edtlink.setText(it.child("driveLink").value.toString())
            }else{
                binding.edtlink.setHint("Drive is not set")
            }
        }.addOnFailureListener{
            Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
        }
    }












}