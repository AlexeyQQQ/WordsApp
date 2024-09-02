package dev.alexeyqqq.wordsapp.presentation.dictionaries

import dev.alexeyqqq.wordsapp.domain.entity.Dictionary
import dev.alexeyqqq.wordsapp.presentation.select_dictionary.UpdateDictionaryList

sealed interface DictionariesUiState {

    fun show(updateDictionaryList: UpdateDictionaryList)

    data class ListDictionaries(
        private val list: List<Dictionary>,
    ) : DictionariesUiState {

        override fun show(updateDictionaryList: UpdateDictionaryList) {
            updateDictionaryList.update(list)
        }
    }
}