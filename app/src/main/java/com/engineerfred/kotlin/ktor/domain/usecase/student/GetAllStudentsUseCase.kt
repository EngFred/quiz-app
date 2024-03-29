package com.engineerfred.kotlin.ktor.domain.usecase.student

import com.engineerfred.kotlin.ktor.domain.repository.StudentsRepository
import javax.inject.Inject

class GetAllStudentsUseCase @Inject constructor(
    private val studentsRepository: StudentsRepository
) {
    operator fun invoke() = studentsRepository.getAllStudents()
}