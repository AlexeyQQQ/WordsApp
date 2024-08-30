package dev.alexeyqqq.wordsapp.presentation.create_word

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.alexeyqqq.wordsapp.domain.usecases.relation.SaveNewWordInDictionaryUseCase
import dev.alexeyqqq.wordsapp.domain.usecases.trainer.TranslateUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreateNewWordViewModel @Inject constructor(
    private val translateUseCase: TranslateUseCase,
    private val saveNewWordInDictionaryUseCase: SaveNewWordInDictionaryUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<CreateNewWordUiState> =
        MutableStateFlow(CreateNewWordUiState.EmptyOriginal)
    val uiState get() = _uiState.asStateFlow()

    private var jobTranslate: Job? = null

    fun checkOriginalInput(word: String) {
        jobTranslate?.cancel()
        if (word.isEmpty()) {
            _uiState.update { CreateNewWordUiState.EmptyOriginal }
        } else {
            _uiState.update { CreateNewWordUiState.Loading }
            jobTranslate = viewModelScope.launch {
                delay(SPAM_DELAY)
                try {
                    val translate = translateUseCase.invoke(word)
                    _uiState.update { CreateNewWordUiState.Translate(translate) }
                } catch (e: Exception) {
                    _uiState.update { CreateNewWordUiState.Translate(TRANSLATION_NOT_EXIST) }
                }
            }
        }
    }

    fun checkTranslateInput(word: String) {
        _uiState.update {
            if (word.isEmpty()) CreateNewWordUiState.EmptyTranslate
            else CreateNewWordUiState.NotEmptyInput
        }
    }

    fun saveNewWord(original: String, translate: String, dictionaryId: Long) {
        viewModelScope.launch {
            saveNewWordInDictionaryUseCase.invoke(original, translate, dictionaryId)
        }
    }

    companion object {
        private const val SPAM_DELAY = 250L
        private const val TRANSLATION_NOT_EXIST = ""
    }
}