package com.example.scholateacher.Adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.scholateacher.LabCourseActivity
import com.example.scholateacher.Model.ClassTest
import com.example.scholateacher.Model.CourseItem
import com.example.scholateacher.R
import com.example.scholateacher.TheoryCourseActivity
import com.example.scholateacher.UpdateClassTestActivity
import com.example.scholateacher.databinding.ItemCourseBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ClassTestAdapter(
    private val context: Context,
    private val classTestList: List<ClassTest>
) :
    RecyclerView.Adapter<ClassTestAdapter.ClassTestViewHolder>() {

     class ClassTestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
       var ctNo: TextView = itemView.findViewById(R.id.ctNo)
         var date: TextView = itemView.findViewById(R.id.date)
         var day: TextView = itemView.findViewById(R.id.day)
         var time: TextView = itemView.findViewById(R.id.time)
         var topic: TextView = itemView.findViewById(R.id.topic)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassTestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_class_test, parent, false)
        return ClassTestViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClassTestViewHolder, position: Int) {
        val classTest = classTestList[position]


        val (formattedDate, dayOfWeek) = formatDateAndFindDay(classTest.date.toString())

        holder.ctNo.text = classTest.ctNo
        holder.time.text = classTest.time
        holder.topic.text ="Topic: "+ classTest.topic
        holder.date.text = formattedDate
        holder.day.text = dayOfWeek



        holder.itemView.setOnClickListener{


                var intent = Intent(context, UpdateClassTestActivity::class.java)
                intent.putExtra("assignCourseId",classTest.assignCourseId)
               intent.putExtra("classTestId",classTest.id)
                context.startActivity(intent)

            if (context is Activity) {
                context.finish()
            }


        }
    }

    override fun getItemCount() = classTestList.size


    fun formatDateAndFindDay(inputDate: String): Pair<String, String> {
        val inputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM, dd", Locale.getDefault())
        val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())

        val date: Date = inputFormat.parse(inputDate) ?: return Pair("", "")
        val formattedDate = outputFormat.format(date)
        val dayOfWeek = dayFormat.format(date)

        return Pair(formattedDate, dayOfWeek)
    }
}
