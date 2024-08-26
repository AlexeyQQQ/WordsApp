package dev.alexeyqqq.wordsapp.domain.usecases.trainer

import dev.alexeyqqq.wordsapp.domain.entity.LoadQuestion
import dev.alexeyqqq.wordsapp.domain.repository.TrainerRepository

class GetNextQuestionUseCase(
    private val trainerRepository: TrainerRepository,
) {

    suspend operator fun invoke(): LoadQuestion = trainerRepository.getNextQuestion()
}