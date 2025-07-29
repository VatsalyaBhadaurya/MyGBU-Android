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
import com.vatty.mygbu.data.model.CreditRule

class CreditRuleAdapter(
    private val onEditClick: (CreditRule) -> Unit,
    private val onDeleteClick: (CreditRule) -> Unit
) : ListAdapter<CreditRule, CreditRuleAdapter.CreditRuleViewHolder>(CreditRuleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditRuleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_credit_rule, parent, false)
        return CreditRuleViewHolder(view)
    }

    override fun onBindViewHolder(holder: CreditRuleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CreditRuleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvProgramName: TextView = itemView.findViewById(R.id.tv_program_name)
        private val tvBatchYear: TextView = itemView.findViewById(R.id.tv_batch_year)
        private val tvTotalCredits: TextView = itemView.findViewById(R.id.tv_total_credits)
        private val tvStatus: TextView = itemView.findViewById(R.id.tv_status)
        private val tvCoreCredits: TextView = itemView.findViewById(R.id.tv_core_credits)
        private val tvElectiveCredits: TextView = itemView.findViewById(R.id.tv_elective_credits)
        private val tvLabCredits: TextView = itemView.findViewById(R.id.tv_lab_credits)
        private val btnEdit: Button = itemView.findViewById(R.id.btn_edit)
        private val btnDelete: Button = itemView.findViewById(R.id.btn_delete)

        fun bind(creditRule: CreditRule) {
            tvProgramName.text = creditRule.programName
            tvBatchYear.text = "Batch ${creditRule.batchYear}"
            tvTotalCredits.text = "Total: ${creditRule.totalCreditsRequired}"
            tvStatus.text = if (creditRule.isActive) "ACTIVE" else "INACTIVE"
            tvCoreCredits.text = creditRule.coreCreditsRequired.toString()
            tvElectiveCredits.text = creditRule.electiveCreditsRequired.toString()
            tvLabCredits.text = creditRule.labCreditsRequired.toString()

            btnEdit.setOnClickListener {
                onEditClick(creditRule)
            }

            btnDelete.setOnClickListener {
                onDeleteClick(creditRule)
            }
        }
    }

    private class CreditRuleDiffCallback : DiffUtil.ItemCallback<CreditRule>() {
        override fun areItemsTheSame(oldItem: CreditRule, newItem: CreditRule): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CreditRule, newItem: CreditRule): Boolean {
            return oldItem == newItem
        }
    }
} 