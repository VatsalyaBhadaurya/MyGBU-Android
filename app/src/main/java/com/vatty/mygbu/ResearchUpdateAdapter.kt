package com.vatty.mygbu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ResearchUpdateAdapter(
    private val updates: List<ResearchUpdate>,
    private val onItemClick: (ResearchUpdate) -> Unit
) : RecyclerView.Adapter<ResearchUpdateAdapter.ResearchUpdateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResearchUpdateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_course, parent, false) // Using course item as placeholder
        return ResearchUpdateViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResearchUpdateViewHolder, position: Int) {
        val update = updates[position]
        holder.bind(update)
        holder.itemView.setOnClickListener { onItemClick(update) }
    }

    override fun getItemCount(): Int = updates.size

    class ResearchUpdateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivIcon: ImageView = itemView.findViewById(R.id.iv_course_icon)
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_course_title)
        private val tvSemester: TextView = itemView.findViewById(R.id.tv_course_semester)

        fun bind(update: ResearchUpdate) {
            ivIcon.setImageResource(update.iconRes)
            tvTitle.text = update.title
            tvSemester.text = update.description
        }
    }
} 