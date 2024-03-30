package com.engineerfred.kotlin.ktor.common

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.engineerfred.kotlin.ktor.R
import com.engineerfred.kotlin.ktor.ui.model.Level
import com.engineerfred.kotlin.ktor.ui.theme.FireEngineRed
import com.engineerfred.kotlin.ktor.ui.theme.QuizAppTheme
import com.engineerfred.kotlin.ktor.ui.theme.SeaGreen


@Composable
fun LevelIndicator(
    modifier: Modifier = Modifier,
    level: String,
    dotSize: Dp = 8.dp,
    fontWeight: FontWeight = FontWeight.Normal,
    fontSize: TextUnit = 13.sp,
    textColor: Color =  if (!isSystemInDarkTheme()) Color.DarkGray else Color.LightGray,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start
) {
    val color: Color = when( level ) {
        Level.Beginner.name -> Color.Gray
        Level.Intermediate.name -> SeaGreen
        Level.Advanced.name -> FireEngineRed
        else -> Color.Gray
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = horizontalArrangement
    ) {
        Box(modifier = Modifier
            .clip(CircleShape)
            .size(dotSize)
            .background(color)
        )
        Text(
            modifier = Modifier.padding(start = 5.dp),
            text = level,
            color = textColor,
            fontSize = fontSize,
            fontWeight = fontWeight,
            fontFamily = Font(R.font.lexend_medium).toFontFamily()
        )
    }
}

@Preview( showBackground = true )
@Composable
fun LevelIndicatorPreview() {
    QuizAppTheme {
        LevelIndicator(level = Level.Beginner.name)
    }
}