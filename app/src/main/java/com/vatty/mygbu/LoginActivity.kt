package com.vatty.mygbu

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.ProgressBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.Response

// Retrofit API interface
interface AuthApi {
    @Headers("Content-Type: application/json")
    @POST("login/")
    suspend fun login(@Body body: LoginRequest): Response<LoginResponse>
}
data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val refresh: String, val access: String)

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val spinnerUserType = findViewById<Spinner>(R.id.spinner_user_type)
        val etUsername = findViewById<EditText>(R.id.et_username)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val btnLogin = findViewById<Button>(R.id.btn_login)
        val btnRegister = findViewById<Button>(R.id.btn_register)
        val progressBar = ProgressBar(this)
        addContentView(progressBar, android.widget.FrameLayout.LayoutParams(100, 100))
        progressBar.visibility = View.GONE

        val userTypes = listOf("Student", "Faculty", "Admin")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, userTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerUserType.adapter = adapter

        val retrofit = Retrofit.Builder()
            .baseUrl("https://auth.tilchattaas.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(AuthApi::class.java)

        btnLogin.setOnClickListener {
            val userType = spinnerUserType.selectedItem.toString()
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            if (username.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            progressBar.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = api.login(LoginRequest(username, password))
                    withContext(Dispatchers.Main) {
                        progressBar.visibility = View.GONE
                        if (response.isSuccessful && response.body() != null) {
                            // TODO: Save tokens securely (SharedPreferences)
                            // val tokens = response.body()!!
                            when (userType) {
                                "Faculty" -> startActivity(Intent(this@LoginActivity, FacultyDashboardActivity::class.java))
                                "Student" -> startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                "Admin" -> startActivity(Intent(this@LoginActivity, FacultyHubActivity::class.java))
                            }
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this@LoginActivity, "Login failed: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
} 