package com.example.scholateacher.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.scholateacher.Model.TheoryAttendance
import com.example.scholateacher.R

class TheoryAttendanceAdapter(
    private val attendanceList: List<TheoryAttendance>
) : RecyclerView.Adapter<TheoryAttendanceAdapter.TheoryAttendanceViewHolder>() {


    class TheoryAttendanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var id : TextView = itemView.findViewById(R.id.id)
        var present: TextView = itemView.findViewById(R.id.present)
        var absent: TextView = itemView.findViewById(R.id.absent)
        var late: TextView = itemView.findViewById(R.id.late)
        var sl: TextView = itemView.findViewById(R.id.sl)

        var c1 : ImageView = itemView.findViewById(R.id.c1)
        var c2 : ImageView = itemView.findViewById(R.id.c2)
        var c3 : ImageView = itemView.findViewById(R.id.c3)
        var c4 : ImageView = itemView.findViewById(R.id.c4)
        var c5 : ImageView = itemView.findViewById(R.id.c5)
        var c6 : ImageView = itemView.findViewById(R.id.c6)
        var c7 : ImageView = itemView.findViewById(R.id.c7)
        var c8 : ImageView = itemView.findViewById(R.id.c8)
        var c9 : ImageView = itemView.findViewById(R.id.c9)
        var c10 : ImageView = itemView.findViewById(R.id.c10)
        var c11 : ImageView = itemView.findViewById(R.id.c11)
        var c12 : ImageView = itemView.findViewById(R.id.c12)
        var c13 : ImageView = itemView.findViewById(R.id.c13)
        var c14 : ImageView = itemView.findViewById(R.id.c14)
        var c15 : ImageView = itemView.findViewById(R.id.c15)
        var c16 : ImageView = itemView.findViewById(R.id.c16)
        var c17 : ImageView = itemView.findViewById(R.id.c17)
        var c18 : ImageView = itemView.findViewById(R.id.c18)
        var c19 : ImageView = itemView.findViewById(R.id.c19)
        var c20 : ImageView = itemView.findViewById(R.id.c20)
        var c21 : ImageView = itemView.findViewById(R.id.c21)
        var c22 : ImageView = itemView.findViewById(R.id.c22)
        var c23 : ImageView = itemView.findViewById(R.id.c23)
        var c24 : ImageView = itemView.findViewById(R.id.c24)
        var c25 : ImageView = itemView.findViewById(R.id.c25)
        var c26 : ImageView = itemView.findViewById(R.id.c26)
        var c27 : ImageView = itemView.findViewById(R.id.c27)
        var c28 : ImageView = itemView.findViewById(R.id.c28)
        var c29 : ImageView = itemView.findViewById(R.id.c29)
        var c30 : ImageView = itemView.findViewById(R.id.c30)
        var c31 : ImageView = itemView.findViewById(R.id.c31)
        var c32 : ImageView = itemView.findViewById(R.id.c32)
        var c33 : ImageView = itemView.findViewById(R.id.c33)
        var c34 : ImageView = itemView.findViewById(R.id.c34)
        var c35 : ImageView = itemView.findViewById(R.id.c35)
        var c36 : ImageView = itemView.findViewById(R.id.c36)
        var c37 : ImageView = itemView.findViewById(R.id.c37)
        var c38 : ImageView = itemView.findViewById(R.id.c38)
        var c39 : ImageView = itemView.findViewById(R.id.c39)
        var c40 : ImageView = itemView.findViewById(R.id.c40)
        var c41 : ImageView = itemView.findViewById(R.id.c41)
        var c42 : ImageView = itemView.findViewById(R.id.c42)


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TheoryAttendanceViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_attendence, parent, false)
        return TheoryAttendanceViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TheoryAttendanceViewHolder, position: Int) {
        val theoryAttendance = attendanceList[position]

        // Set SL (Serial Number) dynamically
        holder.sl.text = (position + 1).toString()

        holder.id.text = theoryAttendance.studentId

        var presentCount = 0
        var lateCount = 0
        var absentCount = 0

        // Using reflection to access the class status dynamically
        for (i in 1..42) {
            val classStatus = TheoryAttendance::class.java.getDeclaredField("class$i").apply { isAccessible = true }
                .get(theoryAttendance) as String

            when (classStatus) {
                "1" -> presentCount++
                "2" -> lateCount++
                "3" -> absentCount++
            }

            // Set images dynamically
            val imageView = TheoryAttendanceViewHolder::class.java.getDeclaredField("c$i").apply { isAccessible = true }
                .get(holder) as ImageView

            when (classStatus) {
                "1" -> imageView.setImageResource(R.drawable.ic_atn2)
                "2" -> imageView.setImageResource(R.drawable.ic_latn2)
            }
        }

        // Set counts
        holder.present.text = presentCount.toString()
        holder.late.text = lateCount.toString()
        holder.absent.text = absentCount.toString()
    }



    override fun getItemCount(): Int = attendanceList.size


}
