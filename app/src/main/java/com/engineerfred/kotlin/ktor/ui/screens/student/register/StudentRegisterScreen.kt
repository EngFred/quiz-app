package com.engineerfred.kotlin.ktor.ui.screens.student.register

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.engineerfred.kotlin.ktor.R
import com.engineerfred.kotlin.ktor.common.ErrorIndicator
import com.engineerfred.kotlin.ktor.ui.screens.student.register.components.StudentRegisterContainer
import com.engineerfred.kotlin.ktor.ui.viewModel.StudentRegisterScreenViewModel

@Composable
fun StudentRegisterScreen(
    viewModel: StudentRegisterScreenViewModel = hiltViewModel(),
    onAuthenticationSuccess: () -> Unit,
    onNotStudentClicked: () -> Unit
) {

    val screenState = viewModel.uiState
    val activity = LocalContext.current as Activity
    val fm = LocalFocusManager.current


    if ( screenState.isAuthenticationSuccessful ) onAuthenticationSuccess.invoke()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "Register, Student",
                fontFamily = Font(R.font.lexend_medium).toFontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        }

        Spacer(modifier = Modifier.size(120.dp))

        StudentRegisterContainer(
            phoneNumberValue = { screenState.phoneNumber },
            phoneNumberValueError = screenState.phoneNumberError,
            onPhoneNumberChanged = {
                 viewModel.onEvent( StudentRegisterScreenEvents.PhoneNumberChanged(it) )
            },
            onVerifyButtonClicked = {
                fm.clearFocus()
                viewModel.onEvent( StudentRegisterScreenEvents.VerifyButtonClicked( activity ) )
            },
            onDoneButtonClicked = {
                fm.clearFocus()
                viewModel.onEvent( StudentRegisterScreenEvents.DoneButtonClicked )
            },
            ottpTextValue = { screenState.verificationCode },
            onOtpTextChange = { s, b ->
                viewModel.onEvent( StudentRegisterScreenEvents.VerificationCodeChanged(s) )
            },
            isVerifying = screenState.verificationInProgress,
            isFinishing = screenState.finishingInProgress,
            enableVerifyButton = screenState.enableVerifyButton  && !screenState.changingUser,
            enableDoneButton = screenState.verificationCode.isNotEmpty() && screenState.verificationId != null  && !screenState.changingUser && !screenState.verificationInProgress,
            enableCodeEntryTextField = screenState.verificationId != null,
            enablePhoneNumberEntryTextField = screenState.verificationId == null
        )
        Spacer(modifier = Modifier.size(30.dp))

        Text(
            text = "Not a student?",
            modifier = Modifier
                .clickable {
                    if ( !screenState.changingUser && !screenState.verificationInProgress && !screenState.finishingInProgress ) {
                        viewModel.onEvent(StudentRegisterScreenEvents.NotStudentClicked)
                    }
                }
                .padding(end = 20.dp, start = 20.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            textDecoration = TextDecoration.Underline,
            fontSize = 14.sp,
            fontFamily = Font(R.font.lexend_bold).toFontFamily(),
            color = MaterialTheme.colorScheme.primary
        )

        if ( screenState.notAStudentClickSuccess ) onNotStudentClicked.invoke()

        AnimatedVisibility(visible = screenState.notAStudentClickError != null ) {
            Spacer(modifier = Modifier.size(15.dp))
            ErrorIndicator(errorText = screenState.notAStudentClickError!!)
        }
        AnimatedVisibility(visible = screenState.registrationError.isNotEmpty() ) {
            Spacer(modifier = Modifier.size(15.dp))
            ErrorIndicator(errorText = screenState.registrationError)
        }

    }
}