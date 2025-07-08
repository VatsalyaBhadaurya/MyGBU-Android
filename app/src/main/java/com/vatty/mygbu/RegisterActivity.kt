package com.vatty.mygbu

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
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

data class RegisterRequest(
    val email: String,
    val password: String,
    val first_name: String,
    val last_name: String,
    val phone: String,
    val user_type: String
)
data class RegisterResponse(val message: String)
data class RegisterError(val email: List<String>?)

interface RegisterApi {
    @Headers("Content-Type: application/json")
    @POST("register/")
    suspend fun register(@Body body: RegisterRequest): Response<RegisterResponse>
}

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etFirstName = findViewById<EditText>(R.id.et_first_name)
        val etLastName = findViewById<EditText>(R.id.et_last_name)
        val etEmail = findViewById<EditText>(R.id.et_email)
        val etPhone = findViewById<EditText>(R.id.et_phone)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val spinnerUserType = findViewById<Spinner>(R.id.spinner_user_type)
        val btnRegister = findViewById<Button>(R.id.btn_register)
        val progressBar = ProgressBar(this)
        addContentView(progressBar, android.widget.FrameLayout.LayoutParams(100, 100))
        progressBar.visibility = View.GONE

        val userTypes = listOf("student", "faculty", "admin")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, userTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerUserType.adapter = adapter

        val retrofit = Retrofit.Builder()
            .baseUrl("https://auth.tilchattaas.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(RegisterApi::class.java)

        btnRegister.setOnClickListener {
            val firstName = etFirstName.text.toString()
            val lastName = etLastName.text.toString()
            val email = etEmail.text.toString()
            val phone = etPhone.text.toString()
            val password = etPassword.text.toString()
            val userType = spinnerUserType.selectedItem.toString()
            if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || phone.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            progressBar.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = api.register(RegisterRequest(email, password, firstName, lastName, phone, userType))
                    withContext(Dispatchers.Main) {
                        progressBar.visibility = View.GONE
                        if (response.isSuccessful && response.body() != null) {
                            Toast.makeText(this@RegisterActivity, "Registration successful! Please login.", Toast.LENGTH_LONG).show()
                            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@RegisterActivity, "Registration failed: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this@RegisterActivity, "Registration failed: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
} 