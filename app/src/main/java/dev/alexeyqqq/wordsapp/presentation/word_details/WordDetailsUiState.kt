package dev.alexeyqqq.wordsapp.presentation.word_details

import android.view.View
import dev.alexeyqqq.wordsapp.R
import dev.alexeyqqq.wordsapp.databinding.FragmentWordDetailsBinding
import dev.alexeyqqq.wordsapp.domain.entity.Dictionary
import dev.alexeyqqq.wordsapp.presentation.select_dictionary.UpdateDictionaryList

sealed interface WordDetailsUiState {

    fun show(binding: FragmentWordDetailsBinding) = Unit

    fun update(updateDictionaryList: UpdateDictionaryList) = Unit

    fun navigate(function: () -> Unit) = Unit

    data object Loading : WordDetailsUiState {

        override fun show(binding: FragmentWordDetailsBinding) = with(binding) {
            progressBarLoading.visibility = View.VISIBLE
            buttonLearn.isEnabled = false
            buttonDelete.isEnabled = false
        }
    }

    data class NotLearned(
        private val list: List<Dictionary>,
    ) : WordDetailsUiState {

        override fun show(binding: FragmentWordDetailsBinding) = with(binding) {
            progressBarLoading.visibility = View.GONE
            buttonLearn.isEnabled = false
            buttonDelete.isEnabled = true

            if (list.isEmpty()) {
                recyclerViewDictionaries.visibility = View.GONE
                textViewHeader.text = root.context.getString(R.string.already_added_to_dictionary)
            } else {
                recyclerViewDictionaries.visibility = View.VISIBLE
                textViewHeader.text = root.context.getString(R.string.add_to_dictionary)
            }
        }

        override fun update(updateDictionaryList: UpdateDictionaryList) {
            updateDictionaryList.update(list)
        }
    }

    data class Learned(
        private val list: List<Dictionary>,
    ) : WordDetailsUiState {

        override fun show(binding: FragmentWordDetailsBinding) = with(binding) {
            progressBarLoading.visibility = View.GONE
            buttonLearn.isEnabled = true
            buttonDelete.isEnabled = true

            if (list.isEmpty()) {
                recyclerViewDictionaries.visibility = View.GONE
                textViewHeader.text = root.context.getString(R.string.already_added_to_dictionary)
            } else {
                recyclerViewDictionaries.visibility = View.VISIBLE
                textViewHeader.text = root.context.getString(R.string.add_to_dictionary)
            }
        }

        override fun update(updateDictionaryList: UpdateDictionaryList) {
            updateDictionaryList.update(list)
        }
    }

    data object Close : WordDetailsUiState {

        override fun navigate(function: () -> Unit) {
            function.invoke()
        }
    }
}