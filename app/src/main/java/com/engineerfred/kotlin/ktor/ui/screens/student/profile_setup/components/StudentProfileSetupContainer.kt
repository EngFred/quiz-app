package com.engineerfred.kotlin.ktor.ui.screens.student.profile_setup.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.engineerfred.kotlin.ktor.R
import com.engineerfred.kotlin.ktor.common.CustomTextEntry
import com.engineerfred.kotlin.ktor.ui.theme.QuizAppTheme

@Composable
fun StudentProfileSetupContainer(
    modifier: Modifier = Modifier,
    profileImage: String,
    cardModifier: Modifier = Modifier,
    onNameChanged: (String) -> Unit,
    nameTextValue: () -> String,
    nameErrorTextValue: String,
    onBioChanged: (String) -> Unit,
    bioTextValue: () -> String,
    onCameraClicked: () -> Unit
) {

    Box(modifier = modifier
        .fillMaxWidth()
        .height(470.dp),
        contentAlignment = Alignment.Center
    ) {

        ElevatedCard(
            onClick = { /*TODO*/ },
            modifier = cardModifier.padding( horizontal = 15.dp )
        ) {
            Column(
                Modifier
                    .padding(top = 70.dp, bottom = 70.dp, start = 10.dp, end = 10.dp)
            ) {
                Text(
                    text = "What should every one call you?",
                    fontFamily = Font(R.font.lexend_bold).toFontFamily(),
                    modifier = Modifier.padding(start = 5.dp, bottom = 10.dp, end = 5.dp),
                    fontSize = 14.sp
                )
                CustomTextEntry(
                    hint = "Enter name (Required)",
                    textValue = nameTextValue(),
                    onValueChanged = onNameChanged,
                    isNameField = true,
                    errorText = nameErrorTextValue,
                    cornerSize = 8.dp,
                )
                Text(
                    text = "Anything to say about yourself?",
                    fontFamily = Font(R.font.lexend_bold).toFontFamily(),
                    modifier = Modifier.padding(start = 5.dp, bottom = 10.dp, end = 5.dp),
                    fontSize = 14.sp
                )
                CustomTextEntry(
                    hint = "Enter bio (Optional)",
                    textValue = bioTextValue(),
                    onValueChanged = onBioChanged,
                    isNameField = true,
                    cornerSize = 8.dp
                )
            }
        }

        Box(modifier = Modifier
            .align(Alignment.TopCenter)
            .clip(CircleShape)
            .size(90.dp)
            .border(2.dp, MaterialTheme.colorScheme.primary, shape = CircleShape)
        ) {

            AsyncImage(
                model = profileImage,
                contentDescription = stringResource(R.string.student_image),
                contentScale = ContentScale.Crop
            )

            Icon(
                imageVector = Icons.Rounded.CameraAlt,
                contentDescription = stringResource(R.string.camera),
                modifier = Modifier
                    .align(Alignment.Center)
                    .clickable {
                        onCameraClicked.invoke()
                    }
            )

        }
    }
}


@Preview( showBackground = true )
@Composable
fun StudentRegisterContainerPreview() {
    QuizAppTheme {
        StudentProfileSetupContainer(
            onNameChanged = {},
            nameTextValue = { "" },
            nameErrorTextValue = "",
            onBioChanged = {},
            bioTextValue = {""},
            onCameraClicked = {},
            profileImage = ""
        )
    }
}