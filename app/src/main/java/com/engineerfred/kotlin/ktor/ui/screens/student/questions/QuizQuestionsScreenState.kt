package com.engineerfred.kotlin.ktor.ui.screens.student.questions

import com.engineerfred.kotlin.ktor.domain.model.Question

data class QuizQuestionsScreenState(
    val questions: List<Question> = emptyList(),
    val listOfTenQuestions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val currentQuestion: Question? = null,
    val correctAnswerTaps: Int = 0,
    val timeUp: Boolean = false,
    val timeLeft: Int = 0,
    val currentSubject: String = "",
    val upgradingLevel: Boolean = false,
    val upgradingLevelError: String? = null,
    val quizCompleted: Boolean = false,
    val enableContinueToNextLevelButton: Boolean = false,
    val enableNextButton: Boolean = false,
    val selectedChoiceIndex: Int = -1,
    val selectedAnswer: Boolean = false,
    val correctAnswersCount: Int = 0,
    val isLoading: Boolean = true,
    val levelEnded: Boolean = false,
    val serverError: String? = null,
    val studentLevel: String? = null,
    val userAnswer: String = "",
)
