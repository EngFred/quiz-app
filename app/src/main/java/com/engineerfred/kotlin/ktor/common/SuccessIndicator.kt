package com.engineerfred.kotlin.ktor.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CloudDone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.engineerfred.kotlin.ktor.R
import com.engineerfred.kotlin.ktor.ui.theme.Charcoal
import com.engineerfred.kotlin.ktor.ui.theme.SeaGreen

@Composable
fun SuccessIndicator(
    text: String,
    onDoneClicked: () -> Unit,
    onSetAnotherQuestionClicked: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Rounded.CloudDone,
            contentDescription = null,
            tint = if (isSystemInDarkTheme()) Color.White else SeaGreen,
            modifier = Modifier.size(54.dp)
        )
        Spacer(modifier = Modifier.size(7.dp))
        Text(
            text = text,
            fontFamily = Font(R.font.lexend_bold).toFontFamily(),
            fontSize = 15.sp,
            color =  if (isSystemInDarkTheme()) Color.White else SeaGreen,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.size(7.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { onSetAnotherQuestionClicked.invoke() },
                colors = ButtonDefaults.buttonColors(
                    contentColor =  if (isSystemInDarkTheme()) Color.White else SeaGreen,
                    containerColor = Color.Transparent,
                ), shape = RoundedCornerShape(6.dp), border = BorderStroke( 2.dp,  if (isSystemInDarkTheme()) Color.White else SeaGreen )
            ) {
                Text(
                    text = "Set another question",
                    fontFamily = Font(R.font.lexend_bold).toFontFamily(),
                )
            }
            Button(
                onClick = { onDoneClicked.invoke() },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor =  if (isSystemInDarkTheme()) Charcoal else SeaGreen
                ), shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = "Done",
                    fontFamily = Font(R.font.lexend_bold).toFontFamily(),
                )
            }
        }
    }
}