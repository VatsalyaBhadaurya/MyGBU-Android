package com.vatty.mygbu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vatty.mygbu.data.model.SavedAssignment
import com.vatty.mygbu.databinding.ItemSavedAssignmentBinding
import com.vatty.mygbu.enums.AssignmentStatus

class SavedAssignmentsAdapter(
    private val assignments: List<SavedAssignment>
) : RecyclerView.Adapter<SavedAssignmentsAdapter.AssignmentViewHolder>() {

    class AssignmentViewHolder(
        private val binding: ItemSavedAssignmentBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(assignment: SavedAssignment) {
            binding.tvAssignmentTitle.text = assignment.title
            binding.tvDueDate.text = "Due: ${assignment.dueDate}"
            binding.tvCourseCode.text = assignment.courseCode
            binding.tvStatus.text = assignment.status.displayName
            
            // Handle attached file display
            assignment.attachmentName?.let { fileName ->
                binding.tvAttachedFile.text = "📎 $fileName"
            } ?: run {
                binding.tvAttachedFile.text = "No attachment"
            }
            
            // Set status color using enum
            val statusColor = ContextCompat.getColor(itemView.context, assignment.status.colorRes)
            binding.tvStatus.setTextColor(statusColor)
            
            binding.cardAssignment.setOnClickListener {
                // Handle assignment click (edit/view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssignmentViewHolder {
        val binding = ItemSavedAssignmentBinding.inflate(
            LayoutInflater.from(parent.context), 
            parent, 
            false
        )
        return AssignmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AssignmentViewHolder, position: Int) {
        holder.bind(assignments[position])
    }

    override fun getItemCount(): Int = assignments.size
}