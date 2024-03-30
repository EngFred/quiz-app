package com.engineerfred.kotlin.ktor.ui.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engineerfred.kotlin.ktor.domain.usecase.questions.GetEnglishQuestionsUseCase
import com.engineerfred.kotlin.ktor.domain.usecase.questions.GetMathQuestionsUseCase
import com.engineerfred.kotlin.ktor.domain.usecase.student.UpgradeStudentLevelUseCase
import com.engineerfred.kotlin.ktor.ui.model.Level
import com.engineerfred.kotlin.ktor.ui.model.Subject
import com.engineerfred.kotlin.ktor.ui.screens.student.questions.QuizQuestionsScreenEvents
import com.engineerfred.kotlin.ktor.ui.screens.student.questions.QuizQuestionsScreenState
import com.engineerfred.kotlin.ktor.util.Constants
import com.engineerfred.kotlin.ktor.util.Constants.LOGGED_IN_USER_ID
import com.engineerfred.kotlin.ktor.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class QuizQuestionsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getEnglishQuestionsUseCase: GetEnglishQuestionsUseCase,
    private val getMathQuestionsUseCase: GetMathQuestionsUseCase,
    private val upgradeStudentLevelUseCase: UpgradeStudentLevelUseCase
) : ViewModel() {

    var uiState by mutableStateOf(QuizQuestionsScreenState())
        private set

    private val level = savedStateHandle.get<String>(Constants.LEVEL_KEY)
    private val subject = savedStateHandle.get<String>(Constants.SUBJECT_KEY)

    companion object {
        private val TAG = QuizQuestionsViewModel::class.java.simpleName
    }

    init {
        if ( subject != null && level != null ) {
            initialiseUiState(subject, level)
        }
    }

    fun onEvent( event: QuizQuestionsScreenEvents ) {
        when( event ) {
            is QuizQuestionsScreenEvents.AnswerSelected -> {

                if( event.answer.lowercase().trim() == uiState.currentQuestion!!.answer ) {
                    uiState = uiState.copy(
                        correctAnswerTaps = uiState.correctAnswerTaps+1
                    )
                    uiState = uiState.copy(
                        correctAnswersCount = if ( uiState.correctAnswerTaps == 1 ) uiState.correctAnswersCount+1 else uiState.correctAnswersCount,
                        enableNextButton = true,
                    )
                    if ( uiState.correctAnswerTaps == 1 ) {
                        Log.v(TAG, "Correct!!!")
                        Log.v(TAG, "Score: ${uiState.correctAnswersCount}/${uiState.listOfTenQuestions.size}")
                    }
                } else {
                    Log.v(TAG, "Wrong!!!")
                    Log.v(TAG, "Selected: ${event.answer.lowercase().trim()} but the answer is ${uiState.currentQuestion!!.answer}")
                    if ( uiState.correctAnswerTaps > 0 ) {
                        uiState = uiState.copy(
                            correctAnswerTaps = 0,
                            correctAnswersCount = uiState.correctAnswersCount-1,
                            enableNextButton = true
                        )
                        Log.v(TAG, "Score: ${uiState.correctAnswersCount}/${uiState.listOfTenQuestions.size}")
                    } else {
                        uiState = uiState.copy(
                            enableNextButton = true
                        )
                    }
                }
            }
            QuizQuestionsScreenEvents.NextButtonClicked -> {

                if ( uiState.currentQuestionIndex == uiState.listOfTenQuestions.size - 1 ) { //a user clicks the next button while they were on the last question!

                    if ( uiState.correctAnswersCount >= 7 ) {
                        if ( uiState.studentLevel != Level.Advanced.name ) {
                            uiState = uiState.copy(
                                levelEnded = true,
                                upgradingLevel = true
                            )
                            val nextLevel = when (uiState.studentLevel) {
                                Level.Beginner.name -> Level.Intermediate.name
                                Level.Intermediate.name -> Level.Advanced.name
                                else -> Level.Intermediate.name
                            }
                            upgradeLevel( nextLevel, uiState.currentSubject )
                        } else {
                            uiState = uiState.copy(
                                quizCompleted = true
                            )
                        }
                    } else {
                        uiState = uiState.copy(
                            levelEnded = true,
                            upgradingLevel = false
                        )
                    }
                } else {
                    //for multi choice questions
                    if ( uiState.currentQuestion!!.answerChoices != null ) {
                        uiState = uiState.copy(
                            enableNextButton = false,
                            currentQuestionIndex = uiState.currentQuestionIndex+1,
                            correctAnswerTaps = 0
                        )
                        val nextQuestion = uiState.listOfTenQuestions[uiState.currentQuestionIndex]
                        uiState = uiState.copy(
                            currentQuestion = nextQuestion
                        )
                    } else {
                        //for typed answer questions
                        if ( uiState.userAnswer.isNotEmpty() ) {
                            if ( uiState.userAnswer.lowercase().trim() == uiState.currentQuestion!!.answer ) {
                                uiState = uiState.copy(
                                    correctAnswersCount = uiState.correctAnswersCount+1,
                                    currentQuestionIndex = uiState.currentQuestionIndex+1,
                                    userAnswer = "",
                                )
                                val nextQuestion = uiState.listOfTenQuestions[uiState.currentQuestionIndex]
                                uiState = uiState.copy(
                                    currentQuestion = nextQuestion
                                )
                                Log.v(TAG, "Correct!!!")
                                Log.v(TAG, "Score: ${uiState.correctAnswersCount}/${uiState.listOfTenQuestions.size}")
                            } else {
                                Log.v(TAG, "Wrong!!!")
                                Log.v(TAG, "Given answer is: '${uiState.userAnswer.lowercase().trim()}' but the answer is '${uiState.currentQuestion!!.answer}'")
                                uiState = uiState.copy(
                                    currentQuestionIndex = uiState.currentQuestionIndex+1,
                                    userAnswer = "",
                                )
                                val nextQuestion = uiState.listOfTenQuestions[uiState.currentQuestionIndex]
                                uiState = uiState.copy(
                                    currentQuestion = nextQuestion
                                )
                            }
                        }
                    }
                }
            }
            is QuizQuestionsScreenEvents.UserAnswerChanged -> {
                uiState = uiState.copy(
                    userAnswer = event.answer
                )
            }

            QuizQuestionsScreenEvents.TimeUp -> {

                if ( uiState.correctAnswersCount >= 7 ) {
                    if ( uiState.studentLevel != Level.Advanced.name ) {
                        uiState = uiState.copy(
                            timeUp = true, //show the timeUp ui
                            upgradingLevel = true,
                            userAnswer = ""
                        )
                        val nextLevel = when (uiState.studentLevel) {
                            Level.Beginner.name -> Level.Intermediate.name
                            Level.Intermediate.name -> Level.Advanced.name
                            else -> Level.Intermediate.name
                        }
                        upgradeLevel( nextLevel, uiState.currentSubject )
                    } else {
                        uiState = uiState.copy(
                            quizCompleted = true, //show the quiz completed ui
                            upgradingLevel = false,
                            userAnswer = ""
                        )
                    }
                } else {
                    uiState = uiState.copy(
                        timeUp = true, //show the timeUp ui
                        upgradingLevel = false,
                        userAnswer = ""
                    )
                }

            }
            QuizQuestionsScreenEvents.ContinueToNexLevelClicked -> {
                //it will only be clickable after a successful level upgrade
                val nextLevel = when (uiState.studentLevel) {
                    Level.Beginner.name -> Level.Intermediate.name
                    Level.Intermediate.name -> Level.Advanced.name
                    else -> Level.Intermediate.name
                }
                if (subject == Subject.English.name) {
                    when (uiState.studentLevel) {
                        Level.Beginner.name -> {
                            uiState = uiState.copy(
                                timeLeft = 140
                            )
                        }
                        Level.Intermediate.name -> {
                            uiState = uiState.copy(
                                timeLeft = 100
                            )
                        }
                        Level.Advanced.name -> {
                            uiState = uiState.copy(
                                timeLeft = 45
                            )
                        }
                    }
                } else {
                    when (uiState.studentLevel) {
                        Level.Beginner.name -> {
                            uiState = uiState.copy(
                                timeLeft = 180
                            )
                        }
                        Level.Intermediate.name -> {
                            uiState = uiState.copy(
                                timeLeft = 120
                            )
                        }
                        Level.Advanced.name -> {
                            uiState = uiState.copy(
                                timeLeft = 80
                            )
                        }
                    }
                }
                uiState = uiState.copy(
                    isLoading = true,
                    levelEnded = false,
                    questions = emptyList(),
                    listOfTenQuestions = emptyList(),
                    currentQuestion = null,
                    currentQuestionIndex = 0,
                    correctAnswersCount = 0,
                    correctAnswerTaps = 0,
                    studentLevel = nextLevel,
                    userAnswer = ""
                )
                fetchNextLevelQuestions(uiState.currentSubject, nextLevel )
            }

            QuizQuestionsScreenEvents.RepeatLevelClicked -> {
                if (subject == Subject.English.name) {
                    when (uiState.studentLevel) {
                        Level.Beginner.name -> {
                            uiState = uiState.copy(
                                timeLeft = 140
                            )
                        }
                        Level.Intermediate.name -> {
                            uiState = uiState.copy(
                                timeLeft = 100
                            )
                        }
                        Level.Advanced.name -> {
                            uiState = uiState.copy(
                                timeLeft = 45
                            )
                        }
                    }
                } else {
                    when (uiState.studentLevel) {
                        Level.Beginner.name -> {
                            uiState = uiState.copy(
                                timeLeft = 180
                            )
                        }
                        Level.Intermediate.name -> {
                            uiState = uiState.copy(
                                timeLeft = 120
                            )
                        }
                        Level.Advanced.name -> {
                            uiState = uiState.copy(
                                timeLeft = 80
                            )
                        }
                    }
                }
                uiState = uiState.copy(
                    levelEnded = false,
                    timeUp = false,
                    questions = uiState.questions.reversed().shuffled(),
                    currentQuestionIndex = 0,
                    correctAnswersCount = 0,
                    correctAnswerTaps = 0,
                    userAnswer = ""
                )
                val listOfTenQuestions = uiState.questions.subList(0, 10)
                val currentQuestion = listOfTenQuestions[uiState.currentQuestionIndex]
                uiState = uiState.copy(
                    listOfTenQuestions = listOfTenQuestions,
                    currentQuestion = currentQuestion
                )
            }

            is QuizQuestionsScreenEvents.LevelCardSelected -> {

                if ( subject == Subject.English.name ) {
                    when (event.level) {
                        Level.Beginner.name -> {
                            uiState = uiState.copy(
                                timeLeft = 145
                            )
                        }

                        Level.Intermediate.name -> {
                            uiState = uiState.copy(
                                timeLeft = 100
                            )
                        }

                        Level.Advanced.name -> {
                            uiState = uiState.copy(
                                timeLeft = 45
                            )
                        }
                    }
                } else {
                    when (event.level) {
                        Level.Beginner.name -> {
                            uiState = uiState.copy(
                                timeLeft = 180
                            )
                        }
                        Level.Intermediate.name -> {
                            uiState = uiState.copy(
                                timeLeft = 120
                            )
                        }
                        Level.Advanced.name -> {
                            uiState = uiState.copy(
                                timeLeft = 80
                            )
                        }
                    }
                }

                uiState = uiState.copy(
                    isLoading = true,
                    quizCompleted = false,
                    questions = emptyList(),
                    listOfTenQuestions = emptyList(),
                    currentQuestion = null,
                    currentQuestionIndex = 0,
                    correctAnswerTaps = 0,
                    correctAnswersCount = 0,
                    userAnswer = "",
                    studentLevel = event.level
                )
                fetchNextLevelQuestions( uiState.currentSubject, event.level )
            }
        }
    }

    private fun fetchNextLevelQuestions(subject: String, nextLevel: String ) {
        viewModelScope.launch {
            when (subject) {
                Subject.English.name -> {
                    getEnglishQuestionsUseCase.invoke( nextLevel ).collectLatest { response ->
                        when (response) {
                            is Response.Error -> {
                                uiState = uiState.copy(
                                    isLoading = false,
                                    serverError = response.errorMessage
                                )
                                Log.v(TAG, "Error: ${response.errorMessage}!")
                            }
                            is Response.Success -> {
                                uiState = uiState.copy(
                                    isLoading = false,
                                    questions = response.data.shuffled().reversed()
                                )
                                Log.v( TAG, "Success: Received ${response.data.size} $subject questions @$nextLevel level!")
                                if ( uiState.questions.isNotEmpty() ) {
                                    val listOfTenQuestions = uiState.questions.subList(0, 10)
                                    val currentQuestion = listOfTenQuestions[uiState.currentQuestionIndex]
                                    uiState = uiState.copy(
                                        listOfTenQuestions = listOfTenQuestions,
                                        currentQuestion = currentQuestion
                                    )
                                }
                            }
                            Response.Undefined -> Unit
                        }
                    }
                }

                Subject.Mathematics.name -> {
                    getMathQuestionsUseCase.invoke(nextLevel).collectLatest { response ->
                        when (response) {
                            is Response.Error -> {
                                uiState = uiState.copy(
                                    isLoading = false,
                                    serverError = response.errorMessage
                                )
                                Log.v(TAG, "Error: ${response.errorMessage}!")
                            }
                            is Response.Success -> {
                                uiState = uiState.copy(
                                    isLoading = false,
                                    questions = response.data.shuffled()
                                )
                                Log.v( TAG, "Success: Received ${response.data.size} $subject questions @$nextLevel level!" )
                                val listOfTenQuestions = uiState.questions.subList(0, 10)
                                val currentQuestion = listOfTenQuestions[uiState.currentQuestionIndex]
                                uiState = uiState.copy(
                                    listOfTenQuestions = listOfTenQuestions,
                                    currentQuestion = currentQuestion
                                )
                            }
                            Response.Undefined -> Unit
                        }
                    }
                }
            }
        }
    }
    private fun initialiseUiState( subject: String, level: String ) {
        viewModelScope.launch(Dispatchers.IO) {
            when (subject) {
                Subject.English.name -> {
                    getEnglishQuestionsUseCase.invoke(level).collectLatest { response ->
                        when (response) {
                            is Response.Error -> {
                                uiState = uiState.copy(
                                    isLoading = false,
                                    serverError = response.errorMessage
                                )
                                Log.v(TAG, "Error: ${response.errorMessage}!")
                            }
                            is Response.Success -> {
                                when (level) {
                                    Level.Beginner.name -> {
                                        uiState = uiState.copy(
                                            timeLeft = 145
                                        )
                                    }
                                    Level.Intermediate.name -> {
                                        uiState = uiState.copy(
                                            timeLeft = 100
                                        )
                                    }
                                    Level.Advanced.name -> {
                                        uiState = uiState.copy(
                                            timeLeft = 45
                                        )
                                    }
                                }
                                uiState = uiState.copy(
                                    isLoading = false,
                                    questions = response.data.shuffled(),
                                    studentLevel = level,
                                    currentSubject = subject
                                )
                                Log.v( TAG, "Success: Received ${response.data.size} $subject questions @$level level!")
                                if ( uiState.questions.isNotEmpty() ) {
                                    val listOfTenQuestions = uiState.questions.subList(0, 10)
                                    val currentQuestion = listOfTenQuestions[uiState.currentQuestionIndex]
                                    uiState = uiState.copy(
                                        listOfTenQuestions = listOfTenQuestions,
                                        currentQuestion = currentQuestion
                                    )
                                }
                                Log.v(TAG, "The initial UiState data: ${uiState}!")
                            }
                            Response.Undefined -> Unit
                        }
                    }
                }

                Subject.Mathematics.name -> {
                    getMathQuestionsUseCase.invoke(level).collectLatest { response ->
                        when (response) {
                            is Response.Error -> {
                                uiState = uiState.copy(
                                    isLoading = false,
                                    serverError = response.errorMessage
                                )
                                Log.v(TAG, "Error: ${response.errorMessage}!")
                            }
                            is Response.Success -> {
                                when (level) {
                                    Level.Beginner.name -> {
                                        uiState = uiState.copy(
                                            timeLeft = 180
                                        )
                                    }
                                    Level.Intermediate.name -> {
                                        uiState = uiState.copy(
                                            timeLeft = 120
                                        )
                                    }
                                    Level.Advanced.name -> {
                                        uiState = uiState.copy(
                                            timeLeft = 80
                                        )
                                    }
                                }
                                uiState = uiState.copy(
                                    isLoading = false,
                                    questions = response.data.shuffled(),
                                    studentLevel = level,
                                    currentSubject = subject
                                )
                                Log.v( TAG, "Success: Received ${response.data.size} $subject questions @$level level!" )
                                if ( uiState.questions.isNotEmpty() ) {
                                    val listOfTenQuestions = uiState.questions.subList(0, 10)
                                    val currentQuestion = listOfTenQuestions[uiState.currentQuestionIndex]
                                    uiState = uiState.copy(
                                        listOfTenQuestions = listOfTenQuestions,
                                        currentQuestion = currentQuestion
                                    )
                                }
                                Log.v(TAG, "The initial UiState data: ${uiState}!")
                            }
                            Response.Undefined -> Unit
                        }
                    }
                }
            }
        }
    }

    private fun upgradeLevel( newLevel: String, subject: String ) {
        LOGGED_IN_USER_ID?.let {
            viewModelScope.launch( Dispatchers.IO ) {
                val task = upgradeStudentLevelUseCase.invoke( it, newLevel, subject )
                when (task) {
                    is Response.Error -> {
                        uiState = uiState.copy(
                            upgradingLevel = false,
                            enableContinueToNextLevelButton = false,
                            upgradingLevelError = task.errorMessage
                        )
                    }
                    is Response.Success -> {
                        uiState = uiState.copy(
                            upgradingLevel = false,
                            enableContinueToNextLevelButton = true
                        )
                    }
                    Response.Undefined -> Unit
                }
            }
        }
    }
}