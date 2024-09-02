package dev.alexeyqqq.wordsapp.presentation.word_details

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.alexeyqqq.wordsapp.presentation.core.navigation.Screen

class WordDetailsScreen(
    private val wordId: Long,
    private val dictionaryId: Long,
) : Screen.ShowBottomSheetDialog() {

    override fun fragment(): BottomSheetDialogFragment =
        WordDetailsFragment.newInstance(wordId, dictionaryId)
}