package dev.alexeyqqq.wordsapp.presentation.create_word

import android.view.View
import dev.alexeyqqq.wordsapp.databinding.FragmentCreateNewWordBinding

sealed interface CreateNewWordUiState {

    fun show(binding: FragmentCreateNewWordBinding)

    data object EmptyOriginal : CreateNewWordUiState {

        override fun show(binding: FragmentCreateNewWordBinding) = with(binding) {
            editTextTranslate.apply {
                setText("")
                isEnabled = false
            }
            progressBarLoading.visibility = View.GONE
            buttonSave.isEnabled = false
        }
    }

    data object EmptyTranslate : CreateNewWordUiState {

        override fun show(binding: FragmentCreateNewWordBinding) = with(binding) {
            progressBarLoading.visibility = View.GONE
            buttonSave.isEnabled = false
        }
    }

    data object Loading : CreateNewWordUiState {

        override fun show(binding: FragmentCreateNewWordBinding) = with(binding) {
            progressBarLoading.visibility = View.VISIBLE
            buttonSave.isEnabled = false
            editTextTranslate.isEnabled = false
        }
    }

    data object NotEmptyInput : CreateNewWordUiState {

        override fun show(binding: FragmentCreateNewWordBinding) = with(binding) {
            progressBarLoading.visibility = View.GONE
            buttonSave.isEnabled = true
            editTextTranslate.isEnabled = true
        }
    }

    data class Translate(
        private val translate: String,
    ) : CreateNewWordUiState {

        override fun show(binding: FragmentCreateNewWordBinding) = with(binding) {
            editTextTranslate.apply {
                setText(translate)
                isEnabled = true
            }
            progressBarLoading.visibility = View.GONE
            buttonSave.isEnabled = true
        }
    }
}