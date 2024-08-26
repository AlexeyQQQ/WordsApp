package dev.alexeyqqq.wordsapp.data.repository

import dev.alexeyqqq.wordsapp.data.database.WordsDao
import dev.alexeyqqq.wordsapp.data.mapToDbModel
import dev.alexeyqqq.wordsapp.data.mapToDomain
import dev.alexeyqqq.wordsapp.domain.entity.Word
import dev.alexeyqqq.wordsapp.domain.repository.WordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WordRepositoryImpl(
    private val wordsDao: WordsDao,
) : WordRepository {

    override fun getAllWords(): Flow<List<Word>> =
        wordsDao.getAllWords().map { list ->
            list.map { word ->
                word.mapToDomain()
            }
        }

    override suspend fun getWord(wordId: Long): Word = wordsDao.getWord(wordId).mapToDomain()

    override suspend fun insertWord(word: Word) = wordsDao.insertWord(word.mapToDbModel())

    override suspend fun updateWord(word: Word) = wordsDao.updateWord(word.mapToDbModel())

    override suspend fun deleteWord(wordId: Long) = wordsDao.deleteWord(wordId)
}