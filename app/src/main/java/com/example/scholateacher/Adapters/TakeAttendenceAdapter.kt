package com.example.scholateacher.Adapters


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.scholateacher.Class.ControlImage
import com.example.scholateacher.Model.Student
import com.example.scholateacher.Model.Teacher
import com.example.scholateacher.R
import com.example.scholateacher.StudentProfileActivity


class TakeAttendenceAdapter(
    private val context: Context,
    private val studentList: List<Student>
) : RecyclerView.Adapter<TakeAttendenceAdapter.TakeAttendenceHolder>() {

    private val buttonStates = mutableMapOf<Int, Int>()

    init {
        initializeButtonStates()
    }

    private fun initializeButtonStates() {
        for (i in studentList.indices) {
            buttonStates[i] = 0 // Default state to 0 for all items
        }
    }

    class TakeAttendenceHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val atn: ImageView = itemView.findViewById(R.id.atn)
        val latn: ImageView = itemView.findViewById(R.id.latn)
        val id: TextView = itemView.findViewById(R.id.id)
        var sl: TextView = itemView.findViewById(R.id.sl)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TakeAttendenceHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_attendence_tracking, parent, false)
        return TakeAttendenceHolder(view)
    }

    override fun onBindViewHolder(holder: TakeAttendenceHolder, position: Int) {
        val student = studentList[position]

        // Set SL (Serial Number)
        holder.sl.text = (position + 1).toString()

        holder.id.text = student.id

        // Set initial state
        when (buttonStates[position]) {
            1 -> {
                holder.atn.setImageResource(R.drawable.ic_atn)
                holder.latn.setImageResource(0) // Clear latn icon
            }
            2 -> {
                holder.atn.setImageResource(0) // Clear atn icon
                holder.latn.setImageResource(R.drawable.ic_latn)
            }
            else -> {
                holder.atn.setImageResource(0) // Clear both icons
                holder.latn.setImageResource(0)
            }
        }

        // Handle atn ImageView click
        holder.atn.setOnClickListener {
            if (buttonStates[position] == 1) {
                // If already set, clear it
                buttonStates[position] = 0
                holder.atn.setImageResource(0) // Clear atn icon
            } else {
                // Set atn icon and clear latn
                buttonStates[position] = 1
                holder.atn.setImageResource(R.drawable.ic_atn)
                holder.latn.setImageResource(0)
            }
        }

        // Handle latn ImageView click
        holder.latn.setOnClickListener {
            if (buttonStates[position] == 2) {
                // If already set, clear it
                buttonStates[position] = 0
                holder.latn.setImageResource(0) // Clear latn icon
            } else {
                // Set latn icon and clear atn
                buttonStates[position] = 2
                holder.latn.setImageResource(R.drawable.ic_latn)
                holder.atn.setImageResource(0)
            }
        }
    }


    override fun getItemCount() = studentList.size

    // Ensure buttonStates is updated dynamically when studentList changes
    fun updateStudentList(newStudentList: List<Student>) {
        (studentList as MutableList).clear()
        studentList.addAll(newStudentList)
        initializeButtonStates()
        notifyDataSetChanged()
    }

    // Method to get the button states for all items, ensuring default states are returned for unclicked items
    fun getButtonStates(): Map<Int, Int> {
        // Ensure every index in studentList is represented in buttonStates
        for (i in studentList.indices) {
            if (!buttonStates.containsKey(i)) {
                buttonStates[i] = 0 // Default state
            }
        }
        return buttonStates
    }
}
