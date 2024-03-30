package com.engineerfred.kotlin.ktor.common

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.engineerfred.kotlin.ktor.R
import com.engineerfred.kotlin.ktor.ui.theme.Charcoal
import com.engineerfred.kotlin.ktor.ui.theme.QuizAppTheme
import com.engineerfred.kotlin.ktor.ui.theme.SeaGreen

@Composable
fun CustomButtonComponent(
    modifier: Modifier = Modifier,
    btnModifier: Modifier = Modifier,
    text: String,
    textSize: TextUnit = 16.sp,
    contentColor: Color = Color.White,
    enabled: () -> Boolean = { true },
    onClick: () -> Unit,
    isLoading: () -> Boolean = { false },
    cornerSize: Dp = 20.dp
) {

    val backGroundColor =  if (isSystemInDarkTheme()) Charcoal else SeaGreen

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        if ( !isLoading() ) {
            Button(
                modifier = btnModifier,
                onClick = onClick,
                shape = RoundedCornerShape(cornerSize),
                colors = ButtonDefaults.buttonColors(
                    containerColor = backGroundColor,
                    contentColor = contentColor,
                    //disabledContainerColor = backGroundColor,
                    //disabledContentColor = contentColor
                ),
                enabled = enabled()
            ) {
                Text(
                    text = text,
                    fontFamily = Font(R.font.lexend_bold).toFontFamily(),
                    fontSize = textSize
                )
            }
        } else {
            CircularProgressIndicator(
                color = if(!isSystemInDarkTheme()) SeaGreen else Color.White,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Preview( showBackground = true )
@Composable
fun CustomButtonComponentPreview() {
    QuizAppTheme {
        CustomButtonComponent(
            text = "Login",
            onClick = { /*TODO*/ },
            enabled = { true }
        )
    }
}