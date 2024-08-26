package dev.alexeyqqq.wordsapp.domain.entity

data class Question(
    val options: List<Word>,
    val correctIndex: Int,
)