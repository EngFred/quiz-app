package com.engineerfred.kotlin.ktor.ui.model

enum class PasswordInputValidationType {
    EmptyField,
    MissingNumber,
    MissingSpecialChar,
    MissingCapitalLetter,
    MissingSmallLetter,
    Short,
    Valid
}