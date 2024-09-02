package dev.alexeyqqq.wordsapp.presentation.word_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.alexeyqqq.wordsapp.App
import dev.alexeyqqq.wordsapp.databinding.FragmentWordDetailsBinding
import dev.alexeyqqq.wordsapp.presentation.ViewModelFactory
import dev.alexeyqqq.wordsapp.presentation.select_dictionary.DictionaryActions
import dev.alexeyqqq.wordsapp.presentation.select_dictionary.DictionaryAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class WordDetailsFragment : BottomSheetDialogFragment(), DictionaryActions {

    private var _binding: FragmentWordDetailsBinding? = null
    private val binding: FragmentWordDetailsBinding
        get() = _binding ?: throw RuntimeException("FragmentWordDetailsBinding == null")

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[WordDetailsViewModel::class.java]
    }

    private val adapter by lazy {
        DictionaryAdapter(dictionaryActions = this)
    }

    private var currentWordId: Long? = null
    private var currentDictionaryId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            currentWordId = it.getLong(WORD_ID_KEY)
            currentDictionaryId = it.getLong(DICTIONARY_ID_KEY)
        }

        currentWordId?.let {
            val component = (requireActivity().application as App).component
                .wordDetailsComponentFactory()
                .create(it)
            component.inject(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWordDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewDictionaries.adapter = adapter
        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() = with(binding) {
        buttonCancel.setOnClickListener {
            dismiss()
        }

        buttonDelete.setOnClickListener {
            currentDictionaryId?.let { dictionary ->
                viewModel.removeWordFromDictionary(dictionary)
                dismiss()
            }
        }

        buttonLearn.setOnClickListener {
            viewModel.learnAgain()
            dismiss()
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

    override fun selectDictionary(dictionaryId: Long) {
        viewModel.saveInDictionary(dictionaryId)
        dismiss()
    }

    companion object {
        fun newInstance(wordId: Long, dictionaryId: Long): WordDetailsFragment =
            WordDetailsFragment().apply {
                arguments = Bundle().apply {
                    putLong(WORD_ID_KEY, wordId)
                    putLong(DICTIONARY_ID_KEY, dictionaryId)
                }
            }

        private const val WORD_ID_KEY = "WORD_ID_KEY"
        private const val DICTIONARY_ID_KEY = "DICTIONARY_ID_KEY"
    }
}