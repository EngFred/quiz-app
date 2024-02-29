package com.engineerfred.kotlin.ktor.ui.screens.admin.admins

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun AdminsScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center ) {
        Text(text = "All Admins screen")
    }
}