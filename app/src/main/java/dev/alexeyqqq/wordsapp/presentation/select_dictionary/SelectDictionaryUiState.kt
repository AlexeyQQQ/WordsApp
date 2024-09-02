package dev.alexeyqqq.wordsapp.presentation.select_dictionary

import dev.alexeyqqq.wordsapp.domain.entity.Dictionary

sealed interface SelectDictionaryUiState {

    fun show(updateDictionaryList: UpdateDictionaryList)

    data class ListDictionaries(
        private val list: List<Dictionary>,
    ) : SelectDictionaryUiState {

        override fun show(updateDictionaryList: UpdateDictionaryList) {
            updateDictionaryList.update(list)
        }
    }
}