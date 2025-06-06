package com.vatty.mygbu

import android.content.DialogInterface
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class MessagesActivity : AppCompatActivity() {
    
    private lateinit var ivBack: ImageView
    private lateinit var btnCompose: MaterialButton
    private lateinit var rvConversations: RecyclerView
    
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
        setupBackButton()
        setupComposeButton()
        setupConversationsList()
    }
    
    private fun initializeViews() {
        ivBack = findViewById(R.id.iv_back)
        btnCompose = findViewById(R.id.btn_compose)
        rvConversations = findViewById(R.id.rv_conversations)
    }
    
    private fun setupBackButton() {
        ivBack.setOnClickListener {
            finish()
        }
    }
    
    private fun setupComposeButton() {
        btnCompose.setOnClickListener {
            showComposeDialog()
        }
    }
    
    private fun setupConversationsList() {
        // Sample conversations data
        val conversations = listOf(
            Conversation("John Smith", "Thanks for the feedback on the assignment...", "2:45 PM"),
            Conversation("Emma Johnson", "Can we schedule a meeting to discuss...", "1:30 PM"),
            Conversation("Michael Brown", "I need clarification on Chapter 5...", "11:15 AM"),
            Conversation("Sarah Davis", "The project presentation went well...", "Yesterday"),
            Conversation("David Wilson", "Could you extend the deadline for...", "Tuesday")
        )
        
        rvConversations.layoutManager = LinearLayoutManager(this)
        rvConversations.adapter = ConversationAdapter(conversations) { conversation ->
            Toast.makeText(this, "Open conversation with ${conversation.name}", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun showComposeDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_compose_message, null)
        val etRecipient = dialogView.findViewById<TextInputEditText>(R.id.et_recipient)
        val etSubject = dialogView.findViewById<TextInputEditText>(R.id.et_subject)
        val etMessage = dialogView.findViewById<TextInputEditText>(R.id.et_message)
        
        AlertDialog.Builder(this)
            .setTitle("Compose Message")
            .setView(dialogView)
            .setPositiveButton("Send") { _: DialogInterface, _: Int ->
                val recipient = etRecipient.text.toString().trim()
                val subject = etSubject.text.toString().trim()
                val message = etMessage.text.toString().trim()
                
                if (recipient.isNotEmpty() && subject.isNotEmpty() && message.isNotEmpty()) {
                    Toast.makeText(this, "Message sent to $recipient", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}

data class Conversation(
    val name: String,
    val lastMessage: String,
    val time: String
)

class ConversationAdapter(
    private val conversations: List<Conversation>,
    private val onConversationClick: (Conversation) -> Unit
) : RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder>() {
    
    class ConversationViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        val tvName: android.widget.TextView = itemView.findViewById(R.id.tv_name)
        val tvLastMessage: android.widget.TextView = itemView.findViewById(R.id.tv_last_message)
        val tvTime: android.widget.TextView = itemView.findViewById(R.id.tv_time)
    }
    
    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ConversationViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.item_conversation, parent, false)
        return ConversationViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        val conversation = conversations[position]
        holder.tvName.text = conversation.name
        holder.tvLastMessage.text = conversation.lastMessage
        holder.tvTime.text = conversation.time
        
        holder.itemView.setOnClickListener {
            onConversationClick(conversation)
        }
    }
    
    override fun getItemCount(): Int = conversations.size
} 