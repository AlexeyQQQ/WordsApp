package dev.alexeyqqq.wordsapp.data

import dev.alexeyqqq.wordsapp.data.database.DictionaryDao
import dev.alexeyqqq.wordsapp.data.database.RelationDao
import dev.alexeyqqq.wordsapp.data.database.WordsDao
import dev.alexeyqqq.wordsapp.data.database.entity.DictionaryDbModel
import dev.alexeyqqq.wordsapp.data.database.entity.WordDbModel
import dev.alexeyqqq.wordsapp.data.network.TranslateService
import dev.alexeyqqq.wordsapp.data.network.model.Pronunciation
import dev.alexeyqqq.wordsapp.data.network.model.RootTranslateResponse
import dev.alexeyqqq.wordsapp.data.repository.TrainerRepositoryImpl
import dev.alexeyqqq.wordsapp.domain.entity.Statistics
import dev.alexeyqqq.wordsapp.domain.entity.Word
import dev.alexeyqqq.wordsapp.domain.exceptions.TrainerExceptions
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.kotlin.never
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class TrainerRepositoryImplTest {

    private lateinit var repository: TrainerRepositoryImpl

    private val wordsDao = mock<WordsDao>()
    private val dictionaryDao = mock<DictionaryDao>()
    private val relationDao = mock<RelationDao>()
    private val translateService = mock<TranslateService>()

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun before() {
        repository = TrainerRepositoryImpl(wordsDao, dictionaryDao, relationDao, translateService)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun after() {
        Mockito.reset(wordsDao)
        Mockito.reset(dictionaryDao)
        Mockito.reset(relationDao)
        Mockito.reset(translateService)
        Dispatchers.resetMain()
    }

    @Test
    fun `should increase correct answers for word`() = runTest {
        val word = Word(1, "Word", "Translation", 0)
        val updatedWord =
            word.copy(correctAnswersCount = word.correctAnswersCount + 1).mapToDbModel()

        repository.increaseCorrectAnswers(word)
        verify(wordsDao, times(1)).updateWord(updatedWord)
    }

    @Test
    fun `should return correct statistics`() = runTest {
        val dictionaries = listOf(
            DictionaryDbModel(1L, "Dictionary1"),
            DictionaryDbModel(2L, "Dictionary2")
        )
        val words = listOf(
            WordDbModel(1L, "Word1", "Translation1", 0),
            WordDbModel(2L, "Word2", "Translation2", 3)
        )

        whenever(dictionaryDao.getAllDictionaries()).thenReturn(flowOf(dictionaries))
        whenever(wordsDao.getAllWords()).thenReturn(flowOf(words))

        val actual = repository.showStatistics()

        assertEquals(
            Statistics(
                dictionariesTotal = 2,
                wordsTotal = 2,
                wordsLearned = 1,
                percentageRatio = 50,
            ), actual
        )
        verify(dictionaryDao, times(1)).getAllDictionaries()
        verify(wordsDao, times(1)).getAllWords()
    }

    @Test
    fun `should return statistics with no words`() = runTest {
        val dictionaries = listOf(DictionaryDbModel(1L, "Dictionary1"))
        val words = emptyList<WordDbModel>()

        whenever(dictionaryDao.getAllDictionaries()).thenReturn(flowOf(dictionaries))
        whenever(wordsDao.getAllWords()).thenReturn(flowOf(words))

        val statistics = repository.showStatistics()

        assertEquals(
            Statistics(
                dictionariesTotal = 1,
                wordsTotal = 0,
                wordsLearned = 0,
                percentageRatio = 0
            ), statistics
        )

        verify(dictionaryDao, times(1)).getAllDictionaries()
        verify(wordsDao, times(1)).getAllWords()
    }

    @Test
    fun `should translate word`() = runTest {
        val translate = RootTranslateResponse(
            sourceLanguage = "en",
            sourceText = "word",
            destinationLanguage = "ru",
            destinationText = "слово",
            pronunciation = Pronunciation("link1", "link2")
        )

        whenever(translateService.translate(text = "word")).thenReturn(translate)

        val actual = repository.translate("word")
        assertEquals("слово", actual)
        verify(translateService, times(1)).translate(text = "word")
    }

    @Test
    fun `should return question`() = runTest {
        val words = listOf(
            WordDbModel(1L, "Word1", "Translation1", 0),
            WordDbModel(2L, "Word2", "Translation2", 1),
            WordDbModel(3L, "Word3", "Translation3", 2),
            WordDbModel(4L, "Word4", "Translation4", 3),
            WordDbModel(5L, "Word5", "Translation5", 0),
            WordDbModel(6L, "Word6", "Translation6", 0),
            WordDbModel(7L, "Word7", "Translation7", 0),
            WordDbModel(8L, "Word8", "Translation8", 0),
            WordDbModel(9L, "Word9", "Translation9", 0),
            WordDbModel(10L, "Word10", "Translation10", 0)
        )

        whenever(relationDao.getDictionaryWords(1L)).thenReturn(flowOf(words))
        whenever(wordsDao.getAllWords()).thenReturn(flowOf(words))

        val actualWithDictionary = repository.getNextQuestion(1L)
        assertEquals(4, actualWithDictionary.options.size)
        assertEquals(10, actualWithDictionary.wordsTotal)
        assertEquals(1, actualWithDictionary.wordsLearned)
        verify(relationDao, times(1)).getDictionaryWords(1L)
        verify(wordsDao, never()).getAllWords()

        val actualAllWords = repository.getNextQuestion(-1L)
        assertEquals(4, actualAllWords.options.size)
        assertEquals(10, actualAllWords.wordsTotal)
        assertEquals(1, actualAllWords.wordsLearned)
        verify(relationDao, times(1)).getDictionaryWords(1L)
        verify(wordsDao, times(1)).getAllWords()
    }

    @Test
    fun `should throw FewWords exception for insufficient words`() = runTest {
        val words = listOf(
            WordDbModel(1L, "Word1", "Translation1", 0),
            WordDbModel(2L, "Word2", "Translation2", 1)
        )

        whenever(relationDao.getDictionaryWords(1L)).thenReturn(flowOf(words))

        try {
            repository.getNextQuestion(1L)
        } catch (e: Exception) {
            assertEquals(true, e is TrainerExceptions.FewWords)
            verify(relationDao, times(1)).getDictionaryWords(1L)
        }
    }

    @Test
    fun `should throw NoWordsToLearn exception when no unlearned words`() = runTest {
        val words = listOf(
            WordDbModel(1L, "Word1", "Translation1", 3),
            WordDbModel(2L, "Word2", "Translation2", 3),
            WordDbModel(3L, "Word3", "Translation3", 3),
            WordDbModel(4L, "Word4", "Translation4", 3),
            WordDbModel(5L, "Word5", "Translation5", 3),
            WordDbModel(6L, "Word6", "Translation6", 3),
            WordDbModel(7L, "Word7", "Translation7", 3),
            WordDbModel(8L, "Word8", "Translation8", 3),
            WordDbModel(9L, "Word9", "Translation9", 3),
            WordDbModel(10L, "Word10", "Translation10", 3)
        )

        whenever(relationDao.getDictionaryWords(1L)).thenReturn(flowOf(words))

        try {
            repository.getNextQuestion(1L)
        } catch (e: Exception) {
            assertEquals(true, e is TrainerExceptions.NoWordsToLearn)
            verify(relationDao, times(1)).getDictionaryWords(1L)
        }
    }
}