package dev.alexeyqqq.wordsapp.domain.usecases.trainer

import dev.alexeyqqq.wordsapp.domain.entity.Question
import dev.alexeyqqq.wordsapp.domain.repository.TrainerRepository

class GetNextQuestionUseCase(
    private val trainerRepository: TrainerRepository,
) {

    suspend operator fun invoke(): Question = trainerRepository.getNextQuestion()
}