package dev.alexeyqqq.wordsapp.presentation.dictionaries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.alexeyqqq.wordsapp.domain.usecases.dictionary.GetAllDictionariesUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class DictionariesViewModel @Inject constructor(
    getAllDictionariesUseCase: GetAllDictionariesUseCase,
) : ViewModel() {

    val uiState: StateFlow<DictionariesUiState> = getAllDictionariesUseCase.invoke()
        .map {
            DictionariesUiState.ListDictionaries(it)
        }.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            DictionariesUiState.ListDictionaries(emptyList())
        )
}