package dev.alexeyqqq.wordsapp.presentation.dictionary_details

import androidx.fragment.app.Fragment
import dev.alexeyqqq.wordsapp.presentation.navigation.Screen

class DictionaryDetailsScreen(
    private val dictionaryId: Long,
) : Screen.ReplaceWithBackstack() {

    override fun fragment(): Fragment = DictionaryDetailsFragment.newInstance(dictionaryId)
}