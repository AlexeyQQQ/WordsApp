package dev.alexeyqqq.wordsapp.domain.usecases.relation

import dev.alexeyqqq.wordsapp.domain.repository.RelationRepository
import javax.inject.Inject

class RemoveWordFromDictionaryUseCase @Inject constructor(
    private val relationRepository: RelationRepository,
) {

    suspend operator fun invoke(wordId: Long, dictionaryId: Long) =
        relationRepository.removeWordFromDictionary(wordId, dictionaryId)
}