package dev.alexeyqqq.wordsapp.domain.usecases.word

import dev.alexeyqqq.wordsapp.domain.entity.Word
import dev.alexeyqqq.wordsapp.domain.repository.WordRepository

class GetWordUseCase(
    private val wordRepository: WordRepository,
) {

    suspend operator fun invoke(wordId: Long): Word = wordRepository.getWord(wordId)
}