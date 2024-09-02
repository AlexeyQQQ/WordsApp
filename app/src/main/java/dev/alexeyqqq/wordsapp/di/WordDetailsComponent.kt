package dev.alexeyqqq.wordsapp.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.BindsInstance
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap
import dev.alexeyqqq.wordsapp.presentation.word_details.WordDetailsFragment
import dev.alexeyqqq.wordsapp.presentation.word_details.WordDetailsViewModel

@Subcomponent(
    modules = [WordDetailsModule::class]
)
interface WordDetailsComponent {

    fun inject(fragment: WordDetailsFragment)

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance @WordIdQualifier wordId: Long,
        ): WordDetailsComponent
    }
}

@Module
interface WordDetailsModule {

    @Binds
    @IntoMap
    @ViewModelKey(WordDetailsViewModel::class)
    fun bindWordDetailsViewModel(viewModel: WordDetailsViewModel): ViewModel
}