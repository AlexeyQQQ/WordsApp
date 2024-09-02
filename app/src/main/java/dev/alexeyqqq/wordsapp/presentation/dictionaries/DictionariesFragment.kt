package dev.alexeyqqq.wordsapp.presentation.dictionaries

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
import dev.alexeyqqq.wordsapp.databinding.FragmentDictionariesBinding
import dev.alexeyqqq.wordsapp.domain.entity.Dictionary
import dev.alexeyqqq.wordsapp.presentation.core.ViewModelFactory
import dev.alexeyqqq.wordsapp.presentation.core.navigation.DictionariesNavigation
import dev.alexeyqqq.wordsapp.presentation.select_dictionary.DictionaryActions
import dev.alexeyqqq.wordsapp.presentation.select_dictionary.DictionaryAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class DictionariesFragment : Fragment(), DictionaryActions {

    private var _binding: FragmentDictionariesBinding? = null
    private val binding: FragmentDictionariesBinding
        get() = _binding ?: throw RuntimeException("FragmentDictionariesBinding == null")

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (requireActivity().application as App).component
    }

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[DictionariesViewModel::class.java]
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
        _binding = FragmentDictionariesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewDictionaries.adapter = adapter
        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() = with(binding) {
        buttonAddDictionary.setOnClickListener {
            (requireActivity() as DictionariesNavigation).toAddDictionaryScreen()
        }

        imageViewBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.uiState.collectLatest { it.show(adapter) }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun selectDictionary(dictionary: Dictionary) {
        (requireActivity() as DictionariesNavigation).toDictionaryDetailsScreen(dictionary)
    }
}