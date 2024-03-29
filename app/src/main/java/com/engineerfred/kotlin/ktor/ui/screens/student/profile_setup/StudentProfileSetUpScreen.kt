package com.engineerfred.kotlin.ktor.ui.screens.student.profile_setup

import android.Manifest
import android.os.Build
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun StudentProfileSetupScreen(
    viewModel: StudentProfileSetupScreenViewModel = hiltViewModel(),
    onRegistrationSuccess: () -> Unit,
    sharedViewModel: SharedViewModel
) {

    val fm = LocalFocusManager.current
    val context =  LocalContext.current
    val screenState = viewModel.uiState

    var isDialogVisible by rememberSaveable {
        mutableStateOf(false)
    }

    var textToShow by rememberSaveable {
        mutableStateOf("")
    }

    val galleryPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        rememberPermissionState(  Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED )
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState( Manifest.permission.READ_MEDIA_IMAGES )
    } else {
        rememberPermissionState(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

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

        Spacer(modifier = Modifier.size(60.dp))

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
                if ( galleryPermissionState.status.isGranted ) {
                    if ( !screenState.registrationInProgress ) {
                        galleryLauncher.launch("image/*")
                    }
                } else {
                    if (galleryPermissionState.status.shouldShowRationale) {
                        // If the user has denied the permission but the rationale can be shown, then gently explain why the app requires this permission
                        textToShow = "The app needs to access your phone gallery inorder to be able" +
                                " to select images as your profile picture. Please grant the permission."
                        isDialogVisible = true
                    } else {
                        // If it's the first time the user lands on this feature
                        galleryPermissionState.launchPermissionRequest()
                        if ( galleryPermissionState.status.isGranted ) {
                            Toast.makeText(context, "Permission granted!", Toast.LENGTH_SHORT).show()
                        }
                    }
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

        if ( isDialogVisible ) {
            AlertDialog(
                onDismissRequest = { isDialogVisible = false },
                confirmButton = {
                    Button(onClick = {
                        isDialogVisible = false
                        galleryPermissionState.launchPermissionRequest()
                    }, colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray,
                        contentColor = Color.Black
                    )) {
                        Text(text = "Grant Permission")
                    }
                },
                title = {
                    Text(text = "Permission Required!")
                },
                text = {
                    Text(text = textToShow)
                },
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = Color.White,
                textContentColor = Color.White
            )
        }
    }

}