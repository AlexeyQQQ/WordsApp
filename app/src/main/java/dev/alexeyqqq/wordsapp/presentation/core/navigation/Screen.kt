package dev.alexeyqqq.wordsapp.presentation.core.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

interface Screen {

    fun show(containerId: Int, fragmentManager: FragmentManager) = Unit

    abstract class Replace : Screen {

        abstract fun fragment(): Fragment

        override fun show(containerId: Int, fragmentManager: FragmentManager) {
            fragmentManager.beginTransaction()
                .replace(containerId, fragment())
                .commit()
        }
    }

    abstract class ReplaceWithBackstack : Screen {

        abstract fun fragment(): Fragment

        override fun show(containerId: Int, fragmentManager: FragmentManager) {
            val fragment = fragment()
            fragmentManager.beginTransaction()
                .replace(containerId, fragment)
                .addToBackStack(fragment.javaClass.simpleName)
                .commit()
        }
    }

    abstract class ShowBottomSheetDialog : Screen {

        abstract fun fragment(): BottomSheetDialogFragment

        override fun show(containerId: Int, fragmentManager: FragmentManager) {
            val fragment = fragment()
            fragment.show(
                fragmentManager,
                fragment.javaClass.simpleName,
            )
        }
    }
}