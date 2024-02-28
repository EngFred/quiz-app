package com.engineerfred.kotlin.ktor.ui.screens.student.questions.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.engineerfred.kotlin.ktor.common.LevelIndicator
import com.engineerfred.kotlin.ktor.ui.model.Level
import com.engineerfred.kotlin.ktor.ui.theme.DarkSlateGrey
import com.engineerfred.kotlin.ktor.ui.theme.QuizAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelSelectionContainer(
    modifier: Modifier = Modifier,
    onCard1Clicked: (String) -> Unit,
    onCard2Clicked: (String) -> Unit,
    onCard3Clicked: (String) -> Unit
) {

    val backGround =  if ( isSystemInDarkTheme() ) DarkSlateGrey else MaterialTheme.colorScheme.surface

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = backGround
            ),
            onClick = {
                onCard1Clicked.invoke( Level.Beginner.name )
            }
        ) {
            LevelIndicator(
                level = Level.Beginner.name,
                fontSize = 25.sp,
                dotSize = 14.dp,
                fontWeight = FontWeight.Bold,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 70.dp, horizontal = 50.dp)
                    .background(backGround)
            )
        }

        Spacer(modifier = Modifier.size(20.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = backGround
            ),
            onClick = {
                onCard2Clicked.invoke( Level.Intermediate.name )
            }
        ) {
            LevelIndicator(
                level = Level.Intermediate.name,
                fontSize = 25.sp,
                dotSize = 14.dp,
                fontWeight = FontWeight.Bold,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 70.dp, horizontal = 50.dp)
                    .background(backGround)
            )
        }

        Spacer(modifier = Modifier.size(20.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = backGround
            ),
            onClick = {
                onCard3Clicked.invoke( Level.Advanced.name )
            }
        ) {
            LevelIndicator(
                level = Level.Advanced.name,
                fontSize = 25.sp,
                dotSize = 14.dp,
                fontWeight = FontWeight.Bold,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 70.dp, horizontal = 50.dp)
                    .background(backGround)
            )
        }
    }
}

@Preview( showBackground = true )
@Composable
fun LevelSelectionContainerPreview() {
    QuizAppTheme {
        LevelSelectionContainer(modifier = Modifier, {},{},{})
    }
}