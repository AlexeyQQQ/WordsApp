package dev.alexeyqqq.wordsapp.domain.usecases.trainer

import dev.alexeyqqq.wordsapp.domain.repository.TrainerRepository
import javax.inject.Inject

class TranslateUseCase @Inject constructor(
    private val trainerRepository: TrainerRepository,
) {

    suspend operator fun invoke(word: String) = trainerRepository.translate(word)
}