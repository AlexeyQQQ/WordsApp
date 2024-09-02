package dev.alexeyqqq.wordsapp.presentation.word_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.alexeyqqq.wordsapp.di.WordIdQualifier
import dev.alexeyqqq.wordsapp.domain.usecases.relation.GetDictionariesWithoutWordUseCase
import dev.alexeyqqq.wordsapp.domain.usecases.relation.RemoveWordFromDictionaryUseCase
import dev.alexeyqqq.wordsapp.domain.usecases.relation.SaveOldWordInDictionaryUseCase
import dev.alexeyqqq.wordsapp.domain.usecases.word.GetWordUseCase
import dev.alexeyqqq.wordsapp.domain.usecases.word.UpdateWordUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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

    val uiState = getDictionariesWithoutWordUseCase.invoke(wordId).map {
        val word = getWordUseCase(wordId)
        if (word.correctAnswersCount < ANSWER_TO_STUDY) {
            WordDetailsUiState.NotLearned(it)
        } else {
            WordDetailsUiState.Learned(it)
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        WordDetailsUiState.Loading
    )

    fun saveInDictionary(dictionaryId: Long) {
        viewModelScope.launch {
            saveOldWordInDictionaryUseCase.invoke(wordId, dictionaryId)
        }
    }

    fun removeWordFromDictionary(dictionaryId: Long) {
        viewModelScope.launch {
            removeWordFromDictionaryUseCase.invoke(wordId, dictionaryId)
        }
    }

    fun learnAgain() {
        viewModelScope.launch {
            val word = getWordUseCase.invoke(wordId).copy(correctAnswersCount = BASIC_VALUE)
            updateWordUseCase.invoke(word)
        }
    }

    companion object {
        private const val BASIC_VALUE = 0
        private const val ANSWER_TO_STUDY = 3
    }
}