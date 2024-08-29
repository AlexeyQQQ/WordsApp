package dev.alexeyqqq.wordsapp.presentation.question

import android.view.View
import androidx.core.content.ContextCompat
import dev.alexeyqqq.wordsapp.R
import dev.alexeyqqq.wordsapp.databinding.FragmentQuestionBinding
import dev.alexeyqqq.wordsapp.domain.exceptions.TrainerExceptions

sealed interface QuestionUiState {

    fun show(binding: FragmentQuestionBinding)

    data object Loading : QuestionUiState {

        override fun show(binding: FragmentQuestionBinding) = with(binding) {
            listOf(
                containerOption1,
                containerOption2,
                containerOption3,
                containerOption4,
                progressBar,
                textViewProgress,
                textViewResult,
                buttonNext,
            ).forEach {
                it.visibility = View.GONE
            }
            progressBarLoading.visibility = View.VISIBLE
        }
    }

    data class NewWord(
        private val word: String,
        private val options: List<String>,
        private val wordsLearned: Int,
        private val wordsTotal: Int,
    ) : QuestionUiState {

        override fun show(binding: FragmentQuestionBinding) = with(binding) {
            val context = binding.root.context
            val optionBackground =
                ContextCompat.getDrawable(context, R.drawable.item_rv_background_shape)
            val optionNumberBackground = ContextCompat.getDrawable(
                context, R.drawable.option_number_shape
            )
            val blackTextColor = ContextCompat.getColor(context, R.color.black)

            progressBarLoading.visibility = View.GONE

            listOf(containerOption1, containerOption2, containerOption3, containerOption4).forEach {
                it.visibility = View.VISIBLE
                it.isClickable = true
                it.background = optionBackground
            }

            listOf(numberOption1, numberOption2, numberOption3, numberOption4).forEach {
                it.background = optionNumberBackground
                it.setTextColor(blackTextColor)
            }

            listOf(
                textViewOption1, textViewOption2, textViewOption3, textViewOption4
            ).forEachIndexed { index, textView ->
                textView.text = options[index]
                textView.setTextColor(blackTextColor)
            }

            progressBar.apply {
                visibility = View.VISIBLE
                max = wordsTotal
                progress = wordsLearned
            }

            textViewProgress.apply {
                visibility = View.VISIBLE
                text = context.getString(
                    R.string.progress_template,
                    wordsLearned.toString(),
                    wordsTotal.toString()
                )
            }

            buttonNext.apply {
                text = context.getString(R.string.skip)
                visibility = View.VISIBLE
            }

            textViewHeader.text = word
            textViewResult.visibility = View.GONE
        }
    }

    data class RightAnswer(
        private val userAnswer: Options,
    ) : QuestionUiState {

        override fun show(binding: FragmentQuestionBinding) = with(binding) {
            val context = binding.root.context
            val rightOptionBackground =
                ContextCompat.getDrawable(context, R.drawable.right_option_background)
            val rightNumberBackground =
                ContextCompat.getDrawable(context, R.drawable.option_number_green_shape)
            val rightTextColor = ContextCompat.getColor(context, R.color.green)
            val whiteTextColor = ContextCompat.getColor(context, R.color.white)

            listOf(containerOption1, containerOption2, containerOption3, containerOption4).forEach {
                it.isClickable = false
            }

            val optionMap = mapOf(
                Options.ONE to Triple(containerOption1, numberOption1, textViewOption1),
                Options.TWO to Triple(containerOption2, numberOption2, textViewOption2),
                Options.THREE to Triple(containerOption3, numberOption3, textViewOption3),
                Options.FOUR to Triple(containerOption4, numberOption4, textViewOption4)
            )
            optionMap[userAnswer]?.let { (container, number, text) ->
                container.background = rightOptionBackground
                number.background = rightNumberBackground
                number.setTextColor(whiteTextColor)
                text.setTextColor(rightTextColor)
            }

            textViewResult.apply {
                text = context.getString(R.string.correct)
                setTextColor(rightTextColor)
                visibility = View.VISIBLE
            }

            buttonNext.text = context.getString(R.string.next)
        }
    }

    data class WrongAnswer(
        private val userAnswerOption: Options,
        private val rightAnswerIndex: Int,
    ) : QuestionUiState {

        override fun show(binding: FragmentQuestionBinding) = with(binding) {
            val context = binding.root.context
            val rightOptionBackground =
                ContextCompat.getDrawable(context, R.drawable.right_option_background)
            val wrongOptionBackground =
                ContextCompat.getDrawable(context, R.drawable.wrong_option_background)
            val rightNumberBackground =
                ContextCompat.getDrawable(context, R.drawable.option_number_green_shape)
            val wrongNumberBackground =
                ContextCompat.getDrawable(context, R.drawable.option_number_red_shape)
            val rightTextColor = ContextCompat.getColor(context, R.color.green)
            val wrongTextColor = ContextCompat.getColor(context, R.color.red)
            val whiteTextColor = ContextCompat.getColor(context, R.color.white)

            listOf(containerOption1, containerOption2, containerOption3, containerOption4).forEach {
                it.isClickable = false
            }

            val optionMap = mapOf(
                Options.ONE to Triple(containerOption1, numberOption1, textViewOption1),
                Options.TWO to Triple(containerOption2, numberOption2, textViewOption2),
                Options.THREE to Triple(containerOption3, numberOption3, textViewOption3),
                Options.FOUR to Triple(containerOption4, numberOption4, textViewOption4)
            )
            optionMap[userAnswerOption]?.let { (container, number, text) ->
                container.background = wrongOptionBackground
                number.background = wrongNumberBackground
                number.setTextColor(whiteTextColor)
                text.setTextColor(wrongTextColor)
            }

            val indexMap = mapOf(
                0 to Triple(containerOption1, numberOption1, textViewOption1),
                1 to Triple(containerOption2, numberOption2, textViewOption2),
                2 to Triple(containerOption3, numberOption3, textViewOption3),
                3 to Triple(containerOption4, numberOption4, textViewOption4)
            )
            indexMap[rightAnswerIndex]?.let { (container, number, text) ->
                container.background = rightOptionBackground
                number.background = rightNumberBackground
                number.setTextColor(whiteTextColor)
                text.setTextColor(rightTextColor)
            }

            textViewResult.apply {
                text = context.getString(R.string.wrong)
                setTextColor(wrongTextColor)
                visibility = View.VISIBLE
            }

            buttonNext.text = context.getString(R.string.next)
        }
    }

    data class Error(
        private val exceptions: TrainerExceptions,
    ) : QuestionUiState {

        override fun show(binding: FragmentQuestionBinding) = with(binding) {
            val context = root.context

            listOf(
                containerOption1,
                containerOption2,
                containerOption3,
                containerOption4,
                progressBar,
                textViewProgress,
                textViewResult,
            ).forEach {
                it.visibility = View.GONE
            }

            textViewError.apply {
                text = when (exceptions) {
                    is TrainerExceptions.FewWords -> context.getString(R.string.few_words_in_dictionary)
                    is TrainerExceptions.NoWordsToLearn -> context.getString(R.string.no_words_to_learn)
                }
                visibility = View.VISIBLE
            }

            textViewHeader.text = context.getString(R.string.have_problem)
            buttonNext.text = context.getString(R.string.return_to_menu)
        }
    }
}