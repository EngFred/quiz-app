package com.engineerfred.kotlin.ktor.ui.screens.admin.admins

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engineerfred.kotlin.ktor.domain.usecase.admin.GetAllAdminsUseCase
import com.engineerfred.kotlin.ktor.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminsViewModel @Inject constructor(
    private val getAllAdminsUseCase: GetAllAdminsUseCase
) : ViewModel() {

    var uiState by mutableStateOf(AdminsUIState())
        private set


    init {
        getAdmins()
    }

    private fun getAdmins() = viewModelScope.launch {
        getAllAdminsUseCase.invoke().collectLatest {
            when(it) {
                is Response.Error -> {
                    uiState = uiState.copy(
                        isLoading = false,
                        loadError = it.errorMessage
                    )
                }
                is Response.Success -> {
                    uiState = uiState.copy(
                        isLoading = false,
                        admins = it.data
                    )
                }
                Response.Undefined -> Unit
            }
        }
    }

    fun onEvent( event: AdminsUiEvents) {
        when(event) {
            AdminsUiEvents.Retry -> {
                uiState = uiState.copy(
                    isLoading = true,
                    loadError = null,
                    admins = emptyList()
                )
                getAdmins()
            }
        }
    }
}