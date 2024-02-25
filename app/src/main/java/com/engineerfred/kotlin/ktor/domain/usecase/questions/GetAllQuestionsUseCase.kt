package com.engineerfred.kotlin.ktor.domain.usecase.questions

import com.engineerfred.kotlin.ktor.domain.repository.QuestionsRepository
import javax.inject.Inject

class GetAllQuestionsUseCase @Inject constructor(
    private val questionsRepository: QuestionsRepository
) {
    operator fun invoke( subject: String ) = questionsRepository.getAllQuestions( subject )
}