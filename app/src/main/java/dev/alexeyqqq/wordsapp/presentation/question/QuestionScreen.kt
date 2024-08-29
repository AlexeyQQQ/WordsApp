package dev.alexeyqqq.wordsapp.presentation.question

import androidx.fragment.app.Fragment
import dev.alexeyqqq.wordsapp.presentation.navigation.Screen

class QuestionScreen(
    private val dictionaryId: Long,
) : Screen.ReplaceWithBackstack() {

    override fun fragment(): Fragment = QuestionFragment.newInstance(dictionaryId)
}