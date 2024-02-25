package com.engineerfred.kotlin.ktor.ui.use_case

import com.engineerfred.kotlin.ktor.ui.model.NameInputValidationType

class ValidateNameInputUseCase() {
    operator fun invoke(name: String) : NameInputValidationType {
        return when {
            name.isEmpty() -> NameInputValidationType.EmptyField
            name.length < 3 -> NameInputValidationType.Invalid
            else -> NameInputValidationType.Valid
        }
    }
}