package dev.alexeyqqq.wordsapp.presentation.select_dictionary

import androidx.fragment.app.Fragment
import dev.alexeyqqq.wordsapp.presentation.core.navigation.Screen

object SelectDictionaryScreen : Screen.ReplaceWithBackstack() {

    override fun fragment(): Fragment = SelectDictionaryFragment()
}