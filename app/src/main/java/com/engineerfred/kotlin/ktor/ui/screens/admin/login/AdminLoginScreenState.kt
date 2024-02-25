package com.engineerfred.kotlin.ktor.ui.screens.admin.login

import com.engineerfred.kotlin.ktor.domain.model.Admin

data class AdminLoginScreenState(
    val loginInProgress: Boolean = false,
    val emailValue: String = "",
    val passwordValue: String = "",

    val lastNameValue: String = "",
    val firstNameValue: String = "",

    val emailValueError: String? = null,

    val loginError: String? = null,

    val admin: Admin? = null,

    val changingUser: Boolean = false,
    val notAdminClickSuccess: Boolean = false,
    val notAdminClickError: String? = null,
)
