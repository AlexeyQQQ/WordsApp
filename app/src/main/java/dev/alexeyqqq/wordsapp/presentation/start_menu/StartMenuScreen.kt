package dev.alexeyqqq.wordsapp.presentation.start_menu

import androidx.fragment.app.Fragment
import dev.alexeyqqq.wordsapp.presentation.core.navigation.Screen

object StartMenuScreen : Screen.Replace() {

    override fun fragment(): Fragment = StartMenuFragment()
}