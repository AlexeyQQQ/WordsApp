package dev.alexeyqqq.wordsapp.domain.exceptions

sealed class TrainerExceptions : Exception() {

    class FewWords : TrainerExceptions()

    class NoWordsToLearn : TrainerExceptions()
}