package com.vatty.mygbu.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.vatty.mygbu.R
import com.vatty.mygbu.adapter.SyllabusDocumentAdapter
import com.vatty.mygbu.data.model.SyllabusDocument
import com.vatty.mygbu.viewmodel.AdminDashboardViewModel
import java.util.*

class SyllabusManagementFragment : Fragment() {
    
    private lateinit var viewModel: AdminDashboardViewModel
    private lateinit var syllabusDocumentAdapter: SyllabusDocumentAdapter
    
    private lateinit var spinnerCourse: AutoCompleteTextView
    private lateinit var etSyllabusTitle: TextInputEditText
    private lateinit var etSyllabusDescription: TextInputEditText
    private lateinit var etSyllabusVersion: TextInputEditText
    private lateinit var btnChooseFile: Button
    private lateinit var btnUploadSyllabus: Button
    private lateinit var tvSelectedFile: TextView
    private lateinit var rvSyllabusDocuments: RecyclerView
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_syllabus_management, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewModel = ViewModelProvider(requireActivity())[AdminDashboardViewModel::class.java]
        
        initializeViews(view)
        setupRecyclerView()
        setupSpinners()
        setupClickListeners()
        observeData()
        
        // Load initial data
        viewModel.loadSyllabusDocuments()
    }
    
    private fun initializeViews(view: View) {
        spinnerCourse = view.findViewById(R.id.spinner_syllabus_course)
        etSyllabusTitle = view.findViewById(R.id.et_syllabus_title)
        etSyllabusDescription = view.findViewById(R.id.et_syllabus_description)
        etSyllabusVersion = view.findViewById(R.id.et_syllabus_version)
        btnChooseFile = view.findViewById(R.id.btn_choose_file)
        btnUploadSyllabus = view.findViewById(R.id.btn_upload_syllabus)
        tvSelectedFile = view.findViewById(R.id.tv_selected_file)
        rvSyllabusDocuments = view.findViewById(R.id.rv_syllabus_documents)
    }
    
    private fun setupRecyclerView() {
        syllabusDocumentAdapter = SyllabusDocumentAdapter(
            onViewClick = { syllabusDocument ->
                Toast.makeText(context, "View: ${syllabusDocument.title}", Toast.LENGTH_SHORT).show()
            },
            onDownloadClick = { syllabusDocument ->
                Toast.makeText(context, "Download: ${syllabusDocument.title}", Toast.LENGTH_SHORT).show()
            },
            onDeleteClick = { syllabusDocument ->
                viewModel.deleteSyllabus(
                    syllabusDocument.id,
                    onSuccess = {
                        Toast.makeText(context, "Syllabus deleted", Toast.LENGTH_SHORT).show()
                    },
                    onError = { error ->
                        Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        )
        
        rvSyllabusDocuments.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = syllabusDocumentAdapter
        }
    }
    
    private fun setupSpinners() {
        // Course Spinner (mock data)
        val courses = listOf(
            "Introduction to Computer Science",
            "Data Structures and Algorithms",
            "Database Management Systems",
            "Machine Learning",
            "Web Development",
            "Mobile App Development"
        )
        val courseAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, courses)
        spinnerCourse.setAdapter(courseAdapter)
    }
    
    private fun setupClickListeners() {
        btnChooseFile.setOnClickListener {
            // Mock file selection
            tvSelectedFile.text = "syllabus_document.pdf (2.5 MB)"
        }
        
        btnUploadSyllabus.setOnClickListener {
            uploadSyllabus()
        }
    }
    
    private fun observeData() {
        viewModel.syllabusDocuments.observe(viewLifecycleOwner) { syllabusDocuments ->
            syllabusDocumentAdapter.submitList(syllabusDocuments)
        }
    }
    
    private fun uploadSyllabus() {
        val course = spinnerCourse.text.toString()
        val title = etSyllabusTitle.text.toString()
        val description = etSyllabusDescription.text.toString()
        val version = etSyllabusVersion.text.toString()
        val selectedFile = tvSelectedFile.text.toString()
        
        if (course.isEmpty() || title.isEmpty() || description.isEmpty() || version.isEmpty() || selectedFile == "No file selected") {
            Toast.makeText(context, "Please fill all fields and select a file", Toast.LENGTH_SHORT).show()
            return
        }
        
        val syllabusDocument = SyllabusDocument(
            id = UUID.randomUUID().toString(),
            courseId = course,
            courseName = course,
            title = title,
            description = description,
            version = version,
            filePath = "/syllabus/${title.lowercase().replace(" ", "_")}.pdf",
            fileSize = "2.5 MB",
            fileType = "PDF",
            uploadedDate = Date(),
            uploadedBy = "Admin User",
            isActive = true
        )
        
        viewModel.uploadSyllabus(
            syllabusDocument,
            onSuccess = {
                Toast.makeText(context, "Syllabus uploaded successfully", Toast.LENGTH_SHORT).show()
                clearForm()
            },
            onError = { error ->
                Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
            }
        )
    }
    
    private fun clearForm() {
        spinnerCourse.text.clear()
        etSyllabusTitle.text?.clear()
        etSyllabusDescription.text?.clear()
        etSyllabusVersion.text?.clear()
        tvSelectedFile.text = "No file selected"
    }
} 