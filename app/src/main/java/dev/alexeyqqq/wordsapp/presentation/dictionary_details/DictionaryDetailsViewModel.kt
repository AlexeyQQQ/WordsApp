package dev.alexeyqqq.wordsapp.presentation.dictionary_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.alexeyqqq.wordsapp.di.DictionaryQualifier
import dev.alexeyqqq.wordsapp.domain.entity.Dictionary
import dev.alexeyqqq.wordsapp.domain.usecases.relation.GetDictionaryWordsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class DictionaryDetailsViewModel @Inject constructor(
    getDictionaryWordsUseCase: GetDictionaryWordsUseCase,
    @DictionaryQualifier private val dictionary: Dictionary,
) : ViewModel() {

    val uiState: StateFlow<DictionaryDetailsUiState> =
        getDictionaryWordsUseCase.invoke(dictionary.id)
            .map { list ->
                DictionaryDetailsUiState.ShowDictionary(
                    list = list,
                    dictionaryName = dictionary.name,
                    wordsLearned = list.filter { word ->
                        word.correctAnswersCount >= ANSWER_TO_STUDY
                    }.size,
                    wordsTotal = list.size,
                )
            }.stateIn(
                viewModelScope,
                SharingStarted.Lazily,
                DictionaryDetailsUiState.Loading
            )

    companion object {
        private const val ANSWER_TO_STUDY = 3
    }
}