package com.engineerfred.kotlin.ktor.ui.screens.admin.create_update

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.DoneAll
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.engineerfred.kotlin.ktor.common.SuccessIndicator
import com.engineerfred.kotlin.ktor.ui.model.AnswerType
import com.engineerfred.kotlin.ktor.ui.screens.admin.create_update.components.AddQuestionContainer
import com.engineerfred.kotlin.ktor.ui.viewModel.CreateUpdateScreenViewModel

@Composable
fun CreateUpdateScreen(
    questionId: String?,
    subject: String?,
    onBackClicked: () -> Unit,
    viewModel: CreateUpdateScreenViewModel = hiltViewModel()
) {

    val screenState = viewModel.uiState

    if ( screenState.updateCompleted ) {
        onBackClicked.invoke()
    }

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
                    text = if( questionId == null ) "Set question" else "Update question",
                    fontFamily = Font(R.font.lexend_bold).toFontFamily(),
                    fontSize = 17.sp,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent( CreateUpdateScreenEvents.SaveClicked )
                },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Rounded.Save,
                    contentDescription = stringResource(R.string.save_button)
                )
            }
        }
    ) {
        when {
            screenState.isLoading -> {
                Box(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    ProgressIndicator(text = "...")
                }
            }

            screenState.remoteErrorMessage.isNotEmpty() -> {
                Box(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    ErrorIndicator(errorText = screenState.remoteErrorMessage)
                }

            }

            else -> {

                Column(
                    Modifier
                        .padding(it)
                        .verticalScroll(rememberScrollState())
                ) {

                    AddQuestionContainer(
                        levelButtonText = screenState.levelButtonText,
                        subjectButtonText = subject?.replaceFirstChar { it.titlecase() } ?: screenState.subject!!.replaceFirstChar { it.titlecase() },
                        onLevelButtonClicked = {
                            if ( !screenState.addingQuestionInProgress ) {
                                viewModel.onEvent( CreateUpdateScreenEvents.LevelButtonClicked )
                            }
                        },
                        onDismissMenuClicked = {
                            viewModel.onEvent( CreateUpdateScreenEvents.LevelButtonClicked )
                        },
                        onMenuItemClicked = {
                            viewModel.onEvent( CreateUpdateScreenEvents.LevelSelected(it) )
                        },
                        showDropDownMenu = screenState.showDropDowMenu,
                        questionTextValue = screenState.questionText,
                        answerChoiceTextValue = screenState.answerChoice,
                        correctAnswerTextValue = screenState.correctAnswer,
                        onQuestionTextChanged = {
                            if ( !screenState.addingQuestionInProgress ) {
                                viewModel.onEvent( CreateUpdateScreenEvents.QuestionTextChanged(it) )
                            }
                        },
                        onAnswerChoiceTextChanged = {
                            if ( !screenState.addingQuestionInProgress ) {
                                viewModel.onEvent( CreateUpdateScreenEvents.AnswerChoiceTextChanged(it) )
                            }
                        },
                        onQuestionCorrectAnswerTextChanged = {
                            if ( !screenState.addingQuestionInProgress ) {
                                viewModel.onEvent( CreateUpdateScreenEvents.CorrectAnswerTextChanged(it) )
                            }
                        },
                        onAddAnswerClicked = {
                            if ( !screenState.addingQuestionInProgress ) {
                                viewModel.onEvent( CreateUpdateScreenEvents.AddAnswerChoiceClicked )
                            }
                        },
                        enabledCorrectAnswerTextField = screenState.listOfAnswerChoicesWithLetters.isEmpty()
                    )

                    AnimatedVisibility( visible = screenState.listOfAnswerChoicesWithLetters.isNotEmpty()) {
                        val gridCellsCount = if ( screenState.answerType == AnswerType.Short.name ) 2 else 1
                        val gridCellHeight = if ( screenState.answerType == AnswerType.Short.name ) 100.dp else 215.dp
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Select the correct answer",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                fontFamily = Font(R.font.lexend_bold).toFontFamily(),
                                fontSize = 15.sp,
                                color = Color.DarkGray,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.size(10.dp))
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(gridCellsCount),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(gridCellHeight)
                            ) {
                                items( screenState.listOfAnswerChoicesWithLetters ) {
                                    Row(
                                        modifier = Modifier
                                            .clickable {
                                                if (!screenState.addingQuestionInProgress) {
                                                    viewModel.onEvent(
                                                        CreateUpdateScreenEvents.SelectedCorrectAnswer(it)
                                                    )
                                                }
                                            }
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = it,
                                            modifier = Modifier
                                                .weight(1f),
                                            textAlign = TextAlign.Start,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Icon(
                                            imageVector = Icons.Rounded.DoneAll,
                                            contentDescription = stringResource(R.string.remove_answer),
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                            AnimatedVisibility(visible = !screenState.addingQuestionInProgress ) {
                                Text(
                                    text = "Remove answer choices?",
                                    fontFamily = Font(R.font.lexend_medium).toFontFamily(),
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .clickable {
                                            viewModel.onEvent(CreateUpdateScreenEvents.DeleteChoicesList)
                                        }
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    //for saving in progress
                    if ( screenState.addingQuestionInProgress ) {
                        ProgressIndicator(text = if ( !screenState.isEditMode ) "Saving question..." else "Updating question...")
                    }

                    //for error message
                    if ( screenState.localErrorMessage.isNotEmpty()  ) {
                        ErrorIndicator(errorText = screenState.localErrorMessage)
                    }

                    //for successMessage
                    AnimatedVisibility( visible = screenState.successMessage.isNotEmpty() ) {
                        SuccessIndicator(
                            text = screenState.successMessage,
                            onDoneClicked = onBackClicked,
                            onSetAnotherQuestionClicked = {
                                viewModel.onEvent( CreateUpdateScreenEvents.SetAnotherQuestionButtonClicked )
                            }
                        )
                    }
                }
            }
        }
    }
}