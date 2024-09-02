package dev.alexeyqqq.wordsapp.presentation.create_dictionary

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.alexeyqqq.wordsapp.domain.entity.Dictionary
import dev.alexeyqqq.wordsapp.presentation.navigation.Screen

abstract class CreateNewDictionaryScreen : Screen.ShowBottomSheetDialog() {

    class ModeAdd : CreateNewDictionaryScreen() {

        override fun fragment(): BottomSheetDialogFragment =
            CreateNewDictionaryFragment.newInstanceModeAdd()
    }

    class ModeEdit(private val dictionary: Dictionary) : CreateNewDictionaryScreen() {

        override fun fragment(): BottomSheetDialogFragment =
            CreateNewDictionaryFragment.newInstanceModeEdit(dictionary)
    }
}