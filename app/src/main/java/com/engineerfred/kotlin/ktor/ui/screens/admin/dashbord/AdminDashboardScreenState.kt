package com.engineerfred.kotlin.ktor.ui.screens.admin.dashbord

import com.engineerfred.kotlin.ktor.domain.model.Admin

data class AdminDashboardScreenState(
    //val isLoading: Boolean = true,
    val error: String? = null,
    val admin: Admin? = null
)
