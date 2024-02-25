package com.engineerfred.kotlin.ktor.domain.usecase.preferences

import com.engineerfred.kotlin.ktor.domain.repository.PreferencesRepository
import javax.inject.Inject

class GetAppUserUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {
    operator fun invoke() = preferencesRepository.getUser()
}