package dev.alexeyqqq.wordsapp.domain.repository

import dev.alexeyqqq.wordsapp.domain.entity.LoadQuestion
import dev.alexeyqqq.wordsapp.domain.entity.Statistics

interface TrainerRepository {

    suspend fun getNextQuestion(): LoadQuestion

    suspend fun increaseCorrectAnswers()

    suspend fun showStatistics(): Statistics

    suspend fun translate(word: String): String
}