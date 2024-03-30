package com.engineerfred.kotlin.ktor.ui.screens.admin.create_update

import com.engineerfred.kotlin.ktor.domain.model.Question
import com.engineerfred.kotlin.ktor.ui.model.AnswerType
import com.engineerfred.kotlin.ktor.util.Constants.LOGGED_IN_USER_EMAIL

data class CreateUpdateScreenState(
    val isLoading: Boolean = false,
    val isEditMode: Boolean = false,
    val updateCompleted: Boolean = false,
    val questionId: String? = null,
    val subject: String? = null,
    val questionText: String = "",
    val answerChoice: String = "",
    val listOfAnswerChoices: MutableList<String> = mutableListOf(),
    val listOfAnswerChoicesWithLetters: MutableList<String> = mutableListOf(),
    val correctAnswer: String = "",
    val answerType: String = AnswerType.Short.name,
    val level: String = "",
    val showDropDowMenu: Boolean = false,
    val levelButtonText: String = "Choose level",
    val invalidForm: Boolean = true,
    val disableCorrectAnswerTextField: Boolean = false,
    val localErrorMessage: String = "",
    val remoteErrorMessage: String = "",
    val successMessage: String = "",
    val addingQuestionInProgress: Boolean = false,

    val deleteError: String? = null,
    val isDeleting: Boolean = false,
    val deleteSuccessful: Boolean = false,
    val currentQuestion: Question? = null
) {
    var question = Question(
        id = questionId ?: "",
        question = questionText.replaceFirstChar { it.titlecase() },
        answer = correctAnswer.lowercase().trim(),
        answerType = answerType,
        answerChoices = if ( listOfAnswerChoicesWithLetters.isEmpty() ) null else listOfAnswerChoicesWithLetters,
        subject = subject?.replaceFirstChar { it.titlecase() } ?: "",
        level = level,
        date = System.currentTimeMillis(),
        setBy = LOGGED_IN_USER_EMAIL ?: "null"
    )
}
