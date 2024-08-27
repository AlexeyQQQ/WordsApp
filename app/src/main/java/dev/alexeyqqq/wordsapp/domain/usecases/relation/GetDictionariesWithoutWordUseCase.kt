package dev.alexeyqqq.wordsapp.domain.usecases.relation

import dev.alexeyqqq.wordsapp.domain.entity.Dictionary
import dev.alexeyqqq.wordsapp.domain.repository.RelationRepository
import kotlinx.coroutines.flow.Flow

class GetDictionariesWithoutWordUseCase(
    private val relationRepository: RelationRepository,
) {

    operator fun invoke(wordId: Long): Flow<List<Dictionary>> =
        relationRepository.getDictionariesWithoutWord(wordId)
}