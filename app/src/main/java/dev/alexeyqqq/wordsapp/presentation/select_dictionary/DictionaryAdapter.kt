package dev.alexeyqqq.wordsapp.presentation.select_dictionary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.alexeyqqq.wordsapp.databinding.ItemRvDictionaryBinding
import dev.alexeyqqq.wordsapp.domain.entity.Dictionary

class DictionaryAdapter(
    private val dictionaryActions: DictionaryActions,
) : ListAdapter<Dictionary, DictionaryAdapter.DictionaryViewHolder>(DictionaryDiffCallback),
    UpdateList {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DictionaryViewHolder {
        val binding = ItemRvDictionaryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DictionaryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DictionaryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DictionaryViewHolder(
        private val binding: ItemRvDictionaryBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(dictionary: Dictionary) {
            binding.textViewName.text = dictionary.name
            itemView.setOnClickListener {
                dictionaryActions.selectDictionary()
            }
        }
    }

    override fun update(list: List<Dictionary>) = submitList(list)
}

object DictionaryDiffCallback : DiffUtil.ItemCallback<Dictionary>() {

    override fun areItemsTheSame(oldItem: Dictionary, newItem: Dictionary): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Dictionary, newItem: Dictionary): Boolean {
        return oldItem == newItem
    }
}

interface UpdateList {

    fun update(list: List<Dictionary>)
}