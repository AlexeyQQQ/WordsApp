package dev.alexeyqqq.wordsapp.presentation.create_dictionary

import dev.alexeyqqq.wordsapp.databinding.FragmentCreateNewDictionaryBinding

sealed interface CreateNewDictionaryUiState {

    fun show(binding: FragmentCreateNewDictionaryBinding)

    data object EmptyInput : CreateNewDictionaryUiState {

        override fun show(binding: FragmentCreateNewDictionaryBinding) {
            binding.buttonSave.isEnabled = false
        }
    }

    data object NewInput : CreateNewDictionaryUiState {

        override fun show(binding: FragmentCreateNewDictionaryBinding) {
            binding.buttonSave.isEnabled = true
        }
    }
}