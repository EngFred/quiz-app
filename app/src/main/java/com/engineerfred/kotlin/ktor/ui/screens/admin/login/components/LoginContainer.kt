package com.engineerfred.kotlin.ktor.ui.screens.admin.login.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.engineerfred.kotlin.ktor.common.CustomTextEntry

@Composable
fun LoginContainer(
    modifier: Modifier = Modifier,
    emailValue: () -> String,
    emailValueError: () -> String,
    passwordValue: () -> String,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        CustomTextEntry(
            inputModifier = Modifier
                .fillMaxWidth(),
            hint = "Email address",
            textValue = emailValue(),
            leadingIcon = Icons.Rounded.Email,
            keyboardType = KeyboardType.Email,
            onValueChanged = onEmailChanged,
            errorText = emailValueError(),
            cornerSize = 10.dp
        )

        CustomTextEntry(
            inputModifier = Modifier
                .fillMaxWidth(),
            hint = "Password",
            keyboardType = KeyboardType.Password,
            isPasswordField = true,
            leadingIcon = Icons.Rounded.Lock,
            textValue = passwordValue(),
            onValueChanged = onPasswordChanged,
            cornerSize = 10.dp
        )

    }

}