package dev.alexeyqqq.wordsapp.domain.entity

data class Word(
    val id: Long,
    val original: String,
    val translation: String,
    var correctAnswersCount: Int = BASIC_VALUE,
) {

    companion object {
        private const val BASIC_VALUE = 0
    }
}