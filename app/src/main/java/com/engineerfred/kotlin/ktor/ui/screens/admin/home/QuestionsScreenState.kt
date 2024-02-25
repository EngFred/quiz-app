package com.engineerfred.kotlin.ktor.ui.screens.admin.home

import com.engineerfred.kotlin.ktor.domain.model.Question

data class QuestionsScreenState(
    val isLoading: Boolean = true,
    val errorMessage: String = "",
    val questions: List<Question> = mutableListOf()
)
