package dev.alexeyqqq.wordsapp.presentation.select_dictionary

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dev.alexeyqqq.wordsapp.App
import dev.alexeyqqq.wordsapp.databinding.FragmentSelectDictionaryBinding
import dev.alexeyqqq.wordsapp.presentation.ViewModelFactory
import dev.alexeyqqq.wordsapp.presentation.navigation.SelectDictionaryNavigation
import kotlinx.coroutines.launch
import javax.inject.Inject

class SelectDictionaryFragment : Fragment(), DictionaryActions {

    private var _binding: FragmentSelectDictionaryBinding? = null
    private val binding: FragmentSelectDictionaryBinding
        get() = _binding ?: throw RuntimeException("FragmentSelectDictionaryBinding == null")

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (requireActivity().application as App).component
    }

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SelectDictionaryViewModel::class.java]
    }

    private val adapter by lazy {
        DictionaryAdapter(dictionaryActions = this)
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSelectDictionaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewDictionaries.adapter = adapter
        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() = with(binding) {
        buttonAllDictionaries.setOnClickListener {
            (requireActivity() as SelectDictionaryNavigation).toQuestionScreen()
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.uiState.collect { it.show(adapter) }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun selectDictionary() {
        (requireActivity() as SelectDictionaryNavigation).toQuestionScreen()
    }
}

interface DictionaryActions {

    fun selectDictionary()
}