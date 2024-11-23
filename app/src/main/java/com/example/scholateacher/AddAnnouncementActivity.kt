package com.example.scholateacher

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.scholateacher.Class.MyClass
import com.example.scholateacher.Model.Announcement
import com.example.scholateacher.databinding.ActivityAddAnnouncementBinding
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddAnnouncementActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddAnnouncementBinding
    lateinit var assignCourseId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddAnnouncementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        assignCourseId = intent.getStringExtra("assignCourseId").toString()



        binding.back.setOnClickListener{
            finish()
        }

        binding.submitbtn.setOnClickListener{
            var text = binding.edttext.text.toString()

            if(text.isEmpty()){
                Toast.makeText(this,"Please Enter something",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            savedataToDatabase(text)






        }






    }

    private fun savedataToDatabase(text: String) {

        var database =  FirebaseDatabase.getInstance().getReference("Announcement")
        var database2 =  FirebaseDatabase.getInstance().getReference("Teacher")

        MyClass().getCurrentTeacher { teacher->
            var teacherId = teacher!!.id.toString()

            database2.child(teacherId!!).get().addOnSuccessListener {
                if (it.exists()){
                    val teacherName = it.child("name").value.toString()
                    val teacherPic = it.child("profilePic").value.toString()

                    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                    val currentDate = dateFormat.format(Date())
                    var id = database.push().key
                    var announcement = Announcement(id, assignCourseId,text,currentDate,teacherName,teacherPic)

                   database.child(id!!).setValue(announcement).addOnSuccessListener {
                       Toast.makeText(this,"Announcementadded successfully",Toast.LENGTH_SHORT).show()
                       finish()
                   }.addOnFailureListener{
                       Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
                   }



                }
            }.addOnFailureListener{
                Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
            }



        }








    }
}