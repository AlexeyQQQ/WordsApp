package dev.alexeyqqq.wordsapp.domain.usecases.word

import dev.alexeyqqq.wordsapp.domain.entity.Word
import dev.alexeyqqq.wordsapp.domain.repository.WordRepository
import javax.inject.Inject

class InsertWordUseCase @Inject constructor(
    private val wordRepository: WordRepository,
) {

    suspend operator fun invoke(word: Word): Long = wordRepository.insertWord(word)
}