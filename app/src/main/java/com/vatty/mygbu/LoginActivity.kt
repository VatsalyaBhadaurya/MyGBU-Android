package com.vatty.mygbu

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {
    private lateinit var roleDropdown: AutoCompleteTextView
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var loginButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize views
        roleDropdown = findViewById(R.id.roleDropdown)
        emailInput = findViewById(R.id.etEmail)
        passwordInput = findViewById(R.id.etPassword)
        loginButton = findViewById(R.id.btnLogin)

        // Setup role dropdown
        val roles = arrayOf("Faculty", "Student", "Admin")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, roles)
        roleDropdown.setAdapter(adapter)

        // Setup login button click listener
        loginButton.setOnClickListener {
            val role = roleDropdown.text.toString()
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (validateInputs(role, email, password)) {
                performLogin(role, email, password)
            }
        }
    }

    private fun validateInputs(role: String, email: String, password: String): Boolean {
        when {
            role.isEmpty() -> {
                Toast.makeText(this, "Please select a role", Toast.LENGTH_SHORT).show()
                return false
            }
            email.isEmpty() -> {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
                return false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                return false
            }
            password.isEmpty() -> {
                Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show()
                return false
            }
            password.length < 6 -> {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }

    private fun performLogin(role: String, email: String, password: String) {
        // TODO: Implement actual authentication logic here
        // For now, we'll just simulate a successful login for faculty
        
        when (role) {
            "Faculty" -> {
                startActivity(Intent(this, FacultyDashboardActivity::class.java))
                finish()
            }
            "Student" -> {
                // TODO: Implement student login flow
                Toast.makeText(this, "Student login coming soon", Toast.LENGTH_SHORT).show()
            }
            "Admin" -> {
                // TODO: Implement admin login flow
                Toast.makeText(this, "Admin login coming soon", Toast.LENGTH_SHORT).show()
            }
        }
    }
} 