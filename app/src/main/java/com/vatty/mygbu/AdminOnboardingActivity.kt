package com.vatty.mygbu

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.vatty.mygbu.data.model.*
import com.vatty.mygbu.data.repository.AdminRepository

class AdminOnboardingActivity : AppCompatActivity() {
    
    private lateinit var adminRepository: AdminRepository
    private lateinit var tabLayout: TabLayout
    private lateinit var rvOnboardingRequests: RecyclerView
    private lateinit var tvEmptyState: TextView
    
    private var currentTab = 0 // 0: Onboarding, 1: Offboarding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_onboarding)
        
        adminRepository = AdminRepository()
        
        initializeViews()
        setupToolbar()
        setupTabLayout()
        loadOnboardingRequests()
    }
    
    private fun initializeViews() {
        tabLayout = findViewById(R.id.tab_layout)
        rvOnboardingRequests = findViewById(R.id.rv_onboarding_requests)
        tvEmptyState = findViewById(R.id.tv_empty_state)
        
        rvOnboardingRequests.layoutManager = LinearLayoutManager(this)
    }
    
    private fun setupToolbar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Onboarding/Offboarding"
        }
    }
    
    private fun setupTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("Onboarding"))
        tabLayout.addTab(tabLayout.newTab().setText("Offboarding"))
        
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                currentTab = tab?.position ?: 0
                loadOnboardingRequests()
            }
            
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
    
    private fun loadOnboardingRequests() {
        val requests = adminRepository.getOnboardingRequests()
        val filteredRequests = if (currentTab == 0) {
            requests.filter { it.requestType == "ONBOARDING" }
        } else {
            requests.filter { it.requestType == "OFFBOARDING" }
        }
        
        if (filteredRequests.isEmpty()) {
            rvOnboardingRequests.visibility = android.view.View.GONE
            tvEmptyState.visibility = android.view.View.VISIBLE
            tvEmptyState.text = if (currentTab == 0) {
                "No pending onboarding requests"
            } else {
                "No pending offboarding requests"
            }
        } else {
            rvOnboardingRequests.visibility = android.view.View.VISIBLE
            tvEmptyState.visibility = android.view.View.GONE
            rvOnboardingRequests.adapter = OnboardingRequestAdapter(filteredRequests) { request ->
                showRequestDetailsDialog(request)
            }
        }
    }
    
    private fun showRequestDetailsDialog(request: OnboardingRequest) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_onboarding_details, null)
        
        // Set request details
        dialogView.findViewById<TextView>(R.id.tv_person_name).text = "Person ID: ${request.personId}"
        dialogView.findViewById<TextView>(R.id.tv_person_type).text = "Type: ${request.personType}"
        dialogView.findViewById<TextView>(R.id.tv_request_type).text = "Request: ${request.requestType}"
        dialogView.findViewById<TextView>(R.id.tv_status).text = "Status: ${request.status.name}"
        dialogView.findViewById<TextView>(R.id.tv_submitted_date).text = "Submitted: ${request.submittedDate}"
        
        // Setup documents list
        val rvDocuments = dialogView.findViewById<RecyclerView>(R.id.rv_documents)
        rvDocuments.layoutManager = LinearLayoutManager(this)
        rvDocuments.adapter = DocumentAdapter(request.documents)
        
        val dialog = AlertDialog.Builder(this)
            .setTitle("Request Details")
            .setView(dialogView)
            .setPositiveButton("Approve") { _, _ ->
                approveRequest(request.id)
            }
            .setNegativeButton("Reject") { _, _ ->
                showRejectionDialog(request.id)
            }
            .setNeutralButton("Close", null)
            .create()
        
        dialog.show()
    }
    
    private fun approveRequest(requestId: String) {
        val success = adminRepository.approveOnboardingRequest(requestId)
        if (success) {
            android.widget.Toast.makeText(this, "Request approved successfully", android.widget.Toast.LENGTH_SHORT).show()
            loadOnboardingRequests()
        } else {
            android.widget.Toast.makeText(this, "Failed to approve request", android.widget.Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun showRejectionDialog(requestId: String) {
        val editText = android.widget.EditText(this)
        editText.hint = "Enter rejection reason"
        
        AlertDialog.Builder(this)
            .setTitle("Reject Request")
            .setView(editText)
            .setPositiveButton("Reject") { _, _ ->
                val reason = editText.text.toString()
                if (reason.isNotBlank()) {
                    val success = adminRepository.rejectOnboardingRequest(requestId, reason)
                    if (success) {
                        android.widget.Toast.makeText(this, "Request rejected", android.widget.Toast.LENGTH_SHORT).show()
                        loadOnboardingRequests()
                    } else {
                        android.widget.Toast.makeText(this, "Failed to reject request", android.widget.Toast.LENGTH_SHORT).show()
                    }
                } else {
                    android.widget.Toast.makeText(this, "Please enter a rejection reason", android.widget.Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

// Adapter for Onboarding Requests
class OnboardingRequestAdapter(
    private val requests: List<OnboardingRequest>,
    private val onRequestClick: (OnboardingRequest) -> Unit
) : androidx.recyclerview.widget.RecyclerView.Adapter<OnboardingRequestAdapter.RequestViewHolder>() {
    
    class RequestViewHolder(view: android.view.View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val tvPersonId: TextView = view.findViewById(R.id.tv_person_id)
        val tvPersonType: TextView = view.findViewById(R.id.tv_person_type)
        val tvStatus: TextView = view.findViewById(R.id.tv_status)
        val tvSubmittedDate: TextView = view.findViewById(R.id.tv_submitted_date)
        val tvDocumentCount: TextView = view.findViewById(R.id.tv_document_count)
        val btnViewDetails: Button = view.findViewById(R.id.btn_view_details)
    }
    
    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): RequestViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.item_onboarding_request, parent, false)
        return RequestViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val request = requests[position]
        
        holder.tvPersonId.text = "ID: ${request.personId}"
        holder.tvPersonType.text = request.personType
        holder.tvStatus.text = request.status.name.replace("_", " ")
        holder.tvSubmittedDate.text = android.text.format.DateFormat.getDateFormat(holder.itemView.context).format(request.submittedDate)
        holder.tvDocumentCount.text = "${request.documents.size} documents"
        
        // Set status color
        val statusColor = when (request.status) {
            OnboardingStatus.PENDING -> android.graphics.Color.parseColor("#FFA500")
            OnboardingStatus.IN_PROGRESS -> android.graphics.Color.BLUE
            OnboardingStatus.COMPLETED -> android.graphics.Color.GREEN
            OnboardingStatus.REJECTED -> android.graphics.Color.RED
        }
        holder.tvStatus.setTextColor(statusColor)
        
        holder.btnViewDetails.setOnClickListener {
            onRequestClick(request)
        }
    }
    
    override fun getItemCount() = requests.size
}

// Adapter for Documents
class DocumentAdapter(
    private val documents: List<Document>
) : androidx.recyclerview.widget.RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder>() {
    
    class DocumentViewHolder(view: android.view.View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val tvDocumentName: TextView = view.findViewById(R.id.tv_document_name)
        val tvDocumentType: TextView = view.findViewById(R.id.tv_document_type)
        val tvUploadDate: TextView = view.findViewById(R.id.tv_upload_date)
        val tvVerificationStatus: TextView = view.findViewById(R.id.tv_verification_status)
    }
    
    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): DocumentViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.item_document, parent, false)
        return DocumentViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        val document = documents[position]
        
        holder.tvDocumentName.text = document.name
        holder.tvDocumentType.text = document.type
        holder.tvUploadDate.text = android.text.format.DateFormat.getDateFormat(holder.itemView.context).format(document.uploadedDate)
        
        if (document.isVerified) {
            holder.tvVerificationStatus.text = "Verified"
            holder.tvVerificationStatus.setTextColor(android.graphics.Color.GREEN)
        } else {
            holder.tvVerificationStatus.text = "Pending"
            holder.tvVerificationStatus.setTextColor(android.graphics.Color.parseColor("#FFA500"))
        }
    }
    
    override fun getItemCount() = documents.size
} 