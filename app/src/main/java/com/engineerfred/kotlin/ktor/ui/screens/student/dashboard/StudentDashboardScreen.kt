package com.engineerfred.kotlin.ktor.ui.screens.student.dashboard

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.engineerfred.kotlin.ktor.common.CardRow
import com.engineerfred.kotlin.ktor.ui.model.Subject
import com.engineerfred.kotlin.ktor.ui.viewModel.SharedViewModel

@Composable
fun StudentDashBoardScreen(
    onEnglishCardClicked: (subject: String, level: String) -> Unit,
    onMathCardClicked: (subject: String, level: String) -> Unit,
    sharedViewModel: SharedViewModel
) {

    val student = sharedViewModel.student

    val view = LocalView.current
    val colorScheme = MaterialTheme.colorScheme

    LaunchedEffect(key1 = Unit) {
        val window = (view.context as Activity).window
        window.statusBarColor = colorScheme.primary.toArgb()
        window.navigationBarColor = colorScheme.primary.toArgb()

        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        if ( sharedViewModel.answeredQuestionsState.isNotEmpty() ) {
            sharedViewModel.answeredQuestionsState.removeAll(sharedViewModel.answeredQuestionsState)
        }
    }

    student?.let {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 0.dp),
                verticalArrangement = Arrangement.Center,
            ) {

                CardRow(
                    card1Text = Subject.English.name.uppercase(),
                    card2Text = Subject.Mathematics.name.uppercase(),
                    onCard1Click = {
                        onEnglishCardClicked.invoke(Subject.English.name, student.englishLevel)
                    },
                    onCard2Click = {
                        onMathCardClicked.invoke(Subject.Mathematics.name, student.mathLevel)
                    }
                )

            }
        }
    }
}