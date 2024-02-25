package com.engineerfred.kotlin.ktor.domain.usecase.student

import android.content.Context
import com.engineerfred.kotlin.ktor.domain.model.Student
import com.engineerfred.kotlin.ktor.domain.repository.StudentsRepository
import javax.inject.Inject

class AddStudentUseCase @Inject constructor(
    private val studentsRepository: StudentsRepository
) {
    suspend operator fun invoke(student: Student, context: Context) = studentsRepository.addStudent( student, context )
}