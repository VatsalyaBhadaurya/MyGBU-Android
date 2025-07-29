package com.vatty.mygbu.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.vatty.mygbu.R
import com.vatty.mygbu.adapter.CourseMappingAdapter
import com.vatty.mygbu.data.model.CourseMapping
import com.vatty.mygbu.data.model.Program
import com.vatty.mygbu.data.model.CourseType
import com.vatty.mygbu.viewmodel.AdminDashboardViewModel
import java.util.*

class CourseMappingFragment : Fragment() {
    
    private lateinit var viewModel: AdminDashboardViewModel
    private lateinit var courseMappingAdapter: CourseMappingAdapter
    
    private lateinit var spinnerProgram: AutoCompleteTextView
    private lateinit var spinnerSemester: AutoCompleteTextView
    private lateinit var spinnerCourse: AutoCompleteTextView
    private lateinit var spinnerCourseType: AutoCompleteTextView
    private lateinit var etCredits: TextInputEditText
    private lateinit var btnAddMapping: Button
    private lateinit var rvCourseMappings: RecyclerView
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_course_mapping, container, false)
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
        viewModel.loadCourseMappings()
        viewModel.loadPrograms()
    }
    
    private fun initializeViews(view: View) {
        spinnerProgram = view.findViewById(R.id.spinner_program)
        spinnerSemester = view.findViewById(R.id.spinner_semester)
        spinnerCourse = view.findViewById(R.id.spinner_course)
        spinnerCourseType = view.findViewById(R.id.spinner_course_type)
        etCredits = view.findViewById(R.id.et_credits)
        btnAddMapping = view.findViewById(R.id.btn_add_mapping)
        rvCourseMappings = view.findViewById(R.id.rv_course_mappings)
    }
    
    private fun setupRecyclerView() {
        courseMappingAdapter = CourseMappingAdapter(
            onEditClick = { courseMapping ->
                // Handle edit click
                Toast.makeText(context, "Edit: ${courseMapping.courseName}", Toast.LENGTH_SHORT).show()
            },
            onDeleteClick = { courseMapping ->
                // Handle delete click
                viewModel.deleteCourseMapping(
                    courseMapping.id,
                    onSuccess = {
                        Toast.makeText(context, "Course mapping deleted", Toast.LENGTH_SHORT).show()
                    },
                    onError = { error ->
                        Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        )
        
        rvCourseMappings.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = courseMappingAdapter
        }
    }
    
    private fun setupSpinners() {
        // Course Type Spinner
        val courseTypes = CourseType.values().map { it.name }
        val courseTypeAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line, courseTypes)
        spinnerCourseType.setAdapter(courseTypeAdapter)
        
        // Semester Spinner
        val semesters = (1..8).map { it.toString() }
        val semesterAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line, semesters)
        spinnerSemester.setAdapter(semesterAdapter)
        
        // Course Spinner (mock data)
        val courses = listOf(
            "Introduction to Computer Science",
            "Data Structures and Algorithms",
            "Database Management Systems",
            "Machine Learning",
            "Web Development",
            "Mobile App Development"
        )
        val courseAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line, courses)
        spinnerCourse.setAdapter(courseAdapter)
    }
    
    private fun setupClickListeners() {
        btnAddMapping.setOnClickListener {
            addCourseMapping()
        }
    }
    
    private fun observeData() {
        viewModel.courseMappings.observe(viewLifecycleOwner) { courseMappings ->
            courseMappingAdapter.submitList(courseMappings)
        }
        
        viewModel.programs.observe(viewLifecycleOwner) { programs ->
            val programNames = programs.map { it.name }
            val programAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line, programNames)
            spinnerProgram.setAdapter(programAdapter)
        }
    }
    
    private fun addCourseMapping() {
        val program = spinnerProgram.text.toString()
        val semester = spinnerSemester.text.toString()
        val course = spinnerCourse.text.toString()
        val courseType = spinnerCourseType.text.toString()
        val credits = etCredits.text.toString()
        
        if (program.isEmpty() || semester.isEmpty() || course.isEmpty() || courseType.isEmpty() || credits.isEmpty()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }
        
        val courseMapping = CourseMapping(
            id = UUID.randomUUID().toString(),
            courseId = "CS${System.currentTimeMillis()}",
            courseName = course,
            programId = program,
            programName = program,
            semester = semester.toInt(),
            courseType = CourseType.valueOf(courseType),
            credits = credits.toInt(),
            isActive = true,
            syllabusPath = null,
            prerequisites = emptyList()
        )
        
        viewModel.addCourseMapping(
            courseMapping,
            onSuccess = {
                Toast.makeText(context, "Course mapping added successfully", Toast.LENGTH_SHORT).show()
                clearForm()
            },
            onError = { error ->
                Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
            }
        )
    }
    
    private fun clearForm() {
        spinnerProgram.text.clear()
        spinnerSemester.text.clear()
        spinnerCourse.text.clear()
        spinnerCourseType.text.clear()
        etCredits.text?.clear()
    }
} 