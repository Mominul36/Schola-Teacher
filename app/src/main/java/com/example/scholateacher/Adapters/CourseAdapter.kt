package com.example.scholateacher.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.scholateacher.LabCourseActivity
import com.example.scholateacher.Model.CourseItem
import com.example.scholateacher.R
import com.example.scholateacher.TheoryCourseActivity
import com.example.scholateacher.databinding.ItemCourseBinding

class CourseAdapter(
    private val context: Context,
    private val courseList: List<CourseItem>
) :
    RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    inner class CourseViewHolder(val binding: ItemCourseBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = ItemCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courseList[position]
        with(holder.binding) {
            courseName.text = course.courseName
            courseCode.text = course.courseCode
            department.text = course.department
            levelTerm.text = course.levelTerm

            Glide.with(icon.context)
                .load(course.icon)
                .placeholder(R.drawable.profile) // Optional placeholder
                .into(icon)
        }

        holder.itemView.setOnClickListener{

            if(course.type=="Theory"){
                var intent = Intent(context,TheoryCourseActivity::class.java)
                intent.putExtra("initialFragmentPosition",0)
                intent.putExtra("assignCourseId",course.assigncourseId)
                context.startActivity(intent)
            }else{
                var intent = Intent(context,LabCourseActivity::class.java)
                intent.putExtra("initialFragmentPosition",0)
                intent.putExtra("assignCourseId",course.assigncourseId)
                context.startActivity(intent)
            }

        }
    }

    override fun getItemCount() = courseList.size
}
