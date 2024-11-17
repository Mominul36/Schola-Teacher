package com.example.scholateacher.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.scholateacher.Class.ControlImage
import com.example.scholateacher.Model.Student
import com.example.scholateacher.Model.Teacher
import com.example.scholateacher.R
import com.example.scholateacher.StudentProfileActivity


class StudentAdapter(
    private val context: Context,
    private var studentList: List<Student>, // Make this a mutable list
    private val controlImage: ControlImage
) : RecyclerView.Adapter<StudentAdapter.TeacherViewHolder>() {

    class TeacherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profile: ImageView = itemView.findViewById(R.id.profile)
        val name: TextView = itemView.findViewById(R.id.name)
        val id: TextView = itemView.findViewById(R.id.id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeacherViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_student, parent, false)
        return TeacherViewHolder(view)
    }

    override fun onBindViewHolder(holder: TeacherViewHolder, position: Int) {
        val student = studentList[position]
        holder.name.text = student.name
        holder.id.text = student.id

        if (student.profilePic.isNullOrEmpty()) {
            holder.profile.setImageResource(R.drawable.man)
        } else {
            controlImage.setImageByURl(student.profilePic.toString(), holder.profile)
        }

        // Navigate to profile activity when an item is clicked
        holder.itemView.setOnClickListener {
            val intent = Intent(context, StudentProfileActivity::class.java)
            intent.putExtra("student_id", student.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() : Int {
        return studentList.size
    }

    // Function to update the student list
    fun updateList(newList: List<Student>) {
        studentList = newList
        notifyDataSetChanged() // Notify the adapter about data changes
    }
}
