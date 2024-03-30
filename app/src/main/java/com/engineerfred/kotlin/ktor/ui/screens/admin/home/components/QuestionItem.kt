package com.engineerfred.kotlin.ktor.ui.screens.admin.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.engineerfred.kotlin.ktor.R
import com.engineerfred.kotlin.ktor.common.LevelIndicator
import com.engineerfred.kotlin.ktor.domain.model.Question
import com.engineerfred.kotlin.ktor.ui.model.AnswerType
import com.engineerfred.kotlin.ktor.ui.model.Level
import com.engineerfred.kotlin.ktor.ui.theme.DarkSlateGrey
import com.engineerfred.kotlin.ktor.ui.theme.QuizAppTheme

@Composable
fun QuestionItem(
    question: Question,
    onQuestionClick: (String) -> Unit
) {

    val bgColor = if (isSystemInDarkTheme()) DarkSlateGrey else Color.White

    Column(
        modifier = Modifier
            .clickable {
                onQuestionClick.invoke(question.id)
            }
            .fillMaxWidth()
            .background(bgColor)
            .padding(start = 15.dp, end = 15.dp, top = 15.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = question.question,
            fontSize = 16.sp,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis,
            fontFamily = Font(R.font.lexend_regular).toFontFamily()
        )
        Spacer(modifier = Modifier.size(5.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LevelIndicator(level = question.level)
            Text(
                modifier = Modifier.padding(start = 5.dp),
                text = if(question.setBy != "null") question.setBy else "...",
                fontSize = 12.sp,
                color = if (!isSystemInDarkTheme()) Color.Gray else Color.LightGray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontFamily = Font(R.font.lexend_regular).toFontFamily()
            )
        }
    }
    HorizontalDivider( color = if ( isSystemInDarkTheme() ) MaterialTheme.colorScheme.surface else Color.DarkGray )
}

@Preview( showBackground = true )
@Composable
fun QuestionItemPreview() {
    QuizAppTheme {
        QuestionItem(
            question = Question(
            id = "",
            question = "Science is the  study of living things and non living things.",
            answer = "yes",
            answerType = AnswerType.Short.name,
            answerChoices = null,
            subject = "",
            level = Level.Beginner.name,
            date = 0L,
            setBy = "engfred88@gmail.com"
            ),
            onQuestionClick = {}
        )
    }
}