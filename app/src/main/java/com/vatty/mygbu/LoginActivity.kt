package com.vatty.mygbu

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vatty.mygbu.databinding.ActivityLoginBinding
import com.vatty.mygbu.utils.ErrorHandler

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var errorHandler: ErrorHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        errorHandler = ErrorHandler(this)
        setupViews()
    }

    private fun setupViews() {
        // Setup role dropdown
        val roles = arrayOf("Faculty", "Student")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, roles)
        binding.roleDropdown.setAdapter(adapter)
        binding.roleDropdown.setText(roles[0], false) // Default to Faculty

        binding.btnLogin.setOnClickListener {
            val userId = binding.etUserId.text.toString()
            val password = binding.etPassword.text.toString()
            val selectedRole = binding.roleDropdown.text.toString()

            if (validateInput(userId, password)) {
                performLogin(userId, password, selectedRole)
            }
        }

        binding.tvForgotPassword.setOnClickListener {
            // TODO: Implement forgot password flow
            Toast.makeText(this, "Forgot password feature coming soon!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInput(userId: String, password: String): Boolean {
        if (userId.isEmpty()) {
            binding.etUserId.error = "User ID is required"
            return false
        }
        if (password.isEmpty()) {
            binding.etPassword.error = "Password is required"
            return false
        }
        return true
    }

    private fun performLogin(userId: String, password: String, role: String) {
        try {
            errorHandler.showLoadingState()
            // TODO: Implement actual login logic
            // For now, simulate API call
            android.os.Handler().postDelayed({
                errorHandler.hideLoadingState()
                val targetActivity = when (role) {
                    "Faculty" -> FacultyDashboardActivity::class.java
                    "Student" -> MainActivity::class.java // Assuming MainActivity is for students
                    else -> FacultyDashboardActivity::class.java
                }
                startActivity(Intent(this, targetActivity))
                finish()
            }, 1000)
        } catch (e: Exception) {
            errorHandler.hideLoadingState()
            errorHandler.showError(e)
        }
    }
} 