package com.engineerfred.kotlin.ktor.ui.screens.student.questions

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.engineerfred.kotlin.ktor.R
import com.engineerfred.kotlin.ktor.common.CustomButtonComponent
import com.engineerfred.kotlin.ktor.ui.screens.student.questions.components.AnswerChoices
import com.engineerfred.kotlin.ktor.ui.screens.student.questions.components.CountdownTimer
import com.engineerfred.kotlin.ktor.ui.screens.student.questions.components.QuestionCard
import com.engineerfred.kotlin.ktor.ui.theme.Charcoal
import com.engineerfred.kotlin.ktor.ui.theme.KtorTheme

@Composable
fun QuizQuestionsScreen(
    studentLevel: String,
    subject: String,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 10.dp, start=10.dp, end = 20.dp)
        ) {
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBackIosNew,
                    contentDescription = stringResource(R.string.back)
                )
            }
            Text(
                text = "7/10",
                fontFamily = Font(R.font.lexend_bold).toFontFamily(),
                modifier = Modifier.align( Alignment.Center )
            )
            CountdownTimer( modifier = Modifier.align(Alignment.TopEnd) )
        }
        Spacer(modifier = Modifier.size(20.dp))
        QuestionCard(questionText = "In what year did the United States host the FIFA World Cup For The Fist time?")
        Spacer(modifier = Modifier.size(20.dp))
        AnswerChoices(answerChoices = listOf("1986", "1994", "2002", "2010"), onChoiceClick = {})
        Spacer(modifier = Modifier.size(60.dp))
        CustomButtonComponent(
            text = "Next",
            backGroundColor = if (!isSystemInDarkTheme() ) MaterialTheme.colorScheme.primary else Charcoal,
            onClick = { /*TODO*/ },
            btnModifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 55.dp),
            cornerSize = 6.dp
        )
        Spacer(modifier = Modifier.size(20.dp))
    }

}

@Preview( showBackground = true )
@Composable
fun QuizQuestionsScreenPreview() {
    KtorTheme {
        QuizQuestionsScreen(studentLevel = "", subject =  "")
    }
}