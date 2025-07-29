package com.vatty.mygbu

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.vatty.mygbu.data.model.StudentFee
import com.vatty.mygbu.data.repository.StudentRepository
import com.vatty.mygbu.utils.BottomNavigationHelper
import com.vatty.mygbu.viewmodel.StudentDashboardViewModel
import java.text.NumberFormat
import java.util.*

class StudentFeesActivity : AppCompatActivity() {

    private lateinit var viewModel: StudentDashboardViewModel
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var repository: StudentRepository
    
    // UI Components
    private lateinit var tvTotalAmount: TextView
    private lateinit var tvPaidAmount: TextView
    private lateinit var tvPendingAmount: TextView
    private lateinit var tvDueDate: TextView
    private lateinit var progressIndicator: LinearProgressIndicator
    private lateinit var tvPaymentStatus: TextView
    private lateinit var btnPayNow: MaterialCardView
    private lateinit var btnViewHistory: MaterialCardView
    private lateinit var rvFeeHistory: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_fees)

        // Initialize ViewModel and Repository
        viewModel = ViewModelProvider(this)[StudentDashboardViewModel::class.java]
        repository = StudentRepository()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeViews()
        setupBottomNavigation()
        setupObservers()
        loadFeeData()
        setupClickListeners()
    }

    private fun initializeViews() {
        bottomNavigation = findViewById(R.id.bottom_navigation)
        tvTotalAmount = findViewById(R.id.tv_total_amount)
        tvPaidAmount = findViewById(R.id.tv_paid_amount)
        tvPendingAmount = findViewById(R.id.tv_pending_amount)
        tvDueDate = findViewById(R.id.tv_due_date)
        progressIndicator = findViewById(R.id.progress_indicator)
        tvPaymentStatus = findViewById(R.id.tv_payment_status)
        btnPayNow = findViewById(R.id.btn_pay_now)
        btnViewHistory = findViewById(R.id.btn_view_history)
        rvFeeHistory = findViewById(R.id.rv_fee_history)
        
        // Back button
        findViewById<View>(R.id.iv_back).setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupBottomNavigation() {
        BottomNavigationHelper.setupBottomNavigation(this, bottomNavigation, StudentFeesActivity::class.java)
    }

    private fun setupObservers() {
        viewModel.feeStatus.observe(this) { fee ->
            updateFeeDisplay(fee)
        }
    }

    private fun loadFeeData() {
        try {
            val fees = repository.getStudentFees("STU001")
            if (fees.isNotEmpty()) {
                updateFeeDisplay(fees.first())
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to load fee data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateFeeDisplay(fee: StudentFee) {
        val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
        
        tvTotalAmount.text = formatter.format(fee.totalAmount)
        tvPaidAmount.text = formatter.format(fee.paidAmount)
        tvPendingAmount.text = formatter.format(fee.pendingAmount)
        tvDueDate.text = "Due: ${fee.dueDate}"
        
        // Calculate progress percentage
        val progressPercentage = ((fee.paidAmount / fee.totalAmount) * 100).toInt()
        progressIndicator.progress = progressPercentage
        
        // Update payment status
        when {
            fee.isPaid -> {
                tvPaymentStatus.text = "Fully Paid"
                tvPaymentStatus.setTextColor(getColor(R.color.success_green))
                btnPayNow.visibility = View.GONE
            }
            fee.pendingAmount > 0 -> {
                tvPaymentStatus.text = "Pending Payment"
                tvPaymentStatus.setTextColor(getColor(R.color.warning_orange))
                btnPayNow.visibility = View.VISIBLE
            }
            else -> {
                tvPaymentStatus.text = "No Dues"
                tvPaymentStatus.setTextColor(getColor(R.color.success_green))
                btnPayNow.visibility = View.GONE
            }
        }
    }

    private fun setupClickListeners() {
        btnPayNow.setOnClickListener {
            showPaymentDialog()
        }
        
        btnViewHistory.setOnClickListener {
            showPaymentHistory()
        }
    }

    private fun showPaymentDialog() {
        // Create a simple payment dialog
        val fee = repository.getStudentFees("STU001").firstOrNull()
        if (fee != null) {
            val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
            val message = "Pay ${formatter.format(fee.pendingAmount)} for Semester ${fee.semester}?"
            
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Payment Confirmation")
                .setMessage(message)
                .setPositiveButton("Pay Now") { _, _ ->
                    processPayment(fee)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun processPayment(fee: StudentFee) {
        // Simulate payment processing
        Toast.makeText(this, "Processing payment...", Toast.LENGTH_SHORT).show()
        
        // In a real app, this would integrate with a payment gateway
        // For now, we'll simulate a successful payment
        val updatedFee = fee.copy(
            paidAmount = fee.totalAmount,
            pendingAmount = 0.0,
            isPaid = true
        )
        
        // Update the display
        updateFeeDisplay(updatedFee)
        
        Toast.makeText(this, "Payment successful!", Toast.LENGTH_LONG).show()
    }

    private fun showPaymentHistory() {
        val fees = repository.getStudentFees("STU001")
        if (fees.isNotEmpty()) {
            val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
            val historyText = fees.joinToString("\n\n") { fee ->
                "Semester ${fee.semester}:\n" +
                "Total: ${formatter.format(fee.totalAmount)}\n" +
                "Paid: ${formatter.format(fee.paidAmount)}\n" +
                "Status: ${if (fee.isPaid) "Paid" else "Pending"}"
            }
            
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Payment History")
                .setMessage(historyText)
                .setPositiveButton("OK", null)
                .show()
        } else {
            Toast.makeText(this, "No payment history available", Toast.LENGTH_SHORT).show()
        }
    }
}