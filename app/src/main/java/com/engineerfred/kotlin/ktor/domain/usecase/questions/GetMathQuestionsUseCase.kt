package com.engineerfred.kotlin.ktor.domain.usecase.questions

import com.engineerfred.kotlin.ktor.domain.repository.QuestionsRepository
import javax.inject.Inject

class GetMathQuestionsUseCase @Inject constructor(
    private val questionsRepository: QuestionsRepository
) {
    operator fun invoke( level: String ) = questionsRepository.getMathQuestions( level )
}