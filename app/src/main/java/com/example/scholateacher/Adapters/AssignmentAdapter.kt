package com.example.scholateacher.Adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.scholateacher.Model.Assignment
import com.example.scholateacher.Model.ClassTest
import com.example.scholateacher.R
import com.example.scholateacher.UpdateAssignmentActivity
import com.example.scholateacher.UpdateClassTestActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AssignmentAdapter(
    private val context: Context,
    private val assignmentList: List<Assignment>
) :
    RecyclerView.Adapter<AssignmentAdapter.AssignmentViewHolder>() {

    class AssignmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var assignmentNo: TextView = itemView.findViewById(R.id.assignmentNo)
        var date: TextView = itemView.findViewById(R.id.date)
        var day: TextView = itemView.findViewById(R.id.day)
        var topic: TextView = itemView.findViewById(R.id.topic)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssignmentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_assignment, parent, false)
        return AssignmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AssignmentViewHolder, position: Int) {
        val assignment = assignmentList[position]


        val (formattedDate, dayOfWeek) = formatDateAndFindDay(assignment.date.toString())

        holder.assignmentNo.text = assignment.assignmentNo
        holder.topic.text ="Topic: "+ assignment.topic
        holder.date.text = formattedDate
        holder.day.text = dayOfWeek



        holder.itemView.setOnClickListener{


            var intent = Intent(context, UpdateAssignmentActivity::class.java)
            intent.putExtra("assignCourseId",assignment.assignCourseId)
            intent.putExtra("assignmentId",assignment.id)
            context.startActivity(intent)

            if (context is Activity) {
                context.finish()
            }


        }
    }

    override fun getItemCount() = assignmentList.size


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
