package dev.alexeyqqq.wordsapp.presentation.dictionaries

import dev.alexeyqqq.wordsapp.domain.entity.Dictionary
import dev.alexeyqqq.wordsapp.presentation.select_dictionary.UpdateList

sealed interface DictionariesUiState {

    fun show(updateList: UpdateList)

    data class ListDictionaries(
        private val list: List<Dictionary>,
    ) : DictionariesUiState {

        override fun show(updateList: UpdateList) {
            updateList.update(list)
        }
    }
}