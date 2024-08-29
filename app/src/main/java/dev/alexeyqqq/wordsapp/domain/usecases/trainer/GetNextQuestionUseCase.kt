package dev.alexeyqqq.wordsapp.domain.usecases.trainer

import dev.alexeyqqq.wordsapp.domain.entity.Question
import dev.alexeyqqq.wordsapp.domain.repository.TrainerRepository
import javax.inject.Inject

class GetNextQuestionUseCase @Inject constructor(
    private val trainerRepository: TrainerRepository,
) {

    suspend operator fun invoke(dictionaryI: Long): Question =
        trainerRepository.getNextQuestion(dictionaryI)
}