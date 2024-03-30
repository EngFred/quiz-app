package com.engineerfred.kotlin.ktor.ui.screens.student.results

import android.app.Activity
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.engineerfred.kotlin.ktor.ui.theme.SeaGreen
import com.engineerfred.kotlin.ktor.ui.viewModel.SharedViewModel

@Composable
fun ResultsScreen(
    sharedViewModel: SharedViewModel
) {

    val view = LocalView.current
    val colorScheme = MaterialTheme.colorScheme

    LaunchedEffect(key1 = true) {
        val window = (view.context as Activity).window
        window.statusBarColor = colorScheme.surface.toArgb()
        window.navigationBarColor = colorScheme.surface.toArgb()
    }

    if ( sharedViewModel.answeredQuestionsState.isNotEmpty() ) {
        val passedQuestionsSize = sharedViewModel.answeredQuestionsState.filter { it.isPassed }.size
        
        val comment = when {
            passedQuestionsSize == 5 -> "Fair"
            passedQuestionsSize == 6 -> "Good"
            passedQuestionsSize == 7 -> "Very Good"
            passedQuestionsSize == 8 -> "Excellent"
            passedQuestionsSize >= 9 -> "Genius"
            else -> "Work hard"
        }
            Column(
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            Text(text = buildAnnotatedString { 
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)){ append("Comment: ") }
                append(comment)
            }, textAlign = TextAlign.Center, maxLines = 1 , modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.size(8.dp))    
            LazyColumn{
                items(sharedViewModel.answeredQuestionsState) {
                    val borderColor = if (it.isPassed) SeaGreen else MaterialTheme.colorScheme.error
                    val icon = if ( it.isPassed ) Icons.Rounded.Done else Icons.Rounded.Close
                    val iconColor = if ( it.isPassed ) SeaGreen else MaterialTheme.colorScheme.error
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .border(2.dp, borderColor, RoundedCornerShape(10.dp))
                            .padding(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = it.answeredQuestion, modifier = Modifier
                                    .weight(1f)
                                    .padding(5.dp)
                            )
                            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(40.dp), tint = iconColor )
                        }
                        if ( !it.isPassed ){
                            Text(text = buildAnnotatedString {
                                withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)){
                                    append("Your answer: ")
                                }
                                append(it.givenAnswer.replaceFirstChar { it.titlecase() })
                            })
                            Text(text = buildAnnotatedString {
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold)){
                                    append("Correct Answer: ")
                                }
                                withStyle(SpanStyle(color = SeaGreen)){
                                    append(it.correctAnswer.replaceFirstChar { it.titlecase() })
                                }
                            })
                        } else {
                            Text(text = buildAnnotatedString {
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold)){
                                    append("Answer: ")
                                }
                                append(it.correctAnswer.replaceFirstChar { it.titlecase() })
                            })
                        }
                    }
                    Spacer(modifier = Modifier.size(10.dp))
                }
            }
        }
    } else
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "You didn't answer anything!!", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }

}