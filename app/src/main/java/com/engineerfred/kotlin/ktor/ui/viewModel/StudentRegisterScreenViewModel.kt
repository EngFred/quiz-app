package com.engineerfred.kotlin.ktor.ui.viewModel

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engineerfred.kotlin.ktor.domain.usecase.preferences.StoreAppUserUseCase
import com.engineerfred.kotlin.ktor.domain.usecase.student.GetCurrentlyLoggedStudentUseCase
import com.engineerfred.kotlin.ktor.domain.usecase.student.SignInStudentWithAuthCredentialsUseCase
import com.engineerfred.kotlin.ktor.domain.usecase.student.VerifyPhoneNumberUseCase
import com.engineerfred.kotlin.ktor.ui.model.PhoneNumberInputValidationType
import com.engineerfred.kotlin.ktor.ui.model.User
import com.engineerfred.kotlin.ktor.ui.screens.student.register.StudentRegisterScreenEvents
import com.engineerfred.kotlin.ktor.ui.screens.student.register.StudentRegisterScreenState
import com.engineerfred.kotlin.ktor.ui.use_case.ValidatePhoneNumberUseCase
import com.engineerfred.kotlin.ktor.util.Response
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentRegisterScreenViewModel @Inject constructor(
    private val validatePhoneNumberUseCase: ValidatePhoneNumberUseCase,
    private val verifyPhoneNumberUseCase: VerifyPhoneNumberUseCase,
    private val signInStudentWithAuthCredentialsUseCase: SignInStudentWithAuthCredentialsUseCase,
    private val storeAppUserUseCase: StoreAppUserUseCase,
    private val getCurrentlyLoggedInStudentUseCase: GetCurrentlyLoggedStudentUseCase
    //private val activityRef: WeakReference<Activity>,
) : ViewModel() {

    var uiState by mutableStateOf(StudentRegisterScreenState())
        private set


    companion object {
        private val TAG = StudentRegisterScreenViewModel::class.java.simpleName
    }


    fun onEvent( event: StudentRegisterScreenEvents ) {
        if ( uiState.registrationError.isNotEmpty() ) uiState = uiState.copy( registrationError = "" )
        when( event ) {
            is StudentRegisterScreenEvents.PhoneNumberChanged -> {
                uiState = uiState.copy(
                    phoneNumber = event.phoneNumber
                )
                validatePhoneNumber( uiState.phoneNumber )
            }
            StudentRegisterScreenEvents.DoneButtonClicked -> {
                if ( uiState.verificationCode.isNotEmpty() && uiState.verificationCode.length == 6 ) {
                    uiState = uiState.copy(
                        finishingInProgress = true,
                        registrationError = ""
                    )
                    signInStudentWithAuthCredentials()
                }
            }
            is StudentRegisterScreenEvents.VerifyButtonClicked -> {
                if ( uiState.phoneNumber.isNotEmpty() && uiState.phoneNumberError.isEmpty() ) {
                    uiState = uiState.copy(
                        verificationInProgress = true,
                        registrationError = ""
                    )
                    verifyStudentPhoneNumber( uiState.phoneNumber.trim(), event.activity )
                }
            }

            is StudentRegisterScreenEvents.VerificationCodeChanged -> {
                uiState = uiState.copy(
                    verificationCode = event.verificationCode
                )
            }
            StudentRegisterScreenEvents.NotStudentClicked -> {
                uiState = uiState.copy(
                    changingUser = true
                )
                changeUser()
            }
        }
    }


    private fun changeUser() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                storeAppUserUseCase.invoke(User.Admin.name.lowercase())
                uiState = uiState.copy(
                    notAStudentClickSuccess = true
                )
            }
        } catch (ex: Exception) {
            uiState = uiState.copy(
                notAStudentClickError = "Opps! something went wrong.!",
                changingUser = false
            )
            Log.d(TAG, "$ex")
        }
    }

    private fun signInStudentWithAuthCredentials() {
        uiState.verificationId?.let {
            viewModelScope.launch( Dispatchers.IO ) {
                val credential = PhoneAuthProvider.getCredential( it, uiState.verificationCode )
                val task = signInStudentWithAuthCredentialsUseCase.invoke( credential )
                when ( task ) {
                    is Response.Error -> {
                        uiState = uiState.copy(
                            registrationError = task.errorMessage,
                            finishingInProgress = false
                        )
                    }
                    is Response.Success -> {
                        uiState = uiState.copy(
                            changingUser = true,
                            enableVerifyButton = false,
                        )
                        val studentId = task.data.user!!.uid
                        getCurrentlyLoggedInStudent(studentId)
                    }
                    Response.Undefined -> Unit
                }
            }
        }
    }

    private fun verifyStudentPhoneNumber( phoneNumber: String, activity: Activity) {
        viewModelScope.launch {
            val task = verifyPhoneNumberUseCase.invoke( phoneNumber, activity )
            when (task) {
                is Response.Error -> {
                    uiState = uiState.copy(
                        registrationError = task.errorMessage,
                        verificationInProgress = false
                    )
                }
                is Response.Success -> {
                    uiState = uiState.copy(
                        enableVerifyButton = false,
                        verificationId = task.data,
                        verificationInProgress = false
                    )
                    Log.d("#", task.data)
                }
                Response.Undefined -> Unit
            }
        }
    }



    private fun validatePhoneNumber( phoneNumber: String ) {
        val task = validatePhoneNumberUseCase.invoke( phoneNumber )

        when( task ) {
            PhoneNumberInputValidationType.EmptyField -> {
                uiState = uiState.copy(
                    phoneNumberError = "The phone number is required!",
                    enableVerifyButton = false
                )
            }

            PhoneNumberInputValidationType.Invalid -> {
                uiState = uiState.copy(
                    phoneNumberError = "The phone number is invalid!",
                    enableVerifyButton = false
                )
            }

            PhoneNumberInputValidationType.NoCountryCode -> {
                uiState = uiState.copy(
                    phoneNumberError = "A phone should start with the country code!",
                    enableVerifyButton = false
                )
            }

            PhoneNumberInputValidationType.Valid -> {
                uiState = uiState.copy(
                    phoneNumberError = "",
                    enableVerifyButton = true
                )
            }
        }
    }

    private fun getCurrentlyLoggedInStudent( studentId: String ) = viewModelScope.launch {
        getCurrentlyLoggedInStudentUseCase.invoke( studentId ).collectLatest { response ->
            when( response ) {
                is Response.Error -> {
                    uiState = uiState.copy(
                        changingUser = false,
                        finishingInProgress = false,
                        registrationError = response.errorMessage
                    )
                }
                Response.Undefined -> Unit
                is Response.Success -> {
                    if ( response.data == null ) {
                        uiState = uiState.copy(
                            isAuthenticationSuccessfulNewStudent = true
                        )
                    } else {
                        uiState = uiState.copy(
                            oldStudent = response.data
                        )
                    }
                }
            }
        }
    }

//    override fun onCleared() {
//        activityRef.clear()
//        super.onCleared()
//    }

}