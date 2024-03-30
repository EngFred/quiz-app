package com.engineerfred.kotlin.ktor.ui.screens.admin.create_update

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.DoneAll
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.engineerfred.kotlin.ktor.ui.theme.DarkSlateGrey
import com.engineerfred.kotlin.ktor.ui.theme.SeaGreen
import com.engineerfred.kotlin.ktor.ui.viewModel.CreateUpdateScreenViewModel

@Composable
fun CreateUpdateScreen(
    questionId: String?,
    subject: String?,
    onBackClicked: () -> Unit,
    viewModel: CreateUpdateScreenViewModel = hiltViewModel()
) {

    val screenState = viewModel.uiState
    val context = LocalContext.current

    LaunchedEffect(key1 = screenState.deleteError) {
        if ( !screenState.deleteError.isNullOrEmpty() ) {
            Toast.makeText(context, screenState.deleteError, Toast.LENGTH_LONG).show()
        }
    }

    if ( screenState.updateCompleted || screenState.deleteSuccessful ) {
        onBackClicked.invoke()
    }

    LaunchedEffect(key1 = screenState.deleteSuccessful) {
        if ( screenState.deleteSuccessful ) {
            Toast.makeText(context,"Question deleted successfully!", Toast.LENGTH_LONG ).show()
        }
    }

    LaunchedEffect(key1 = screenState.updateCompleted) {
        if ( screenState.updateCompleted ) {
            Toast.makeText(context,"Question updated successfully!", Toast.LENGTH_LONG ).show()
        }
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
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )
                AnimatedVisibility(visible = questionId != null) {
                    if ( !screenState.isDeleting ) {
                        IconButton(onClick = {
                            if ( !screenState.addingQuestionInProgress ) {
                                viewModel.onEvent( CreateUpdateScreenEvents.QuestionDeleted )
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Rounded.DeleteOutline,
                                contentDescription = "delete",
                                tint = Color.White
                            )
                        }
                    } else {
                        Text(
                            text = "Deleting...",
                            fontFamily = Font(R.font.lexend_bold).toFontFamily(),
                            fontSize = 14.sp,
                            color = Color.Red,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(end = 6.dp)
                        )
                    }
                }
            }

        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if ( !screenState.isDeleting && !screenState.addingQuestionInProgress ) {
                        viewModel.onEvent( CreateUpdateScreenEvents.SaveClicked )
                    }
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
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
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
                                color = if (isSystemInDarkTheme()) Color.LightGray else Color.DarkGray,
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
                                                        CreateUpdateScreenEvents.SelectedCorrectAnswer(
                                                            it
                                                        )
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
                                            tint = if (isSystemInDarkTheme()) Color.White else SeaGreen,
                                        )
                                    }
                                }
                            }
                            AnimatedVisibility(visible = !screenState.addingQuestionInProgress ) {
                                Text(
                                    text = "Remove answer choices?",
                                    fontFamily = Font(R.font.lexend_medium).toFontFamily(),
                                    fontSize = 14.sp,
                                    color = if (isSystemInDarkTheme()) DarkSlateGrey else SeaGreen,
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