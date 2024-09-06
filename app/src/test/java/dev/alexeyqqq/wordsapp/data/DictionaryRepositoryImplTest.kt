package dev.alexeyqqq.wordsapp.data

import dev.alexeyqqq.wordsapp.data.database.DictionaryDao
import dev.alexeyqqq.wordsapp.data.database.RelationDao
import dev.alexeyqqq.wordsapp.data.database.entity.DictionaryDbModel
import dev.alexeyqqq.wordsapp.data.repository.DictionaryRepositoryImpl
import dev.alexeyqqq.wordsapp.domain.entity.Dictionary
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
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
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class DictionaryRepositoryImplTest {

    private lateinit var repository: DictionaryRepositoryImpl

    private val dictionaryDao = mock<DictionaryDao>()
    private val relationDao = mock<RelationDao>()

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun before() {
        repository = DictionaryRepositoryImpl(dictionaryDao, relationDao)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun after() {
        Mockito.reset(dictionaryDao, relationDao)
        Dispatchers.resetMain()
    }

    @Test
    fun `should get all dictionaries`() = runTest {
        val dbDictionaries = listOf(
            DictionaryDbModel(1L, "Dictionary1"),
            DictionaryDbModel(2L, "Dictionary2")
        )
        val domainDictionaries = listOf(
            Dictionary(1L, "Dictionary1"),
            Dictionary(2L, "Dictionary2")
        )

        whenever(dictionaryDao.getAllDictionaries()).thenReturn(flowOf(dbDictionaries))

        val actual = repository.getAllDictionaries().first()
        assertEquals(domainDictionaries, actual)
        verify(dictionaryDao, times(1)).getAllDictionaries()
    }

    @Test
    fun `should get dictionary by id`() = runTest {
        val dbDictionary = DictionaryDbModel(1L, "Dictionary1")
        val domainDictionary = Dictionary(1L, "Dictionary1")

        whenever(dictionaryDao.getDictionary(1L)).thenReturn(dbDictionary)

        val actual = repository.getDictionary(1L)
        assertEquals(domainDictionary, actual)
        verify(dictionaryDao, times(1)).getDictionary(1L)
    }

    @Test
    fun `should insert dictionary and return id`() = runTest {
        val dbDictionary = DictionaryDbModel(name = "New Dictionary")
        val domainDictionary = Dictionary(name = "New Dictionary")

        whenever(dictionaryDao.insertDictionary(dbDictionary)).thenReturn(1L)

        val actual = repository.insertDictionary(domainDictionary)
        assertEquals(1L, actual)
        verify(dictionaryDao, times(1)).insertDictionary(dbDictionary)
    }

    @Test
    fun `should update dictionary`() = runTest {
        repository.updateDictionary(1L, "Updated Name")
        verify(dictionaryDao, times(1))
            .updateDictionary(1L, "Updated Name")
    }

    @Test
    fun `should delete dictionary and all relations`() = runTest {
        repository.deleteDictionary(1L)
        verify(dictionaryDao, times(1)).deleteDictionary(1L)
        verify(relationDao, times(1))
            .deleteAllRelationsForDictionary(1L)
    }
}