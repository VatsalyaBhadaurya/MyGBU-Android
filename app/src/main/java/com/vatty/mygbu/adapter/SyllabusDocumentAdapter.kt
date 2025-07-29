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
import com.vatty.mygbu.data.model.SyllabusDocument
import java.text.SimpleDateFormat
import java.util.*

class SyllabusDocumentAdapter(
    private val onViewClick: (SyllabusDocument) -> Unit,
    private val onDownloadClick: (SyllabusDocument) -> Unit,
    private val onDeleteClick: (SyllabusDocument) -> Unit
) : ListAdapter<SyllabusDocument, SyllabusDocumentAdapter.SyllabusDocumentViewHolder>(SyllabusDocumentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SyllabusDocumentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_syllabus_document, parent, false)
        return SyllabusDocumentViewHolder(view)
    }

    override fun onBindViewHolder(holder: SyllabusDocumentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SyllabusDocumentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvSyllabusTitle: TextView = itemView.findViewById(R.id.tv_syllabus_title)
        private val tvCourseName: TextView = itemView.findViewById(R.id.tv_course_name)
        private val tvVersionDate: TextView = itemView.findViewById(R.id.tv_version_date)
        private val tvFileSize: TextView = itemView.findViewById(R.id.tv_file_size)
        private val tvFileType: TextView = itemView.findViewById(R.id.tv_file_type)
        private val btnView: Button = itemView.findViewById(R.id.btn_view)
        private val btnDownload: Button = itemView.findViewById(R.id.btn_download)
        private val btnDelete: Button = itemView.findViewById(R.id.btn_delete)

        fun bind(syllabusDocument: SyllabusDocument) {
            tvSyllabusTitle.text = syllabusDocument.title
            tvCourseName.text = syllabusDocument.courseName
            
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            tvVersionDate.text = "Version ${syllabusDocument.version} - ${dateFormat.format(syllabusDocument.uploadedDate)}"
            
            tvFileSize.text = syllabusDocument.fileSize
            tvFileType.text = syllabusDocument.fileType

            btnView.setOnClickListener {
                onViewClick(syllabusDocument)
            }

            btnDownload.setOnClickListener {
                onDownloadClick(syllabusDocument)
            }

            btnDelete.setOnClickListener {
                onDeleteClick(syllabusDocument)
            }
        }
    }

    private class SyllabusDocumentDiffCallback : DiffUtil.ItemCallback<SyllabusDocument>() {
        override fun areItemsTheSame(oldItem: SyllabusDocument, newItem: SyllabusDocument): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SyllabusDocument, newItem: SyllabusDocument): Boolean {
            return oldItem == newItem
        }
    }
} 