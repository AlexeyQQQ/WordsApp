package dev.alexeyqqq.wordsapp.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.alexeyqqq.wordsapp.data.database.AppDatabase
import dev.alexeyqqq.wordsapp.data.database.DictionaryDao
import dev.alexeyqqq.wordsapp.data.database.RelationDao
import dev.alexeyqqq.wordsapp.data.database.WordsDao
import dev.alexeyqqq.wordsapp.data.database.entity.DictionaryDbModel
import dev.alexeyqqq.wordsapp.data.database.entity.RelationDbModel
import dev.alexeyqqq.wordsapp.data.database.entity.WordDbModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RelationDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var wordsDao: WordsDao
    private lateinit var dictionaryDao: DictionaryDao
    private lateinit var relationDao: RelationDao

    @Before
    fun setupDb() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        wordsDao = db.wordsDao()
        dictionaryDao = db.dictionaryDao()
        relationDao = db.relationDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun testAddWordToDictionary() = runBlocking {
        val word1 = WordDbModel(id = 10L, original = "hello", translation = "привет")
        wordsDao.insertWord(word1)

        val dictionary1 = DictionaryDbModel(id = 1L, "Dictionary1")
        val dictionary2 = DictionaryDbModel(id = 2L, "Dictionary2")
        val dictionary3 = DictionaryDbModel(id = 3L, "Dictionary3")
        dictionaryDao.insertDictionary(dictionary1)
        dictionaryDao.insertDictionary(dictionary2)
        dictionaryDao.insertDictionary(dictionary3)

        assertEquals(
            listOf(dictionary1, dictionary2, dictionary3),
            relationDao.getDictionariesWithoutWord(10L).first()
        )

        val relation = RelationDbModel(id = 1L, wordId = 10L, dictionaryId = 2L)
        relationDao.insertRelation(relation)
        assertEquals(
            listOf(dictionary1, dictionary3),
            relationDao.getDictionariesWithoutWord(10L).first()
        )
        assertEquals(
            listOf(word1),
            relationDao.getDictionaryWords(2L).first()
        )
        assertEquals(
            listOf(relation),
            relationDao.getRelationsForWord(10L)
        )
    }

    @Test
    fun testDeleteAllRelationsForDictionary() = runBlocking {
        val word1 = WordDbModel(id = 10L, original = "hello", translation = "привет")
        wordsDao.insertWord(word1)

        val dictionary1 = DictionaryDbModel(id = 1L, "Dictionary1")
        val dictionary2 = DictionaryDbModel(id = 2L, "Dictionary2")
        dictionaryDao.insertDictionary(dictionary1)
        dictionaryDao.insertDictionary(dictionary2)

        assertEquals(
            listOf(dictionary1, dictionary2),
            relationDao.getDictionariesWithoutWord(10L).first()
        )
        assertEquals(
            emptyList<RelationDbModel>(),
            relationDao.getRelationsForWord(10L)
        )

        val relation1 = RelationDbModel(id = 1L, wordId = 10L, dictionaryId = 1L)
        val relation2 = RelationDbModel(id = 2L, wordId = 10L, dictionaryId = 2L)
        relationDao.insertRelation(relation1)
        relationDao.insertRelation(relation2)

        assertEquals(
            emptyList<DictionaryDbModel>(),
            relationDao.getDictionariesWithoutWord(10L).first()
        )
        assertEquals(
            listOf(relation1, relation2),
            relationDao.getRelationsForWord(10L)
        )

        relationDao.deleteAllRelationsForDictionary(1L)

        assertEquals(
            listOf(dictionary1),
            relationDao.getDictionariesWithoutWord(10L).first()
        )
        assertEquals(
            listOf(relation2),
            relationDao.getRelationsForWord(10L)
        )
    }

    @Test
    fun testWordFromDictionary() = runBlocking {
        val word1 = WordDbModel(id = 10L, original = "hello", translation = "привет")
        val word2 = WordDbModel(id = 20L, original = "dog", translation = "собака")
        wordsDao.insertWord(word1)
        wordsDao.insertWord(word2)

        val dictionary1 = DictionaryDbModel(id = 1L, "Dictionary1")
        dictionaryDao.insertDictionary(dictionary1)

        assertEquals(
            emptyList<WordDbModel>(),
            relationDao.getDictionaryWords(2L).first()
        )

        val relation1 = RelationDbModel(id = 1L, wordId = 10L, dictionaryId = 1L)
        val relation2 = RelationDbModel(id = 2L, wordId = 20L, dictionaryId = 1L)
        relationDao.insertRelation(relation1)
        relationDao.insertRelation(relation2)

        assertEquals(
            listOf(word1, word2),
            relationDao.getDictionaryWords(1L).first()
        )

        relationDao.removeWordFromDictionary(wordId = 10L, dictionaryId = 1L)
        assertEquals(
            listOf(word2),
            relationDao.getDictionaryWords(1L).first()
        )
    }
}