package com.vatty.mygbu

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
            viewModel.onComposeMessageClicked()
        }
        
        findViewById<MaterialCardView>(R.id.card_broadcast_announcement).setOnClickListener {
            viewModel.onBroadcastAnnouncementClicked()
        }
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