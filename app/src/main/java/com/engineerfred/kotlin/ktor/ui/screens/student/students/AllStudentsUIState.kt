package com.engineerfred.kotlin.ktor.ui.screens.student.students

import com.engineerfred.kotlin.ktor.domain.model.Student

data class AllStudentsUIState(
    val isLoading: Boolean = true,
    val loadError: String? = null,
    val students: List<Student> = emptyList()
)
