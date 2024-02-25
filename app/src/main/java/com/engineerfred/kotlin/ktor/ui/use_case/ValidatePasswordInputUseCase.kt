package com.engineerfred.kotlin.ktor.ui.use_case

import com.engineerfred.kotlin.ktor.ui.model.PasswordInputValidationType

class ValidatePasswordInputUseCase() {

    operator fun invoke(password: String): PasswordInputValidationType {
        val regexLength = ".{8,}" // At least 8 characters
        val containsSpecialChar = password.contains("[@\$!%*?&]".toRegex())
        val containsNumber = password.contains("\\d".toRegex())
        val containsCapitalLetter = password.contains("[A-Z]".toRegex())
        val containsSmallLetter = password.contains("[a-z]".toRegex())
        return when {
            password.isEmpty() -> PasswordInputValidationType.EmptyField
            !password.matches(regexLength.toRegex()) -> PasswordInputValidationType.Short
            !containsSpecialChar -> PasswordInputValidationType.MissingSpecialChar
            !containsNumber -> PasswordInputValidationType.MissingNumber
            !containsCapitalLetter -> PasswordInputValidationType.MissingCapitalLetter
            !containsSmallLetter -> PasswordInputValidationType.MissingSmallLetter
            else -> PasswordInputValidationType.Valid
        }
    }
}