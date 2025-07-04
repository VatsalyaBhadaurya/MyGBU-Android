package com.vatty.mygbu.data.model

data class Student(
    val id: String,
    val name: String,
    val section: String,
    var isPresent: Boolean = false
) 