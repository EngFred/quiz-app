package com.engineerfred.kotlin.ktor.domain.model

import com.engineerfred.kotlin.ktor.ui.model.AnswerType
import com.engineerfred.kotlin.ktor.ui.model.Level
import com.engineerfred.kotlin.ktor.ui.model.Subject

data class Question(
    val id: String,
    val question: String,
    val answer: String,
    val answerType: String,
    val answerChoices: List<String>?,
    val subject: String,
    val level: String,
    val date: Long,
    val setBy: String
) {
    constructor() : this(
        id = "",
        question = "",
        answer = "",
        answerType = AnswerType.Short.name,
        answerChoices = null,
        subject = Subject.English.name,
        level = Level.Beginner.name,
        date = 0L,
        setBy = ""
    )
}

