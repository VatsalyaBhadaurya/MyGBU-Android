package com.vatty.mygbu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CourseAdapter(
    private val courses: List<Course>,
    private val onItemClick: (Course) -> Unit
) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_course, parent, false)
        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courses[position]
        holder.bind(course)
        holder.itemView.setOnClickListener { onItemClick(course) }
    }

    override fun getItemCount(): Int = courses.size

    class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivIcon: ImageView = itemView.findViewById(R.id.iv_course_icon)
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_course_title)
        private val tvSemester: TextView = itemView.findViewById(R.id.tv_course_semester)

        fun bind(course: Course) {
            ivIcon.setImageResource(course.iconRes)
            tvTitle.text = course.title
            tvSemester.text = course.semester
        }
    }
} 