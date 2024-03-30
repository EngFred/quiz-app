package com.engineerfred.kotlin.ktor.ui.screens.student.register

import android.app.Activity
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.engineerfred.kotlin.ktor.ui.theme.DarkSlateGrey
import com.engineerfred.kotlin.ktor.ui.theme.SeaGreen
import com.engineerfred.kotlin.ktor.ui.viewModel.SharedViewModel
import com.engineerfred.kotlin.ktor.ui.viewModel.StudentRegisterScreenViewModel

@Composable
fun StudentRegisterScreen(
    viewModel: StudentRegisterScreenViewModel = hiltViewModel(),
    onNewStudentAuthenticationSuccess: () -> Unit,
    onOldStudentAuthenticationSuccess: () -> Unit,
    onNotStudentClicked: () -> Unit,
    sharedViewModel: SharedViewModel
) {

    val screenState = viewModel.uiState
    val activity = LocalContext.current as Activity
    val fm = LocalFocusManager.current
    val context = LocalContext.current

    LaunchedEffect(key1 = screenState.isAuthenticationSuccessfulNewStudent) {
        if ( screenState.isAuthenticationSuccessfulNewStudent && screenState.oldStudent == null ) {
            onNewStudentAuthenticationSuccess.invoke()
        }
    }

    LaunchedEffect(key1 = screenState.oldStudent) {
        if ( !screenState.isAuthenticationSuccessfulNewStudent && screenState.oldStudent != null ) {
            sharedViewModel.addStudent( screenState.oldStudent )
            onOldStudentAuthenticationSuccess.invoke()
            Toast.makeText(context, "Welcome back ${screenState.oldStudent.name}!", Toast.LENGTH_LONG).show()
        }
    }

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
                    if (!screenState.changingUser && !screenState.verificationInProgress && !screenState.finishingInProgress) {
                        viewModel.onEvent(StudentRegisterScreenEvents.NotStudentClicked)
                    }
                }
                .padding(end = 20.dp, start = 20.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            textDecoration = TextDecoration.Underline,
            fontSize = 14.sp,
            fontFamily = Font(R.font.lexend_bold).toFontFamily(),
            color = if (isSystemInDarkTheme()) DarkSlateGrey else SeaGreen
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