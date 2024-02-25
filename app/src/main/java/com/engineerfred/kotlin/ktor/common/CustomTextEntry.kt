package com.engineerfred.kotlin.ktor.common

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.engineerfred.kotlin.ktor.R
import com.engineerfred.kotlin.ktor.ui.theme.KtorTheme

@Composable
fun CustomTextEntry(
    inputModifier: Modifier = Modifier,
    parentModifier: Modifier = Modifier,
    hint: String,
    textValue: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    textColor: Color = Color.Black,
    cursorColor: Color = Color.Transparent,
    backGroundColor: Color = Color.White,
    iconsColor: Color = MaterialTheme.colorScheme.primary,
    cornerSize: Dp = 20.dp,
    height: Dp = 60.dp,
    shadow: Dp = 7.dp,
    onValueChanged: (String) -> Unit,
    leadingIcon: ImageVector? = null,
    isPasswordField: Boolean = false,
    isNameField: Boolean = false,
    errorText: String? = null,
    errorTextColor: Color = MaterialTheme.colorScheme.error,
    enabled: Boolean = true
) {

    var showPassword by rememberSaveable {
        mutableStateOf(false)
    }

    val trailingIcon =  if ( !showPassword )
        Icons.Rounded.Visibility
    else Icons.Rounded.VisibilityOff

    val passwordTransformation = if ( !showPassword ) PasswordVisualTransformation() else VisualTransformation.None

    if ( !isNameField ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            TextField(
                modifier = inputModifier
                    .fillMaxWidth()
                    .height(height)
                    .shadow(shadow)
                    .border(1.dp, iconsColor, RoundedCornerShape(cornerSize)),
                enabled = enabled,
                colors = TextFieldDefaults.colors(
                    cursorColor = iconsColor,
                    focusedContainerColor = backGroundColor,
                    unfocusedContainerColor = backGroundColor,
                    focusedIndicatorColor = cursorColor,
                    unfocusedIndicatorColor = cursorColor,
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor
                ),
                textStyle = TextStyle.Default.copy(
                    fontSize = 14.sp,
                    fontFamily = Font(R.font.lexend_regular).toFontFamily()
                ),
                value = textValue,
                onValueChange = onValueChanged,
                shape = RoundedCornerShape(cornerSize),
                singleLine = true,
                leadingIcon = {
                    leadingIcon?.let {
                        Icon(
                            imageVector = it,
                            contentDescription = stringResource(R.string.leading_icon),
                            tint = iconsColor
                        )
                    }
                },
                trailingIcon = {
                    if ( isPasswordField ) {
                        Icon(
                            imageVector = trailingIcon,
                            contentDescription = stringResource(R.string.trailing_icon),
                            modifier = Modifier.clickable {
                                showPassword = !showPassword
                            },
                            tint = iconsColor
                        )
                    }
                },
                placeholder = {
                    Text(
                        text = hint,
                        fontFamily = Font(R.font.lexend_medium).toFontFamily(),
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                visualTransformation = if ( isPasswordField )  passwordTransformation else VisualTransformation.None
            )

            Text(
                text = errorText ?: "",
                fontSize = 12.sp,
                fontFamily = Font(R.font.lexend_regular).toFontFamily(),

                modifier = Modifier.padding(bottom = 2.dp, start = 4.dp, end = 4.dp),
                color = errorTextColor
            )
        }
    } else {
        Column(
            modifier = parentModifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {

            TextField(
                modifier = inputModifier
                    .fillMaxWidth()
                    .height(height)
                    .shadow(shadow)
                    .border(1.dp, iconsColor, RoundedCornerShape(cornerSize)),
                enabled = enabled,
                colors = TextFieldDefaults.colors(
                    cursorColor = iconsColor,
                    focusedContainerColor = backGroundColor,
                    unfocusedContainerColor = backGroundColor,
                    focusedIndicatorColor = cursorColor,
                    unfocusedIndicatorColor = cursorColor,
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor
                ),
                textStyle = TextStyle.Default.copy(
                    fontSize = 14.sp,
                    fontFamily = Font(R.font.lexend_regular).toFontFamily()
                ),
                value = textValue,
                onValueChange = onValueChanged,
                shape = RoundedCornerShape(cornerSize),
                singleLine = true,
                placeholder = {
                    Text(
                        text = hint,
                        fontFamily = Font(R.font.lexend_medium).toFontFamily(),
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                visualTransformation = if ( isPasswordField )  passwordTransformation else VisualTransformation.None
            )
            Text(
                text = errorText ?: "",
                fontSize = 12.sp,
                fontFamily = Font(R.font.lexend_regular).toFontFamily(),
                modifier = Modifier.padding(bottom = 2.dp, start = 4.dp, end = 4.dp),
                color = errorTextColor
            )

        }

    }
}
@Preview( showBackground = true )
@Composable
fun CustomTextEntryPreview() {
    KtorTheme {
        CustomTextEntry(
            hint = "Email",
            textValue = "fred@gmail.com",
            onValueChanged = {},
            leadingIcon = Icons.Rounded.Email,
            cornerSize = 11.dp,
            isNameField = true,
            errorText = "error text here"
        )
    }
}