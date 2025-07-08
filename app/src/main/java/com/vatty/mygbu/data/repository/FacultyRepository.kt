package com.vatty.mygbu.data.repository

import com.vatty.mygbu.data.model.Faculty
import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.Date

interface FacultyApi {
    @GET("joining")
    suspend fun getFacultyList(): List<Faculty>
}

class FacultyRepository {
    private val TAG = "FacultyRepository"
    
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://684a98a1165d05c5d35977db.mockapi.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        
    private val api = retrofit.create(FacultyApi::class.java)
    
    suspend fun getFacultyList(): List<Faculty> {
        return try {
            Log.i(TAG, "Fetching faculty list from API")
            val response = api.getFacultyList()
            Log.i(TAG, "Successfully fetched ${response.size} faculty members")
            response
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching faculty list: ${e.message}")
            emptyList()
        }
    }
    
    suspend fun getFacultyById(facultyId: String): Faculty? {
        return try {
            Log.i(TAG, "Fetching faculty member with ID: $facultyId")
            val allFaculty = api.getFacultyList()
            val faculty = allFaculty.find { it.facultyId == facultyId }
            if (faculty != null) {
                Log.i(TAG, "Found faculty member: ${faculty.name}")
            } else {
                Log.w(TAG, "No faculty member found with ID: $facultyId")
            }
            faculty
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching faculty member: ${e.message}")
            null
        }
    }
} 