package dev.alexeyqqq.wordsapp.presentation.dictionary_details

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
import dev.alexeyqqq.wordsapp.databinding.FragmentDictionaryDetailsBinding
import dev.alexeyqqq.wordsapp.presentation.ViewModelFactory
import dev.alexeyqqq.wordsapp.presentation.navigation.DictionaryDetailsNavigation
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class DictionaryDetailsFragment : Fragment(), WordActions {

    private var _binding: FragmentDictionaryDetailsBinding? = null
    private val binding: FragmentDictionaryDetailsBinding
        get() = _binding ?: throw RuntimeException("FragmentDictionaryDetailsBinding == null")

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[DictionaryDetailsViewModel::class.java]
    }

    private val adapter by lazy {
        WordAdapter(wordActions = this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val dictionaryId = it.getLong(DICTIONARY_DETAILS_FRAGMENT_KEY)
            val component = (requireActivity().application as App).component
                .dictionaryDetailsComponentFactory()
                .create(dictionaryId)
            component.inject(this)
        } ?: throw RuntimeException("DictionaryDetailsFragment arguments == null")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDictionaryDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewWords.adapter = adapter
        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() = with(binding) {
        imageViewBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        buttonAddWord.setOnClickListener {
            (requireActivity() as DictionaryDetailsNavigation).toCreateNewWordScreen()
        }

        imageViewMenu.setOnClickListener {
            (requireActivity() as DictionaryDetailsNavigation).toAddDictionaryScreen()
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.uiState.collectLatest {
                    it.show(binding)
                    it.update(adapter)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun selectWord(wordId: Long) {
        (requireActivity() as DictionaryDetailsNavigation).toWordDetailsScreen(wordId)
    }

    companion object {
        fun newInstance(dictionaryId: Long): DictionaryDetailsFragment =
            DictionaryDetailsFragment().apply {
                arguments = Bundle().apply {
                    putLong(DICTIONARY_DETAILS_FRAGMENT_KEY, dictionaryId)
                }
            }

        private const val DICTIONARY_DETAILS_FRAGMENT_KEY = "DICTIONARY_DETAILS_FRAGMENT_KEY"
    }
}