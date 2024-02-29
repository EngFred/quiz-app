package com.engineerfred.kotlin.ktor.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engineerfred.kotlin.ktor.domain.usecase.admin.GetCurrentlyLoggedAdminUseCase
import com.engineerfred.kotlin.ktor.domain.usecase.preferences.GetAppUserUseCase
import com.engineerfred.kotlin.ktor.domain.usecase.preferences.StoreAppUserUseCase
import com.engineerfred.kotlin.ktor.domain.usecase.student.GetCurrentlyLoggedStudentUseCase
import com.engineerfred.kotlin.ktor.ui.model.User
import com.engineerfred.kotlin.ktor.ui.screens.welcome.WelcomeScreenEvents
import com.engineerfred.kotlin.ktor.ui.screens.welcome.WelcomeScreenState
import com.engineerfred.kotlin.ktor.util.Constants.LOGGED_IN_USER_ID
import com.engineerfred.kotlin.ktor.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeScreenViewModel @Inject constructor(
    private val storeAppUserUseCase: StoreAppUserUseCase,
    private val getAppUserUseCase: GetAppUserUseCase,
    private val getCurrentlyLoggedInStudentUseCase: GetCurrentlyLoggedStudentUseCase,
    private val getCurrentlyLoggedInAdminUseCase: GetCurrentlyLoggedAdminUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WelcomeScreenState())
    val uiState = _uiState.asStateFlow()

    companion object {
        private val TAG = WelcomeScreenViewModel::class.java.simpleName
    }

    init {
        getAppUser()
    }

    private fun getCurrentlyLoggedInAdmin() = viewModelScope.launch( Dispatchers.IO ) {
        LOGGED_IN_USER_ID?.let {
            val admin = getCurrentlyLoggedInAdminUseCase.invoke(it)
            when( admin ) {
                is Response.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = admin.errorMessage
                    )
                }
                Response.Undefined -> Unit
                is Response.Success -> {
                    if ( admin.data == null ) {
                        _uiState.value = _uiState.value.copy(
                            adminNotInDatabase = true,
                            isLoading = false
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            adminNotInDatabase = false,
                            admin = admin.data
                        )
                    }
                }
            }
        }
    }

    private fun getCurrentlyLoggedInStudent() = viewModelScope.launch {
        LOGGED_IN_USER_ID?.let {
            getCurrentlyLoggedInStudentUseCase.invoke(it).collectLatest {
                when( it ) {
                    is Response.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = it.errorMessage
                        )
                    }
                    Response.Undefined -> Unit
                    is Response.Success -> {
                        if ( it.data == null ) {
                            _uiState.value = _uiState.value.copy(
                                studentNotInDatabase = true
                            )
                        } else {
                            _uiState.value = _uiState.value.copy(
                                student = it.data,
                                studentNotInDatabase = false
                            )
                        }
                    }
                }
            }
        }
    }

    fun onEvent( event: WelcomeScreenEvents ) {

        when( event ) {
            is WelcomeScreenEvents.CardClicked  -> {
                storeAppUser( event.userSelected )
            }
        }
    }

    private fun storeAppUser( user: String ) {
        try {
            viewModelScope.launch( Dispatchers.IO ) {
                _uiState.value = _uiState.value.copy(
                    appUser = user
                )
                storeAppUserUseCase.invoke( user )
                Log.i(TAG, "App user store successfully as $user")
            }
        } catch ( ex : Exception ) {
            Log.d(TAG, "Failed to store app user! $ex")
        }
    }

    private fun getAppUser() {
        viewModelScope.launch {
            getAppUserUseCase.invoke().collectLatest {
                if ( it == null ) _uiState.value = _uiState.value.copy( isLoading = false )  //display the cards
                else _uiState.value = _uiState.value.copy( appUser = it ) //there's already a selected app user, now we have to change the destinations based on if the app user is an admin or a student without stopping the loading!

                if ( it == User.Student.name.lowercase() ) {
                    Log.d("#", "The user is a student!")
                    getCurrentlyLoggedInStudent()
                }
                else if ( it == User.Admin.name.lowercase()  ) {
                    Log.d("#", "The user is an admin!")
                    getCurrentlyLoggedInAdmin()
                }
            }
        }
    }

}