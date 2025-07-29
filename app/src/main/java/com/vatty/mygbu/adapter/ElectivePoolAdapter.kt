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
import com.vatty.mygbu.data.model.ElectivePool

class ElectivePoolAdapter(
    private val onViewCoursesClick: (ElectivePool) -> Unit,
    private val onEditClick: (ElectivePool) -> Unit,
    private val onDeleteClick: (ElectivePool) -> Unit
) : ListAdapter<ElectivePool, ElectivePoolAdapter.ElectivePoolViewHolder>(ElectivePoolDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectivePoolViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_elective_pool, parent, false)
        return ElectivePoolViewHolder(view)
    }

    override fun onBindViewHolder(holder: ElectivePoolViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ElectivePoolViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvPoolName: TextView = itemView.findViewById(R.id.tv_pool_name)
        private val tvPoolType: TextView = itemView.findViewById(R.id.tv_pool_type)
        private val tvTargetPrograms: TextView = itemView.findViewById(R.id.tv_target_programs)
        private val tvPoolDescription: TextView = itemView.findViewById(R.id.tv_pool_description)
        private val tvCourseCount: TextView = itemView.findViewById(R.id.tv_course_count)
        private val tvTotalCredits: TextView = itemView.findViewById(R.id.tv_total_credits)
        private val btnViewCourses: Button = itemView.findViewById(R.id.btn_view_courses)
        private val btnEdit: Button = itemView.findViewById(R.id.btn_edit)
        private val btnDelete: Button = itemView.findViewById(R.id.btn_delete)

        fun bind(electivePool: ElectivePool) {
            tvPoolName.text = electivePool.name
            tvPoolType.text = electivePool.type
            tvTargetPrograms.text = electivePool.targetPrograms.joinToString(", ")
            tvPoolDescription.text = electivePool.description
            tvCourseCount.text = "${electivePool.courses.size} Courses"
            tvTotalCredits.text = "${electivePool.totalCredits} Credits"

            btnViewCourses.setOnClickListener {
                onViewCoursesClick(electivePool)
            }

            btnEdit.setOnClickListener {
                onEditClick(electivePool)
            }

            btnDelete.setOnClickListener {
                onDeleteClick(electivePool)
            }
        }
    }

    private class ElectivePoolDiffCallback : DiffUtil.ItemCallback<ElectivePool>() {
        override fun areItemsTheSame(oldItem: ElectivePool, newItem: ElectivePool): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ElectivePool, newItem: ElectivePool): Boolean {
            return oldItem == newItem
        }
    }
} 