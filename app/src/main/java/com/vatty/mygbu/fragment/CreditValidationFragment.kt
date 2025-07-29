package com.vatty.mygbu.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.vatty.mygbu.R
import com.vatty.mygbu.adapter.CreditRuleAdapter
import com.vatty.mygbu.data.model.CreditRule
import com.vatty.mygbu.viewmodel.AdminDashboardViewModel
import java.util.*

class CreditValidationFragment : Fragment() {
    
    private lateinit var viewModel: AdminDashboardViewModel
    private lateinit var creditRuleAdapter: CreditRuleAdapter
    
    private lateinit var spinnerValidationProgram: AutoCompleteTextView
    private lateinit var etTotalCredits: TextInputEditText
    private lateinit var etCoreCredits: TextInputEditText
    private lateinit var etElectiveCredits: TextInputEditText
    private lateinit var etLabCredits: TextInputEditText
    private lateinit var btnSaveCreditRules: Button
    private lateinit var spinnerValidateProgram: AutoCompleteTextView
    private lateinit var btnValidateCredits: Button
    private lateinit var llValidationResults: LinearLayout
    private lateinit var tvValidationResults: TextView
    private lateinit var rvCreditRules: RecyclerView
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_credit_validation, container, false)
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
        viewModel.loadCreditRules()
        viewModel.loadPrograms()
    }
    
    private fun initializeViews(view: View) {
        spinnerValidationProgram = view.findViewById(R.id.spinner_validation_program)
        etTotalCredits = view.findViewById(R.id.et_total_credits)
        etCoreCredits = view.findViewById(R.id.et_core_credits)
        etElectiveCredits = view.findViewById(R.id.et_elective_credits)
        etLabCredits = view.findViewById(R.id.et_lab_credits)
        btnSaveCreditRules = view.findViewById(R.id.btn_save_credit_rules)
        spinnerValidateProgram = view.findViewById(R.id.spinner_validate_program)
        btnValidateCredits = view.findViewById(R.id.btn_validate_credits)
        llValidationResults = view.findViewById(R.id.ll_validation_results)
        tvValidationResults = view.findViewById(R.id.tv_validation_results)
        rvCreditRules = view.findViewById(R.id.rv_credit_rules)
    }
    
    private fun setupRecyclerView() {
        creditRuleAdapter = CreditRuleAdapter(
            onEditClick = { creditRule ->
                Toast.makeText(context, "Edit: ${creditRule.programName}", Toast.LENGTH_SHORT).show()
            },
            onDeleteClick = { creditRule ->
                viewModel.deleteCreditRule(
                    creditRule.id,
                    onSuccess = {
                        Toast.makeText(context, "Credit rule deleted", Toast.LENGTH_SHORT).show()
                    },
                    onError = { error ->
                        Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        )
        
        rvCreditRules.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = creditRuleAdapter
        }
    }
    
    private fun setupSpinners() {
        // Program spinners will be populated from ViewModel data
    }
    
    private fun setupClickListeners() {
        btnSaveCreditRules.setOnClickListener {
            saveCreditRules()
        }
        
        btnValidateCredits.setOnClickListener {
            validateCredits()
        }
    }
    
    private fun observeData() {
        viewModel.creditRules.observe(viewLifecycleOwner) { creditRules ->
            creditRuleAdapter.submitList(creditRules)
        }
        
        viewModel.programs.observe(viewLifecycleOwner) { programs ->
            val programNames = programs.map { it.name }
            val programAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, programNames)
            spinnerValidationProgram.setAdapter(programAdapter)
            spinnerValidateProgram.setAdapter(programAdapter)
        }
        
        viewModel.creditValidationResult.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                displayValidationResults(result)
            }
        }
    }
    
    private fun saveCreditRules() {
        val program = spinnerValidationProgram.text.toString()
        val totalCredits = etTotalCredits.text.toString()
        val coreCredits = etCoreCredits.text.toString()
        val electiveCredits = etElectiveCredits.text.toString()
        val labCredits = etLabCredits.text.toString()
        
        if (program.isEmpty() || totalCredits.isEmpty() || coreCredits.isEmpty() || electiveCredits.isEmpty() || labCredits.isEmpty()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }
        
        val creditRule = CreditRule(
            id = UUID.randomUUID().toString(),
            programId = program,
            programName = program,
            batchYear = "2024",
            totalCreditsRequired = totalCredits.toInt(),
            coreCreditsRequired = coreCredits.toInt(),
            electiveCreditsRequired = electiveCredits.toInt(),
            labCreditsRequired = labCredits.toInt(),
            isActive = true,
            createdDate = Date(),
            updatedDate = Date()
        )
        
        viewModel.addCreditRule(
            creditRule,
            onSuccess = {
                Toast.makeText(context, "Credit rules saved successfully", Toast.LENGTH_SHORT).show()
                clearForm()
            },
            onError = { error ->
                Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
            }
        )
    }
    
    private fun validateCredits() {
        val program = spinnerValidateProgram.text.toString()
        
        if (program.isEmpty()) {
            Toast.makeText(context, "Please select a program", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Find the program ID from the name
        val programId = viewModel.programs.value?.find { it.name == program }?.id ?: return
        
        viewModel.validateCredits(
            programId,
            onSuccess = {
                // Results will be displayed through the observer
            },
            onError = { error ->
                Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
            }
        )
    }
    
    private fun displayValidationResults(result: com.vatty.mygbu.data.model.CreditValidationResult) {
        val resultText = buildString {
            appendLine("Program: ${result.programName}")
            appendLine()
            appendLine("Total Credits:")
            appendLine("  Required: ${result.totalCreditsRequired}")
            appendLine("  Earned: ${result.totalCreditsEarned}")
            appendLine("  Status: ${if (result.totalCreditsEarned >= result.totalCreditsRequired) "✓" else "✗"}")
            appendLine()
            appendLine("Core Credits:")
            appendLine("  Required: ${result.coreCreditsRequired}")
            appendLine("  Earned: ${result.coreCreditsEarned}")
            appendLine("  Status: ${if (result.coreCreditsEarned >= result.coreCreditsRequired) "✓" else "✗"}")
            appendLine()
            appendLine("Elective Credits:")
            appendLine("  Required: ${result.electiveCreditsRequired}")
            appendLine("  Earned: ${result.electiveCreditsEarned}")
            appendLine("  Status: ${if (result.electiveCreditsEarned >= result.electiveCreditsRequired) "✓" else "✗"}")
            appendLine()
            appendLine("Lab Credits:")
            appendLine("  Required: ${result.labCreditsRequired}")
            appendLine("  Earned: ${result.labCreditsEarned}")
            appendLine("  Status: ${if (result.labCreditsEarned >= result.labCreditsRequired) "✓" else "✗"}")
            appendLine()
            appendLine("Overall Status: ${if (result.isValid) "VALID" else "INVALID"}")
            appendLine()
            if (result.issues.isNotEmpty()) {
                appendLine("Issues:")
                result.issues.forEach { issue ->
                    appendLine("  • $issue")
                }
            }
        }
        
        tvValidationResults.text = resultText
        llValidationResults.visibility = View.VISIBLE
    }
    
    private fun clearForm() {
        spinnerValidationProgram.text.clear()
        etTotalCredits.text?.clear()
        etCoreCredits.text?.clear()
        etElectiveCredits.text?.clear()
        etLabCredits.text?.clear()
    }
} 