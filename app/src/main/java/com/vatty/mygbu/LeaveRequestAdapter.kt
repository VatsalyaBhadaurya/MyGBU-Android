package com.vatty.mygbu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LeaveRequestAdapter(
    private val requests: List<LeaveRequest>,
    private val onItemClick: (LeaveRequest) -> Unit
) : RecyclerView.Adapter<LeaveRequestAdapter.LeaveRequestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaveRequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_course, parent, false) // Using course item as placeholder
        return LeaveRequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: LeaveRequestViewHolder, position: Int) {
        val request = requests[position]
        holder.bind(request)
        holder.itemView.setOnClickListener { onItemClick(request) }
    }

    override fun getItemCount(): Int = requests.size

    class LeaveRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivIcon: ImageView = itemView.findViewById(R.id.iv_course_icon)
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_course_title)
        private val tvSemester: TextView = itemView.findViewById(R.id.tv_course_semester)

        fun bind(request: LeaveRequest) {
            ivIcon.setImageResource(request.avatarRes)
            tvTitle.text = request.studentName
            tvSemester.text = "Reason: ${request.reason}"
        }
    }
} 