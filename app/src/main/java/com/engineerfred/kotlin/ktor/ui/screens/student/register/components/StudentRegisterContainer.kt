package com.engineerfred.kotlin.ktor.ui.screens.student.register.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.engineerfred.kotlin.ktor.common.CustomButtonComponent
import com.engineerfred.kotlin.ktor.common.CustomTextEntry
import com.engineerfred.kotlin.ktor.ui.theme.KtorTheme

@Composable
fun StudentRegisterContainer(
    modifier: Modifier = Modifier,
    phoneNumberValue: () -> String,
    phoneNumberValueError: String,
    onPhoneNumberChanged: (String) -> Unit,
    onVerifyButtonClicked: () -> Unit,
    onDoneButtonClicked: () -> Unit,
    enableCodeEntryTextField: Boolean = false,
    enablePhoneNumberEntryTextField: Boolean = true,
    enableDoneButton: Boolean = false,
    enableVerifyButton: Boolean = false,
    ottpTextValue: () -> String,
    onOtpTextChange: (String, Boolean) -> Unit,
    isVerifying: Boolean,
    isFinishing: Boolean,
) {

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 24.dp)
        ) {
            CustomTextEntry(
                hint = "Enter phone Number e.g +256...",
                textValue = phoneNumberValue(),
                onValueChanged = onPhoneNumberChanged,
                isNameField = true,
                keyboardType = KeyboardType.Phone,
                errorText = phoneNumberValueError,
                cornerSize = 8.dp,
                shadow = 0.dp,
                enabled = enablePhoneNumberEntryTextField
            )

            CustomButtonComponent(
                text = "Verify",
                backGroundColor = MaterialTheme.colorScheme.primary,
                onClick = onVerifyButtonClicked,
                enabled = { enableVerifyButton },
                cornerSize = 5.dp,
                btnModifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 60.dp),
                isLoading = { isVerifying }
            )

            Spacer(modifier = Modifier.size(50.dp))

            Divider()

            Spacer(modifier = Modifier.size(30.dp))

            OtpTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                otpText = ottpTextValue(),
                onOtpTextChange = onOtpTextChange,
                enabled = enableCodeEntryTextField
            )

            CustomButtonComponent(
                text = "Done",
                backGroundColor = MaterialTheme.colorScheme.primary,
                onClick = onDoneButtonClicked,
                enabled = { enableDoneButton },
                cornerSize = 5.dp,
                btnModifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 60.dp),
                isLoading = { isFinishing }
            )

        }
    }

}

@Preview( showBackground = true )
@Composable
fun StudentRegisterContainerPreview() {
    KtorTheme {
        StudentRegisterContainer(
            phoneNumberValue = {""},
            phoneNumberValueError = "",
            onPhoneNumberChanged = {},
            onVerifyButtonClicked = {},
            isVerifying = false,
            isFinishing = false,
            ottpTextValue = { "" },
            onOtpTextChange = {s,b ->},
            onDoneButtonClicked = {}
        )
    }
}