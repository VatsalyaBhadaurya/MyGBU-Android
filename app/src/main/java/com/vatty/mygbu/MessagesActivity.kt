package com.vatty.mygbu

import android.app.Dialog
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.google.android.material.card.MaterialCardView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.vatty.mygbu.viewmodel.MessagesViewModel

class MessagesActivity : AppCompatActivity() {
    
    private val viewModel: MessagesViewModel by viewModels()
    private lateinit var ivBack: ImageView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_messages)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        initializeViews()
        setupObservers()
        setupBackButton()
        setupClickListeners()
    }
    
    private fun initializeViews() {
        ivBack = findViewById(R.id.iv_back)
    }
    
    private fun setupObservers() {
        // Observe messages
        viewModel.messages.observe(this, Observer { messages ->
            // Handle messages data update
            // In a full implementation, this would update RecyclerView
        })
        
        // Observe announcements
        viewModel.announcements.observe(this, Observer { announcements ->
            // Handle announcements data update
            // In a full implementation, this would update RecyclerView
        })
        
        // Observe loading state
        viewModel.isLoading.observe(this, Observer { isLoading ->
            // Handle loading state (show/hide progress bar)
        })
        
        // Observe errors
        viewModel.error.observe(this, Observer { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
    }
    
    private fun setupBackButton() {
        ivBack.setOnClickListener {
            finish()
        }
    }
    
    private fun setupClickListeners() {
        findViewById<MaterialCardView>(R.id.card_compose_message).setOnClickListener {
            showComposeMessageDialog()
        }
    }
    
    private fun showComposeMessageDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_compose_message)
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        )
        
        val etRecipient = dialog.findViewById<TextInputEditText>(R.id.et_recipient)
        val etSubject = dialog.findViewById<TextInputEditText>(R.id.et_subject)
        val etMessage = dialog.findViewById<TextInputEditText>(R.id.et_message)
        val btnSend = dialog.findViewById<MaterialButton>(R.id.btn_send)
        val btnCancel = dialog.findViewById<MaterialButton>(R.id.btn_cancel)
        
        btnSend.setOnClickListener {
            val recipient = etRecipient.text.toString().trim()
            val subject = etSubject.text.toString().trim()
            val message = etMessage.text.toString().trim()
            
            if (recipient.isEmpty() || subject.isEmpty() || message.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            // Here you would implement the actual message sending logic
            Toast.makeText(this, "Message sent to $recipient", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        
        dialog.show()
    }
}

data class MessageItem(
    val title: String,
    val content: String,
    val timestamp: String,
    val isRead: Boolean
)

data class AnnouncementItem(
    val title: String,
    val content: String,
    val sender: String,
    val timestamp: String
) 