package dev.alexeyqqq.wordsapp.presentation.dictionary_details

import androidx.fragment.app.Fragment
import dev.alexeyqqq.wordsapp.domain.entity.Dictionary
import dev.alexeyqqq.wordsapp.presentation.core.navigation.Screen

class DictionaryDetailsScreen(
    private val dictionary: Dictionary,
) : Screen.ReplaceWithBackstack() {

    override fun fragment(): Fragment = DictionaryDetailsFragment.newInstance(dictionary)
}