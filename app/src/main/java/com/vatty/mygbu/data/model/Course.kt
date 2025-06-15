package com.vatty.mygbu.data.model

data class Course(
    val courseCode: String,
    val courseName: String,
    val credits: Int,
    val enrolledStudents: Int,
    val schedule: String,
    val room: String = "",
    val description: String = ""
) 