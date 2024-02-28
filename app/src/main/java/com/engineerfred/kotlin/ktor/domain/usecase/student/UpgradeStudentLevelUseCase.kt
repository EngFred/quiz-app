package com.engineerfred.kotlin.ktor.domain.usecase.student

import com.engineerfred.kotlin.ktor.domain.repository.StudentsRepository
import javax.inject.Inject

class UpgradeStudentLevelUseCase @Inject constructor(
    private val studentsRepository: StudentsRepository
) {
    suspend operator fun invoke(studentId: String, newLevel: String, subject: String) = studentsRepository.upgradeStudentLevel(studentId, newLevel, subject)
}