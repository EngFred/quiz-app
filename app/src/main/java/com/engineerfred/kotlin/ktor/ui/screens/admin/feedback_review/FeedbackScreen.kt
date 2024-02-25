package com.engineerfred.kotlin.ktor.ui.screens.admin.feedback_review

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun FeedbackScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center ) {
        Text(text = "Admins feedback review screen")
    }
}