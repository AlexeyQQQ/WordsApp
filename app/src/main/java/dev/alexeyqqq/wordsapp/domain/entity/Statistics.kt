package dev.alexeyqqq.wordsapp.domain.entity

data class Statistics(
    val dictionariesTotal: Int,
    val wordsTotal: Int,
    val wordsLearned: Int,
    val percentageRatio: Int,
)