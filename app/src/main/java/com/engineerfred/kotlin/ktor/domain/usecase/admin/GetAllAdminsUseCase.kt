package com.engineerfred.kotlin.ktor.domain.usecase.admin

import com.engineerfred.kotlin.ktor.domain.repository.AdminRepository
import javax.inject.Inject

class GetAllAdminsUseCase @Inject constructor(
    private val adminRepository: AdminRepository
) {
    operator fun invoke() = adminRepository.getAllAdmins()
}