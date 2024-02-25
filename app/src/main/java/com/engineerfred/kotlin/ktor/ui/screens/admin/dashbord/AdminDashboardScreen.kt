package com.engineerfred.kotlin.ktor.ui.screens.admin.dashbord

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.engineerfred.kotlin.ktor.common.CardRow
import com.engineerfred.kotlin.ktor.ui.model.Subject

@Composable
fun AdminDashboardScreen(
    onMathCardClicked: (String) -> Unit,
    onEnglishCardClicked: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 0.dp),
            verticalArrangement = Arrangement.Center,
        ) {

            CardRow(
                card1Text = Subject.English.name.uppercase(),
                card2Text = Subject.Mathematics.name.uppercase(),
                onCard1Click = {
                    onEnglishCardClicked.invoke( Subject.English.name )
                },
                onCard2Click = {
                    onMathCardClicked.invoke( Subject.Mathematics.name )
                }
            )

        }

    }
}