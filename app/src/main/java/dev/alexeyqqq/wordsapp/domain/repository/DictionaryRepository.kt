package dev.alexeyqqq.wordsapp.domain.repository

import dev.alexeyqqq.wordsapp.domain.entity.Dictionary
import kotlinx.coroutines.flow.Flow

interface DictionaryRepository {

    fun getAllDictionaries(): Flow<List<Dictionary>>

    suspend fun getDictionary(dictionaryId: Long): Dictionary

    suspend fun insertDictionary(dictionary: Dictionary): Long

    suspend fun updateDictionary(dictionaryId: Long, newName: String)

    suspend fun deleteDictionary(dictionaryId: Long)
}