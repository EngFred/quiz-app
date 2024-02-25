package com.engineerfred.kotlin.ktor.ui.screens.admin.add_admin

import com.engineerfred.kotlin.ktor.domain.model.Admin
import com.engineerfred.kotlin.ktor.util.Constants.LOGGED_IN_USER_EMAIL
import com.engineerfred.kotlin.ktor.util.Constants.userIsCurrentlyLoggedIn

data class AdminRegisterScreenState(
    val adminId: String = "",
    val adminEmail: String? = null,

    val registrationInProgress: Boolean = false,
    val emailValue: String = "",
    val passwordValue: String = "",
    val rePasswordValue: String = "",

    val lastNameValue: String = "",
    val firstNameValue: String = "",

    val emailValueError: String? = null,
    val passwordValueError: String? = null,
    val rePasswordValueError: String? = null,
    val lastNameValueError: String? = null,
    val firstNameValueError: String? = null,

    val registrationError: String? = null,
) {
    val admin = Admin (
        id = adminId,
        lastName = lastNameValue.trim(),
        firstName = firstNameValue.trim(),
        email = emailValue,
        password = passwordValue,
        addedBy = if ( userIsCurrentlyLoggedIn ) LOGGED_IN_USER_EMAIL else "self"
    )

}
