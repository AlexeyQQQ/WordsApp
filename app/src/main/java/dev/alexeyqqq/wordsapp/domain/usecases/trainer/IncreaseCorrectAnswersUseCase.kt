package dev.alexeyqqq.wordsapp.domain.usecases.trainer

import dev.alexeyqqq.wordsapp.domain.entity.Word
import dev.alexeyqqq.wordsapp.domain.repository.TrainerRepository

class IncreaseCorrectAnswersUseCase(
    private val trainerRepository: TrainerRepository,
) {

    suspend operator fun invoke(word: Word) = trainerRepository.increaseCorrectAnswers(word)
}