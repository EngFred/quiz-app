package com.engineerfred.kotlin.ktor.ui.screens.student.questions

import android.app.Activity
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.engineerfred.kotlin.ktor.R
import com.engineerfred.kotlin.ktor.common.CustomButtonComponent
import com.engineerfred.kotlin.ktor.common.ErrorIndicator
import com.engineerfred.kotlin.ktor.common.LevelIndicator
import com.engineerfred.kotlin.ktor.common.ProgressIndicator
import com.engineerfred.kotlin.ktor.domain.model.QuestionState
import com.engineerfred.kotlin.ktor.ui.model.Subject
import com.engineerfred.kotlin.ktor.ui.screens.student.questions.components.ChoiceCard
import com.engineerfred.kotlin.ktor.ui.screens.student.questions.components.LevelSelectionContainer
import com.engineerfred.kotlin.ktor.ui.screens.student.questions.components.QuestionCard
import com.engineerfred.kotlin.ktor.ui.theme.Charcoal
import com.engineerfred.kotlin.ktor.ui.theme.DarkSlateGrey
import com.engineerfred.kotlin.ktor.ui.viewModel.QuizQuestionsViewModel
import com.engineerfred.kotlin.ktor.ui.viewModel.SharedViewModel

@Composable
fun QuizQuestionsScreen(
    studentLevel: String,
    subject: String,
    viewModel: QuizQuestionsViewModel = hiltViewModel(),
    onBackClicked: () -> Unit,
    sharedViewModel: SharedViewModel,
    onShowPerformance: () -> Unit
) {

    val view = LocalView.current
    val activity = (LocalContext.current as? Activity)
    val window = (view.context as Activity).window
    
    val uiState = viewModel.uiState

    val colorScheme = MaterialTheme.colorScheme

    LaunchedEffect(key1 = Unit) {
        window.statusBarColor = colorScheme.surface.toArgb()
        window.navigationBarColor = colorScheme.surface.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = true
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when {
            uiState.isLoading -> {
                ProgressIndicator(text = "...", modifier = Modifier.align(Alignment.Center))
            }
            uiState.serverError != null -> {
                ErrorIndicator(errorText = uiState.serverError,modifier = Modifier.align(Alignment.Center) )
            }
            uiState.levelEnded -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 100.dp, bottom = 10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Level Ended!")
                    Spacer(modifier = Modifier.size(7.dp))
                    Text(text = "${uiState.correctAnswersCount}/${uiState.listOfTenQuestions.size}", fontSize = 45.sp, fontWeight = FontWeight.ExtraBold)
                    Spacer(modifier = Modifier.size(7.dp))
                    if ( uiState.correctAnswersCount >= 7 ) {
                        Text(text = "Congratulations! You passed.")
                        Spacer(modifier = Modifier.size(7.dp))
                        CustomButtonComponent(
                            text = "Repeat Level",
                            backGroundColor = if ( isSystemInDarkTheme() ) DarkSlateGrey else MaterialTheme.colorScheme.primary,
                            onClick = {
                                if ( sharedViewModel.answeredQuestionsState.isNotEmpty() ) {
                                    sharedViewModel.answeredQuestionsState.removeAll(sharedViewModel.answeredQuestionsState)
                                }
                                viewModel.onEvent( QuizQuestionsScreenEvents.RepeatLevelClicked )
                            },
                            cornerSize = 6.dp,
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                        CustomButtonComponent(
                            text = "Continue to the next Level",
                            backGroundColor = if ( isSystemInDarkTheme() ) DarkSlateGrey else MaterialTheme.colorScheme.primary,
                            onClick = {
                                if ( sharedViewModel.answeredQuestionsState.isNotEmpty() ) {
                                    sharedViewModel.answeredQuestionsState.removeAll(sharedViewModel.answeredQuestionsState)
                                }
                                viewModel.onEvent( QuizQuestionsScreenEvents.ContinueToNexLevelClicked )
                            },
                            enabled = { uiState.enableContinueToNextLevelButton },
                            cornerSize = 6.dp,
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                        Text(text = "Check performance", modifier = Modifier.clickable { onShowPerformance() })
                    } else {
                        Text(text = "You Failed!")
                        Spacer(modifier = Modifier.size(7.dp))
                        CustomButtonComponent(
                            text = "Try again",
                            backGroundColor = if ( isSystemInDarkTheme() ) DarkSlateGrey else MaterialTheme.colorScheme.primary,
                            onClick = {
                                if ( sharedViewModel.answeredQuestionsState.isNotEmpty() ) {
                                    sharedViewModel.answeredQuestionsState.removeAll(sharedViewModel.answeredQuestionsState)
                                }
                                viewModel.onEvent( QuizQuestionsScreenEvents.RepeatLevelClicked )
                            },
                            cornerSize = 6.dp,
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                        Text(text = "Check performance", modifier = Modifier.clickable { onShowPerformance() })
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    if ( uiState.upgradingLevel ) {
                        LinearProgressIndicator(  modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 200.dp)
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                    }
                    if ( uiState.upgradingLevelError != null ) {
                        Text(text = uiState.upgradingLevelError, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.size(10.dp))
                    }
                    Text(
                        modifier = Modifier.clickable { activity?.finish() },
                        text = "Quit Quiz",
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )
                }
            }
            uiState.timeUp -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 100.dp, bottom = 10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Time Up!", fontSize = 25.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 10.dp))
                        Text(text = "Your too slow.")
                    }
                    Spacer(modifier = Modifier.size(7.dp))
                    Text(text = "${uiState.correctAnswersCount}/${uiState.listOfTenQuestions.size}", fontSize = 45.sp, fontWeight = FontWeight.ExtraBold)
                    Spacer(modifier = Modifier.size(7.dp))
                    if ( uiState.correctAnswersCount >= 7 ) {
                        Text(text = "Congratulations! You passed.")
                        Spacer(modifier = Modifier.size(7.dp))
                        CustomButtonComponent(
                            text = "Repeat Level",
                            backGroundColor = if ( isSystemInDarkTheme() ) DarkSlateGrey else MaterialTheme.colorScheme.primary,
                            onClick = {
                                if ( sharedViewModel.answeredQuestionsState.isNotEmpty() ) {
                                    sharedViewModel.answeredQuestionsState.removeAll(sharedViewModel.answeredQuestionsState)
                                }
                                viewModel.onEvent( QuizQuestionsScreenEvents.RepeatLevelClicked )
                            },
                            cornerSize = 6.dp,
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                        CustomButtonComponent(
                            text = "Continue to the next Level",
                            backGroundColor = if ( isSystemInDarkTheme() ) DarkSlateGrey else MaterialTheme.colorScheme.primary,
                            enabled = { uiState.enableContinueToNextLevelButton },
                            onClick = {
                                if ( sharedViewModel.answeredQuestionsState.isNotEmpty() ) {
                                    sharedViewModel.answeredQuestionsState.removeAll(sharedViewModel.answeredQuestionsState)
                                }
                                viewModel.onEvent( QuizQuestionsScreenEvents.ContinueToNexLevelClicked)
                            },
                            cornerSize = 6.dp,
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                        Text(text = "Check performance", modifier = Modifier.clickable { onShowPerformance() })
                    } else {
                        Text(text = "You Failed!")
                        Spacer(modifier = Modifier.size(7.dp))
                        CustomButtonComponent(
                            text = "Try again",
                            backGroundColor = if ( isSystemInDarkTheme() ) DarkSlateGrey else MaterialTheme.colorScheme.primary,
                            onClick = {
                                if ( sharedViewModel.answeredQuestionsState.isNotEmpty() ) {
                                    sharedViewModel.answeredQuestionsState.removeAll(sharedViewModel.answeredQuestionsState)
                                }
                                viewModel.onEvent( QuizQuestionsScreenEvents.RepeatLevelClicked )
                            },
                            cornerSize = 6.dp,
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                        Text(text = "Check performance", modifier = Modifier.clickable { onShowPerformance() })
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    if ( uiState.upgradingLevel ) {
                        LinearProgressIndicator(  modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 200.dp)
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                    }
                    if ( uiState.upgradingLevelError != null ) {
                        Text(text = uiState.upgradingLevelError, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.size(10.dp))
                    }
                    Text(
                        modifier = Modifier.clickable { activity?.finish() },
                        text = "Quit Quiz",
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )
                }
            }
            uiState.quizCompleted -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 10.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp),
                    ) {
                        Text(
                            text = "Quiz Completed! - ",
                            fontSize = 25.sp,
                            maxLines = 1,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                        Text(
                            text = "${uiState.correctAnswersCount}/${uiState.listOfTenQuestions.size}",
                            fontSize = 21.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if ( !isSystemInDarkTheme() ) MaterialTheme.colorScheme.primary else DarkSlateGrey
                        )
                    }
                    Text(
                        text = "Thank you for participating!...Play again? Choose a level.",
                        maxLines = 2,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.size(15.dp))

                    LevelSelectionContainer(
                        onCard1Clicked = {
                            if ( sharedViewModel.answeredQuestionsState.isNotEmpty() ) {
                                sharedViewModel.answeredQuestionsState.removeAll(sharedViewModel.answeredQuestionsState)
                            }
                              viewModel.onEvent( QuizQuestionsScreenEvents.LevelCardSelected(it) )
                        },
                        onCard2Clicked = {
                            if ( sharedViewModel.answeredQuestionsState.isNotEmpty() ) {
                                sharedViewModel.answeredQuestionsState.removeAll(sharedViewModel.answeredQuestionsState)
                            }
                            viewModel.onEvent( QuizQuestionsScreenEvents.LevelCardSelected(it) )
                        },
                        onCard3Clicked = {
                            if ( sharedViewModel.answeredQuestionsState.isNotEmpty() ) {
                                sharedViewModel.answeredQuestionsState.removeAll(sharedViewModel.answeredQuestionsState)
                            }
                            viewModel.onEvent( QuizQuestionsScreenEvents.LevelCardSelected(it) )
                        }
                    )

                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        modifier = Modifier.clickable { activity?.finish() },
                        text = "Quit Quiz",
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )
                }
            }
            else -> {

                var selectedIndex by rememberSaveable {
                    mutableIntStateOf(-1 )
                }

                var selectedAnswer by rememberSaveable {
                    mutableStateOf("null")
                }

                if ( uiState.questions.size <= 10 ) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp), contentAlignment = Alignment.Center
                    ) {
                        Text(text = buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Not yet enough questions!\n")
                            }
                            append("Come back letter.")
                        }, textAlign = TextAlign.Center)
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .fillMaxSize()
                    ) {
                        if (  uiState.currentQuestion != null ) {
                            Box(modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(top = 10.dp, start = 10.dp, end = 20.dp)
                            ) {
                                IconButton(
                                    onClick = { onBackClicked.invoke() },
                                    modifier = Modifier.align(Alignment.TopStart)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.ArrowBackIosNew,
                                        contentDescription = stringResource(R.string.back)
                                    )
                                }
                                Text(
                                    text = "${uiState.currentQuestionIndex+1}/${uiState.listOfTenQuestions.size}",
                                    fontFamily = Font(R.font.lexend_bold).toFontFamily(),
                                    modifier = Modifier.align( Alignment.Center )
                                )
                                Text(
                                    text = if (subject == Subject.English.name) "ENG" else "MAT",
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.align(
                                        Alignment.TopEnd
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.size(20.dp))
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                QuestionCard(
                                    questionText = uiState.currentQuestion.question,
                                    uiState.timeLeft,
                                    onTimeUp = { viewModel.onEvent(QuizQuestionsScreenEvents.TimeUp) }
                                )
                                Spacer(modifier = Modifier.size(20.dp))
                                if ( uiState.currentQuestion.answerChoices != null ) {

                                    uiState.currentQuestion.answerChoices.forEachIndexed { currentIndex, s ->
                                        ChoiceCard(
                                            answerChoice = s, onChoiceClick = {
                                                selectedIndex = currentIndex
                                                selectedAnswer = it
                                                viewModel.onEvent( QuizQuestionsScreenEvents.AnswerSelected(it) )
                                            }, selected = currentIndex == selectedIndex
                                        )
                                    }
                                } else {
                                    TextField(
                                        value = uiState.userAnswer,
                                        onValueChange = { viewModel.onEvent( QuizQuestionsScreenEvents.UserAnswerChanged(it) ) },
                                        placeholder = { Text(text = "Type your answer here") },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = TextFieldDefaults.colors(
                                            unfocusedContainerColor = Color.Transparent,
                                            focusedContainerColor = Color.Transparent,
                                            focusedIndicatorColor = if ( !isSystemInDarkTheme() ) MaterialTheme.colorScheme.primary else DarkSlateGrey,
                                            unfocusedIndicatorColor = if ( !isSystemInDarkTheme() ) MaterialTheme.colorScheme.primary else DarkSlateGrey
                                        ), keyboardOptions = KeyboardOptions( imeAction = ImeAction.Done )
                                    )
                                }
                            }
                            Column(
                                modifier = Modifier.padding(14.dp)
                            ) {
                                CustomButtonComponent(
                                    text = "Next",
                                    backGroundColor = if (!isSystemInDarkTheme() ) MaterialTheme.colorScheme.primary else Charcoal,
                                    onClick = {
                                        selectedIndex = -1
                                        sharedViewModel.addAnsweredQuestion(
                                            QuestionState(
                                                answeredQuestion = uiState.currentQuestion.question,
                                                correctAnswer = uiState.currentQuestion.answer,
                                                givenAnswer = if (uiState.currentQuestion.answerChoices == null){
                                                    uiState.userAnswer.lowercase().trim()
                                                } else {
                                                    selectedAnswer
                                                },
                                                isPassed = if (uiState.currentQuestion.answerChoices == null) { //for questions that require typing
                                                    uiState.userAnswer.lowercase().trim() == uiState.currentQuestion.answer
                                                } else {
                                                    //for multi choice questions
                                                    selectedAnswer.lowercase() == uiState.currentQuestion.answer
                                                }
                                            )
                                        )
                                        viewModel.onEvent( QuizQuestionsScreenEvents.NextButtonClicked )
                                    },
                                    enabled = {
                                        if (uiState.currentQuestion.answerChoices != null) {
                                            uiState.enableNextButton
                                        } else {
                                            uiState.userAnswer.isNotEmpty()
                                        }
                                    },
                                    btnModifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 55.dp),
                                    cornerSize = 6.dp
                                )
                                Box(modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp), contentAlignment = Alignment.Center){
                                    LevelIndicator(
                                        level = uiState.studentLevel!!,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 15.sp,
                                        dotSize = 12.dp,
                                        textColor = if ( isSystemInDarkTheme() ) Color.White else Color.DarkGray
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}