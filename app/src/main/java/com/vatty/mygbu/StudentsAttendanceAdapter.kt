package com.vatty.mygbu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vatty.mygbu.data.model.StudentAttendance
import com.vatty.mygbu.databinding.ItemStudentAttendanceBinding

class StudentsAttendanceAdapter(
    private val studentsList: List<StudentAttendance>,
    private val onAttendanceChanged: (Int, Boolean) -> Unit
) : RecyclerView.Adapter<StudentsAttendanceAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemStudentAttendanceBinding) : RecyclerView.ViewHolder(binding.root) {
        private var isInitializing = false

        init {
            binding.switchAttendance.setOnCheckedChangeListener { _, isChecked ->
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION && !isInitializing) {
                    onAttendanceChanged(position, isChecked)
                }
            }
        }

        fun bind(student: StudentAttendance) {
            isInitializing = true
            binding.tvStudentName.text = student.name
            binding.tvRollNumber.text = student.rollNumber
            binding.switchAttendance.isChecked = student.isPresent
            isInitializing = false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStudentAttendanceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(studentsList[position])
    }

    override fun getItemCount() = studentsList.size
} 