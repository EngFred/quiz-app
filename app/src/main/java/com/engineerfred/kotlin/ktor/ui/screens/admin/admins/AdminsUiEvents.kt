package com.engineerfred.kotlin.ktor.ui.screens.admin.admins

sealed class AdminsUiEvents {
    data object Retry: AdminsUiEvents()
}