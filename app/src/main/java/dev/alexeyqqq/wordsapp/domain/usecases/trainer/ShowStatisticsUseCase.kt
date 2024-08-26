package dev.alexeyqqq.wordsapp.domain.usecases.trainer

import dev.alexeyqqq.wordsapp.domain.entity.Statistics
import dev.alexeyqqq.wordsapp.domain.repository.TrainerRepository

class ShowStatisticsUseCase(
    private val trainerRepository: TrainerRepository,
) {

    suspend operator fun invoke(): Statistics = trainerRepository.showStatistics()
}