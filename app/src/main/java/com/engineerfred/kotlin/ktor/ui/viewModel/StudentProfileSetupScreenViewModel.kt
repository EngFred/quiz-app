package com.engineerfred.kotlin.ktor.ui.viewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engineerfred.kotlin.ktor.domain.usecase.student.AddStudentUseCase
import com.engineerfred.kotlin.ktor.domain.usecase.student.GetCurrentlyLoggedStudentUseCase
import com.engineerfred.kotlin.ktor.ui.screens.student.profile_setup.StudentProfileSetupScreenEvents
import com.engineerfred.kotlin.ktor.ui.screens.student.profile_setup.StudentProfileSetupScreenState
import com.engineerfred.kotlin.ktor.util.Constants.LOGGED_IN_USER_ID
import com.engineerfred.kotlin.ktor.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentProfileSetupScreenViewModel @Inject constructor(
    private val addStudentUseCase: AddStudentUseCase,
    private val getCurrentlyLoggedStudentUseCase: GetCurrentlyLoggedStudentUseCase
) : ViewModel() {

    var uiState by mutableStateOf(StudentProfileSetupScreenState())
        private set

    companion object {
        private val TAG = StudentProfileSetupScreenViewModel::class.java.simpleName
    }

    fun onEvent( event: StudentProfileSetupScreenEvents) {
        when (event) {
            is StudentProfileSetupScreenEvents.BioChanged -> {
                uiState = uiState.copy(
                    bioValue = event.bio
                )
            }
            is StudentProfileSetupScreenEvents.NameChanged -> {
                uiState = uiState.copy(
                    nameValue = event.name
                )
                validateUserName( uiState.nameValue )
            }

            is StudentProfileSetupScreenEvents.ProfileImageUrlChanged -> {
                uiState = uiState.copy(
                    profileImageUrl = event.imageUri
                )
            }
            is StudentProfileSetupScreenEvents.RegisterButtonClicked -> {
                if ( uiState.nameValue.isNotEmpty() ) {
                    Log.d(TAG, "Everything is good! The student generated is ${uiState.student}")
                    uiState = uiState.copy(
                        registrationInProgress = true
                    )
                    registerNewStudent( event.context )
                } else {
                    uiState = uiState.copy(
                        registrationError = "Provide a username!"
                    )
                }
            }
        }
    }

    private fun validateUserName( name: String ) {
        when {
            name.length < 3 -> {
                uiState = uiState.copy(
                    nameValueError = "The name is too short"
                )
            }
            name.contains(" ") -> {
                uiState = uiState.copy(
                    nameValueError = "The name should not contain white spaces"
                )
            }
            name.isEmpty() -> {
                uiState = uiState.copy(
                    nameValueError = "A name is required"
                )
            }
            else -> {
                uiState = uiState.copy(
                    nameValueError = ""
                )
            }
        }
    }

    private fun registerNewStudent( context: Context ) {
        LOGGED_IN_USER_ID?.let {
            viewModelScope.launch {
                getCurrentlyLoggedStudentUseCase.invoke(it).collectLatest { response ->
                    when ( response ) {
                        is Response.Error -> {
                            uiState = uiState.copy(
                                registrationInProgress = false,
                                registrationError = response.message
                            )
                        }
                        is Response.Success -> {
                            if ( response.data != null ) {
                                uiState = uiState.copy(
                                    id = response.data.id,
                                    nameValue = response.data.name,
                                    profileImageUrl = Uri.parse(response.data.profileImage),
                                    bioValue = response.data.about ?: "",
                                    englishLevel = response.data.englishLevel,
                                    mathLevel = response.data.mathLevel,
                                    dateJoined = response.data.dateJoined,
                                )
                                uiState = uiState.copy(registrationSuccessful = true)
                            } else {
                                uiState = uiState.copy( id = it )
                                val task = addStudentUseCase.invoke( uiState.student, context )
                                when (task) {
                                    is Response.Error -> {
                                        uiState = uiState.copy(
                                            registrationInProgress = false,
                                            registrationError = task.message
                                        )
                                    }
                                    is Response.Success -> {
                                        uiState = uiState.copy(
                                            registrationSuccessful = true
                                        )
                                    }
                                    Response.Undefined -> Unit
                                }
                            }
                        }
                        Response.Undefined -> Unit
                    }
                }
            }
        }
    }

}