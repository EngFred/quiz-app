package com.engineerfred.kotlin.ktor.domain.usecase.student

import android.app.Activity
import com.engineerfred.kotlin.ktor.domain.repository.StudentsRepository
import javax.inject.Inject

class VerifyPhoneNumberUseCase @Inject constructor(
    private val studentsRepository: StudentsRepository
) {
    suspend operator fun invoke( phoneNumber: String, activity: Activity) = studentsRepository.verifyStudentPhoneNumber( phoneNumber, activity )
}