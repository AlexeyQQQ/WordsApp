package dev.alexeyqqq.wordsapp.domain.repository

import dev.alexeyqqq.wordsapp.domain.entity.Dictionary
import dev.alexeyqqq.wordsapp.domain.entity.Word
import kotlinx.coroutines.flow.Flow

interface RelationRepository {

    fun getDictionaryWords(dictionaryId: Long): Flow<List<Word>>

    fun getDictionariesWithoutWord(wordId: Long): Flow<List<Dictionary>>

    suspend fun saveNewWordInDictionary(original: String, translate: String, dictionaryId: Long)

    suspend fun saveOldWordInDictionary(wordId: Long, dictionaryId: Long)

    suspend fun removeWordFromDictionary(wordId: Long, dictionaryId: Long)
}