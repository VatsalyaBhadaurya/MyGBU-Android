package com.vatty.mygbu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vatty.mygbu.data.model.StudentAttendance
import com.vatty.mygbu.databinding.ItemStudentAttendanceBinding

class StudentsAttendanceAdapter(
    private var students: MutableList<StudentAttendance>,
    private val onAttendanceChanged: (Int, Boolean) -> Unit
) : RecyclerView.Adapter<StudentsAttendanceAdapter.StudentViewHolder>() {

    fun submitList(newStudents: List<StudentAttendance>) {
        students.clear()
        students.addAll(newStudents)
        notifyDataSetChanged()
    }

    fun markAll(present: Boolean) {
        students.forEach { it.updateAttendance(present) }
        notifyDataSetChanged()
    }

    fun getPresentCount(): Int {
        return students.count { it.isPresent }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val binding = ItemStudentAttendanceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(students[position])
    }

    override fun getItemCount() = students.size

    inner class StudentViewHolder(
        private val binding: ItemStudentAttendanceBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.switchAttendance.setOnCheckedChangeListener { _, isChecked ->
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    students[position].updateAttendance(isChecked)
                    onAttendanceChanged(position, isChecked)
                }
            }
        }

        fun bind(student: StudentAttendance) {
            binding.tvStudentName.text = student.name
            binding.tvRollNumber.text = student.rollNumber
            binding.switchAttendance.isChecked = student.isPresent
        }
    }
} 