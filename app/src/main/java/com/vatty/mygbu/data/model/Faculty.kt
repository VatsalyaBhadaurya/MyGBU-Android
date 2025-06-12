package com.vatty.mygbu.data.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Faculty(
    @SerializedName("faculty_id")
    val facultyId: String,
    
    val designation: String,
    
    val name: String,
    
    @SerializedName("joining_date")
    val joiningDate: Date
) 