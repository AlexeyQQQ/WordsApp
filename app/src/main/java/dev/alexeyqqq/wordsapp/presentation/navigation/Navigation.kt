package dev.alexeyqqq.wordsapp.presentation.navigation

interface Navigation : StartMenuNavigation {

    fun navigate(screen: Screen)

    override fun toSelectDictionaryScreen() {
        TODO()
    }

    override fun toDictionariesScreen() {
        TODO()
    }

    override fun toStatisticsScreen() {
        TODO()
    }
}

interface StartMenuNavigation {

    fun toSelectDictionaryScreen()

    fun toDictionariesScreen()

    fun toStatisticsScreen()
}