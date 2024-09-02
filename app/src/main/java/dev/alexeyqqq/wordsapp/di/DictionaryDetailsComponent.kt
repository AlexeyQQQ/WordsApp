package dev.alexeyqqq.wordsapp.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.BindsInstance
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap
import dev.alexeyqqq.wordsapp.domain.entity.Dictionary
import dev.alexeyqqq.wordsapp.presentation.dictionary_details.DictionaryDetailsFragment
import dev.alexeyqqq.wordsapp.presentation.dictionary_details.DictionaryDetailsViewModel

@Subcomponent(
    modules = [DictionaryDetailsModule::class]
)
interface DictionaryDetailsComponent {

    fun inject(fragment: DictionaryDetailsFragment)

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance @DictionaryQualifier dictionary: Dictionary,
        ): DictionaryDetailsComponent
    }
}

@Module
interface DictionaryDetailsModule {

    @Binds
    @IntoMap
    @ViewModelKey(DictionaryDetailsViewModel::class)
    fun bindDictionaryDetailsViewModel(viewModel: DictionaryDetailsViewModel): ViewModel
}