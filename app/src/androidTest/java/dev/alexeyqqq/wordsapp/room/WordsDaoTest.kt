package dev.alexeyqqq.wordsapp.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.alexeyqqq.wordsapp.data.database.AppDatabase
import dev.alexeyqqq.wordsapp.data.database.WordsDao
import dev.alexeyqqq.wordsapp.data.database.entity.WordDbModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class WordsDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var wordsDao: WordsDao

    @Before
    fun setupDb() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        wordsDao = db.wordsDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun getAllSortedWordsTest() = runBlocking {
        val word1 = WordDbModel(id = 1L, original = "hello", translation = "привет")
        val word2 = WordDbModel(id = 2L, original = "dog", translation = "собака")
        wordsDao.insertWord(word1)
        wordsDao.insertWord(word2)

        val actual = wordsDao.getAllWords().first()
        assertEquals(listOf(word2, word1), actual)
    }

    @Test
    fun insertWordAndGetWordByIdTest() = runBlocking {
        val word = WordDbModel(id = 10L, original = "hello", translation = "привет")

        val wordId = wordsDao.insertWord(word)
        val actual = wordsDao.getWord(wordId)
        assertEquals(word, actual)
    }

    @Test
    fun updateWordTest() = runBlocking {
        val word = WordDbModel(id = 1L, original = "hello", translation = "привет")
        wordsDao.insertWord(word)

        var actual = wordsDao.getWord(1L)
        assertEquals(0, actual.correctAnswersCount)

        wordsDao.updateWord(word.copy(correctAnswersCount = word.correctAnswersCount + 1))
        actual = wordsDao.getWord(1L)
        assertEquals(1, actual.correctAnswersCount)
    }

    @Test
    fun deleteWordTest() = runBlocking {
        val word = WordDbModel(id = 1L, original = "hello", translation = "привет")
        val wordId = wordsDao.insertWord(word)

        var actual = wordsDao.getAllWords().first()
        assertEquals(1, actual.size)

        wordsDao.deleteWord(wordId)
        actual = wordsDao.getAllWords().first()
        assertTrue(actual.isEmpty())
    }
}