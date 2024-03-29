package com.engineerfred.kotlin.ktor.domain.model

data class QuestionState(
    val answeredQuestion: String,
    val correctAnswer: String,
    val givenAnswer: String,
    val isPassed: Boolean
)
