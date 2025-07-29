package com.vatty.mygbu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vatty.mygbu.R
import com.vatty.mygbu.data.model.CourseMapping

class CourseMappingAdapter(
    private val onEditClick: (CourseMapping) -> Unit,
    private val onDeleteClick: (CourseMapping) -> Unit
) : ListAdapter<CourseMapping, CourseMappingAdapter.CourseMappingViewHolder>(CourseMappingDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseMappingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_course_mapping, parent, false)
        return CourseMappingViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseMappingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CourseMappingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCourseName: TextView = itemView.findViewById(R.id.tv_course_name)
        private val tvProgramSemester: TextView = itemView.findViewById(R.id.tv_program_semester)
        private val tvCourseType: TextView = itemView.findViewById(R.id.tv_course_type)
        private val tvCredits: TextView = itemView.findViewById(R.id.tv_credits)
        private val btnEdit: Button = itemView.findViewById(R.id.btn_edit)
        private val btnDelete: Button = itemView.findViewById(R.id.btn_delete)

        fun bind(courseMapping: CourseMapping) {
            tvCourseName.text = courseMapping.courseName
            tvProgramSemester.text = "${courseMapping.programName} - Semester ${courseMapping.semester}"
            tvCourseType.text = courseMapping.courseType.name
            tvCredits.text = "${courseMapping.credits} Credits"

            btnEdit.setOnClickListener {
                onEditClick(courseMapping)
            }

            btnDelete.setOnClickListener {
                onDeleteClick(courseMapping)
            }
        }
    }

    private class CourseMappingDiffCallback : DiffUtil.ItemCallback<CourseMapping>() {
        override fun areItemsTheSame(oldItem: CourseMapping, newItem: CourseMapping): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CourseMapping, newItem: CourseMapping): Boolean {
            return oldItem == newItem
        }
    }
} 