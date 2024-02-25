package com.engineerfred.kotlin.ktor.ui.screens.admin.add_admin.components

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
fun RegisterContainer(
    modifier: Modifier = Modifier,
    lastNameValue: () -> String,
    lastNameValueError: () -> String,
    firstNameValue: () -> String,
    firstNameValueError: () -> String,
    emailValue: () -> String,
    emailValueError: () -> String,
    passwordValue: () -> String,
    passwordValueError: () -> String,
    rePasswordValue: () -> String,
    rePasswordValueError: () -> String,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onRePasswordChanged: (String) -> Unit,
    onLastNameChanged: (String) -> Unit,
    onFirstNameChanged: (String) -> Unit,
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomTextEntry(
                parentModifier = Modifier
                    .fillMaxWidth(.5f)
                    .padding(end = 10.dp),
                hint = "Last Name",
                textValue = lastNameValue(),
                isNameField = true,
                errorText = lastNameValueError(),
                keyboardType = KeyboardType.Text,
                onValueChanged = onLastNameChanged,
                cornerSize = 10.dp
            )
            CustomTextEntry(
                inputModifier = Modifier.fillMaxWidth(1f),
                hint = "First Name",
                isNameField = true,
                keyboardType = KeyboardType.Text,
                errorText = firstNameValueError(),
                textValue = firstNameValue(),
                onValueChanged = onFirstNameChanged,
                cornerSize = 10.dp
            )
        }

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
            errorText = passwordValueError(),
            onValueChanged = onPasswordChanged,
            cornerSize = 10.dp
        )

        CustomTextEntry(
            inputModifier = Modifier.fillMaxWidth(),
            hint = "Confirm password",
            keyboardType = KeyboardType.Password,
            isPasswordField = true,
            leadingIcon = Icons.Rounded.Lock,
            textValue = rePasswordValue(),
            errorText = rePasswordValueError(),
            onValueChanged = onRePasswordChanged,
            cornerSize = 10.dp
        )

    }

}