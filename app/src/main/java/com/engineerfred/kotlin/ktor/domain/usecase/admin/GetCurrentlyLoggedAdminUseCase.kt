package com.engineerfred.kotlin.ktor.domain.usecase.admin

import com.engineerfred.kotlin.ktor.domain.repository.AdminRepository
import javax.inject.Inject

class GetCurrentlyLoggedAdminUseCase @Inject constructor(
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke( adminId: String ) = adminRepository.checkIfAdminExistsInDatabase( adminId )
}