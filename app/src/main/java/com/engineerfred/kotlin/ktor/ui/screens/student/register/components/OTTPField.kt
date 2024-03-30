package com.engineerfred.kotlin.ktor.ui.screens.student.register.components


import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.engineerfred.kotlin.ktor.ui.theme.DarkSlateGrey
import com.engineerfred.kotlin.ktor.ui.theme.QuizAppTheme
import com.engineerfred.kotlin.ktor.ui.theme.SeaGreen

@Composable
fun OtpTextField(
    modifier: Modifier = Modifier,
    otpText: String,
    otpCount: Int = 6,
    onOtpTextChange: (String, Boolean) -> Unit,
    enabled: Boolean = false
) {

    BasicTextField(
        modifier = modifier,
        value = TextFieldValue(otpText, selection = TextRange(otpText.length)),
        enabled = enabled,
        onValueChange = {
            if (it.text.length <= otpCount) {
                onOtpTextChange.invoke(it.text, it.text.length == otpCount)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        decorationBox = {
            Row(horizontalArrangement = Arrangement.Center) {
                repeat(otpCount) { index ->
                    CharView(
                        index = index,
                        text = otpText
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    )
}

@Composable
private fun CharView(
    index: Int,
    text: String
) {
    val isFocused = text.length == index
    val char = when {
        index == text.length -> "0"
        index > text.length -> ""
        else -> text[index].toString()
    }
    Text(
        modifier = Modifier
            .size(50.dp)
            .border(
                1.dp, when {
                    isFocused -> if (isSystemInDarkTheme()) DarkSlateGrey else SeaGreen
                    else -> Color.LightGray
                }, RoundedCornerShape(8.dp)
            )
            .padding(top = 8.dp),
        fontSize = 28.sp,
        text = char,
        color = if (isFocused) {
            Color.LightGray
        } else {
            if (isSystemInDarkTheme()) DarkSlateGrey else SeaGreen
        },
        textAlign = TextAlign.Center
    )
}

@Preview( showBackground = true )
@Composable
fun OTTPFieldPreview() {
    QuizAppTheme {
        OtpTextField(otpText = "", onOtpTextChange = { s, b -> } )
    }
}