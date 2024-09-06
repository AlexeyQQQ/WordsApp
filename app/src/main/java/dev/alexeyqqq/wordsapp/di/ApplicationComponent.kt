package dev.alexeyqqq.wordsapp.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dev.alexeyqqq.wordsapp.presentation.core.MainActivity
import dev.alexeyqqq.wordsapp.presentation.create_dictionary.CreateNewDictionaryFragment
import dev.alexeyqqq.wordsapp.presentation.create_word.CreateNewWordFragment
import dev.alexeyqqq.wordsapp.presentation.dictionaries.DictionariesFragment
import dev.alexeyqqq.wordsapp.presentation.question.QuestionFragment
import dev.alexeyqqq.wordsapp.presentation.select_dictionary.SelectDictionaryFragment
import dev.alexeyqqq.wordsapp.presentation.statistics.StatisticsFragment
import kotlinx.coroutines.CoroutineScope

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        ViewModelModule::class,
    ]
)
interface ApplicationComponent {

    fun inject(activity: MainActivity)

    fun inject(fragment: SelectDictionaryFragment)

    fun inject(fragment: QuestionFragment)

    fun inject(fragment: StatisticsFragment)

    fun inject(fragment: DictionariesFragment)

    fun inject(fragment: CreateNewWordFragment)

    fun inject(fragment: CreateNewDictionaryFragment)

    fun dictionaryDetailsComponentFactory(): DictionaryDetailsComponent.Factory

    fun wordDetailsComponentFactory(): WordDetailsComponent.Factory

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context,
            @BindsInstance scope: CoroutineScope,
        ): ApplicationComponent
    }
}