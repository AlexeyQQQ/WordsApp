package dev.alexeyqqq.wordsapp

import dev.alexeyqqq.wordsapp.domain.entity.Question
import dev.alexeyqqq.wordsapp.domain.entity.Word
import dev.alexeyqqq.wordsapp.domain.exceptions.TrainerExceptions
import dev.alexeyqqq.wordsapp.domain.usecases.trainer.GetNextQuestionUseCase
import dev.alexeyqqq.wordsapp.domain.usecases.trainer.IncreaseCorrectAnswersUseCase
import dev.alexeyqqq.wordsapp.presentation.question.Options
import dev.alexeyqqq.wordsapp.presentation.question.QuestionUiState
import dev.alexeyqqq.wordsapp.presentation.question.QuestionViewModel
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
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class QuestionViewModelTest {

    private lateinit var viewModel: QuestionViewModel

    private val getNextQuestionUseCase = mock<GetNextQuestionUseCase>()
    private val increaseCorrectAnswersUseCase = mock<IncreaseCorrectAnswersUseCase>()

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun before() {
        viewModel = QuestionViewModel(getNextQuestionUseCase, increaseCorrectAnswersUseCase)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun after() {
        Mockito.reset(getNextQuestionUseCase)
        Mockito.reset(increaseCorrectAnswersUseCase)
        Dispatchers.resetMain()
    }

    @Test
    fun `should get NewWord and RightAnswer states`() = runTest {
        val question = Question(
            options = listOf(
                Word(1, "Word1", "Translation1", 0),
                Word(2, "Word2", "Translation2", 0),
                Word(3, "Word3", "Translation3", 0),
                Word(4, "Word4", "Translation4", 0),
            ),
            correctIndex = 0,
            wordsTotal = 10,
            wordsLearned = 2,
        )
        whenever(getNextQuestionUseCase.invoke(1L)).thenReturn(question)

        assertEquals(QuestionUiState.Loading, viewModel.uiState.value)

        viewModel.nextQuestion(1L)
        assertEquals(
            QuestionUiState.NewWord(
                word = "Word1",
                options = listOf("Translation1", "Translation2", "Translation3", "Translation4"),
                wordsTotal = 10,
                wordsLearned = 2,
            ),
            viewModel.uiState.value
        )
        verify(getNextQuestionUseCase, times(1)).invoke(1L)
        verify(increaseCorrectAnswersUseCase, never()).invoke(any())

        viewModel.chooseOption(Options.ONE)
        assertEquals(
            QuestionUiState.RightAnswer(Options.ONE),
            viewModel.uiState.value
        )
        verify(getNextQuestionUseCase, times(1)).invoke(1L)
        verify(increaseCorrectAnswersUseCase, times(1))
            .invoke(Word(1, "Word1", "Translation1", 0))
    }

    @Test
    fun `should get Question and WrongAnswer states`() = runTest {
        val question = Question(
            options = listOf(
                Word(1, "Word1", "Translation1", 0),
                Word(2, "Word2", "Translation2", 0),
                Word(3, "Word3", "Translation3", 0),
                Word(4, "Word4", "Translation4", 0),
            ),
            correctIndex = 1,
            wordsTotal = 10,
            wordsLearned = 2,
        )
        whenever(getNextQuestionUseCase.invoke(1L)).thenReturn(question)

        assertEquals(QuestionUiState.Loading, viewModel.uiState.value)

        viewModel.nextQuestion(1L)
        assertEquals(
            QuestionUiState.NewWord(
                word = "Word2",
                options = listOf("Translation1", "Translation2", "Translation3", "Translation4"),
                wordsTotal = 10,
                wordsLearned = 2,
            ),
            viewModel.uiState.value
        )
        verify(getNextQuestionUseCase, times(1)).invoke(1L)
        verify(increaseCorrectAnswersUseCase, never()).invoke(any())

        viewModel.chooseOption(Options.ONE)
        assertEquals(
            QuestionUiState.WrongAnswer(Options.ONE, 1),
            viewModel.uiState.value
        )
        verify(getNextQuestionUseCase, times(1)).invoke(1L)
        verify(increaseCorrectAnswersUseCase, never()).invoke(any())
    }

    @Test
    fun `when received FewWords exception should get Error state`() = runTest {
        whenever(getNextQuestionUseCase.invoke(1L))
            .thenThrow(TrainerExceptions.FewWords())

        assertEquals(QuestionUiState.Loading, viewModel.uiState.value)

        viewModel.nextQuestion(1L)
        assertEquals(true, viewModel.uiState.value is QuestionUiState.Error)
        verify(getNextQuestionUseCase, times(1)).invoke(1L)
        verify(increaseCorrectAnswersUseCase, never()).invoke(any())
    }

    @Test
    fun `when received NoWordsToLearn exception should get Error state`() = runTest {
        whenever(getNextQuestionUseCase.invoke(1L))
            .thenThrow(TrainerExceptions.NoWordsToLearn())

        assertEquals(QuestionUiState.Loading, viewModel.uiState.value)

        viewModel.nextQuestion(1L)
        assertEquals(true, viewModel.uiState.value is QuestionUiState.Error)
        verify(getNextQuestionUseCase, times(1)).invoke(1L)
        verify(increaseCorrectAnswersUseCase, never()).invoke(any())
    }
}