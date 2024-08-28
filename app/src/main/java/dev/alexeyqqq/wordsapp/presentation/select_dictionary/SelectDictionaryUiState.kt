package dev.alexeyqqq.wordsapp.presentation.select_dictionary

import dev.alexeyqqq.wordsapp.domain.entity.Dictionary

sealed interface SelectDictionaryUiState {

    fun show(updateList: UpdateList)

    data class ListDictionaries(
        private val list: List<Dictionary>,
    ) : SelectDictionaryUiState {

        override fun show(updateList: UpdateList) {
            updateList.update(list)
        }
    }
}