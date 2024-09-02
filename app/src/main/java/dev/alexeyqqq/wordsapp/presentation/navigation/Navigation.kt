package dev.alexeyqqq.wordsapp.presentation.navigation

import dev.alexeyqqq.wordsapp.presentation.create_word.CreateNewWordScreen
import dev.alexeyqqq.wordsapp.presentation.dictionaries.DictionariesScreen
import dev.alexeyqqq.wordsapp.presentation.dictionary_details.DictionaryDetailsScreen
import dev.alexeyqqq.wordsapp.presentation.question.QuestionScreen
import dev.alexeyqqq.wordsapp.presentation.select_dictionary.SelectDictionaryScreen
import dev.alexeyqqq.wordsapp.presentation.start_menu.StartMenuScreen
import dev.alexeyqqq.wordsapp.presentation.statistics.StatisticsScreen
import dev.alexeyqqq.wordsapp.presentation.word_details.WordDetailsScreen

interface Navigation : StartMenuNavigation, SelectDictionaryNavigation, QuestionNavigation,
    DictionariesNavigation, DictionaryDetailsNavigation {

    fun navigate(screen: Screen)

    override fun toSelectDictionaryScreen() {
        navigate(SelectDictionaryScreen)
    }

    override fun toDictionariesScreen() {
        navigate(DictionariesScreen)
    }

    override fun toStatisticsScreen() {
        navigate(StatisticsScreen)
    }

    override fun toQuestionScreen(dictionaryId: Long) {
        navigate(QuestionScreen(dictionaryId))
    }

    override fun toStartMenuScreen() {
        navigate(StartMenuScreen)
    }

    override fun toDictionaryDetailsScreen(dictionaryId: Long) {
        navigate(DictionaryDetailsScreen(dictionaryId))
    }

    override fun toAddDictionaryScreen() {
        TODO()
    }

    override fun toWordDetailsScreen(wordId: Long, dictionaryId: Long) {
        navigate(WordDetailsScreen(wordId, dictionaryId))
    }

    override fun toCreateNewWordScreen(dictionaryId: Long) {
        navigate(CreateNewWordScreen(dictionaryId))
    }
}

interface StartMenuNavigation {

    fun toSelectDictionaryScreen()

    fun toDictionariesScreen()

    fun toStatisticsScreen()
}

interface SelectDictionaryNavigation {

    fun toQuestionScreen(dictionaryId: Long)
}

interface QuestionNavigation {

    fun toStartMenuScreen()
}

interface DictionariesNavigation {

    fun toDictionaryDetailsScreen(dictionaryId: Long)

    fun toAddDictionaryScreen()
}

interface DictionaryDetailsNavigation {

    fun toWordDetailsScreen(wordId: Long, dictionaryId: Long)

    fun toCreateNewWordScreen(dictionaryId: Long)

    fun toAddDictionaryScreen()
}