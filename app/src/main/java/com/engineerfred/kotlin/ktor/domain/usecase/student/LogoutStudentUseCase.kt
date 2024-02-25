package com.engineerfred.kotlin.ktor.domain.usecase.student

import com.engineerfred.kotlin.ktor.domain.repository.StudentsRepository
import javax.inject.Inject

class LogoutStudentUseCase @Inject constructor(
    private val studentsRepository: StudentsRepository
) {
    suspend operator fun invoke() = studentsRepository.logoutStudent()
}