package com.engineerfred.kotlin.ktor.domain.model

import com.engineerfred.kotlin.ktor.ui.model.Level

data class Student(
    val id: String,
    val profileImage: String?,
    val name: String,
    val about: String? = null,
    val dateJoined: Long,
    val mathLevel: String,
    val englishLevel: String
) {
    constructor() : this(
        id = "",
        profileImage = null,
        name = "",
        about = null,
        dateJoined = 0L,
        englishLevel = Level.Beginner.name,
        mathLevel  =  Level.Beginner.name
    )
}
