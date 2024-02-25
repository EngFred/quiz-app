package com.engineerfred.kotlin.ktor.domain.usecase.admin

import com.engineerfred.kotlin.ktor.domain.model.Admin
import com.engineerfred.kotlin.ktor.domain.repository.AdminRepository
import javax.inject.Inject

class AddAdminUseCase @Inject constructor(
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke( admin: Admin ) = adminRepository.registerAdmin( admin )
}