package dev.alexeyqqq.wordsapp.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dev.alexeyqqq.wordsapp.presentation.question.QuestionViewModel
import dev.alexeyqqq.wordsapp.presentation.select_dictionary.SelectDictionaryViewModel

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SelectDictionaryViewModel::class)
    fun bindSelectDictionaryViewModel(viewModel: SelectDictionaryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(QuestionViewModel::class)
    fun bindQuestionViewModel(viewModel: QuestionViewModel): ViewModel
}