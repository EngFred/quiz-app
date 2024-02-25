package com.engineerfred.kotlin.ktor.ui.use_case

import android.util.Patterns
import com.engineerfred.kotlin.ktor.ui.model.PhoneNumberInputValidationType

class ValidatePhoneNumberUseCase() {
    operator fun invoke(phoneNumber: String) : PhoneNumberInputValidationType {
        return when {
            phoneNumber.isEmpty() -> PhoneNumberInputValidationType.EmptyField
            !phoneNumber.startsWith("+") ->PhoneNumberInputValidationType.NoCountryCode
            !Patterns.PHONE.matcher(phoneNumber).matches()-> PhoneNumberInputValidationType.Invalid
            else -> PhoneNumberInputValidationType.Valid
        }
    }
}