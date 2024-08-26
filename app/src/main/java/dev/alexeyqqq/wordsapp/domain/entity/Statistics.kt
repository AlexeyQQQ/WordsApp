package dev.alexeyqqq.wordsapp.domain.entity

data class Statistics(
    val dictionariesTotal: Int = BASIC_VALUE,
    val wordsTotal: Int = BASIC_VALUE,
    val wordsLearned: Int = BASIC_VALUE,
    val percentageRatio: Int = BASIC_VALUE,
) {

    companion object {
        private const val BASIC_VALUE = 0
    }
}