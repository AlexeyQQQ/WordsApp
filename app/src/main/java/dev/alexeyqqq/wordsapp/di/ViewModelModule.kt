package dev.alexeyqqq.wordsapp.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dev.alexeyqqq.wordsapp.presentation.dictionaries.DictionariesViewModel
import dev.alexeyqqq.wordsapp.presentation.question.QuestionViewModel
import dev.alexeyqqq.wordsapp.presentation.select_dictionary.SelectDictionaryViewModel
import dev.alexeyqqq.wordsapp.presentation.statistics.StatisticsViewModel

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

    @Binds
    @IntoMap
    @ViewModelKey(StatisticsViewModel::class)
    fun bindStatisticsViewModel(viewModel: StatisticsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DictionariesViewModel::class)
    fun bindDictionariesViewModel(viewModel: DictionariesViewModel): ViewModel
}