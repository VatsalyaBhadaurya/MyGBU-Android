package com.vatty.mygbu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class QuickActionsAdapter(
    private val actions: List<QuickAction>,
    private val onActionClick: (QuickAction) -> Unit
) : RecyclerView.Adapter<QuickActionsAdapter.QuickActionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuickActionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_quick_action, parent, false)
        return QuickActionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuickActionViewHolder, position: Int) {
        val action = actions[position]
        holder.bind(action)
    }

    override fun getItemCount(): Int = actions.size

    inner class QuickActionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardView: MaterialCardView = itemView.findViewById(R.id.card_quick_action)
        private val iconView: ImageView = itemView.findViewById(R.id.iv_action_icon)
        private val titleView: TextView = itemView.findViewById(R.id.tv_action_title)
        private val subtitleView: TextView = itemView.findViewById(R.id.tv_action_subtitle)

        fun bind(action: QuickAction) {
            titleView.text = action.title
            subtitleView.text = action.subtitle
            iconView.setImageResource(action.icon)

            // Set card background color
            cardView.setCardBackgroundColor(
                itemView.context.getColor(action.color)
            )

            // Handle click
            cardView.setOnClickListener {
                onActionClick(action)
            }

            // Add ripple effect animation
            cardView.isClickable = true
            cardView.isFocusable = true
        }
    }
} 