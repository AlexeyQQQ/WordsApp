package dev.alexeyqqq.wordsapp.presentation.question

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dev.alexeyqqq.wordsapp.App
import dev.alexeyqqq.wordsapp.databinding.FragmentQuestionBinding
import dev.alexeyqqq.wordsapp.domain.repository.TrainerRepository.Companion.LEARN_ALL_WORDS
import dev.alexeyqqq.wordsapp.presentation.ViewModelFactory
import dev.alexeyqqq.wordsapp.presentation.navigation.QuestionNavigation
import kotlinx.coroutines.launch
import javax.inject.Inject

class QuestionFragment : Fragment() {

    private var _binding: FragmentQuestionBinding? = null
    private val binding: FragmentQuestionBinding
        get() = _binding ?: throw RuntimeException("FragmentQuestionBinding == null")

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (requireActivity().application as App).component
    }

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[QuestionViewModel::class.java]
    }

    private var dictionaryId: Long? = null

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { dictionaryId = it.getLong(QUESTION_FRAGMENT_KEY, LEARN_ALL_WORDS) }
        dictionaryId?.let { viewModel.nextQuestion(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() = with(binding) {
        val containers =
            listOf(containerOption1, containerOption2, containerOption3, containerOption4)
        val options = listOf(Options.ONE, Options.TWO, Options.THREE, Options.FOUR)

        containers.forEachIndexed { index, container ->
            container.setOnClickListener {
                viewModel.chooseOption(options[index])
            }
        }

        imageViewBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        buttonNext.setOnClickListener {
            if (viewModel.uiState.value is QuestionUiState.Error) {
                requireActivity().supportFragmentManager.popBackStack(
                    null,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
                (requireActivity() as QuestionNavigation).toStartMenuScreen()
            } else {
                dictionaryId?.let { viewModel.nextQuestion(it) }
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.uiState.collect { it.show(binding) }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(dictionaryId: Long): QuestionFragment =
            QuestionFragment().apply {
                arguments = Bundle().apply {
                    putLong(QUESTION_FRAGMENT_KEY, dictionaryId)
                }
            }

        private const val QUESTION_FRAGMENT_KEY = "QUESTION_FRAGMENT_KEY"
    }
}