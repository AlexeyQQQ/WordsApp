package dev.alexeyqqq.wordsapp

import android.app.Application
import dev.alexeyqqq.wordsapp.di.DaggerApplicationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class App : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    val component by lazy {
        DaggerApplicationComponent.factory().create(this, applicationScope)
    }
}