package com.engineerfred.kotlin.ktor.ui.screens.admin.admins

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.engineerfred.kotlin.ktor.R
import com.engineerfred.kotlin.ktor.ui.theme.SeaGreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AdminsScreen(
    modifier: Modifier,
    viewModel: AdminsViewModel = hiltViewModel()
) {

    val uiState = viewModel.uiState

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
                Button(onClick = { viewModel.onEvent(AdminsUiEvents.Retry) },
                    shape = RoundedCornerShape(10.dp)) {
                    Text(text = "Retry", fontWeight = FontWeight.Bold)
                }
            }
        }
        !uiState.isLoading && uiState.loadError.isNullOrEmpty() -> {
            val admins = uiState.admins
            if ( admins.isEmpty() ) {
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
                        count = admins.size,
                        key = { admins[it].id }
                    ) {
                        val admin = admins[it]
                        if ( admin.id != FirebaseAuth.getInstance().uid ) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.teacher),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(55.dp)
                                        .clip(CircleShape)
                                        .background(Color.LightGray).border(1.dp, if (isSystemInDarkTheme()) Color.White else SeaGreen, CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 13.dp)
                                ) {
                                    Text(
                                        text = "${admin.firstName.replaceFirstChar { it.titlecase() }} ${admin.lastName.replaceFirstChar { it.titlecase() }}",
                                        fontWeight = FontWeight.ExtraBold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = admin.email,
                                        fontSize = 13.sp,
                                        color = Color.Gray
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