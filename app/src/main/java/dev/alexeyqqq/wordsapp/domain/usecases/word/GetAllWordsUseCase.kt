package dev.alexeyqqq.wordsapp.domain.usecases.word

import dev.alexeyqqq.wordsapp.domain.entity.Word
import dev.alexeyqqq.wordsapp.domain.repository.WordRepository
import kotlinx.coroutines.flow.Flow

class GetAllWordsUseCase(
    private val wordRepository: WordRepository,
) {

    operator fun invoke(): Flow<List<Word>> = wordRepository.getAllWords()
}