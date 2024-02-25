package com.engineerfred.kotlin.ktor.ui.screens.admin.login

sealed class AdminLoginScreenEvents {
    data class EmailChanged( val email: String ) : AdminLoginScreenEvents()
    data class PasswordChanged( val password: String ) : AdminLoginScreenEvents()
    data object LoginButtonClicked: AdminLoginScreenEvents()
    data object NotAdminClicked: AdminLoginScreenEvents()
}
