package dev.alexeyqqq.wordsapp.presentation.dictionaries

import androidx.fragment.app.Fragment
import dev.alexeyqqq.wordsapp.presentation.core.navigation.Screen

object DictionariesScreen : Screen.ReplaceWithBackstack() {

    override fun fragment(): Fragment = DictionariesFragment()
}