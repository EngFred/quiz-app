package com.engineerfred.kotlin.ktor.ui.screens.admin.create_update.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.engineerfred.kotlin.ktor.R
import com.engineerfred.kotlin.ktor.ui.theme.KtorTheme

@Composable
fun QuestionTextEntry(
    modifier: Modifier = Modifier,
    textValue: () -> String,
    placeholder: String,
    onTextChanged: (String) -> Unit,
    onAddAnswerClicked: (() -> Unit)? = null,
    isForAnswerChoices: Boolean =  false,
    title: String,
    imeAction: ImeAction = ImeAction.Next,
    enabled: Boolean = true
) {

    Column( modifier  = modifier.fillMaxWidth() ) {

        Text(
            modifier = Modifier.padding(start = 14.dp),
            text = "${title}:",
            fontFamily = Font(R.font.lexend_bold).toFontFamily(),
            fontSize = 15.sp,
            color = Color.DarkGray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.size(6.dp))

        if ( !isForAnswerChoices ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = textValue(),
                onValueChange = onTextChanged,
                placeholder = { Text(
                    text = placeholder,
                    fontFamily = Font(R.font.lexend_regular).toFontFamily(),
                    color = Color.Gray,
                    fontSize = 14.sp,
                    maxLines = 1
                ) },
                keyboardOptions = KeyboardOptions( imeAction = imeAction ),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor =MaterialTheme.colorScheme.primary
                ),
                textStyle = TextStyle.Default.copy(
                    fontSize = 14.sp,
                    fontFamily = Font(R.font.lexend_regular).toFontFamily()
                ), enabled = enabled
            )
        } else {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = textValue(),
                onValueChange = onTextChanged,
                placeholder = {
                    Text(
                        text = placeholder,
                        fontFamily = Font(R.font.lexend_regular).toFontFamily(),
                        color = Color.Gray,
                        fontSize = 14.sp,
                        maxLines = 1
                    )
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary
                ),
                keyboardOptions = KeyboardOptions( imeAction = imeAction ),
                textStyle = TextStyle.Default.copy(
                    fontSize = 14.sp,
                    fontFamily = Font(R.font.lexend_regular).toFontFamily()
                ),
                trailingIcon = {
                    IconButton(
                        onClick = { onAddAnswerClicked?.invoke() },
                        enabled = textValue().isNotEmpty()
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Done,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    }
}

@Preview( showBackground = true )
@Composable
fun QuestionTextEntryPreview() {
    KtorTheme {
        QuestionTextEntry(
            textValue = { "" },
            placeholder = "Enter here the question",
            onTextChanged = {},
            title = "Question"
        )
    }
}