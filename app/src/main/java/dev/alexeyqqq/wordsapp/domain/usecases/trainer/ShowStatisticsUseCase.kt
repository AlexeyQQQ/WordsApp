package dev.alexeyqqq.wordsapp.domain.usecases.trainer

import dev.alexeyqqq.wordsapp.domain.entity.Statistics
import dev.alexeyqqq.wordsapp.domain.repository.TrainerRepository
import javax.inject.Inject

class ShowStatisticsUseCase @Inject constructor(
    private val trainerRepository: TrainerRepository,
) {

    suspend operator fun invoke(): Statistics = trainerRepository.showStatistics()
}