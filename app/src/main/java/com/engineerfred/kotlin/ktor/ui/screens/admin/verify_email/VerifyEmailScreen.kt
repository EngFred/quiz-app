package com.engineerfred.kotlin.ktor.ui.screens.admin.verify_email

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.engineerfred.kotlin.ktor.R
import com.engineerfred.kotlin.ktor.common.CustomButtonComponent
import com.engineerfred.kotlin.ktor.ui.theme.QuizAppTheme

@Composable
fun VerifyEmailScreen(
    email: String,
    onDoneClicked: () -> Unit
) {

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){

        Image(
            painter = painterResource(id = R.drawable.email_verify),
            contentDescription = "verify email"
        )

        Text(
            text = "We have sent a verification code" +
                " on the the email; ${email}. Check the mail inbox" +
                " to verify this email then after" +
                " click 'done' below to proceed.",
            fontFamily = Font(R.font.lexend_medium).toFontFamily(),
            textAlign = TextAlign.Start,
            fontSize = 18.sp,
        )

        CustomButtonComponent(
            text = "Done",
            backGroundColor = MaterialTheme.colorScheme.primary,
            onClick = { onDoneClicked() },
            modifier = Modifier.padding(top = 20.dp, start = 30.dp, end= 30.dp),
            btnModifier = Modifier.fillMaxWidth(),
            cornerSize = 7.dp,
            enabled = { true }
        )

    }

}

@Preview( showBackground = true )
@Composable
fun VerifyEmailScreenPreview() {
    QuizAppTheme {
        VerifyEmailScreen(
            email = "omongolealfred4@gmail.com"
        ) {}
    }
}