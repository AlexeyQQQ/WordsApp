package dev.alexeyqqq.wordsapp.data.repository

import dev.alexeyqqq.wordsapp.data.database.RelationDao
import dev.alexeyqqq.wordsapp.data.mapToDomainDictionaryList
import dev.alexeyqqq.wordsapp.data.mapToDomainWordList
import dev.alexeyqqq.wordsapp.domain.entity.Dictionary
import dev.alexeyqqq.wordsapp.domain.entity.Word
import dev.alexeyqqq.wordsapp.domain.repository.RelationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RelationRepositoryImpl @Inject constructor(
    private val relationDao: RelationDao,
) : RelationRepository {

    override fun getDictionaryWords(dictionaryId: Long): Flow<List<Word>> =
        relationDao.getDictionaryWords(dictionaryId).map { it.mapToDomainWordList() }


    override fun getDictionariesWithoutWord(wordId: Long): Flow<List<Dictionary>> =
        relationDao.getDictionariesWithoutWord(wordId).map { it.mapToDomainDictionaryList() }
}