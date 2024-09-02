package dev.alexeyqqq.wordsapp.presentation.statistics

import androidx.fragment.app.Fragment
import dev.alexeyqqq.wordsapp.presentation.core.navigation.Screen

object StatisticsScreen : Screen.ReplaceWithBackstack() {

    override fun fragment(): Fragment = StatisticsFragment()
}