package dev.alexeyqqq.wordsapp.domain.usecases.trainer

import dev.alexeyqqq.wordsapp.domain.repository.TrainerRepository

class IncreaseCorrectAnswersUseCase(
    private val trainerRepository: TrainerRepository,
) {

    suspend operator fun invoke() = trainerRepository.increaseCorrectAnswers()
}