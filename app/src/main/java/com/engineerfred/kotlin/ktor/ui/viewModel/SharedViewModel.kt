package com.engineerfred.kotlin.ktor.ui.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.engineerfred.kotlin.ktor.domain.model.Admin
import com.engineerfred.kotlin.ktor.domain.model.QuestionState
import com.engineerfred.kotlin.ktor.domain.model.Student

class SharedViewModel : ViewModel() {
    companion object {
        private val TAG = SharedViewModel::class.java.simpleName
    }

    var admin by mutableStateOf<Admin?>( null )
        private set

    var student by mutableStateOf<Student?>( null )
        private set

    var answeredQuestionsState = mutableListOf<QuestionState>()
        private set

    fun addAdmin( newAdmin: Admin ) {
        admin = newAdmin
    }

    fun addStudent( newStudent: Student ) {
        student = newStudent
    }

    fun addAnsweredQuestion( questionState: QuestionState ) {
        answeredQuestionsState.add(questionState)
    }

}