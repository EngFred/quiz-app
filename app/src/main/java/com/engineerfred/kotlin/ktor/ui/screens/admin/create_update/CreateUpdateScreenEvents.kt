package com.engineerfred.kotlin.ktor.ui.screens.admin.create_update

sealed class CreateUpdateScreenEvents {

    data class QuestionTextChanged(val questionText: String ) : CreateUpdateScreenEvents()
    data class AnswerChoiceTextChanged(val answerChoiceText: String ) : CreateUpdateScreenEvents()
    data object AddAnswerChoiceClicked: CreateUpdateScreenEvents()
    data object DeleteChoicesList: CreateUpdateScreenEvents()
    data class CorrectAnswerTextChanged(val answerText: String ) : CreateUpdateScreenEvents()
    data class SelectedCorrectAnswer( val correctAnswer: String ) : CreateUpdateScreenEvents()
    data object LevelButtonClicked: CreateUpdateScreenEvents()
    data class LevelSelected( val levelText: String ) : CreateUpdateScreenEvents()
    data object SaveClicked: CreateUpdateScreenEvents()
    data object SetAnotherQuestionButtonClicked: CreateUpdateScreenEvents()

}