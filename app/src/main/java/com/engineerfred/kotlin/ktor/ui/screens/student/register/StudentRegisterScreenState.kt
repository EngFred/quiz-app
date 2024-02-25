package com.engineerfred.kotlin.ktor.ui.screens.student.register

data class StudentRegisterScreenState(
    val phoneNumber: String = "",
    val verificationId: String? = null,
    val isAuthenticationSuccessful: Boolean = false,
    val verificationInProgress: Boolean = false,
    val finishingInProgress: Boolean = false,
    val verificationCode: String = "",
    val phoneNumberError: String = "",
    val enableVerifyButton:  Boolean = false,
    val registrationError: String = "",
    val changingUser: Boolean = false,
    val notAStudentClickSuccess: Boolean = false,
    val notAStudentClickError: String? = null,
)
