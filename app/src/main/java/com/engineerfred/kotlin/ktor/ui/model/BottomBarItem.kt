package com.engineerfred.kotlin.ktor.ui.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomBarItem(
    val label: String = "",
    val description: String = "",
    val icon: ImageVector = Icons.Rounded.Home,
    val destinationScreen: String = ""
)
