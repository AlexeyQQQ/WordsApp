package dev.alexeyqqq.wordsapp.domain.usecases.trainer

import dev.alexeyqqq.wordsapp.domain.repository.TrainerRepository

class TranslateUseCase(
    private val trainerRepository: TrainerRepository,
) {

    suspend operator fun invoke(word: String) = trainerRepository.translate(word)
}