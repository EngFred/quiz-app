package com.engineerfred.kotlin.ktor.ui.screens.student.students

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.engineerfred.kotlin.ktor.ui.theme.SeaGreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AllStudentsScreen(
    modifier: Modifier,
    allStudentsViewModel: AllStudentsViewModel = hiltViewModel()
) {
    val uiState = allStudentsViewModel.uiState

    val colorScheme = MaterialTheme.colorScheme
    val view = LocalView.current

    LaunchedEffect(key1 = Unit) {
        val window = (view.context as Activity).window
        window.statusBarColor = colorScheme.primary.toArgb()
        window.navigationBarColor = colorScheme.primary.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
    }

    when{
        uiState.isLoading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                CircularProgressIndicator( color = if ( isSystemInDarkTheme() ) Color.White else SeaGreen )
            }
        }
        !uiState.isLoading && !uiState.loadError.isNullOrEmpty() -> {
            Column(
                modifier = modifier
                    .padding(15.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "${uiState.loadError}", color = Color.Red, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.size(10.dp))
                Button(onClick = { allStudentsViewModel.onEvent(AllStudentsUiEvents.Retry) }, shape = RoundedCornerShape(10.dp)) {
                    Text(text = "Retry", fontWeight = FontWeight.Bold)
                }
            }
        }
        !uiState.isLoading && uiState.loadError.isNullOrEmpty() -> {
            val students = uiState.students
            if ( students.isEmpty() ) {
                Box(modifier = modifier
                    .fillMaxSize()
                    .padding(15.dp), contentAlignment = Alignment.Center){
                    Text(
                        text = "There are no other students yet!!",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = modifier.fillMaxSize()
                ) {
                    items(
                        count = students.size,
                        key = { students[it].id }
                    ) {
                        val student = students[it]
                        if ( student.id != FirebaseAuth.getInstance().uid ) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Box(modifier = Modifier.wrapContentSize().padding(top = 7.dp)) {
                                    AsyncImage(
                                        model = student.profileImage,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(75.dp)
                                            .clip(CircleShape)
                                            .background(Color.LightGray),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 13.dp)
                                ) {
                                    Text(
                                        text = student.name,
                                        fontWeight = FontWeight.ExtraBold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = buildAnnotatedString {
                                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                                append("English level: ")
                                            }
                                            append(student.englishLevel)
                                        },
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = buildAnnotatedString {
                                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                                append("Math level: ")
                                            }
                                            append(student.mathLevel)
                                        },
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }

                    }
                }
            }
        }

    }
}