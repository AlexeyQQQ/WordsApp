package dev.alexeyqqq.wordsapp.presentation.select_dictionary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.alexeyqqq.wordsapp.domain.usecases.dictionary.GetAllDictionariesUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class SelectDictionaryViewModel @Inject constructor(
    getAllDictionariesUseCase: GetAllDictionariesUseCase,
) : ViewModel() {

    val uiState: StateFlow<SelectDictionaryUiState> = getAllDictionariesUseCase.invoke()
        .map {
            SelectDictionaryUiState.ListDictionaries(it)
        }.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            SelectDictionaryUiState.ListDictionaries(emptyList())
        )
}