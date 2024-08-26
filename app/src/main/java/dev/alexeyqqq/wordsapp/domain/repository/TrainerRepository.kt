package dev.alexeyqqq.wordsapp.domain.repository

import dev.alexeyqqq.wordsapp.domain.entity.Question
import dev.alexeyqqq.wordsapp.domain.entity.Statistics
import dev.alexeyqqq.wordsapp.domain.entity.Word

interface TrainerRepository {

    suspend fun getNextQuestion(dictionaryId: Long = LEARN_ALL_WORDS): Question

    suspend fun increaseCorrectAnswers(word: Word)

    suspend fun showStatistics(): Statistics

    suspend fun translate(word: String): String

    companion object {
        const val LEARN_ALL_WORDS = -1L
    }
}