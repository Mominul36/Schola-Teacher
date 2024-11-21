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
import com.example.scholateacher.Model.LabReport
import com.example.scholateacher.R
import com.example.scholateacher.UpdateAssignmentActivity
import com.example.scholateacher.UpdateClassTestActivity
import com.example.scholateacher.UpdateLabReportActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LabReportAdapter(
    private val context: Context,
    private val labReportList: List<LabReport>
) :
    RecyclerView.Adapter<LabReportAdapter.LabReportViewHolder>() {

    class LabReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var lrNo: TextView = itemView.findViewById(R.id.lrNo)
        var date: TextView = itemView.findViewById(R.id.date)
        var day: TextView = itemView.findViewById(R.id.day)
        var topic: TextView = itemView.findViewById(R.id.topic)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabReportViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lab_report, parent, false)
        return LabReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: LabReportViewHolder, position: Int) {
        val labReport = labReportList[position]


        val (formattedDate, dayOfWeek) = formatDateAndFindDay(labReport.date.toString())

        holder.lrNo.text = labReport.lrNo
        holder.topic.text ="Topic: "+ labReport.topic
        holder.date.text = formattedDate
        holder.day.text = dayOfWeek



        holder.itemView.setOnClickListener{


            var intent = Intent(context, UpdateLabReportActivity::class.java)
            intent.putExtra("assignCourseId",labReport.assignCourseId)
            intent.putExtra("labReportId",labReport.id)
            context.startActivity(intent)

            if (context is Activity) {
                context.finish()
            }


        }
    }

    override fun getItemCount() = labReportList.size


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
