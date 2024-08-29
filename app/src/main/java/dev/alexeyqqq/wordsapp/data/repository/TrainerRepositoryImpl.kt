package dev.alexeyqqq.wordsapp.data.repository

import dev.alexeyqqq.wordsapp.data.database.DictionaryDao
import dev.alexeyqqq.wordsapp.data.database.RelationDao
import dev.alexeyqqq.wordsapp.data.database.WordsDao
import dev.alexeyqqq.wordsapp.data.database.entity.WordDbModel
import dev.alexeyqqq.wordsapp.data.mapToDbModel
import dev.alexeyqqq.wordsapp.data.mapToDomain
import dev.alexeyqqq.wordsapp.data.network.TranslateService
import dev.alexeyqqq.wordsapp.domain.entity.Question
import dev.alexeyqqq.wordsapp.domain.entity.Statistics
import dev.alexeyqqq.wordsapp.domain.entity.Word
import dev.alexeyqqq.wordsapp.domain.exceptions.TrainerExceptions
import dev.alexeyqqq.wordsapp.domain.repository.TrainerRepository
import dev.alexeyqqq.wordsapp.domain.repository.TrainerRepository.Companion.LEARN_ALL_WORDS
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class TrainerRepositoryImpl @Inject constructor(
    private val wordsDao: WordsDao,
    private val dictionaryDao: DictionaryDao,
    private val relationDao: RelationDao,
    private val translateService: TranslateService,
) : TrainerRepository {

    override suspend fun increaseCorrectAnswers(word: Word) {
        val updatedWord = word.copy(correctAnswersCount = word.correctAnswersCount + 1)
        wordsDao.updateWord(updatedWord.mapToDbModel())
    }

    override suspend fun showStatistics(): Statistics {
        val dictionariesTotal = dictionaryDao.getAllDictionaries().first().size
        val words = wordsDao.getAllWords().first()
        val wordsTotal = words.size

        if (wordsTotal == 0) return Statistics(dictionariesTotal = dictionariesTotal)

        val wordsLearned = words.count { it.correctAnswersCount >= ANSWER_TO_STUDY }
        val percentageRatio = (wordsLearned * 100 / wordsTotal)

        return Statistics(dictionariesTotal, wordsTotal, wordsLearned, percentageRatio)
    }

    override suspend fun translate(word: String): String =
        translateService.translate(text = word).destinationText

    override suspend fun getNextQuestion(dictionaryId: Long): Question {
        val allWords = getWordsByDictionaryId(dictionaryId)
        if (allWords.size < MIN_WORDS) throw TrainerExceptions.FewWords()

        var (unlearnedWords, learnedWords)
                = allWords.partition { it.correctAnswersCount < ANSWER_TO_STUDY }
        if (unlearnedWords.isEmpty()) throw TrainerExceptions.NoWordsToLearn()

        if (unlearnedWords.size < COUNT_OF_QUESTION_WORDS) {
            learnedWords =
                learnedWords.shuffled().take(COUNT_OF_QUESTION_WORDS - unlearnedWords.size)
            unlearnedWords = unlearnedWords + learnedWords
        }

        val finalWords = unlearnedWords.shuffled()
            .take(COUNT_OF_QUESTION_WORDS)

        val correctAnswer = finalWords.filter {
            it.correctAnswersCount < ANSWER_TO_STUDY
        }.random()

        val question = Question(
            options = finalWords.map { it.mapToDomain() },
            correctIndex = finalWords.indexOf(correctAnswer)
        )
        return question
    }

    private suspend fun getWordsByDictionaryId(dictionaryId: Long): List<WordDbModel> {
        return if (dictionaryId == LEARN_ALL_WORDS) {
            wordsDao.getAllWords()
        } else {
            relationDao.getDictionaryWords(dictionaryId)
        }.first()
    }

    companion object {
        private const val MIN_WORDS = 10
        private const val ANSWER_TO_STUDY = 3
        private const val COUNT_OF_QUESTION_WORDS = 4
    }
}