package com.engineerfred.kotlin.ktor.ui.screens.welcome

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.engineerfred.kotlin.ktor.R
import com.engineerfred.kotlin.ktor.common.CardRow
import com.engineerfred.kotlin.ktor.common.ProgressIndicator
import com.engineerfred.kotlin.ktor.ui.model.User
import com.engineerfred.kotlin.ktor.ui.viewModel.SharedViewModel
import com.engineerfred.kotlin.ktor.ui.viewModel.WelcomeScreenViewModel

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

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        if ( state.error != null ) {

            Text(
                text = "${state.error}",
                color = MaterialTheme.colorScheme.errorContainer,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

        } else {

            when( state.isLoading ) {
                true -> {
                   ProgressIndicator(text = "...")
                }
                false -> {

                    Text(
                        text = "Who are we dealing with.",
                        fontFamily = Font(R.font.lexend_bold).toFontFamily(),
                        fontSize = 19.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align( Alignment.TopStart )
                            .padding( horizontal = 28.dp , vertical = 10.dp),
                    )

                    CardRow(
                        card1Text = User.Student.name.uppercase(),
                        card2Text = User.Admin.name.uppercase(),
                        onCard1Click = {
                            welcomeScreenViewModel.onEvent( WelcomeScreenEvents.CardClicked(it) )
                            navigateToStudentRegister.invoke()
                        } ,
                        onCard2Click = {
                            welcomeScreenViewModel.onEvent( WelcomeScreenEvents.CardClicked(it) )
                            navigateToAdminLogin.invoke()
                        }
                    )
                }
            }
        }
    }
}