package com.engineerfred.kotlin.ktor.ui.screens.admin.create_update.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.engineerfred.kotlin.ktor.R
import com.engineerfred.kotlin.ktor.ui.model.Level

@Composable
fun AddQuestionContainer(
    levelButtonText: String,
    subjectButtonText: String,
    onLevelButtonClicked: () -> Unit,
    onDismissMenuClicked: () -> Unit,
    onMenuItemClicked: (String) -> Unit,
    showDropDownMenu: Boolean,
    questionTextValue: String,
    answerChoiceTextValue: String,
    correctAnswerTextValue: String,
    onQuestionTextChanged: (String) -> Unit,
    onAnswerChoiceTextChanged: (String) -> Unit,
    onQuestionCorrectAnswerTextChanged: (String) -> Unit,
    onAddAnswerClicked: (() -> Unit)? = null,
    enabledCorrectAnswerTextField: Boolean = true
) {

    val items = listOf(
        Level.Beginner.name,
        Level.Intermediate.name,
        Level.Advanced.name
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 15.dp)
    ) {
        QuestionTextEntry(
            textValue = { questionTextValue },
            placeholder = "Type here the question",
            onTextChanged = onQuestionTextChanged,
            title = "Question"
        )
        QuestionTextEntry(
            textValue = { answerChoiceTextValue },
            placeholder = "Type here the answer choices",
            onTextChanged = onAnswerChoiceTextChanged,
            title = "Answer choices (optional)",
            isForAnswerChoices = true,
            onAddAnswerClicked = onAddAnswerClicked,
            imeAction = if ( enabledCorrectAnswerTextField ) ImeAction.Next else ImeAction.Done
        )
        QuestionTextEntry(
            textValue = { correctAnswerTextValue },
            placeholder = if ( enabledCorrectAnswerTextField ) "Type here the correct answer" else "Choose the correct answer below",
            onTextChanged = onQuestionCorrectAnswerTextChanged,
            title = "Answer",
            imeAction = ImeAction.Done,
            enabled = enabledCorrectAnswerTextField
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.SpaceBetween
            ) {
                Column {
                    Text(
                        modifier = Modifier.padding(start = 3.dp),
                        text = "Level:",
                        fontFamily = Font(R.font.lexend_bold).toFontFamily(),
                        fontSize = 15.sp,
                        color = Color.DarkGray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.size(5.dp))
                    Button(
                        onClick = { onLevelButtonClicked.invoke() },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = MaterialTheme.colorScheme.primary
                        ), shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(text = levelButtonText)
                        Icon(imageVector = Icons.Rounded.KeyboardArrowDown, contentDescription = null )
                    }
                }

                Column {
                    Text(
                        modifier = Modifier.padding(start = 3.dp),
                        text = "Subject:",
                        fontFamily = Font(R.font.lexend_bold).toFontFamily(),
                        fontSize = 15.sp,
                        color = Color.DarkGray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.size(5.dp))
                    Button(
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = MaterialTheme.colorScheme.primary
                        ), enabled = false,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(text = subjectButtonText)
                    }
                }
            }
            // Dropdown menu
            DropdownMenu(
                expanded = showDropDownMenu,
                onDismissRequest = {
                     onDismissMenuClicked()
                },
                //offset = DpOffset(x = 15.dp, y = (-10).dp)
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(
                            text = item,
                            fontFamily = Font(R.font.lexend_regular).toFontFamily(),
                        ) },
                        onClick = {
                            onMenuItemClicked.invoke(item)
                        }
                    )
                }
            }
        }
    }
}

//@Preview( showBackground = true )
//@Composable
//fun AddQuestionContainerPreview() {
//    KtorTheme {
//        AddQuestionContainer(
//            levelButtonText = "Beginner",
//            subjectButtonText = "Mathematics",
//            {},
//            expandedState,
//            selectedItem
//        )
//    }
//}