package dev.alexeyqqq.wordsapp.presentation.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.alexeyqqq.wordsapp.App
import dev.alexeyqqq.wordsapp.R
import dev.alexeyqqq.wordsapp.databinding.ActivityMainBinding
import dev.alexeyqqq.wordsapp.presentation.core.navigation.Navigation
import dev.alexeyqqq.wordsapp.presentation.core.navigation.Screen
import dev.alexeyqqq.wordsapp.presentation.start_menu.StartMenuScreen
import javax.inject.Inject

class MainActivity : AppCompatActivity(), Navigation {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).component.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            navigate(StartMenuScreen)
            notificationHelper.checkAndSetupNotifications(this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        notificationHelper.handlePermissionResult(requestCode, grantResults)
    }

    override fun navigate(screen: Screen) {
        screen.show(R.id.fragmentContainer, supportFragmentManager)
    }
}