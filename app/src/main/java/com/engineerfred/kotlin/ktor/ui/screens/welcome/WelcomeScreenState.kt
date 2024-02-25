package com.engineerfred.kotlin.ktor.ui.screens.welcome

import com.engineerfred.kotlin.ktor.domain.model.Admin
import com.engineerfred.kotlin.ktor.domain.model.Student

data class WelcomeScreenState(
    val isLoading: Boolean = true,
    val appUser: String? = null,
    val error: String? = null,

    val student: Student? = null,
    val studentNotInDatabase: Boolean? = null,

    val admin: Admin? = null,
    val adminNotInDatabase: Boolean? = null
)
