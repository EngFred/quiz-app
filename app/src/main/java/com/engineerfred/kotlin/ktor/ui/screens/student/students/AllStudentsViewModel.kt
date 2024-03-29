package com.engineerfred.kotlin.ktor.ui.screens.student.students

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engineerfred.kotlin.ktor.domain.usecase.student.GetAllStudentsUseCase
import com.engineerfred.kotlin.ktor.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllStudentsViewModel @Inject constructor(
    private val getAllStudentsUseCase: GetAllStudentsUseCase
) : ViewModel() {

    var uiState by mutableStateOf(AllStudentsUIState())
        private set


    init {
        getStudents()
    }

    private fun getStudents() = viewModelScope.launch {
        getAllStudentsUseCase.invoke().collectLatest {
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
                        students = it.data
                    )
                }
                Response.Undefined -> Unit
            }
        }
    }

    fun onEvent( event: AllStudentsUiEvents ) {
        when(event) {
            AllStudentsUiEvents.Retry -> {
                uiState = uiState.copy(
                    isLoading = true,
                    loadError = null,
                    students = emptyList()
                )
                getStudents()
            }
        }
    }
}