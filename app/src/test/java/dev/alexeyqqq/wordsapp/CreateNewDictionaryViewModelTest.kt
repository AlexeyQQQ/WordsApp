package dev.alexeyqqq.wordsapp

import dev.alexeyqqq.wordsapp.domain.entity.Dictionary
import dev.alexeyqqq.wordsapp.domain.usecases.dictionary.DeleteDictionaryUseCase
import dev.alexeyqqq.wordsapp.domain.usecases.dictionary.InsertDictionaryUseCase
import dev.alexeyqqq.wordsapp.domain.usecases.dictionary.UpdateDictionaryUseCase
import dev.alexeyqqq.wordsapp.presentation.create_dictionary.CreateNewDictionaryUiState
import dev.alexeyqqq.wordsapp.presentation.create_dictionary.CreateNewDictionaryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class CreateNewDictionaryViewModelTest {

    private lateinit var viewModel: CreateNewDictionaryViewModel

    private val insertDictionaryUseCase = mock<InsertDictionaryUseCase>()
    private val updateDictionaryUseCase = mock<UpdateDictionaryUseCase>()
    private val deleteDictionaryUseCase = mock<DeleteDictionaryUseCase>()

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun before() {
        viewModel = CreateNewDictionaryViewModel(
            insertDictionaryUseCase,
            updateDictionaryUseCase,
            deleteDictionaryUseCase,
        )
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun after() {
        Mockito.reset(insertDictionaryUseCase)
        Mockito.reset(updateDictionaryUseCase)
        Mockito.reset(deleteDictionaryUseCase)
        Dispatchers.resetMain()
    }


    @Test
    fun `test case create new dictionary`() = runTest {
        assertEquals(CreateNewDictionaryUiState.EmptyInput, viewModel.uiState.value)

        viewModel.checkInput("w")
        assertEquals(CreateNewDictionaryUiState.NewInput, viewModel.uiState.value)

        viewModel.checkInput("")
        assertEquals(CreateNewDictionaryUiState.EmptyInput, viewModel.uiState.value)

        viewModel.checkInput("q")
        assertEquals(CreateNewDictionaryUiState.NewInput, viewModel.uiState.value)
        verify(insertDictionaryUseCase, never()).invoke(any())
        verify(updateDictionaryUseCase, never()).invoke(any(), any())
        verify(deleteDictionaryUseCase, never()).invoke(any())

        viewModel.saveNewDictionary("q")
        assertEquals(CreateNewDictionaryUiState.NewInput, viewModel.uiState.value)
        verify(insertDictionaryUseCase, times(1))
            .invoke(Dictionary(name = "q"))
        verify(updateDictionaryUseCase, never()).invoke(any(), any())
        verify(deleteDictionaryUseCase, never()).invoke(any())
    }

    @Test
    fun `test case rename dictionary`() = runTest {
        assertEquals(CreateNewDictionaryUiState.EmptyInput, viewModel.uiState.value)

        viewModel.checkInput("name1", "name")
        assertEquals(CreateNewDictionaryUiState.NewInput, viewModel.uiState.value)

        viewModel.checkInput("name", "name")
        assertEquals(CreateNewDictionaryUiState.EmptyInput, viewModel.uiState.value)

        viewModel.checkInput("nam", "name")
        assertEquals(CreateNewDictionaryUiState.NewInput, viewModel.uiState.value)
        verify(insertDictionaryUseCase, never()).invoke(any())
        verify(updateDictionaryUseCase, never()).invoke(any(), any())
        verify(deleteDictionaryUseCase, never()).invoke(any())

        viewModel.renameDictionary(1L, "nam")
        assertEquals(CreateNewDictionaryUiState.NewInput, viewModel.uiState.value)
        verify(insertDictionaryUseCase, never()).invoke(any())
        verify(updateDictionaryUseCase, times(1))
            .invoke(1L, "nam")
        verify(deleteDictionaryUseCase, never()).invoke(any())
    }

    @Test
    fun `test case delete dictionary`() = runTest {
        viewModel.deleteDictionary(1L)
        assertEquals(CreateNewDictionaryUiState.EmptyInput, viewModel.uiState.value)
        verify(insertDictionaryUseCase, never()).invoke(any())
        verify(updateDictionaryUseCase, never()).invoke(any(), any())
        verify(deleteDictionaryUseCase, times(1)).invoke(1L)
    }
}