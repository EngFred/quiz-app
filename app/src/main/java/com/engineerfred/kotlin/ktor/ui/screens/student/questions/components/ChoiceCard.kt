package com.engineerfred.kotlin.ktor.ui.screens.student.questions.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.engineerfred.kotlin.ktor.ui.theme.DarkSlateGrey
import com.engineerfred.kotlin.ktor.ui.theme.GainsboroWhite
import com.engineerfred.kotlin.ktor.ui.theme.KtorTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChoiceCard(
    answerChoice : String,
    selected: Boolean = false,
    onChoiceClick: (String) -> Unit
) {

    val cardColor = if ( !isSystemInDarkTheme() ) {
        if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f) else Color.White
    } else {
        if (selected) DarkSlateGrey.copy(alpha = 0.5f) else GainsboroWhite
    }

    val textColor = if ( !isSystemInDarkTheme() ) {
        if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f) else Color.Black
    } else {
        if (selected) GainsboroWhite else Color.Black
    }

    val iconTint = if ( !isSystemInDarkTheme() ) {
        if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f) else Color.LightGray
    } else {
        if (selected) GainsboroWhite else Color.LightGray
    }

    ElevatedCard(
        onClick = { onChoiceClick.invoke(answerChoice) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, bottom = 13.dp),
        shape = RoundedCornerShape(10.dp),
    ) {
        Row ( modifier = Modifier
            .fillMaxWidth()
            .background(cardColor)
            .padding(horizontal = 10.dp)
            .height(55.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = answerChoice,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 14.dp),
                color = textColor
            )
            Icon(
                imageVector = if ( selected ) Icons.Rounded.CheckCircle else Icons.Rounded.CheckCircleOutline ,
                contentDescription = null,
                tint = iconTint
            )
        }
    }
}

@Preview( showBackground = true )
@Composable
fun ChoiceCardPreview() {
    KtorTheme {
        ChoiceCard(
            answerChoice = "1983",
            selected = false,
            onChoiceClick = {}
        )
    }
}