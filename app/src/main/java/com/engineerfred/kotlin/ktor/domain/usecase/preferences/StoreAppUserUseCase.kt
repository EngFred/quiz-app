package com.engineerfred.kotlin.ktor.domain.usecase.preferences

import com.engineerfred.kotlin.ktor.domain.repository.PreferencesRepository
import javax.inject.Inject

class StoreAppUserUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke( user: String ) = preferencesRepository.storeUser( user )
}