package dev.alexeyqqq.wordsapp.domain.usecases.relation

import dev.alexeyqqq.wordsapp.domain.repository.RelationRepository
import javax.inject.Inject

class SaveNewWordInDictionaryUseCase @Inject constructor(
    private val relationRepository: RelationRepository,
) {

    suspend operator fun invoke(original: String, translate: String, dictionaryId: Long) =
        relationRepository.saveNewWordInDictionary(original, translate, dictionaryId)
}