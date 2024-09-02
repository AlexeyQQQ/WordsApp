package dev.alexeyqqq.wordsapp.presentation.create_word

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.alexeyqqq.wordsapp.presentation.core.navigation.Screen

class CreateNewWordScreen(
    private val dictionaryId: Long,
) : Screen.ShowBottomSheetDialog() {

    override fun fragment(): BottomSheetDialogFragment =
        CreateNewWordFragment.newInstance(dictionaryId)
}