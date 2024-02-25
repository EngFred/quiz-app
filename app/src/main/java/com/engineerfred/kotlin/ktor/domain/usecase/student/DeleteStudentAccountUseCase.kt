package com.engineerfred.kotlin.ktor.domain.usecase.student

import com.engineerfred.kotlin.ktor.domain.repository.StudentsRepository
import com.engineerfred.kotlin.ktor.util.Constants.LOGGED_IN_USER_ID
import javax.inject.Inject

class DeleteStudentAccountUseCase @Inject constructor(
    private val studentsRepository: StudentsRepository
) {
    suspend operator fun invoke() = studentsRepository.deleteStudent( LOGGED_IN_USER_ID!! )
}