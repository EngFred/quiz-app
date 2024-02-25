package com.engineerfred.kotlin.ktor.domain.usecase.student

import com.engineerfred.kotlin.ktor.domain.repository.StudentsRepository
import com.google.firebase.auth.PhoneAuthCredential
import javax.inject.Inject

class SignInStudentWithAuthCredentialsUseCase @Inject constructor(
    private val studentsRepository: StudentsRepository
) {
    suspend operator fun invoke( credential: PhoneAuthCredential ) = studentsRepository.signInStudentWithAuthCredentials(credential)
}