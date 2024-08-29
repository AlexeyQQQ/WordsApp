package dev.alexeyqqq.wordsapp.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dev.alexeyqqq.wordsapp.presentation.question.QuestionFragment
import dev.alexeyqqq.wordsapp.presentation.select_dictionary.SelectDictionaryFragment
import dev.alexeyqqq.wordsapp.presentation.statistics.StatisticsFragment
import kotlinx.coroutines.CoroutineScope

@Component(
    modules = [
        DataModule::class,
        ViewModelModule::class,
    ]
)
interface ApplicationComponent {

    fun inject(fragment: SelectDictionaryFragment)

    fun inject(fragment: QuestionFragment)

    fun inject(fragment: StatisticsFragment)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context,
            @BindsInstance scope: CoroutineScope,
        ): ApplicationComponent
    }
}