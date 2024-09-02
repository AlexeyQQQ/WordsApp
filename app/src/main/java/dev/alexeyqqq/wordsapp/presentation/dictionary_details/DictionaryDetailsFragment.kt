package dev.alexeyqqq.wordsapp.presentation.dictionary_details

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dev.alexeyqqq.wordsapp.App
import dev.alexeyqqq.wordsapp.databinding.FragmentDictionaryDetailsBinding
import dev.alexeyqqq.wordsapp.domain.entity.Dictionary
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

    private var currentDictionary: Dictionary? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            currentDictionary = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(DICTIONARY_KEY, Dictionary::class.java)
            } else {
                it.getParcelable(DICTIONARY_KEY)
            }
        }

        currentDictionary?.let {
            val component = (requireActivity().application as App).component
                .dictionaryDetailsComponentFactory()
                .create(it)
            component.inject(this)
        }
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

        setFragmentResultListener(REQUEST_DICTIONARY_KEY) { _, bundle ->
            val result = bundle.getString(REQUEST_BUNDLE_KEY)
            binding.textViewHeader.text = result
        }
    }

    private fun setupClickListeners() = with(binding) {
        imageViewBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        buttonAddWord.setOnClickListener {
            currentDictionary?.let {
                (requireActivity() as DictionaryDetailsNavigation).toCreateNewWordScreen(it.id)
            }
        }

        imageViewMenu.setOnClickListener {
            currentDictionary?.let {
                (requireActivity() as DictionaryDetailsNavigation).toRenameDictionaryScreen(it)
            }
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
        currentDictionary?.let {
            (requireActivity() as DictionaryDetailsNavigation).toWordDetailsScreen(wordId, it.id)
        }
    }

    companion object {
        fun newInstance(dictionary: Dictionary): DictionaryDetailsFragment =
            DictionaryDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(DICTIONARY_KEY, dictionary)
                }
            }

        private const val DICTIONARY_KEY = "DICTIONARY_KEY"

        const val REQUEST_DICTIONARY_KEY = "REQUEST_DICTIONARY_KEY"
        const val REQUEST_BUNDLE_KEY = "REQUEST_BUNDLE_KEY"
    }
}