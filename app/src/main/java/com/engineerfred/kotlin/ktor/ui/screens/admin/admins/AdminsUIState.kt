package com.engineerfred.kotlin.ktor.ui.screens.admin.admins

import com.engineerfred.kotlin.ktor.domain.model.Admin

data class AdminsUIState(
    val isLoading: Boolean = true,
    val loadError: String? = null,
    val admins: List<Admin> = emptyList()
)
