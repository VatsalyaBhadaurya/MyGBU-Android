package com.vatty.mygbu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.materialswitch.MaterialSwitch

class StudentsAttendanceAdapter(
    private val students: List<StudentAttendance>,
    private val onAttendanceToggle: (Int) -> Unit
) : RecyclerView.Adapter<StudentsAttendanceAdapter.StudentViewHolder>() {

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvStudentName: TextView = itemView.findViewById(R.id.tv_student_name)
        val tvRollNumber: TextView = itemView.findViewById(R.id.tv_roll_number)
        val switchAttendance: MaterialSwitch = itemView.findViewById(R.id.switch_attendance)
        val cardStudent: MaterialCardView = itemView.findViewById(R.id.card_student)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student_attendance, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]
        
        holder.tvStudentName.text = student.name
        holder.tvRollNumber.text = student.rollNumber
        
        // Clear listener first to prevent triggering during binding
        holder.switchAttendance.setOnCheckedChangeListener(null)
        holder.switchAttendance.isChecked = student.isPresent
        
        // Update card background based on attendance
        val cardBackgroundColor = if (student.isPresent) {
            R.color.success_light
        } else {
            R.color.error_light
        }
        holder.cardStudent.setCardBackgroundColor(
            holder.itemView.context.getColor(cardBackgroundColor)
        )
        
        // Set listener after updating the checked state
        holder.switchAttendance.setOnCheckedChangeListener { _, _ ->
            // Post the update to avoid modifying adapter during layout
            holder.itemView.post {
                onAttendanceToggle(holder.adapterPosition)
            }
        }
        
        // Handle card click to toggle attendance
        holder.cardStudent.setOnClickListener {
            // Clear listener temporarily
            holder.switchAttendance.setOnCheckedChangeListener(null)
            holder.switchAttendance.isChecked = !holder.switchAttendance.isChecked
            
            // Post the update to avoid modifying adapter during layout
            holder.itemView.post {
                onAttendanceToggle(holder.adapterPosition)
                
                // Restore listener
                holder.switchAttendance.setOnCheckedChangeListener { _, _ ->
                    holder.itemView.post {
                        onAttendanceToggle(holder.adapterPosition)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = students.size
} 