package com.vatty.mygbu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.vatty.mygbu.data.model.StudentAttendance
import com.vatty.mygbu.enums.AttendanceStatus

class StudentsAttendanceAdapter(
    private val studentsList: List<StudentAttendance>,
    private val onAttendanceChanged: (Int, AttendanceStatus) -> Unit
) : RecyclerView.Adapter<StudentsAttendanceAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvStudentName: TextView = itemView.findViewById(R.id.tv_student_name)
        private val tvRollNumber: TextView = itemView.findViewById(R.id.tv_roll_number)
        private val tvAttendancePercentage: TextView = itemView.findViewById(R.id.tv_attendance_percentage)
        private val tvCgpa: TextView = itemView.findViewById(R.id.tv_cgpa)
        private val tvClassesAttended: TextView = itemView.findViewById(R.id.tv_classes_attended)
        private val progressBar: android.widget.ProgressBar = itemView.findViewById(R.id.progress_bar)
        private val btnPresent: MaterialButton = itemView.findViewById(R.id.btn_present)
        private val btnLate: MaterialButton = itemView.findViewById(R.id.btn_late)
        private val btnAbsent: MaterialButton = itemView.findViewById(R.id.btn_absent)
        
        init {
            // Set click listeners for attendance buttons
            btnPresent.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    updateButtonStates(AttendanceStatus.PRESENT)
                    onAttendanceChanged(position, AttendanceStatus.PRESENT)
                }
            }
            
            btnLate.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    updateButtonStates(AttendanceStatus.LATE)
                    onAttendanceChanged(position, AttendanceStatus.LATE)
                }
            }
            
            btnAbsent.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    updateButtonStates(AttendanceStatus.ABSENT)
                    onAttendanceChanged(position, AttendanceStatus.ABSENT)
                }
            }
        }

        fun bind(student: StudentAttendance) {
            tvStudentName.text = student.name
            tvRollNumber.text = "Roll No: ${student.rollNumber}"
            
            // Set attendance percentage (mock data - in real app, this would come from database)
            val attendancePercentage = 85.5
            tvAttendancePercentage.text = "${attendancePercentage}%"
            
            // Set CGPA (mock data - in real app, this would come from database)
            val cgpa = 8.75
            tvCgpa.text = cgpa.toString()
            
            // Set classes attended (mock data - in real app, this would come from database)
            val classesAttended = 17
            val totalClasses = 20
            tvClassesAttended.text = "$classesAttended/$totalClasses classes"
            
            // Set progress bar
            val progress = ((classesAttended.toFloat() / totalClasses) * 100).toInt()
            progressBar.progress = progress
            
            // Update button states based on current attendance status
            updateButtonStates(student.status)
        }
        
        private fun updateButtonStates(selectedStatus: AttendanceStatus) {
            // Reset all buttons to default state
            btnPresent.backgroundTintList = null
            btnLate.backgroundTintList = null
            btnAbsent.backgroundTintList = null
            
            btnPresent.setTextColor(itemView.context.getColor(R.color.text_secondary))
            btnLate.setTextColor(itemView.context.getColor(R.color.text_secondary))
            btnAbsent.setTextColor(itemView.context.getColor(R.color.text_secondary))
            
            // Highlight the selected button
            when (selectedStatus) {
                AttendanceStatus.PRESENT -> {
                    btnPresent.backgroundTintList = itemView.context.getColorStateList(R.color.success_green)
                    btnPresent.setTextColor(itemView.context.getColor(R.color.white))
                }
                AttendanceStatus.LATE -> {
                    btnLate.backgroundTintList = itemView.context.getColorStateList(R.color.warning_orange)
                    btnLate.setTextColor(itemView.context.getColor(R.color.white))
                }
                AttendanceStatus.ABSENT -> {
                    btnAbsent.backgroundTintList = itemView.context.getColorStateList(R.color.university_secondary)
                    btnAbsent.setTextColor(itemView.context.getColor(R.color.white))
                }
                else -> {
                    // Default to present for any other status
                    btnPresent.backgroundTintList = itemView.context.getColorStateList(R.color.success_green)
                    btnPresent.setTextColor(itemView.context.getColor(R.color.white))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student_attendance, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(studentsList[position])
    }

    override fun getItemCount() = studentsList.size
} 