package com.engineerfred.kotlin.ktor.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.engineerfred.kotlin.ktor.ui.theme.KtorTheme

@Composable
fun HeaderBackground(
    modifier: Modifier = Modifier,
    leftColor: Color,
    rightColor: Color
) {

    val colorsList = rememberSaveable {
        listOf(leftColor, rightColor)
    }
    
    Canvas(modifier = modifier) {
        drawCircle(
            radius = size.width,
            center = Offset(center.x, -size.width/1.5f),
            brush = Brush.linearGradient(colors = colorsList, end = Offset(center.x+500f, 0f))
        )
    }

}

@Preview( showBackground = true )
@Composable
fun HeaderBackGroundPreview() {
    KtorTheme {
        HeaderBackground(
            leftColor = MaterialTheme.colorScheme.primary,
            rightColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.fillMaxSize()
        )
    }
}