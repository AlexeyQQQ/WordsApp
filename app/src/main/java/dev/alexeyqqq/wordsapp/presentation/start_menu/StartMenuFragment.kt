package dev.alexeyqqq.wordsapp.presentation.start_menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.alexeyqqq.wordsapp.databinding.FragmentStartMenuBinding
import dev.alexeyqqq.wordsapp.presentation.navigation.StartMenuNavigation

class StartMenuFragment : Fragment() {

    private var _binding: FragmentStartMenuBinding? = null
    private val binding: FragmentStartMenuBinding
        get() = _binding ?: throw RuntimeException("FragmentStartMenuBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentStartMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }

    private fun setupClickListeners() = with(binding) {
        buttonLearnWords.setOnClickListener {
            (requireActivity() as StartMenuNavigation).toSelectDictionaryScreen()
        }

        buttonDictionaries.setOnClickListener {
            (requireActivity() as StartMenuNavigation).toDictionariesScreen()
        }

        buttonStatistics.setOnClickListener {
            (requireActivity() as StartMenuNavigation).toStatisticsScreen()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}