package dev.alexeyqqq.wordsapp.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dev.alexeyqqq.wordsapp.data.database.AppDatabase
import dev.alexeyqqq.wordsapp.data.database.DictionaryDao
import dev.alexeyqqq.wordsapp.data.database.RelationDao
import dev.alexeyqqq.wordsapp.data.database.WordsDao
import dev.alexeyqqq.wordsapp.data.network.ApiFactory
import dev.alexeyqqq.wordsapp.data.network.TranslateService
import dev.alexeyqqq.wordsapp.data.repository.DictionaryRepositoryImpl
import dev.alexeyqqq.wordsapp.data.repository.RelationRepositoryImpl
import dev.alexeyqqq.wordsapp.data.repository.TrainerRepositoryImpl
import dev.alexeyqqq.wordsapp.data.repository.WordRepositoryImpl
import dev.alexeyqqq.wordsapp.domain.repository.DictionaryRepository
import dev.alexeyqqq.wordsapp.domain.repository.RelationRepository
import dev.alexeyqqq.wordsapp.domain.repository.TrainerRepository
import dev.alexeyqqq.wordsapp.domain.repository.WordRepository
import kotlinx.coroutines.CoroutineScope

@Module
interface DataModule {

    @Binds
    fun bindWordRepository(impl: WordRepositoryImpl): WordRepository

    @Binds
    fun bindDictionaryRepository(impl: DictionaryRepositoryImpl): DictionaryRepository

    @Binds
    fun bindRelationRepository(impl: RelationRepositoryImpl): RelationRepository

    @Binds
    fun bindTrainerRepository(impl: TrainerRepositoryImpl): TrainerRepository

    companion object {

        @Provides
        fun provideWordsDao(context: Context, scope: CoroutineScope): WordsDao {
            return AppDatabase.getInstance(context, scope).wordsDao()
        }

        @Provides
        fun provideDictionaryDao(context: Context, scope: CoroutineScope): DictionaryDao {
            return AppDatabase.getInstance(context, scope).dictionaryDao()
        }

        @Provides
        fun provideRelationDao(context: Context, scope: CoroutineScope): RelationDao {
            return AppDatabase.getInstance(context, scope).relationDao()
        }

        @Provides
        fun provideTranslateService(): TranslateService {
            return ApiFactory.translateService
        }
    }
}