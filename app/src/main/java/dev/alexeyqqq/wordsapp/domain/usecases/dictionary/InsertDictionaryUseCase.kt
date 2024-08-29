package dev.alexeyqqq.wordsapp.domain.usecases.dictionary

import dev.alexeyqqq.wordsapp.domain.entity.Dictionary
import dev.alexeyqqq.wordsapp.domain.repository.DictionaryRepository
import javax.inject.Inject

class InsertDictionaryUseCase @Inject constructor(
    private val dictionaryRepository: DictionaryRepository,
) {

    suspend operator fun invoke(dictionary: Dictionary): Long =
        dictionaryRepository.insertDictionary(dictionary)
}