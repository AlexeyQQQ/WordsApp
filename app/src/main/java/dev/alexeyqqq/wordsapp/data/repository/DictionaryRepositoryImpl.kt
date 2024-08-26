package dev.alexeyqqq.wordsapp.data.repository

import dev.alexeyqqq.wordsapp.data.database.DictionaryDao
import dev.alexeyqqq.wordsapp.data.database.RelationDao
import dev.alexeyqqq.wordsapp.data.mapToDbModel
import dev.alexeyqqq.wordsapp.data.mapToDomain
import dev.alexeyqqq.wordsapp.domain.entity.Dictionary
import dev.alexeyqqq.wordsapp.domain.repository.DictionaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DictionaryRepositoryImpl(
    private val dictionaryDao: DictionaryDao,
    private val relationDao: RelationDao,
) : DictionaryRepository {

    override fun getAllDictionaries(): Flow<List<Dictionary>> =
        dictionaryDao.getAllDictionaries().map { list ->
            list.map { dictionary ->
                dictionary.mapToDomain()
            }
        }

    override suspend fun insertDictionary(dictionary: Dictionary) =
        dictionaryDao.insertDictionary(dictionary.mapToDbModel())

    override suspend fun updateDictionary(dictionaryId: Long, newName: String) =
        dictionaryDao.updateDictionary(dictionaryId, newName)

    override suspend fun deleteDictionary(dictionaryId: Long) {
        dictionaryDao.deleteDictionary(dictionaryId)
        relationDao.deleteAllRelationsForDictionary(dictionaryId)
    }
}