package dev.alexeyqqq.wordsapp.presentation.dictionary_details

import android.view.View
import dev.alexeyqqq.wordsapp.R
import dev.alexeyqqq.wordsapp.databinding.FragmentDictionaryDetailsBinding
import dev.alexeyqqq.wordsapp.domain.entity.Word

sealed interface DictionaryDetailsUiState {

    fun show(binding: FragmentDictionaryDetailsBinding) = Unit

    fun update(updateWordList: UpdateWordList) = Unit

    data object Loading : DictionaryDetailsUiState {

        override fun show(binding: FragmentDictionaryDetailsBinding) = with(binding) {
            progressBarLoading.visibility = View.VISIBLE
        }
    }

    data class ShowDictionary(
        private val list: List<Word>,
        private val dictionaryName: String,
        private val wordsLearned: Int,
        private val wordsTotal: Int,
    ) : DictionaryDetailsUiState {

        override fun show(binding: FragmentDictionaryDetailsBinding) = with(binding) {
            progressBarLoading.visibility = View.GONE
            textViewHeader.text = dictionaryName
            textViewStatistics.text = binding.root.context.getString(
                R.string.statistics_dictionary,
                wordsLearned.toString(),
                wordsTotal.toString(),
            )
        }

        override fun update(updateWordList: UpdateWordList) {
            updateWordList.update(list)
        }
    }
}