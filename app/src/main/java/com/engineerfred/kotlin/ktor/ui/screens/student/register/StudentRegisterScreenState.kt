package com.engineerfred.kotlin.ktor.ui.screens.student.register

import com.engineerfred.kotlin.ktor.domain.model.Student

data class StudentRegisterScreenState(
    val phoneNumber: String = "",
    val verificationId: String? = null,
    val isAuthenticationSuccessfulNewStudent: Boolean = false,
    val verificationInProgress: Boolean = false,
    val finishingInProgress: Boolean = false,
    val verificationCode: String = "",
    val phoneNumberError: String = "",
    val oldStudent: Student? = null,
    val enableVerifyButton:  Boolean = false,
    val registrationError: String = "",
    val changingUser: Boolean = false,
    val notAStudentClickSuccess: Boolean = false,
    val notAStudentClickError: String? = null,
)
