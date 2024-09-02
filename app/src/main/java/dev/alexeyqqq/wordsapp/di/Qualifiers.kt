package dev.alexeyqqq.wordsapp.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DictionaryQualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class WordIdQualifier