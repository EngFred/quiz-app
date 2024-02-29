package com.engineerfred.kotlin.ktor.ui.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engineerfred.kotlin.ktor.domain.usecase.questions.AddQuestionUseCase
import com.engineerfred.kotlin.ktor.domain.usecase.questions.GetQuestionByIdUseCase
import com.engineerfred.kotlin.ktor.ui.model.AnswerType
import com.engineerfred.kotlin.ktor.ui.screens.admin.create_update.CreateUpdateScreenEvents
import com.engineerfred.kotlin.ktor.ui.screens.admin.create_update.CreateUpdateScreenState
import com.engineerfred.kotlin.ktor.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CreateUpdateScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val addQuestionUseCase: AddQuestionUseCase,
    private val getQuestionByIdUseCase: GetQuestionByIdUseCase
) : ViewModel() {

    var uiState by mutableStateOf(CreateUpdateScreenState())
        private set

    companion object {
        private val TAG = CreateUpdateScreenViewModel::class.java.simpleName
    }

    init {
        initializeUiState()
    }

    private fun initializeUiState() {
        val questionId = savedStateHandle.get<String>("questionId")
        val subject = savedStateHandle.get<String>("subject")

        when {
            questionId != null -> {
                uiState = uiState.copy(
                    questionId = questionId,
                    isEditMode = true,
                    isLoading = true
                )
                getQuestionById(questionId)
            }
            subject != null -> {
                uiState = uiState.copy( subject = subject )
            }
        }
    }

    private fun getQuestionById( questionId: String ) {
        viewModelScope.launch {
            getQuestionByIdUseCase.invoke( questionId ).collectLatest { response ->
                when( response ) {
                    is Response.Error -> {
                        uiState = uiState.copy(
                            remoteErrorMessage = response.errorMessage,
                            isLoading = false
                        )
                    }
                    Response.Undefined -> Unit
                    is Response.Success -> {
                        uiState = uiState.copy(
                            isLoading = false,
                            questionId = response.data.id,
                            subject = response.data.subject,
                            answerType = response.data.answerType,
                            questionText = response.data.question,
                            listOfAnswerChoicesWithLetters = if ( response.data.answerChoices != null ) response.data.answerChoices.toMutableList() else mutableListOf(),
                            correctAnswer = response.data.answer,
                            level = response.data.level,
                            levelButtonText = response.data.level
                        )
                        Log.v(TAG, "The question to update ${uiState.question}")
                    }
                }
            }
        }
    }

    fun onEvent( event: CreateUpdateScreenEvents ) {
        if ( uiState.localErrorMessage.isNotEmpty() ) uiState = uiState.copy( localErrorMessage = "" )
        if ( uiState.successMessage.isNotEmpty() ) uiState = uiState.copy( successMessage = "" )
        when( event ) {
            is CreateUpdateScreenEvents.AddAnswerChoiceClicked -> {
                when {

                    uiState.listOfAnswerChoicesWithLetters.size < 4 -> {
                        if ( uiState.answerChoice.length > 8 ) uiState = uiState.copy( answerType = AnswerType.Long.name )
                        uiState.listOfAnswerChoices.add( uiState.answerChoice.trim() )
                        val itemsWithLetters = uiState.listOfAnswerChoices.mapIndexed { index, item ->
                            val letter = ('A' + index).toString()
                            "$letter. ${item.replaceFirstChar { it.titlecase() }}"
                        }
                        itemsWithLetters.forEachIndexed { index, s ->
                            if ( s !in uiState.listOfAnswerChoicesWithLetters )
                                uiState.listOfAnswerChoicesWithLetters.add( s )
                        }
                        uiState = uiState.copy( correctAnswer = "", answerChoice = "", disableCorrectAnswerTextField = true )
                    } else -> {
                        uiState = uiState.copy(
                            localErrorMessage = "Only four answer choices are required!"
                        )
                    }
                }
                Log.v( TAG, "Question: $uiState" )
            }
            is CreateUpdateScreenEvents.AnswerChoiceTextChanged -> {
                uiState = uiState.copy(
                    answerChoice = event.answerChoiceText
                )
            }
            CreateUpdateScreenEvents.LevelButtonClicked -> {
                uiState = uiState.copy(
                    showDropDowMenu = !uiState.showDropDowMenu
                )
            }
            is CreateUpdateScreenEvents.CorrectAnswerTextChanged -> {
                if ( uiState.listOfAnswerChoices.isEmpty() ) {
                    uiState = uiState.copy(
                        correctAnswer = event.answerText
                    )
                }
            }
            is CreateUpdateScreenEvents.LevelSelected -> {
                uiState = uiState.copy(
                    level = event.levelText,
                    levelButtonText = event.levelText,
                    showDropDowMenu = !uiState.showDropDowMenu
                )
            }
            is CreateUpdateScreenEvents.QuestionTextChanged -> {
                uiState = uiState.copy(
                    questionText = event.questionText
                )
            }
            CreateUpdateScreenEvents.SaveClicked -> {
                when {
                    uiState.questionText.isEmpty() -> {
                        uiState = uiState.copy(
                            localErrorMessage = "The form is invalid! A question is required!"
                        )
                    }
                    uiState.correctAnswer.isEmpty() -> {
                        uiState = uiState.copy(
                            localErrorMessage = "The form is invalid! A correct answer is required!"
                        )
                    }
                    uiState.level.isEmpty() -> {
                        uiState = uiState.copy(
                            localErrorMessage = "The form is invalid! A level is required!"
                        )
                    }

                    uiState.listOfAnswerChoicesWithLetters.isNotEmpty() && uiState.listOfAnswerChoicesWithLetters.size != 4 -> {
                        uiState = uiState.copy(
                            localErrorMessage = "The form is invalid! Four answer choices are required!"
                        )
                    }
                    uiState.questionText.isEmpty() && uiState.correctAnswer.isEmpty() && uiState.level.isEmpty() && uiState.listOfAnswerChoicesWithLetters.size != 4 -> {
                        uiState = uiState.copy(
                            localErrorMessage = "The form is invalid!"
                        )
                    }
                    else -> {
                        uiState = uiState.copy(
                            invalidForm = false,
                            addingQuestionInProgress = true
                        )
                        setQuestion()
                        Log.v(TAG, "The question can now be saved in the database!")
                        Log.v( TAG, "Question: ${uiState.question}" )
                    }
                }
            }
            is CreateUpdateScreenEvents.SelectedCorrectAnswer -> {
                uiState = uiState.copy(
                    correctAnswer = event.correctAnswer
                )
            }

            is CreateUpdateScreenEvents.DeleteChoicesList -> {
                uiState = uiState.copy(
                    listOfAnswerChoicesWithLetters = ArrayList(),
                    listOfAnswerChoices = ArrayList(),
                    correctAnswer = "",
                    answerType = if ( uiState.answerChoice == AnswerType.Long.name ) AnswerType.Short.name else AnswerType.Short.name
                )
            }

            CreateUpdateScreenEvents.SetAnotherQuestionButtonClicked -> {
                uiState = uiState.copy(
                    successMessage = ""
                )
            }
        }
    }

    private fun setQuestion() {
        viewModelScope.launch( Dispatchers.IO ) {
            val question = if ( uiState.isEditMode ) uiState.question else uiState.question.copy( id = UUID.randomUUID().toString() )
            Log.v(TAG, "$question")
            val task = addQuestionUseCase.invoke( question )
            when (task) {
                is Response.Error -> {
                    uiState = uiState.copy(
                        localErrorMessage = task.errorMessage,
                        addingQuestionInProgress = false
                    )
                }
                Response.Undefined -> Unit
                is Response.Success -> {
                    if ( uiState.isEditMode ) {
                        //to enable navigation back to the questions screen
                        uiState = uiState.copy(updateCompleted = true )
                    } else {
                        resetState()
                    }
                }
            }
        }
    }

    private fun resetState() {
        uiState = uiState.copy(
            questionId = null,
            questionText = "",
            answerChoice = "",
            listOfAnswerChoices = ArrayList(),
            listOfAnswerChoicesWithLetters = ArrayList(),
            correctAnswer = "",
            level = "",
            answerType = AnswerType.Short.name,
            showDropDowMenu = false,
            levelButtonText = "Choose level",
            invalidForm = true,
            disableCorrectAnswerTextField = false,
            localErrorMessage = "",
            successMessage = "Question has been set successfully!",
            addingQuestionInProgress = false
        )
    }

}