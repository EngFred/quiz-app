package com.engineerfred.kotlin.ktor.ui.screens.student.questions.components

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.engineerfred.kotlin.ktor.ui.theme.KtorTheme

@Composable
fun AnswerChoices(
    answerChoices: List<String>,
    onChoiceClick: (String) -> Unit
) {

    var selected by rememberSaveable {
        mutableIntStateOf(-1 )
    }

    val context = LocalContext.current

    var selectedAnswer by rememberSaveable {
       mutableStateOf("")
    }

    LaunchedEffect( selectedAnswer ) {
        if ( selectedAnswer.isNotEmpty() ) {
            Toast.makeText(context, "Selected $selectedAnswer", Toast.LENGTH_SHORT ).show()
        }
    }

    answerChoices.forEachIndexed { index, s ->
        ChoiceCard(
            answerChoice = s, onChoiceClick = {
                selected = index
                selectedAnswer = it
            }, selected = index == selected
        )
    }
}


@Preview( showBackground = true )
@Composable
fun AnswerChoicesPreview() {
    KtorTheme {
        AnswerChoices( answerChoices = listOf("1986", "1994", "2002", "2010"), {} )
    }
}