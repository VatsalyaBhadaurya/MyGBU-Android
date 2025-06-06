package com.vatty.mygbu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vatty.mygbu.data.model.TimetableItem

class TimetableAdapter(
    private val timetableItems: List<TimetableItem>,
    private val onItemClick: (TimetableItem) -> Unit
) : RecyclerView.Adapter<TimetableAdapter.TimetableViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimetableViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_timetable, parent, false)
        return TimetableViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimetableViewHolder, position: Int) {
        val item = timetableItems[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    override fun getItemCount(): Int = timetableItems.size

    class TimetableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivIcon: ImageView = itemView.findViewById(R.id.iv_timetable_icon)
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_timetable_title)
        private val tvTime: TextView = itemView.findViewById(R.id.tv_timetable_time)

        fun bind(item: TimetableItem) {
            ivIcon.setImageResource(item.iconRes)
            tvTitle.text = item.title
            tvTime.text = item.time
        }
    }
} 