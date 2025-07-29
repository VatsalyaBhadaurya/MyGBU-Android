package com.vatty.mygbu.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.vatty.mygbu.R
import com.vatty.mygbu.adapter.ElectivePoolAdapter
import com.vatty.mygbu.data.model.ElectivePool
import com.vatty.mygbu.data.model.ElectivePoolCourse
import com.vatty.mygbu.viewmodel.AdminDashboardViewModel
import java.util.*

class ElectivePoolFragment : Fragment() {
    
    private lateinit var viewModel: AdminDashboardViewModel
    private lateinit var electivePoolAdapter: ElectivePoolAdapter
    
    private lateinit var etPoolName: TextInputEditText
    private lateinit var spinnerPoolType: AutoCompleteTextView
    private lateinit var etTargetPrograms: TextInputEditText
    private lateinit var etPoolDescription: TextInputEditText
    private lateinit var btnAddPool: Button
    private lateinit var spinnerSelectPool: AutoCompleteTextView
    private lateinit var spinnerPoolCourse: AutoCompleteTextView
    private lateinit var btnAddCourseToPool: Button
    private lateinit var rvElectivePools: RecyclerView
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_elective_pool, container, false)
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
        viewModel.loadElectivePools()
    }
    
    private fun initializeViews(view: View) {
        etPoolName = view.findViewById(R.id.et_pool_name)
        spinnerPoolType = view.findViewById(R.id.spinner_pool_type)
        etTargetPrograms = view.findViewById(R.id.et_target_programs)
        etPoolDescription = view.findViewById(R.id.et_pool_description)
        btnAddPool = view.findViewById(R.id.btn_add_pool)
        spinnerSelectPool = view.findViewById(R.id.spinner_select_pool)
        spinnerPoolCourse = view.findViewById(R.id.spinner_pool_course)
        btnAddCourseToPool = view.findViewById(R.id.btn_add_course_to_pool)
        rvElectivePools = view.findViewById(R.id.rv_elective_pools)
    }
    
    private fun setupRecyclerView() {
        electivePoolAdapter = ElectivePoolAdapter(
            onViewCoursesClick = { electivePool ->
                Toast.makeText(context, "View courses for: ${electivePool.name}", Toast.LENGTH_SHORT).show()
            },
            onEditClick = { electivePool ->
                Toast.makeText(context, "Edit: ${electivePool.name}", Toast.LENGTH_SHORT).show()
            },
            onDeleteClick = { electivePool ->
                viewModel.deleteElectivePool(
                    electivePool.id,
                    onSuccess = {
                        Toast.makeText(context, "Elective pool deleted", Toast.LENGTH_SHORT).show()
                    },
                    onError = { error ->
                        Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        )
        
        rvElectivePools.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = electivePoolAdapter
        }
    }
    
    private fun setupSpinners() {
        // Pool Type Spinner
        val poolTypes = listOf("INTERDISCIPLINARY", "OPEN_ELECTIVE")
        val poolTypeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, poolTypes)
        spinnerPoolType.setAdapter(poolTypeAdapter)
        
        // Course Spinner (mock data)
        val courses = listOf(
            "Machine Learning",
            "Deep Learning",
            "Natural Language Processing",
            "Computer Vision",
            "Business Communication",
            "Environmental Science",
            "Digital Marketing"
        )
        val courseAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, courses)
        spinnerPoolCourse.setAdapter(courseAdapter)
    }
    
    private fun setupClickListeners() {
        btnAddPool.setOnClickListener {
            addElectivePool()
        }
        
        btnAddCourseToPool.setOnClickListener {
            addCourseToPool()
        }
    }
    
    private fun observeData() {
        viewModel.electivePools.observe(viewLifecycleOwner) { electivePools ->
            electivePoolAdapter.submitList(electivePools)
            
            // Update pool selection spinner
            val poolNames = electivePools.map { it.name }
            val poolAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, poolNames)
            spinnerSelectPool.setAdapter(poolAdapter)
        }
    }
    
    private fun addElectivePool() {
        val poolName = etPoolName.text.toString()
        val poolType = spinnerPoolType.text.toString()
        val targetPrograms = etTargetPrograms.text.toString()
        val description = etPoolDescription.text.toString()
        
        if (poolName.isEmpty() || poolType.isEmpty() || targetPrograms.isEmpty() || description.isEmpty()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }
        
        val electivePool = ElectivePool(
            id = UUID.randomUUID().toString(),
            name = poolName,
            type = poolType,
            description = description,
            targetPrograms = targetPrograms.split(",").map { it.trim() },
            courses = emptyList(),
            totalCredits = 0,
            isActive = true,
            createdDate = Date()
        )
        
        viewModel.addElectivePool(
            electivePool,
            onSuccess = {
                Toast.makeText(context, "Elective pool added successfully", Toast.LENGTH_SHORT).show()
                clearPoolForm()
            },
            onError = { error ->
                Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
            }
        )
    }
    
    private fun addCourseToPool() {
        val selectedPool = spinnerSelectPool.text.toString()
        val selectedCourse = spinnerPoolCourse.text.toString()
        
        if (selectedPool.isEmpty() || selectedCourse.isEmpty()) {
            Toast.makeText(context, "Please select pool and course", Toast.LENGTH_SHORT).show()
            return
        }
        
        val course = ElectivePoolCourse(
            id = UUID.randomUUID().toString(),
            courseId = selectedCourse,
            courseName = selectedCourse,
            credits = 3,
            addedDate = Date()
        )
        
        // Find the pool ID from the name
        val poolId = viewModel.electivePools.value?.find { it.name == selectedPool }?.id ?: return
        
        viewModel.addCourseToPool(
            poolId,
            course,
            onSuccess = {
                Toast.makeText(context, "Course added to pool successfully", Toast.LENGTH_SHORT).show()
                clearCourseForm()
            },
            onError = { error ->
                Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
            }
        )
    }
    
    private fun clearPoolForm() {
        etPoolName.text?.clear()
        spinnerPoolType.text.clear()
        etTargetPrograms.text?.clear()
        etPoolDescription.text?.clear()
    }
    
    private fun clearCourseForm() {
        spinnerSelectPool.text.clear()
        spinnerPoolCourse.text.clear()
    }
} 