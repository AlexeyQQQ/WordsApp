package dev.alexeyqqq.wordsapp.domain.usecases.dictionary

import dev.alexeyqqq.wordsapp.domain.repository.DictionaryRepository

class DeleteDictionaryUseCase(
    private val dictionaryRepository: DictionaryRepository,
) {

    suspend operator fun invoke(dictionaryId: Long) =
        dictionaryRepository.deleteDictionary(dictionaryId)
}