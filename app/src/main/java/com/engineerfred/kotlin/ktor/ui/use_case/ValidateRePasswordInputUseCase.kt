package com.engineerfred.kotlin.ktor.ui.use_case

import com.engineerfred.kotlin.ktor.ui.model.RePasswordInputValidationType

class ValidateRePasswordInputUseCase() {
    operator fun invoke( password: String, rePassword: String ) : RePasswordInputValidationType {
        return when {
            rePassword.isEmpty() -> RePasswordInputValidationType.EmptyField
            password != rePassword -> RePasswordInputValidationType.NoMatch
            else -> RePasswordInputValidationType.Valid
        }
    }
}