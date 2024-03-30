package com.engineerfred.kotlin.ktor.domain.usecase.questions

import com.engineerfred.kotlin.ktor.domain.model.Question
import com.engineerfred.kotlin.ktor.domain.repository.QuestionsRepository
import javax.inject.Inject

class DeleteQuestionUseCase @Inject constructor(
    private val questionsRepository: QuestionsRepository
) {
    suspend operator fun invoke( question: Question ) = questionsRepository.deleteQuestion(question)
}