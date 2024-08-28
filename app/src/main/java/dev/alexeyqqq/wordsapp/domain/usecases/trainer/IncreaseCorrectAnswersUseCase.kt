package dev.alexeyqqq.wordsapp.domain.usecases.trainer

import dev.alexeyqqq.wordsapp.domain.entity.Word
import dev.alexeyqqq.wordsapp.domain.repository.TrainerRepository
import javax.inject.Inject

class IncreaseCorrectAnswersUseCase @Inject constructor(
    private val trainerRepository: TrainerRepository,
) {

    suspend operator fun invoke(word: Word) = trainerRepository.increaseCorrectAnswers(word)
}