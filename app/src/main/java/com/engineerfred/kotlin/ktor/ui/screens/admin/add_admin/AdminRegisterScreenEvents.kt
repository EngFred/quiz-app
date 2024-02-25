package com.engineerfred.kotlin.ktor.ui.screens.admin.add_admin

sealed class AdminRegisterScreenEvents {
    data class EmailChanged( val email: String ) : AdminRegisterScreenEvents()
    data class PasswordChanged( val password: String ) : AdminRegisterScreenEvents()
    data class RePasswordChanged( val rePassword: String ) : AdminRegisterScreenEvents()
    data class LastNameChanged( val lastName: String ) : AdminRegisterScreenEvents()
    data class FirstNameChanged( val firstName: String ) : AdminRegisterScreenEvents()
    data object RegisterButtonClicked: AdminRegisterScreenEvents()
}
