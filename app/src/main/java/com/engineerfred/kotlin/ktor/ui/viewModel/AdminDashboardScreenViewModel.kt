package com.engineerfred.kotlin.ktor.ui.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.engineerfred.kotlin.ktor.ui.screens.admin.dashbord.AdminDashboardScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdminDashboardScreenViewModel @Inject constructor(

) : ViewModel() {

    private var uiState by mutableStateOf(AdminDashboardScreenState())
        private set

    init {

    }

}