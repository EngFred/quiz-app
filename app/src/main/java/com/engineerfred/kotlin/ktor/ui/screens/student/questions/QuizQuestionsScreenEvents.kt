package com.engineerfred.kotlin.ktor.ui.screens.student.questions

sealed class QuizQuestionsScreenEvents {
    data object NextButtonClicked: QuizQuestionsScreenEvents()
    data class LevelCardSelected( val level: String ): QuizQuestionsScreenEvents()
    data object TimeUp: QuizQuestionsScreenEvents()
    data object RepeatLevelClicked: QuizQuestionsScreenEvents()
    data object ContinueToNexLevelClicked: QuizQuestionsScreenEvents()
    data class AnswerSelected(val answer: String) : QuizQuestionsScreenEvents()
    data class UserAnswerChanged( val answer: String ) : QuizQuestionsScreenEvents()
}
