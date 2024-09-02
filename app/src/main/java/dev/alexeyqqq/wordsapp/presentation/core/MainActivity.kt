package dev.alexeyqqq.wordsapp.presentation.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.alexeyqqq.wordsapp.R
import dev.alexeyqqq.wordsapp.databinding.ActivityMainBinding
import dev.alexeyqqq.wordsapp.presentation.core.navigation.Navigation
import dev.alexeyqqq.wordsapp.presentation.core.navigation.Screen
import dev.alexeyqqq.wordsapp.presentation.start_menu.StartMenuScreen

class MainActivity : AppCompatActivity(), Navigation {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) navigate(StartMenuScreen)
    }

    override fun navigate(screen: Screen) {
        screen.show(R.id.fragmentContainer, supportFragmentManager)
    }
}