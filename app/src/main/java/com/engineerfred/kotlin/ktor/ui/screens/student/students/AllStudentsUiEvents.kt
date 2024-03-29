package com.engineerfred.kotlin.ktor.ui.screens.student.students

sealed class AllStudentsUiEvents {
    data object Retry: AllStudentsUiEvents()
}