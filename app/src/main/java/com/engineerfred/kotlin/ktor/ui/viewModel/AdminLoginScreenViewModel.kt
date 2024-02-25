package com.engineerfred.kotlin.ktor.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engineerfred.kotlin.ktor.domain.usecase.admin.LoginAdminUseCase
import com.engineerfred.kotlin.ktor.domain.usecase.preferences.StoreAppUserUseCase
import com.engineerfred.kotlin.ktor.ui.model.EmailInputValidationType
import com.engineerfred.kotlin.ktor.ui.model.User
import com.engineerfred.kotlin.ktor.ui.screens.admin.login.AdminLoginScreenEvents
import com.engineerfred.kotlin.ktor.ui.screens.admin.login.AdminLoginScreenState
import com.engineerfred.kotlin.ktor.ui.use_case.ValidateEmailInputUseCase
import com.engineerfred.kotlin.ktor.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminLoginScreenViewModel @Inject constructor(
    private val loginAdminUseCase: LoginAdminUseCase,
    private val validateEmailInputUseCase: ValidateEmailInputUseCase,
    private val storeAppUserUseCase: StoreAppUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminLoginScreenState())
    val uiState = _uiState.asStateFlow()


    companion object {
        private val TAG = AdminLoginScreenViewModel::class.java.simpleName
    }

    fun validateForm() : Boolean {
        return _uiState.value.emailValue.isNotEmpty() &&
        _uiState.value.passwordValue.isNotEmpty() &&
        _uiState.value.emailValueError == null
    }

    fun onEvent( event: AdminLoginScreenEvents ) {
        validateForm()
        when ( event ) {
            is AdminLoginScreenEvents.EmailChanged -> {
                _uiState.value = _uiState.value.copy(
                    emailValue = event.email
                )
                validateEmail()
            }
            is AdminLoginScreenEvents.PasswordChanged -> {
                _uiState.value = _uiState.value.copy(
                    passwordValue = event.password
                )
            }
            AdminLoginScreenEvents.LoginButtonClicked -> {
                _uiState.value = _uiState.value.copy(
                    loginInProgress = true,
                    loginError = null
                )
                loginAdmin( _uiState.value.emailValue, _uiState.value.passwordValue )
            }

            AdminLoginScreenEvents.NotAdminClicked -> {
                _uiState.value = uiState.value.copy(
                    changingUser = true
                )
                changeUser()
            }
        }
    }

    private fun validateEmail() {
        val status = validateEmailInputUseCase.invoke( _uiState.value.emailValue )
        when( status ) {
            EmailInputValidationType.EmptyField -> {
                _uiState.value = _uiState.value.copy(
                    emailValueError = "Email is required"
                )
            }
            EmailInputValidationType.Invalid -> {
                _uiState.value = _uiState.value.copy(
                    emailValueError = "The email is invalid"
                )
            }
            EmailInputValidationType.Valid -> {
                _uiState.value = _uiState.value.copy(
                    emailValueError = null
                )
            }
        }
    }

    private fun changeUser() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                storeAppUserUseCase.invoke(User.Student.name.lowercase())
                _uiState.value = uiState.value.copy(
                    notAdminClickSuccess = true
                )
            }
        } catch (ex: Exception) {
            _uiState.value = _uiState.value.copy(
                notAdminClickError = "Opps! something went wrong.!",
                changingUser = false
            )
            Log.d(TAG, "$ex")
        }
    }

    private fun loginAdmin( email: String, password: String ) {
        viewModelScope.launch( Dispatchers.IO ) {
            val status = loginAdminUseCase.invoke( email, password )
            when( status ) {
                is Response.Error -> {
                    _uiState.value = _uiState.value.copy(
                        loginError = status.message,
                        loginInProgress = false
                    )
                }
                is Response.Success -> {
                    if ( status.data != null ) { //we have successfully returned an admin
                        _uiState.value = _uiState.value.copy(
                            admin = status.data,
                            loginInProgress = false
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            loginError = "The admin with the provided credentials was deleted!",
                            loginInProgress = false
                        )
                    }
                }
                Response.Undefined -> Unit
            }
        }
    }
}