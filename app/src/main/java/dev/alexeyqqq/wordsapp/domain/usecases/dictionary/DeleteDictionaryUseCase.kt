package dev.alexeyqqq.wordsapp.domain.usecases.dictionary

import dev.alexeyqqq.wordsapp.domain.repository.DictionaryRepository
import javax.inject.Inject

class DeleteDictionaryUseCase @Inject constructor(
    private val dictionaryRepository: DictionaryRepository,
) {

    suspend operator fun invoke(dictionaryId: Long) =
        dictionaryRepository.deleteDictionary(dictionaryId)
}