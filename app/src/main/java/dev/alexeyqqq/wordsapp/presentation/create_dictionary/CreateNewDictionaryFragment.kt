package dev.alexeyqqq.wordsapp.presentation.create_dictionary

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.alexeyqqq.wordsapp.App
import dev.alexeyqqq.wordsapp.R
import dev.alexeyqqq.wordsapp.databinding.FragmentCreateNewDictionaryBinding
import dev.alexeyqqq.wordsapp.domain.entity.Dictionary
import dev.alexeyqqq.wordsapp.presentation.ViewModelFactory
import dev.alexeyqqq.wordsapp.presentation.dictionary_details.DictionaryDetailsFragment.Companion.REQUEST_BUNDLE_KEY
import dev.alexeyqqq.wordsapp.presentation.dictionary_details.DictionaryDetailsFragment.Companion.REQUEST_DICTIONARY_KEY
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreateNewDictionaryFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentCreateNewDictionaryBinding? = null
    private val binding: FragmentCreateNewDictionaryBinding
        get() = _binding ?: throw RuntimeException("FragmentCreateNewDictionaryBinding == null")

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (requireActivity().application as App).component
    }

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[CreateNewDictionaryViewModel::class.java]
    }

    private var currentDictionary: Dictionary? = null

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCreateNewDictionaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launchRightMode()
        observeViewModel()
    }

    private fun launchRightMode() {
        when (requireArguments().getString(LAUNCH_MODE)) {
            MODE_ADD -> launchAddMode()

            MODE_EDIT -> {
                currentDictionary = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requireArguments().getParcelable(DICTIONARY_KEY, Dictionary::class.java)
                } else {
                    requireArguments().getParcelable(DICTIONARY_KEY)
                }
                launchEditMode()
            }

            else -> throw RuntimeException("CreateNewDictionaryFragment argument mode is unknown")
        }
    }

    private fun launchAddMode() = with(binding) {
        textViewHeader.setText(R.string.adding_dictionary)
        buttonCancel.setText(R.string.cancel)

        editTextName.addTextChangedListener {
            viewModel.checkInput(it.toString())
        }

        buttonSave.setOnClickListener {
            viewModel.saveNewDictionary(editTextName.text.toString())
            dismiss()
        }

        buttonCancel.setOnClickListener { dismiss() }
    }

    private fun launchEditMode() = with(binding) {
        currentDictionary?.let { dictionary ->
            editTextName.setText(dictionary.name)
            buttonCancel.setText(R.string.delete)
            textViewHeader.setText(R.string.rename_dictionary)

            editTextName.addTextChangedListener {
                viewModel.checkInput(it.toString(), dictionary.name)
            }

            buttonSave.setOnClickListener {
                val newName = editTextName.text.toString()
                viewModel.renameDictionary(dictionary.id, newName)
                setFragmentResult(
                    REQUEST_DICTIONARY_KEY,
                    bundleOf(REQUEST_BUNDLE_KEY to newName)
                )
                dismiss()
            }

            buttonCancel.setOnClickListener {
                viewModel.deleteDictionary(dictionary.id)
                dismiss()
                requireActivity().onBackPressedDispatcher.onBackPressed()
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
        fun newInstanceModeAdd(): CreateNewDictionaryFragment =
            newInstance(MODE_ADD, null)

        fun newInstanceModeEdit(dictionary: Dictionary): CreateNewDictionaryFragment =
            newInstance(MODE_EDIT, dictionary)

        private fun newInstance(
            mode: String,
            dictionary: Dictionary?,
        ): CreateNewDictionaryFragment =
            CreateNewDictionaryFragment().apply {
                arguments = Bundle().apply {
                    putString(LAUNCH_MODE, mode)
                    putParcelable(DICTIONARY_KEY, dictionary)
                }
            }

        private const val LAUNCH_MODE = "LAUNCH_MODE"
        private const val MODE_ADD = "MODE_ADD"
        private const val MODE_EDIT = "MODE_EDIT"

        private const val DICTIONARY_KEY = "DICTIONARY_KEY"
    }
}