package dev.alexeyqqq.wordsapp.domain.usecases.dictionary

import dev.alexeyqqq.wordsapp.domain.repository.DictionaryRepository

class UpdateDictionaryUseCase(
    private val dictionaryRepository: DictionaryRepository,
) {

    suspend operator fun invoke(dictionaryId: Long, newName: String) =
        dictionaryRepository.updateDictionary(dictionaryId, newName)
}