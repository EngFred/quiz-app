package com.engineerfred.kotlin.ktor.ui.screens.student.questions.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.engineerfred.kotlin.ktor.ui.theme.CoralRed
import com.engineerfred.kotlin.ktor.ui.theme.DarkSlateGrey
import com.engineerfred.kotlin.ktor.ui.theme.KtorTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionCard(
    questionText: String
) {

    val backGround =  if ( isSystemInDarkTheme() ) DarkSlateGrey else CoralRed
    ElevatedCard(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 30.dp)
            .shadow(10.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = backGround
        )
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = questionText, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview( showBackground = true )
@Composable
fun QuestionCardPreview() {
    KtorTheme {
        QuestionCard(questionText = "In what year did the United States host the FIFA World Cup For The Fist time?")
    }
}