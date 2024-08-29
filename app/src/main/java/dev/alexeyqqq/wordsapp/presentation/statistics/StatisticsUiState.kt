package dev.alexeyqqq.wordsapp.presentation.statistics

import android.view.View
import dev.alexeyqqq.wordsapp.R
import dev.alexeyqqq.wordsapp.databinding.FragmentStatisticsBinding
import dev.alexeyqqq.wordsapp.domain.entity.Statistics

sealed interface StatisticsUiState {

    fun show(binding: FragmentStatisticsBinding)

    data object Loading : StatisticsUiState {

        override fun show(binding: FragmentStatisticsBinding) = with(binding) {
            progressBar.visibility = View.VISIBLE
            textViewStatistics.visibility = View.GONE
        }
    }

    data class ShowStatistics(
        private val statistics: Statistics,
    ) : StatisticsUiState {

        override fun show(binding: FragmentStatisticsBinding) = with(binding) {
            progressBar.visibility = View.GONE
            textViewStatistics.text = binding.root.context.getString(
                R.string.detailed_statistics,
                statistics.dictionariesTotal.toString(),
                statistics.wordsTotal.toString(),
                statistics.wordsLearned.toString(),
                statistics.percentageRatio.toString()
            )
            textViewStatistics.visibility = View.VISIBLE
        }
    }
}