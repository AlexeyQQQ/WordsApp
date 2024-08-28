package dev.alexeyqqq.wordsapp.domain.usecases.dictionary

import dev.alexeyqqq.wordsapp.domain.entity.Dictionary
import dev.alexeyqqq.wordsapp.domain.repository.DictionaryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllDictionariesUseCase @Inject constructor(
    private val dictionaryRepository: DictionaryRepository,
) {

    operator fun invoke(): Flow<List<Dictionary>> = dictionaryRepository.getAllDictionaries()
}