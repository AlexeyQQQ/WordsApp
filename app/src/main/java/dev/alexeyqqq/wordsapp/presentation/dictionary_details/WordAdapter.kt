package dev.alexeyqqq.wordsapp.presentation.dictionary_details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.alexeyqqq.wordsapp.R
import dev.alexeyqqq.wordsapp.databinding.ItemRvWordBinding
import dev.alexeyqqq.wordsapp.domain.entity.Word

class WordAdapter(
    private val wordActions: WordActions,
) : ListAdapter<Word, WordAdapter.WordViewHolder>(WordDiffCallback), UpdateWordList {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val binding = ItemRvWordBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return WordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun update(list: List<Word>) = submitList(list)

    inner class WordViewHolder(
        private val binding: ItemRvWordBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(word: Word) = with(binding) {
            val context = itemView.context
            if (word.correctAnswersCount < ANSWER_TO_STUDY) {
                itemContainer.background =
                    ContextCompat.getDrawable(context, R.drawable.item_rv_background_shape)
                textViewCount.setTextColor(ContextCompat.getColor(context, R.color.black))
            } else {
                itemContainer.background =
                    ContextCompat.getDrawable(context, R.drawable.item_rv_learned_background_shape)
                textViewCount.setTextColor(ContextCompat.getColor(context, R.color.green))
            }

            textViewOriginal.text = word.original
            textViewTranslation.text = word.translation
            textViewCount.text = context.getString(
                R.string.count_repeat,
                word.correctAnswersCount.toString(),
                ANSWER_TO_STUDY.toString()
            )

            itemView.setOnClickListener {
                wordActions.selectWord(word.id)
            }
        }
    }

    companion object {
        private const val ANSWER_TO_STUDY = 3
    }
}

object WordDiffCallback : DiffUtil.ItemCallback<Word>() {

    override fun areItemsTheSame(oldItem: Word, newItem: Word): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Word, newItem: Word): Boolean {
        return oldItem == newItem
    }
}

interface UpdateWordList {

    fun update(list: List<Word>)
}

interface WordActions {

    fun selectWord(wordId: Long)
}