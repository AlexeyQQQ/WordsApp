package dev.alexeyqqq.wordsapp.domain.usecases.word

import dev.alexeyqqq.wordsapp.domain.repository.WordRepository

class DeleteWordUseCase(
    private val wordRepository: WordRepository,
) {

    suspend operator fun invoke(wordId: Long) = wordRepository.deleteWord(wordId)
}