package dev.alexeyqqq.wordsapp.presentation.question

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.alexeyqqq.wordsapp.domain.entity.Question
import dev.alexeyqqq.wordsapp.domain.exceptions.TrainerExceptions
import dev.alexeyqqq.wordsapp.domain.usecases.trainer.GetNextQuestionUseCase
import dev.alexeyqqq.wordsapp.domain.usecases.trainer.IncreaseCorrectAnswersUseCase
import dev.alexeyqqq.wordsapp.domain.usecases.trainer.ShowStatisticsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class QuestionViewModel @Inject constructor(
    private val getNextQuestionUseCase: GetNextQuestionUseCase,
    private val increaseCorrectAnswersUseCase: IncreaseCorrectAnswersUseCase,
    private val showStatisticsUseCase: ShowStatisticsUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<QuestionUiState> =
        MutableStateFlow(QuestionUiState.Loading)
    val uiState get() = _uiState.asStateFlow()

    private var cachedQuestion: Question? = null

    fun nextQuestion(dictionaryId: Long) {
        viewModelScope.launch {
            val statistics = showStatisticsUseCase.invoke()
            try {
                val question = getNextQuestionUseCase.invoke(dictionaryId)
                _uiState.update {
                    QuestionUiState.NewWord(
                        question.options[question.correctIndex].original,
                        question.options.map { it.translation },
                        statistics.wordsLearned,
                        statistics.wordsTotal,
                    )
                }
                cachedQuestion = question
            } catch (e: TrainerExceptions) {
                when (e) {
                    is TrainerExceptions.FewWords -> _uiState.update {
                        QuestionUiState.Error(e)
                    }

                    is TrainerExceptions.NoWordsToLearn -> _uiState.update {
                        QuestionUiState.Error(e)
                    }
                }
            }
        }
    }

    fun chooseOption(option: Options) {
        cachedQuestion?.let { question ->
            val correctIndex = question.correctIndex
            if (option.index == correctIndex) {
                viewModelScope.launch {
                    increaseCorrectAnswersUseCase.invoke(question.options[correctIndex])
                }
                _uiState.update {
                    QuestionUiState.RightAnswer(option)
                }
            } else {
                _uiState.update {
                    QuestionUiState.WrongAnswer(option, question.correctIndex)
                }
            }
        }
    }
}