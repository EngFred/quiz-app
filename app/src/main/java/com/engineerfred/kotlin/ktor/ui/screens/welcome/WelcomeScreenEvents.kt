package com.engineerfred.kotlin.ktor.ui.screens.welcome

sealed class WelcomeScreenEvents {
    data class CardClicked( val userSelected: String ) : WelcomeScreenEvents()
}