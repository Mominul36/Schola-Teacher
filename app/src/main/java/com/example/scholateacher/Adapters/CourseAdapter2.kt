package com.example.scholateacher.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.scholateacher.LabCourseActivity
import com.example.scholateacher.Model.CourseItem2
import com.example.scholateacher.R
import com.example.scholateacher.TheoryCourseActivity
import com.example.scholateacher.databinding.ItemCourse2Binding
import com.example.scholateacher.databinding.ItemCourseBinding

class CourseAdapter2(
    private val context: Context,
    private val courseList: List<CourseItem2>
) :
    RecyclerView.Adapter<CourseAdapter2.CourseViewHolder>() {

    inner class CourseViewHolder(val binding: ItemCourse2Binding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = ItemCourse2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courseList[position]
        with(holder.binding) {
            courseName.text = course.courseName
            courseCode.text = course.courseCode
            teacherName.text = course.teacherName


            Glide.with(context).load(course.courseIcon).into(courseIcon)

            Glide.with(context).load(course.teacherPic).into(teacherPic)


        }


    }

    override fun getItemCount() = courseList.size
}
