package com.engineerfred.kotlin.ktor.common

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.engineerfred.kotlin.ktor.ui.theme.QuizAppTheme
import kotlinx.coroutines.Dispatchers

@Composable
fun DisplayImage(
    modifier: Modifier = Modifier,
    imageUrl: String
) {

    val context = LocalContext.current

    val listener = object : ImageRequest.Listener {
        override fun onError(request: ImageRequest, result: ErrorResult) {
            Log.d("#", "coil image error : ${result.throwable.message}")
            super.onError(request, result)
        }

        override fun onSuccess(request: ImageRequest, result: SuccessResult) {
            Log.d("#", "coil image SUCCESS")
            super.onSuccess(request, result)
        }
    }

    val imageRequest = ImageRequest.Builder(context)
        .data(imageUrl)
        .crossfade(true)
        .listener(listener)
        .dispatcher(Dispatchers.IO)
        .memoryCacheKey(imageUrl)
        .diskCacheKey(imageUrl)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()

    AsyncImage(
        model = imageRequest,
        contentDescription = "profile image",
        modifier = modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(Color.LightGray),
        contentScale = ContentScale.Crop
    )

}

@Preview( showBackground = true )
@Composable
fun DisplayImagePreview() {
    QuizAppTheme {
        DisplayImage(imageUrl = "")
    }
}