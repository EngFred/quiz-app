package com.engineerfred.kotlin.ktor.ui.screens.admin.home.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.engineerfred.kotlin.ktor.domain.model.Question

@Composable
fun QuestionsList(
    modifier: Modifier = Modifier,
    questions: List<Question>,
    onQuestionClick: (String) -> Unit
) {
    LazyColumn( modifier = modifier.fillMaxSize() ) {
        items(
            count = questions.size,
            key = { questions[it].id },
            contentType = { "questions" }
        ) {
            val question = questions[it]
            QuestionItem(
                question = question,
                onQuestionClick = onQuestionClick
            )
        }
    }
}