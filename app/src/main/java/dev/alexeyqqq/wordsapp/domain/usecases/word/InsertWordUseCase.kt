package dev.alexeyqqq.wordsapp.domain.usecases.word

import dev.alexeyqqq.wordsapp.domain.entity.Word
import dev.alexeyqqq.wordsapp.domain.repository.WordRepository

class InsertWordUseCase(
    private val wordRepository: WordRepository,
) {

    suspend operator fun invoke(word: Word) = wordRepository.insertWord(word)
}