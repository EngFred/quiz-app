package com.engineerfred.kotlin.ktor.domain.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {

    suspend fun storeUser( user: String )

    fun getUser() : Flow<String?>

}