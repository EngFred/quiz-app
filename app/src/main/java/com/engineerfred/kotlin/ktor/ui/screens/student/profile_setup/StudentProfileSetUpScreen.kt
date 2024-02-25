package com.engineerfred.kotlin.ktor.ui.screens.student.profile_setup

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.engineerfred.kotlin.ktor.R
import com.engineerfred.kotlin.ktor.common.CustomButtonComponent
import com.engineerfred.kotlin.ktor.common.ErrorIndicator
import com.engineerfred.kotlin.ktor.ui.screens.student.profile_setup.components.StudentProfileSetupContainer
import com.engineerfred.kotlin.ktor.ui.viewModel.SharedViewModel
import com.engineerfred.kotlin.ktor.ui.viewModel.StudentProfileSetupScreenViewModel

@Composable
fun StudentProfileSetupScreen(
    viewModel: StudentProfileSetupScreenViewModel = hiltViewModel(),
    onRegistrationSuccess: () -> Unit,
    sharedViewModel: SharedViewModel
) {

    val fm = LocalFocusManager.current
    val context =  LocalContext.current
    val screenState = viewModel.uiState

    if ( screenState.registrationSuccessful ) {
        sharedViewModel.addStudent( screenState.student )
        onRegistrationSuccess.invoke()
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            try {
                if ( it != null ) {
                    Log.d("#", "Selected image is $it")
                    viewModel.onEvent( StudentProfileSetupScreenEvents.ProfileImageUrlChanged(it) )
                } else {
                    Log.d("#", "Selected nothing!")
                }
            } catch ( ex: Exception ) {
                Log.d("#", "$ex")
            }
        }
    )

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
                text = "Almost there",
                fontFamily = Font(R.font.lexend_medium).toFontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        }

        Spacer(modifier = Modifier.size(100.dp))

        StudentProfileSetupContainer(
            onNameChanged = {
                if ( !screenState.registrationInProgress ) {
                    viewModel.onEvent( StudentProfileSetupScreenEvents.NameChanged(it) )
                }
            },
            nameTextValue = { screenState.nameValue },
            nameErrorTextValue = screenState.nameValueError,
            onBioChanged = {
                if ( !screenState.registrationInProgress ) {
                    viewModel.onEvent( StudentProfileSetupScreenEvents.BioChanged(it) )
                }
            },
            bioTextValue = { screenState.bioValue },
            profileImage = screenState.profileImageUrl.toString(),
            onCameraClicked = {
                if ( !screenState.registrationInProgress ) {
                    galleryLauncher.launch("image/*")
                }
            }
        )

        CustomButtonComponent(
            modifier = Modifier.padding(top = 60.dp, start = 60.dp, end = 60.dp),
            btnModifier = Modifier.fillMaxWidth(),
            text = "Register",
            backGroundColor =  MaterialTheme.colorScheme.primary,
            isLoading = { screenState.registrationInProgress },
            enabled = { screenState.nameValue.isNotEmpty() && screenState.nameValueError.isEmpty() },
            cornerSize = 10.dp,
            onClick = {
                fm.clearFocus()
                viewModel.onEvent( StudentProfileSetupScreenEvents.RegisterButtonClicked( context ) )
            }
        )

        Spacer(modifier = Modifier.size(10.dp))
        AnimatedVisibility(visible = screenState.registrationError != null ) {
            ErrorIndicator(errorText = screenState.registrationError!!)
        }
    }

}