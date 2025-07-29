package com.vatty.mygbu

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.materialswitch.MaterialSwitch
import com.vatty.mygbu.data.model.StudentAttendance

class StudentsAttendanceAdapter(
    private val studentsList: List<StudentAttendance>,
    private val onAttendanceChanged: (Int, Boolean) -> Unit
) : RecyclerView.Adapter<StudentsAttendanceAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvStudentName: TextView = itemView.findViewById(android.R.id.text1)
        private val tvRollNumber: TextView = itemView.findViewById(android.R.id.text2)
        private val switchAttendance: MaterialSwitch = itemView.findViewById(android.R.id.checkbox)
        
        init {
            switchAttendance.setOnCheckedChangeListener { _, isChecked ->
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onAttendanceChanged(position, isChecked)
                }
            }
        }

        fun bind(student: StudentAttendance) {
            tvStudentName.text = student.name
            tvRollNumber.text = student.rollNumber
            switchAttendance.isChecked = student.isPresent
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a simple layout programmatically
        val cardView = MaterialCardView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setPadding(32, 16, 32, 16)
        }
        
        val linearLayout = android.widget.LinearLayout(parent.context).apply {
            orientation = android.widget.LinearLayout.HORIZONTAL
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        
        val tvStudentName = TextView(parent.context).apply {
            id = android.R.id.text1
            layoutParams = android.widget.LinearLayout.LayoutParams(
                0,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
            textSize = 16f
        }
        
        val tvRollNumber = TextView(parent.context).apply {
            id = android.R.id.text2
            layoutParams = android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
            )
            textSize = 14f
            setPadding(16, 0, 16, 0)
        }
        
        val switchAttendance = MaterialSwitch(parent.context).apply {
            id = android.R.id.checkbox
            layoutParams = android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
        
        linearLayout.addView(tvStudentName)
        linearLayout.addView(tvRollNumber)
        linearLayout.addView(switchAttendance)
        cardView.addView(linearLayout)
        
        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(studentsList[position])
    }

    override fun getItemCount() = studentsList.size
} 