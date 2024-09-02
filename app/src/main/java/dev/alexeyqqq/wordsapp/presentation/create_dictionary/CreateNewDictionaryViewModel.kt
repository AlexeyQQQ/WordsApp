package dev.alexeyqqq.wordsapp.presentation.create_dictionary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.alexeyqqq.wordsapp.domain.entity.Dictionary
import dev.alexeyqqq.wordsapp.domain.usecases.dictionary.DeleteDictionaryUseCase
import dev.alexeyqqq.wordsapp.domain.usecases.dictionary.InsertDictionaryUseCase
import dev.alexeyqqq.wordsapp.domain.usecases.dictionary.UpdateDictionaryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreateNewDictionaryViewModel @Inject constructor(
    private val insertDictionaryUseCase: InsertDictionaryUseCase,
    private val updateDictionaryUseCase: UpdateDictionaryUseCase,
    private val deleteDictionaryUseCase: DeleteDictionaryUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<CreateNewDictionaryUiState> =
        MutableStateFlow(CreateNewDictionaryUiState.EmptyInput)
    val uiState get() = _uiState.asStateFlow()

    fun checkInput(text: String) {
        _uiState.update {
            if (text.isEmpty()) CreateNewDictionaryUiState.EmptyInput
            else CreateNewDictionaryUiState.NewInput
        }
    }

    fun checkInput(text: String, name: String) {
        _uiState.update {
            if (text.isEmpty() || text == name) CreateNewDictionaryUiState.EmptyInput
            else CreateNewDictionaryUiState.NewInput
        }
    }

    fun saveNewDictionary(name: String) {
        viewModelScope.launch {
            insertDictionaryUseCase.invoke(Dictionary(name = name))
        }
    }

    fun renameDictionary(dictionaryId: Long, newName: String) {
        viewModelScope.launch {
            updateDictionaryUseCase.invoke(dictionaryId, newName)
        }
    }

    fun deleteDictionary(dictionaryId: Long) {
        viewModelScope.launch {
            deleteDictionaryUseCase.invoke(dictionaryId)
        }
    }
}