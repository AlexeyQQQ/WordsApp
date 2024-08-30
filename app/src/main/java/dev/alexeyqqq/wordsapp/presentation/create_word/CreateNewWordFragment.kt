package dev.alexeyqqq.wordsapp.presentation.create_word

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.alexeyqqq.wordsapp.App
import dev.alexeyqqq.wordsapp.databinding.FragmentCreateNewWordBinding
import dev.alexeyqqq.wordsapp.presentation.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreateNewWordFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentCreateNewWordBinding? = null
    private val binding: FragmentCreateNewWordBinding
        get() = _binding ?: throw RuntimeException("FragmentCreateNewWordBinding == null")

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (requireActivity().application as App).component
    }

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[CreateNewWordViewModel::class.java]
    }

    private var dictionaryId: Long? = null

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dictionaryId = it.getLong(DICTIONARY_ID_KEY)
        } ?: throw RuntimeException("DictionaryDetailsFragment arguments == null")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCreateNewWordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        setupTextChangedListeners()
        observeViewModel()
    }

    private fun setupTextChangedListeners() {
        binding.editTextOriginal.addTextChangedListener {
            viewModel.checkOriginalInput(it.toString())
        }

        binding.editTextTranslate.addTextChangedListener {
            viewModel.checkTranslateInput(it.toString())
        }
    }

    private fun setupClickListeners() = with(binding) {
        buttonCancel.setOnClickListener {
            dismiss()
        }

        buttonSave.setOnClickListener {
            dictionaryId?.let {
                val original = binding.editTextOriginal.text.toString()
                val translate = binding.editTextTranslate.text.toString()
                viewModel.saveNewWord(original, translate, it)
                dismiss()
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.uiState.collectLatest {
                    it.show(binding)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(dictionaryId: Long): CreateNewWordFragment =
            CreateNewWordFragment().apply {
                arguments = Bundle().apply {
                    putLong(DICTIONARY_ID_KEY, dictionaryId)
                }
            }

        private const val DICTIONARY_ID_KEY = "DICTIONARY_ID_KEY"
    }
}