package com.vatty.mygbu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class LeaveRequestAdapter(
    private val requests: List<LeaveRequest>,
    private val onItemClick: (LeaveRequest) -> Unit,
    private val onApprovalChange: ((LeaveRequest, Boolean) -> Unit)? = null
) : RecyclerView.Adapter<LeaveRequestAdapter.LeaveRequestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaveRequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_leave_request, parent, false)
        return LeaveRequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: LeaveRequestViewHolder, position: Int) {
        val request = requests[position]
        holder.bind(request)
        holder.itemView.setOnClickListener { onItemClick(request) }
    }

    override fun getItemCount(): Int = requests.size

    inner class LeaveRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivStudentAvatar: ImageView = itemView.findViewById(R.id.iv_student_avatar)
        private val tvStudentName: TextView = itemView.findViewById(R.id.tv_student_name)
        private val tvLeaveReason: TextView = itemView.findViewById(R.id.tv_leave_reason)
        private val tvLeaveDates: TextView = itemView.findViewById(R.id.tv_leave_dates)
        private val tvStatus: TextView = itemView.findViewById(R.id.tv_status)
        private val cbApprove: CheckBox = itemView.findViewById(R.id.cb_approve)
        private val cardLeaveRequest: MaterialCardView = itemView.findViewById(R.id.card_leave_request)

        fun bind(request: LeaveRequest) {
            ivStudentAvatar.setImageResource(request.avatarRes)
            tvStudentName.text = request.studentName
            tvLeaveReason.text = request.reason
            tvLeaveDates.text = request.dates ?: "Dec 21-23, 2024"
            tvStatus.text = request.status.uppercase()
            
            // Show/hide checkbox based on status
            if (request.status == "pending") {
                cbApprove.visibility = View.VISIBLE
                cbApprove.setOnCheckedChangeListener { _, isChecked ->
                    onApprovalChange?.invoke(request, isChecked)
                }
                
                // Set card background for pending requests
                cardLeaveRequest.setCardBackgroundColor(
                    itemView.context.getColor(R.color.warning_light)
                )
                tvStatus.setTextColor(itemView.context.getColor(R.color.warning_orange))
            } else {
                cbApprove.visibility = View.GONE
                
                // Set card background for approved requests
                cardLeaveRequest.setCardBackgroundColor(
                    itemView.context.getColor(R.color.success_light)
                )
                tvStatus.setTextColor(itemView.context.getColor(R.color.success_green))
            }
        }
    }
} 