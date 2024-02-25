package com.engineerfred.kotlin.ktor.ui.screens.admin.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.engineerfred.kotlin.ktor.R
import com.engineerfred.kotlin.ktor.domain.model.Question
import com.engineerfred.kotlin.ktor.ui.model.AnswerType
import com.engineerfred.kotlin.ktor.ui.model.Level
import com.engineerfred.kotlin.ktor.ui.theme.KtorTheme

@Composable
fun QuestionItem(
    question: Question,
    onQuestionClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .clickable {
                onQuestionClick.invoke(question.id)
            }
            .fillMaxWidth()
            .padding(start = 15.dp, end=15.dp, top=15.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = question.question,
            fontSize = 16.sp,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black,
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
                text = question.setBy,
                fontSize = 12.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontFamily = Font(R.font.lexend_regular).toFontFamily()
            )
        }
    }
    Divider()
}

@Composable
private fun LevelIndicator(level: String) {
    val color: Color = when( level ) {
        Level.Beginner.name -> Color.Gray
        Level.Intermediate.name -> MaterialTheme.colorScheme.primary
        Level.Advanced.name -> MaterialTheme.colorScheme.error
        else -> Color.Gray
    }
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier
            .clip(CircleShape)
            .size(8.dp)
            .background(color)
        )
        Text(
            modifier = Modifier.padding(start = 5.dp),
            text = level,
            color = Color.DarkGray,
            fontSize = 13.sp,
            fontFamily = Font(R.font.lexend_medium).toFontFamily()
        )
    }
}

@Preview( showBackground = true )
@Composable
fun LevelIndicatorPreview() {
    KtorTheme {
        LevelIndicator(level = Level.Beginner.name)
    }
}

@Preview( showBackground = true )
@Composable
fun QuestionItemPreview() {
    KtorTheme {
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