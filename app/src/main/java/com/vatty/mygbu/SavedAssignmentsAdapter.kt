package com.vatty.mygbu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class SavedAssignmentsAdapter(
    private val assignments: List<SavedAssignment>
) : RecyclerView.Adapter<SavedAssignmentsAdapter.AssignmentViewHolder>() {

    class AssignmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tv_assignment_title)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_assignment_description)
        val tvDueDate: TextView = itemView.findViewById(R.id.tv_due_date)
        val tvAttachedFile: TextView = itemView.findViewById(R.id.tv_attached_file)
        val tvStatus: TextView = itemView.findViewById(R.id.tv_status)
        val tvCourseCode: TextView = itemView.findViewById(R.id.tv_course_code)
        val cardAssignment: MaterialCardView = itemView.findViewById(R.id.card_assignment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssignmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_saved_assignment, parent, false)
        return AssignmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AssignmentViewHolder, position: Int) {
        val assignment = assignments[position]
        
        holder.tvTitle.text = assignment.title
        holder.tvDescription.text = assignment.description
        holder.tvDueDate.text = "Due: ${assignment.dueDate}"
        holder.tvAttachedFile.text = "📎 ${assignment.attachedFile}"
        holder.tvStatus.text = assignment.status
        holder.tvCourseCode.text = assignment.courseCode
        
        // Set status color
        val statusColor = when (assignment.status) {
            "Published" -> R.color.success_green
            "Draft" -> R.color.warning_orange
            else -> R.color.text_secondary
        }
        holder.tvStatus.setTextColor(holder.itemView.context.getColor(statusColor))
        
        holder.cardAssignment.setOnClickListener {
            // Handle assignment click (edit/view)
        }
    }

    override fun getItemCount(): Int = assignments.size
} 