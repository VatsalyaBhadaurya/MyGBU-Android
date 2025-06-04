package com.vatty.mygbu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class ScheduleAdapter(
    private val scheduleItems: List<ScheduleItem>
) : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDay: TextView = itemView.findViewById(R.id.tv_day)
        val rvTimeSlots: RecyclerView = itemView.findViewById(R.id.rv_time_slots)
        val cardSchedule: MaterialCardView = itemView.findViewById(R.id.card_schedule)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_schedule_day, parent, false)
        return ScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val scheduleItem = scheduleItems[position]
        
        holder.tvDay.text = scheduleItem.day
        
        // Setup nested RecyclerView for time slots
        val timeSlotsAdapter = TimeSlotsAdapter(scheduleItem.timeSlots)
        holder.rvTimeSlots.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.rvTimeSlots.adapter = timeSlotsAdapter
        
        // Different colors for different days
        val dayColors = listOf(
            R.color.card_courses,
            R.color.card_students,
            R.color.card_attendance,
            R.color.card_grades,
            R.color.card_schedule,
            R.color.card_announcements
        )
        
        val colorIndex = position % dayColors.size
        holder.cardSchedule.setCardBackgroundColor(
            holder.itemView.context.getColor(dayColors[colorIndex])
        )
    }

    override fun getItemCount(): Int = scheduleItems.size
}

class TimeSlotsAdapter(
    private val timeSlots: List<TimeSlot>
) : RecyclerView.Adapter<TimeSlotsAdapter.TimeSlotViewHolder>() {

    class TimeSlotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTime: TextView = itemView.findViewById(R.id.tv_time)
        val tvSubject: TextView = itemView.findViewById(R.id.tv_subject)
        val tvLocation: TextView = itemView.findViewById(R.id.tv_location)
        val tvType: TextView = itemView.findViewById(R.id.tv_type)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeSlotViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_time_slot, parent, false)
        return TimeSlotViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimeSlotViewHolder, position: Int) {
        val timeSlot = timeSlots[position]
        
        holder.tvTime.text = timeSlot.time
        holder.tvSubject.text = timeSlot.subject
        holder.tvLocation.text = timeSlot.location
        holder.tvType.text = timeSlot.type
        
        // Set type color
        val typeColor = when (timeSlot.type) {
            "Lecture" -> R.color.primary
            "Practical" -> R.color.success_green
            "Tutorial" -> R.color.warning_orange
            "Meeting" -> R.color.error_red
            else -> R.color.text_secondary
        }
        holder.tvType.setTextColor(holder.itemView.context.getColor(typeColor))
    }

    override fun getItemCount(): Int = timeSlots.size
} 