package dev.alexeyqqq.wordsapp.domain.repository

import dev.alexeyqqq.wordsapp.domain.entity.Dictionary
import dev.alexeyqqq.wordsapp.domain.entity.Word
import kotlinx.coroutines.flow.Flow

interface RelationRepository {

    fun getDictionaryWords(dictionaryId: Long): Flow<List<Word>>

    fun getDictionariesWithoutWord(original: String): Flow<List<Dictionary>>
}