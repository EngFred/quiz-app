package com.engineerfred.kotlin.ktor.ui.screens.student.questions.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.engineerfred.kotlin.ktor.R
import com.engineerfred.kotlin.ktor.ui.theme.DarkRed
import com.engineerfred.kotlin.ktor.ui.theme.DarkSlateGrey
import com.engineerfred.kotlin.ktor.ui.theme.QuizAppTheme
import kotlinx.coroutines.delay

@Composable
fun CountdownTimer(
    modifier: Modifier = Modifier,
    time: Int,
    onTimeUp: () -> Unit
) {
    var timeLeft by rememberSaveable {
        mutableIntStateOf(time)
    }

    LaunchedEffect(key1 = timeLeft) {
        while (timeLeft > 0) {
            delay(1000L)
            timeLeft--
        }
    }

    if( timeLeft == 0 ) onTimeUp.invoke()

    val borderColor = if ( !isSystemInDarkTheme() ) {
        if ( timeLeft < 15 ) DarkRed else MaterialTheme.colorScheme.primary
    } else if ( timeLeft < 15 ) DarkRed else DarkSlateGrey

    Box(
        modifier = modifier
            .size(40.dp)
            //.border(2.dp, borderColor, CircleShape)
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        //CircularProgressIndicator( progress = (timeLeft / time).toFloat() )
        Text(text = "$timeLeft", fontFamily = Font(R.font.lexend_medium).toFontFamily(), fontSize = 14.sp)
    }
}

@Preview( showBackground = true )
@Composable
fun CountDownTimerPreview() {
    QuizAppTheme {
        CountdownTimer(onTimeUp = {}, time = 40)
    }
}