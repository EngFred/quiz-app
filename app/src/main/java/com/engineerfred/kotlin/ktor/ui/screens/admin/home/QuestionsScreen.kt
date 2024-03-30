package com.engineerfred.kotlin.ktor.ui.screens.admin.home

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.engineerfred.kotlin.ktor.R
import com.engineerfred.kotlin.ktor.common.ErrorIndicator
import com.engineerfred.kotlin.ktor.common.ProgressIndicator
import com.engineerfred.kotlin.ktor.ui.screens.admin.home.components.QuestionsList
import com.engineerfred.kotlin.ktor.ui.theme.DarkSlateGrey
import com.engineerfred.kotlin.ktor.ui.viewModel.QuestionsScreenViewModel
import java.util.Locale

@Composable
fun QuestionsScreen(
    subject: String,
    onQuestionClicked:  (id: String) -> Unit,
    onBackClicked:  () -> Unit,
    onAddQuestionClicked:  (subject: String) -> Unit,
    viewModel: QuestionsScreenViewModel = hiltViewModel()
) {
    val screenState = viewModel.uiState

    Scaffold(
        topBar = {

            Row(
                Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .fillMaxWidth()
                    .padding(start = 10.dp, top = 10.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = { onBackClicked.invoke() }) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = stringResource( R.string.back_button),
                        tint = Color.White
                    )
                }
                Text(
                    text = "${subject.replaceFirstChar { it.titlecase(Locale.ROOT) }} Desk",
                    fontFamily = Font(R.font.lexend_bold).toFontFamily(),
                    fontSize = 18.sp,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onAddQuestionClicked.invoke(subject)
                },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(R.string.add_icon)
                )
            }
        }
    ) {

        val linearGradient = Brush.linearGradient(
            colors = listOf(if (isSystemInDarkTheme()) DarkSlateGrey else Color.White, MaterialTheme.colorScheme.primary),
            start = Offset.Zero,
            end = Offset.Infinite
        )

        Box(
            modifier = Modifier
                .background(linearGradient)
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ) {
            when {
                screenState.isLoading -> {
                    ProgressIndicator(text = "", color = Color.White)
                }
                screenState.errorMessage.isNotEmpty() -> {
                    ErrorIndicator(errorText = screenState.errorMessage)
                }
                screenState.errorMessage.isEmpty() && screenState.questions.isEmpty() && !screenState.isLoading -> {
                    Info(text = "There are no $subject questions in the database yet")
                }
                else -> {
                    //Info(text = "Received ${screenState.questions.size} $subject questions")
                    QuestionsList(
                        questions = screenState.questions,
                        onQuestionClick = onQuestionClicked,
                        modifier = Modifier.background(Color.White)
                    )
                }
            }
        }
    }
}

@Composable
private fun Info(text: String) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Rounded.Info,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(54.dp)
        )
        Spacer(modifier = Modifier.size(7.dp))
        Text(
            text = text,
            fontFamily = Font(R.font.lexend_bold).toFontFamily(),
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}