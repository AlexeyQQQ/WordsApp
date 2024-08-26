package dev.alexeyqqq.wordsapp.domain.repository

import dev.alexeyqqq.wordsapp.domain.entity.Word
import kotlinx.coroutines.flow.Flow

interface WordRepository {

    fun getAllWords(): Flow<List<Word>>

    suspend fun getWord(wordId: Long): Word

    suspend fun insertWord(word: Word)

    suspend fun updateWord(word: Word)

    suspend fun deleteWord(wordId: Long)
}