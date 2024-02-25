package com.engineerfred.kotlin.ktor.domain.usecase.student

import com.engineerfred.kotlin.ktor.domain.repository.StudentsRepository
import javax.inject.Inject

class GetCurrentlyLoggedStudentUseCase @Inject constructor(
    private val studentsRepository: StudentsRepository
) {
    operator fun invoke( studentId: String ) = studentsRepository.checkIfStudentExistsInDatabase( studentId )
}