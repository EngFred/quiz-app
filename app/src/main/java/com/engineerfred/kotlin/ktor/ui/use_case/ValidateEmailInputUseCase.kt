package com.engineerfred.kotlin.ktor.ui.use_case

import android.util.Patterns
import com.engineerfred.kotlin.ktor.ui.model.EmailInputValidationType

class ValidateEmailInputUseCase() {
    operator fun invoke(email: String) : EmailInputValidationType {
        return when {
            email.isEmpty() -> EmailInputValidationType.EmptyField
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> EmailInputValidationType.Invalid
            else -> EmailInputValidationType.Valid
        }
    }
}