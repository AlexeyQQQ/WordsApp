package dev.alexeyqqq.wordsapp.presentation.navigation

import dev.alexeyqqq.wordsapp.presentation.select_dictionary.SelectDictionaryScreen

interface Navigation : StartMenuNavigation, SelectDictionaryNavigation {

    fun navigate(screen: Screen)

    override fun toSelectDictionaryScreen() {
        navigate(SelectDictionaryScreen)
    }

    override fun toDictionariesScreen() {
        TODO()
    }

    override fun toStatisticsScreen() {
        TODO()
    }

    override fun toQuestionScreen() {
        TODO()
    }
}

interface StartMenuNavigation {

    fun toSelectDictionaryScreen()

    fun toDictionariesScreen()

    fun toStatisticsScreen()
}

interface SelectDictionaryNavigation {

    fun toQuestionScreen()
}