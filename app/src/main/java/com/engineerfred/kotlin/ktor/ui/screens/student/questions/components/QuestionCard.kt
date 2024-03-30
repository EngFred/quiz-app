package com.engineerfred.kotlin.ktor.ui.screens.student.questions.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.engineerfred.kotlin.ktor.ui.theme.DarkSlateGrey
import com.engineerfred.kotlin.ktor.ui.theme.QuizAppTheme
import com.engineerfred.kotlin.ktor.ui.theme.SeaGreen
import kotlinx.coroutines.delay

@Composable
fun QuestionCard(
    questionText: String,
    timeLeft: Int,
    onTimeUp: () ->  Unit
) {

    val backGround =  if ( isSystemInDarkTheme() ) DarkSlateGrey else MaterialTheme.colorScheme.primary.copy(alpha = .6f)

    var time by rememberSaveable {
        mutableIntStateOf(timeLeft)
    }

    if( time == 0 ) onTimeUp.invoke()

    LaunchedEffect(key1 = timeLeft) {
        while (time > 0) {
            delay(1000L)
            time--
        }
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        ElevatedCard(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp, top = 40.dp, bottom = 20.dp),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = backGround
            )
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(230.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = questionText, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 6.dp))
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .border(3.dp, SeaGreen, CircleShape)
                    .background(if (isSystemInDarkTheme()) DarkSlateGrey.copy(alpha = .5f) else SeaGreen.copy(alpha = .5f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = formatTime(time),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
        }
    }
}

private fun formatTime(timeLeft: Int): String {
    val minutes = timeLeft / 60
    val seconds = timeLeft % 60
    return "%02d:%02d".format(minutes, seconds)
}

@Preview( showBackground = true )
@Composable
fun QuestionCardPreview() {
    QuizAppTheme {
        QuestionCard(
            questionText = "In what year" +
                " did the United States host " +
                "the FIFA World Cup For " +
                "The Fist time?", 60, {}
        )
    }
}