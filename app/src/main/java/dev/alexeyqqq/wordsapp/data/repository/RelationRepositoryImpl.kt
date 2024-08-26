package dev.alexeyqqq.wordsapp.data.repository

import dev.alexeyqqq.wordsapp.data.database.RelationDao
import dev.alexeyqqq.wordsapp.data.mapToDomain
import dev.alexeyqqq.wordsapp.domain.entity.Dictionary
import dev.alexeyqqq.wordsapp.domain.entity.Word
import dev.alexeyqqq.wordsapp.domain.repository.RelationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RelationRepositoryImpl(
    private val relationDao: RelationDao,
) : RelationRepository {

    override fun getDictionaryWords(dictionaryId: Long): Flow<List<Word>> =
        relationDao.getDictionaryWords(dictionaryId).map { list ->
            list.map { word ->
                word.mapToDomain()
            }
        }

    override fun getDictionariesWithoutWord(wordId: Long): Flow<List<Dictionary>> =
        relationDao.getDictionariesWithoutWord(wordId).map { list ->
            list.map { dictionary ->
                dictionary.mapToDomain()
            }
        }
}