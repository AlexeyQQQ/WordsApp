package dev.alexeyqqq.wordsapp.domain.usecases.dictionary

import dev.alexeyqqq.wordsapp.domain.entity.Dictionary
import dev.alexeyqqq.wordsapp.domain.repository.DictionaryRepository

class InsertDictionaryUseCase(
    private val dictionaryRepository: DictionaryRepository,
) {

    suspend operator fun invoke(dictionary: Dictionary) =
        dictionaryRepository.insertDictionary(dictionary)
}