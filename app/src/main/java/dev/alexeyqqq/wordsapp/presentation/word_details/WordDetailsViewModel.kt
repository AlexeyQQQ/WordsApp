package dev.alexeyqqq.wordsapp.presentation.word_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.alexeyqqq.wordsapp.di.WordIdQualifier
import dev.alexeyqqq.wordsapp.domain.usecases.relation.GetDictionariesWithoutWordUseCase
import dev.alexeyqqq.wordsapp.domain.usecases.relation.RemoveWordFromDictionaryUseCase
import dev.alexeyqqq.wordsapp.domain.usecases.relation.SaveOldWordInDictionaryUseCase
import dev.alexeyqqq.wordsapp.domain.usecases.word.GetWordUseCase
import dev.alexeyqqq.wordsapp.domain.usecases.word.UpdateWordUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class WordDetailsViewModel @Inject constructor(
    getDictionariesWithoutWordUseCase: GetDictionariesWithoutWordUseCase,
    private val saveOldWordInDictionaryUseCase: SaveOldWordInDictionaryUseCase,
    private val getWordUseCase: GetWordUseCase,
    private val updateWordUseCase: UpdateWordUseCase,
    private val removeWordFromDictionaryUseCase: RemoveWordFromDictionaryUseCase,
    @WordIdQualifier private val wordId: Long,
) : ViewModel() {

    private val _uiState = MutableStateFlow<WordDetailsUiState>(WordDetailsUiState.Loading)
    val uiState: StateFlow<WordDetailsUiState> get() = _uiState

    init {
        viewModelScope.launch {
            val word = getWordUseCase(wordId)
            getDictionariesWithoutWordUseCase(wordId).collect { dictionaries ->
                _uiState.value = if (word.correctAnswersCount < ANSWER_TO_STUDY) {
                    WordDetailsUiState.NotLearned(dictionaries)
                } else {
                    WordDetailsUiState.Learned(dictionaries)
                }
            }
        }
    }

    fun saveInDictionary(dictionaryId: Long) {
        viewModelScope.launch {
            saveOldWordInDictionaryUseCase.invoke(wordId, dictionaryId)
            _uiState.update { WordDetailsUiState.Close }
        }
    }

    fun removeWordFromDictionary(dictionaryId: Long) {
        viewModelScope.launch {
            removeWordFromDictionaryUseCase.invoke(wordId, dictionaryId)
            _uiState.update { WordDetailsUiState.Close }
        }
    }

    fun learnAgain() {
        viewModelScope.launch {
            val word = getWordUseCase.invoke(wordId).copy(correctAnswersCount = BASIC_VALUE)
            updateWordUseCase.invoke(word)
            _uiState.update { WordDetailsUiState.Close }
        }
    }

    companion object {
        private const val BASIC_VALUE = 0
        private const val ANSWER_TO_STUDY = 3
    }
}