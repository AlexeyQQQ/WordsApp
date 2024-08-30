package dev.alexeyqqq.wordsapp.presentation.navigation

import dev.alexeyqqq.wordsapp.presentation.dictionaries.DictionariesScreen
import dev.alexeyqqq.wordsapp.presentation.dictionary_details.DictionaryDetailsScreen
import dev.alexeyqqq.wordsapp.presentation.question.QuestionScreen
import dev.alexeyqqq.wordsapp.presentation.select_dictionary.SelectDictionaryScreen
import dev.alexeyqqq.wordsapp.presentation.start_menu.StartMenuScreen
import dev.alexeyqqq.wordsapp.presentation.statistics.StatisticsScreen

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

    override fun toWordDetailsScreen(wordId: Long) {
        TODO()
    }

    override fun toCreateNewWordScreen() {
        TODO()
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

    fun toWordDetailsScreen(wordId: Long)

    fun toCreateNewWordScreen()

    fun toAddDictionaryScreen()
}