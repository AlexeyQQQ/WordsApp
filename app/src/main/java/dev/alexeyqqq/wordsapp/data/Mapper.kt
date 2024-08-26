package dev.alexeyqqq.wordsapp.data

import dev.alexeyqqq.wordsapp.data.database.entity.DictionaryDbModel
import dev.alexeyqqq.wordsapp.data.database.entity.WordDbModel
import dev.alexeyqqq.wordsapp.domain.entity.Dictionary
import dev.alexeyqqq.wordsapp.domain.entity.Word

fun DictionaryDbModel.mapToDomain() = Dictionary(
    id = id,
    name = name,
)

fun Dictionary.mapToDbModel() = DictionaryDbModel(
    id = id,
    name = name,
)

fun WordDbModel.mapToDomain() = Word(
    id = id,
    original = original,
    translation = translation,
    correctAnswersCount = correctAnswersCount,
)

fun Word.mapToDbModel() = WordDbModel(
    id = id,
    original = original,
    translation = translation,
    correctAnswersCount = correctAnswersCount,
)