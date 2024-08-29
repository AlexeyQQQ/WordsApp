package dev.alexeyqqq.wordsapp.presentation.navigation

import dev.alexeyqqq.wordsapp.presentation.question.QuestionScreen
import dev.alexeyqqq.wordsapp.presentation.select_dictionary.SelectDictionaryScreen
import dev.alexeyqqq.wordsapp.presentation.start_menu.StartMenuScreen
import dev.alexeyqqq.wordsapp.presentation.statistics.StatisticsScreen

interface Navigation : StartMenuNavigation, SelectDictionaryNavigation, QuestionNavigation {

    fun navigate(screen: Screen)

    override fun toSelectDictionaryScreen() {
        navigate(SelectDictionaryScreen)
    }

    override fun toDictionariesScreen() {
        TODO()
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