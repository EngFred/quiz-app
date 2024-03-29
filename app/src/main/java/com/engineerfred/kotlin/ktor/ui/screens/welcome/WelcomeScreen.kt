package com.engineerfred.kotlin.ktor.ui.screens.welcome

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.engineerfred.kotlin.ktor.R
import com.engineerfred.kotlin.ktor.common.ProgressIndicator
import com.engineerfred.kotlin.ktor.ui.model.User
import com.engineerfred.kotlin.ktor.ui.viewModel.SharedViewModel
import com.engineerfred.kotlin.ktor.ui.viewModel.WelcomeScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(
    welcomeScreenViewModel: WelcomeScreenViewModel,
    sharedViewModel: SharedViewModel = viewModel(),
    navigateToStudentRegister: () -> Unit,
    navigateToAdminLogin: () -> Unit
) {
    val state = welcomeScreenViewModel.uiState.collectAsState().value

    if ( state.admin != null ) {
        sharedViewModel.addAdmin(newAdmin = state.admin)
    }
    
    val view = LocalView.current
    val colorScheme = MaterialTheme.colorScheme
    val isDarkTheme = isSystemInDarkTheme()
    
    LaunchedEffect(key1 = true) {
        val window = (view.context as Activity).window
        window.statusBarColor = colorScheme.surface.toArgb()
        window.navigationBarColor = colorScheme.surface.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDarkTheme
        WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !isDarkTheme
    }

    if ( state.error != null ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${state.error}",
                color = MaterialTheme.colorScheme.errorContainer,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    } else {
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ProgressIndicator(text = "...")
                }
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 15.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Welcome to QuizQuest!",
                        fontFamily = Font(R.font.lexend_bold).toFontFamily(),
                        fontSize = 22.sp
                    )
                    Spacer(modifier = Modifier.size(10.dp))
                    Text(text = "Dear User,\nThank you for choosing ${stringResource(id = R.string.app_name)} for your quiz needs. " +
                            "Before you proceed, here are some important points to note:", fontSize = 14.sp, lineHeight = 17.sp
                    )
                    Spacer(modifier = Modifier.size(14.dp))
                    Text(text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Admins ")
                        }
                        append("are responsible for setting up questions and managing the quiz environment.you will be required to login using your registered email and password.")
                    }, fontSize = 14.sp, lineHeight = 17.sp)
                    Spacer(modifier = Modifier.size(14.dp))
                    Text(text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Students ")
                        }
                        append("participate in quizzes by answering questions set by admins.you will be required to login using your registered phone number.")
                    }, fontSize = 14.sp, lineHeight = 17.sp)

                    Spacer(modifier = Modifier.size(10.dp))
                    Text(text = "Choose your role below to get started:", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.size(10.dp))
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            welcomeScreenViewModel.onEvent( WelcomeScreenEvents.CardClicked(User.Student.name.lowercase()) )
                            navigateToStudentRegister.invoke()
                        }
                    ) {
                        Row(
                            Modifier.padding(start = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "I'm a student", modifier = Modifier
                                .weight(1f)
                                .padding(end = 10.dp))
                            Image(
                                painter = painterResource(id = R.drawable.student),
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(70.dp)
                                    .clip(RoundedCornerShape(10.dp)),
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                    Spacer(modifier = Modifier.size(10.dp))
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = {
                            welcomeScreenViewModel.onEvent( WelcomeScreenEvents.CardClicked(User.Admin.name.lowercase()) )
                            navigateToAdminLogin.invoke()
                        }
                    ) {
                        Row(
                            Modifier.padding(start = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "I'm an admin", modifier = Modifier
                                .weight(1f)
                                .padding(end = 10.dp))
                            Image(
                                painter = painterResource(id = R.drawable.teacher),
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(70.dp)
                                    .clip(RoundedCornerShape(10.dp)),
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f).padding(10.dp))
                    Text(
                        text = "If you encounter any issues or have feedback, feel free to reach out to us.",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}