package com.engineerfred.kotlin.ktor.ui.screens.admin.add_admin

import android.app.Activity
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.engineerfred.kotlin.ktor.R
import com.engineerfred.kotlin.ktor.common.CustomButtonComponent
import com.engineerfred.kotlin.ktor.ui.screens.admin.add_admin.components.RegisterContainer
import com.engineerfred.kotlin.ktor.ui.viewModel.AdminRegisterScreenViewModel
import com.engineerfred.kotlin.ktor.util.restartApp

@Composable
fun AdminRegisterScreen(
    onRegistrationSuccessful: () -> Unit,
    viewModel: AdminRegisterScreenViewModel = hiltViewModel()
) {
    val fm = LocalFocusManager.current

    val uiState = viewModel.uiState.collectAsState().value

    val view = LocalView.current
    val colorScheme = MaterialTheme.colorScheme
    val isDarkTheme = isSystemInDarkTheme()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        val window = (view.context as Activity).window
        window.statusBarColor = colorScheme.surface.toArgb()
        window.navigationBarColor = colorScheme.surface.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDarkTheme
        WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !isDarkTheme
    }

    if (  uiState.adminEmail != null ) {
        restartApp(context)
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
                text = "Register Admin",
                fontFamily = Font(R.font.lexend_medium).toFontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        }
        RegisterContainer(
            modifier = Modifier.padding(top = 100.dp, start = 16.dp, end = 16.dp),
            lastNameValue = { uiState.lastNameValue },
            lastNameValueError = { uiState.lastNameValueError ?: "" },
            firstNameValue = { uiState.firstNameValue },
            firstNameValueError = { uiState.firstNameValueError ?: "" },
            emailValue = { uiState.emailValue },
            emailValueError = { uiState.emailValueError ?: "" },
            passwordValue = { uiState.passwordValue },
            passwordValueError = { uiState.passwordValueError ?: "" },
            rePasswordValue = { uiState.rePasswordValue },
            rePasswordValueError = { uiState.rePasswordValueError ?: "" },
            onEmailChanged = {
                 viewModel.onEvent( AdminRegisterScreenEvents.EmailChanged(it) )
            },
            onPasswordChanged = {
                viewModel.onEvent( AdminRegisterScreenEvents.PasswordChanged(it) )
            },
            onRePasswordChanged = {
                viewModel.onEvent( AdminRegisterScreenEvents.RePasswordChanged(it) )
            },
            onLastNameChanged = {
                viewModel.onEvent( AdminRegisterScreenEvents.LastNameChanged(it) )
            },
            onFirstNameChanged = {
                viewModel.onEvent( AdminRegisterScreenEvents.FirstNameChanged(it) )
            }
        )

        CustomButtonComponent(
            modifier = Modifier.padding(top = 60.dp, start = 60.dp, end = 60.dp),
            btnModifier = Modifier.fillMaxWidth(),
            text = "Register",
            isLoading = { uiState.registrationInProgress },
            enabled = { viewModel::validateForm.invoke() },
            cornerSize = 10.dp,
            onClick = {
                fm.clearFocus()
                viewModel.onEvent( AdminRegisterScreenEvents.RegisterButtonClicked )
            }
        )

        uiState.registrationError?.let {
            Spacer(modifier = Modifier.size(44.dp))
            Text(
                text = uiState.registrationError,
                modifier = Modifier
                    .padding(end = 20.dp, start = 20.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}