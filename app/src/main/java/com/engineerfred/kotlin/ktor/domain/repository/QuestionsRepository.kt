package com.engineerfred.kotlin.ktor.domain.repository

import com.engineerfred.kotlin.ktor.domain.model.Question
import com.engineerfred.kotlin.ktor.util.Response
import kotlinx.coroutines.flow.Flow

interface QuestionsRepository {

    suspend fun setQuestion( question: Question ) : Response<Any>
    suspend fun updateQuestion( question: Question ) : Response<Any>
    suspend fun deleteQuestion(question: Question ) : Response<Any>

    fun getQuestionById( questionId: String ) : Flow<Response<Question>>

    fun getAllQuestions( subject: String ) : Flow<Response<List<Question>>>

    //for students
    fun getEnglishQuestions( level: String ) : Flow<Response<List<Question>>>
    fun getMathQuestions( level: String ) : Flow<Response<List<Question>>>

}