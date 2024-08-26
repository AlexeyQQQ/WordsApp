package dev.alexeyqqq.wordsapp.domain.usecases.dictionary

import dev.alexeyqqq.wordsapp.domain.entity.Dictionary
import dev.alexeyqqq.wordsapp.domain.repository.DictionaryRepository
import kotlinx.coroutines.flow.Flow

class GetAllDictionariesUseCase(
    private val dictionaryRepository: DictionaryRepository,
) {

    operator fun invoke(): Flow<List<Dictionary>> = dictionaryRepository.getAllDictionaries()
}