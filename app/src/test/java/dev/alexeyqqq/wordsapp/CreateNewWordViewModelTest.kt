package dev.alexeyqqq.wordsapp

import dev.alexeyqqq.wordsapp.domain.usecases.relation.SaveNewWordInDictionaryUseCase
import dev.alexeyqqq.wordsapp.domain.usecases.trainer.TranslateUseCase
import dev.alexeyqqq.wordsapp.presentation.create_word.CreateNewWordUiState
import dev.alexeyqqq.wordsapp.presentation.create_word.CreateNewWordViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
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
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class CreateNewWordViewModelTest {

    private lateinit var viewModel: CreateNewWordViewModel

    private val translateUseCase = mock<TranslateUseCase>()
    private val saveNewWordInDictionaryUseCase = mock<SaveNewWordInDictionaryUseCase>()

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun before() {
        viewModel = CreateNewWordViewModel(
            translateUseCase,
            saveNewWordInDictionaryUseCase,
        )
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun after() {
        Mockito.reset(translateUseCase)
        Mockito.reset(saveNewWordInDictionaryUseCase)
        Dispatchers.resetMain()
    }

    @Test
    fun `test case create new word`() = runTest {
        whenever(translateUseCase.invoke("m")).thenReturn("м")
        whenever(translateUseCase.invoke("ma")).thenReturn("ма")
        whenever(translateUseCase.invoke("max")).thenThrow(Exception())

        // step 1: state EmptyOriginal
        assertEquals(CreateNewWordUiState.EmptyOriginal, viewModel.uiState.value)

        // step 2: changing original input
        viewModel.checkOriginalInput("m")
        assertEquals(CreateNewWordUiState.Loading, viewModel.uiState.value)
        advanceUntilIdle()
        assertEquals(CreateNewWordUiState.Translate("м"), viewModel.uiState.value)

        viewModel.checkOriginalInput("")
        assertEquals(CreateNewWordUiState.EmptyOriginal, viewModel.uiState.value)

        viewModel.checkOriginalInput("m")
        assertEquals(CreateNewWordUiState.Loading, viewModel.uiState.value)
        advanceUntilIdle()
        assertEquals(CreateNewWordUiState.Translate("м"), viewModel.uiState.value)

        viewModel.checkOriginalInput("ma")
        assertEquals(CreateNewWordUiState.Loading, viewModel.uiState.value)
        advanceUntilIdle()
        assertEquals(CreateNewWordUiState.Translate("ма"), viewModel.uiState.value)

        // step 3: handle error
        viewModel.checkOriginalInput("max")
        assertEquals(CreateNewWordUiState.Loading, viewModel.uiState.value)
        advanceUntilIdle()
        assertEquals(CreateNewWordUiState.Translate(""), viewModel.uiState.value)

        viewModel.checkOriginalInput("ma")
        assertEquals(CreateNewWordUiState.Loading, viewModel.uiState.value)
        advanceUntilIdle()
        assertEquals(CreateNewWordUiState.Translate("ма"), viewModel.uiState.value)

        // step 4: changing translate input
        viewModel.checkTranslateInput("м")
        assertEquals(CreateNewWordUiState.NotEmptyInput, viewModel.uiState.value)

        viewModel.checkTranslateInput("")
        assertEquals(CreateNewWordUiState.EmptyTranslate, viewModel.uiState.value)

        viewModel.checkTranslateInput("д")
        assertEquals(CreateNewWordUiState.NotEmptyInput, viewModel.uiState.value)

        // step 5: save word
        viewModel.saveNewWord("ma", "д", 1L)
        assertEquals(CreateNewWordUiState.NotEmptyInput, viewModel.uiState.value)

        verify(translateUseCase, times(2)).invoke("m")
        verify(translateUseCase, times(2)).invoke("ma")
        verify(translateUseCase, times(1)).invoke("max")
        verify(saveNewWordInDictionaryUseCase, times(1))
            .invoke("ma", "д", 1L)
    }
}