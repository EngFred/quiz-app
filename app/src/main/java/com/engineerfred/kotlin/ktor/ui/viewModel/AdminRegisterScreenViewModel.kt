package com.engineerfred.kotlin.ktor.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engineerfred.kotlin.ktor.domain.usecase.admin.AddAdminUseCase
import com.engineerfred.kotlin.ktor.ui.model.EmailInputValidationType
import com.engineerfred.kotlin.ktor.ui.model.NameInputValidationType
import com.engineerfred.kotlin.ktor.ui.model.PasswordInputValidationType
import com.engineerfred.kotlin.ktor.ui.model.RePasswordInputValidationType
import com.engineerfred.kotlin.ktor.ui.screens.admin.add_admin.AdminRegisterScreenEvents
import com.engineerfred.kotlin.ktor.ui.screens.admin.add_admin.AdminRegisterScreenState
import com.engineerfred.kotlin.ktor.ui.use_case.ValidateEmailInputUseCase
import com.engineerfred.kotlin.ktor.ui.use_case.ValidateNameInputUseCase
import com.engineerfred.kotlin.ktor.ui.use_case.ValidatePasswordInputUseCase
import com.engineerfred.kotlin.ktor.ui.use_case.ValidateRePasswordInputUseCase
import com.engineerfred.kotlin.ktor.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminRegisterScreenViewModel @Inject constructor(
    private val addAdminUseCase: AddAdminUseCase,
    private val validateEmailInputUseCase: ValidateEmailInputUseCase,
    private val validatePasswordInputUseCase: ValidatePasswordInputUseCase,
    private val validateRePasswordInputUseCase: ValidateRePasswordInputUseCase,
    private val validateNameInputUseCase: ValidateNameInputUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminRegisterScreenState())
    val uiState = _uiState.asStateFlow()

    fun validateForm() : Boolean = _uiState.value.lastNameValue.isNotEmpty() &&
            _uiState.value.firstNameValue.isNotEmpty() &&
            _uiState.value.emailValue.isNotEmpty() &&
            _uiState.value.passwordValue.isNotEmpty() &&
            _uiState.value.rePasswordValue.isNotEmpty() &&
            _uiState.value.passwordValue == _uiState.value.rePasswordValue &&
            _uiState.value.lastNameValueError == null &&
            _uiState.value.firstNameValueError == null &&
            _uiState.value.emailValueError == null &&
            _uiState.value.passwordValueError == null &&
            _uiState.value.rePasswordValueError == null

    fun onEvent( event: AdminRegisterScreenEvents ) {
        validateForm()
        when ( event ) {
            is AdminRegisterScreenEvents.EmailChanged -> {
                _uiState.value = _uiState.value.copy(
                    emailValue = event.email
                )
                validateEmail()
            }
            is AdminRegisterScreenEvents.PasswordChanged -> {
                _uiState.value = _uiState.value.copy(
                    passwordValue = event.password
                )
                validatePassword()
            }
            is AdminRegisterScreenEvents.RePasswordChanged -> {
                _uiState.value = _uiState.value.copy(
                    rePasswordValue = event.rePassword
                )
                validateRePassword()
            }

            is AdminRegisterScreenEvents.FirstNameChanged -> {
                _uiState.value = _uiState.value.copy(
                    firstNameValue = event.firstName
                )
                validateFirstName()
            }
            is AdminRegisterScreenEvents.LastNameChanged -> {
                _uiState.value = _uiState.value.copy(
                    lastNameValue = event.lastName
                )
                validateLastName()
            }

            AdminRegisterScreenEvents.RegisterButtonClicked -> {
                _uiState.value = _uiState.value.copy(
                    registrationInProgress = true
                )
                registerAdmin()
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

    private fun validatePassword() {
        val status = validatePasswordInputUseCase.invoke( _uiState.value.passwordValue )
        when (status) {
            PasswordInputValidationType.EmptyField -> {
                _uiState.value = _uiState.value.copy(
                    passwordValueError = "Password is required"
                )
            }
            PasswordInputValidationType.MissingNumber -> {
                _uiState.value = _uiState.value.copy(
                    passwordValueError = "missing a number"
                )
            }
            PasswordInputValidationType.MissingSpecialChar -> {
                _uiState.value = _uiState.value.copy(
                    passwordValueError = "missing a special character"
                )
            }
            PasswordInputValidationType.MissingCapitalLetter -> {
                _uiState.value = _uiState.value.copy(
                    passwordValueError = "missing s capital letter"
                )
            }
            PasswordInputValidationType.MissingSmallLetter -> {
                _uiState.value = _uiState.value.copy(
                    passwordValueError = "missing a small letter"
                )
            }
            PasswordInputValidationType.Short -> {
                _uiState.value = _uiState.value.copy(
                    passwordValueError = "Password is too short"
                )
            }
            PasswordInputValidationType.Valid -> {
                _uiState.value = _uiState.value.copy(
                    passwordValueError = null
                )
            }
        }
    }

    private fun validateRePassword() {
        val status = validateRePasswordInputUseCase.invoke( _uiState.value.passwordValue, _uiState.value.rePasswordValue )
        when( status ) {
            RePasswordInputValidationType.EmptyField -> {
                _uiState.value = _uiState.value.copy(
                    rePasswordValueError = "Password is required"
                )
            }
            RePasswordInputValidationType.NoMatch -> {
                _uiState.value = _uiState.value.copy(
                    rePasswordValueError = "Passwords don't match"
                )
            }
            RePasswordInputValidationType.Valid -> {
                _uiState.value = _uiState.value.copy(
                    rePasswordValueError = null
                )
            }
        }
    }

    private fun validateLastName() {
        val status = validateNameInputUseCase.invoke(_uiState.value.lastNameValue)
        when( status ) {
            NameInputValidationType.EmptyField -> {
                _uiState.value = _uiState.value.copy(
                    lastNameValueError = "Required"
                )
            }
            NameInputValidationType.Invalid -> {
                _uiState.value = _uiState.value.copy(
                    lastNameValueError = "Invalid"
                )
            }
            NameInputValidationType.Valid -> {
                _uiState.value = _uiState.value.copy(
                    lastNameValueError = null
                )
            }
        }
    }

    private fun validateFirstName() {
        val status = validateNameInputUseCase.invoke(_uiState.value.firstNameValue)
        when( status ) {
            NameInputValidationType.EmptyField -> {
                _uiState.value = _uiState.value.copy(
                    firstNameValueError = "Required"
                )
            }
            NameInputValidationType.Invalid -> {
                _uiState.value = _uiState.value.copy(
                    firstNameValueError = "Invalid"
                )
            }
            NameInputValidationType.Valid -> {
                _uiState.value = _uiState.value.copy(
                    firstNameValueError = null
                )
            }
        }
    }

    private fun registerAdmin() {
        viewModelScope.launch( Dispatchers.IO ) {
            val status = addAdminUseCase.invoke( _uiState.value.admin )
            when( status ) {
                is Response.Error -> {
                    _uiState.value = _uiState.value.copy(
                        registrationError = status.errorMessage,
                        registrationInProgress = false
                    )
                }
                is Response.Success -> {
                    _uiState.value = _uiState.value.copy(
                        adminEmail = status.data.email, // to enable navigation to the next screen that shows that email verification code has been sent
                        registrationInProgress = false
                    )
                }
                Response.Undefined -> Unit
            }
        }
    }
}