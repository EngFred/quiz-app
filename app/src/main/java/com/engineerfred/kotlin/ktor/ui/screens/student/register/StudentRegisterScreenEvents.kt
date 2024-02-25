package com.engineerfred.kotlin.ktor.ui.screens.student.register

import android.app.Activity

sealed class StudentRegisterScreenEvents {
    data class PhoneNumberChanged( val phoneNumber: String ) : StudentRegisterScreenEvents()
    data class VerificationCodeChanged( val verificationCode: String ) : StudentRegisterScreenEvents()
    data object DoneButtonClicked: StudentRegisterScreenEvents()
    data class VerifyButtonClicked( val activity: Activity ): StudentRegisterScreenEvents()
    data object NotStudentClicked: StudentRegisterScreenEvents()
}