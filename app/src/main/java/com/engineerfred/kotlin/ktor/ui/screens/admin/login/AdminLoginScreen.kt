package com.engineerfred.kotlin.ktor.ui.screens.admin.login

import android.app.Activity
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.engineerfred.kotlin.ktor.R
import com.engineerfred.kotlin.ktor.common.CustomButtonComponent
import com.engineerfred.kotlin.ktor.ui.screens.admin.login.components.LoginContainer
import com.engineerfred.kotlin.ktor.ui.theme.SeaGreen
import com.engineerfred.kotlin.ktor.ui.viewModel.AdminLoginScreenViewModel
import com.engineerfred.kotlin.ktor.ui.viewModel.SharedViewModel

@Composable
fun AdminLoginScreen(
    onLoginSuccessful: () -> Unit,
    onNotAdminClick: () -> Unit,
    viewModel: AdminLoginScreenViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel
) {

    val fm = LocalFocusManager.current

    val uiState = viewModel.uiState.collectAsState().value

    val colorScheme = MaterialTheme.colorScheme
    val view = LocalView.current
    val isDarkTheme = isSystemInDarkTheme()

    LaunchedEffect(key1 = Unit) {
        val window = (view.context as Activity).window
        window.statusBarColor = colorScheme.surface.toArgb()
        window.navigationBarColor = colorScheme.surface.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDarkTheme
        WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !isDarkTheme
    }

    if ( uiState.notAdminClickSuccess ) onNotAdminClick.invoke()

    if (  uiState.admin != null ) { //admin won't be null after successful login
        sharedViewModel.addAdmin( uiState.admin )
        onLoginSuccessful.invoke()
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
                text = "Login, Admin",
                fontFamily = Font(R.font.lexend_medium).toFontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        }

        LoginContainer(
            modifier = Modifier.padding(top = 100.dp, start = 16.dp, end = 16.dp),
            emailValue = { uiState.emailValue },
            emailValueError = { uiState.emailValueError ?: "" },
            passwordValue = { uiState.passwordValue },
            onEmailChanged = {
                  viewModel.onEvent( AdminLoginScreenEvents.EmailChanged(it) )
            },
            onPasswordChanged = {
                viewModel.onEvent( AdminLoginScreenEvents.PasswordChanged(it) )
            }
        )

        CustomButtonComponent(
            modifier = Modifier.padding(top = 60.dp, start = 60.dp, end = 60.dp),
            btnModifier = Modifier.fillMaxWidth(),
            text = "Login",
            isLoading = { uiState.loginInProgress },
            enabled = { viewModel::validateForm.invoke() && !uiState.changingUser },
            cornerSize = 10.dp,
            onClick = {
                fm.clearFocus()
                viewModel.onEvent( AdminLoginScreenEvents.LoginButtonClicked )
            }
        )

        Spacer(modifier = Modifier.size(44.dp))

        Text(
            text = "Not an admin?",
            modifier = Modifier
                .clickable {
                    viewModel.onEvent(AdminLoginScreenEvents.NotAdminClicked)
                }
                .padding(end = 20.dp, start = 20.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            textDecoration = TextDecoration.Underline,
            fontSize = 14.sp,
            fontFamily = Font(R.font.lexend_bold).toFontFamily(),
            color =  if (isSystemInDarkTheme()) Color.LightGray else SeaGreen
        )

        uiState.loginError?.let {
            Text(
                text = it,
                modifier = Modifier
                    .padding(top = 44.dp, end = 20.dp, start = 20.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontFamily = Font(R.font.lexend_medium).toFontFamily(),
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}