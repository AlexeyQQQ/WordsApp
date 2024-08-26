package dev.alexeyqqq.wordsapp.domain.usecases.relation

import dev.alexeyqqq.wordsapp.domain.entity.Word
import dev.alexeyqqq.wordsapp.domain.repository.RelationRepository
import kotlinx.coroutines.flow.Flow

class GetDictionaryWordsUseCase(
    private val relationRepository: RelationRepository,
) {

    operator fun invoke(dictionaryId: Long): Flow<List<Word>> =
        relationRepository.getDictionaryWords(dictionaryId)
}