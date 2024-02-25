package com.engineerfred.kotlin.ktor.ui.screens.student.profile_setup

import android.net.Uri
import com.engineerfred.kotlin.ktor.domain.model.Student
import com.engineerfred.kotlin.ktor.ui.model.Level

data class StudentProfileSetupScreenState(
    val id: String = "",
    val nameValue: String = "",
    val bioValue: String = "",
    val profileImageUrl: Uri? = null,
    val registrationInProgress: Boolean = false,
    val registrationSuccessful: Boolean = false,
    val registrationError: String? = null,
    val nameValueError: String = "",
    val englishLevel: String = Level.Beginner.name,
    val mathLevel: String = Level.Beginner.name,
    val dateJoined: Long = System.currentTimeMillis()
) {
    val student = Student(
        id = id,
        profileImage = profileImageUrl.toString(),
        name = nameValue.trim(),
        about = bioValue.trim(),
        dateJoined = dateJoined,
        mathLevel = englishLevel,
        englishLevel = mathLevel
    )
}
