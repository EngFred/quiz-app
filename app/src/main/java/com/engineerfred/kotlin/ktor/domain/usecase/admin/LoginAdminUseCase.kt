package com.engineerfred.kotlin.ktor.domain.usecase.admin

import com.engineerfred.kotlin.ktor.domain.repository.AdminRepository
import javax.inject.Inject

class LoginAdminUseCase @Inject constructor(
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke( email: String, password: String ) = adminRepository.loginAdmin( email, password )
}