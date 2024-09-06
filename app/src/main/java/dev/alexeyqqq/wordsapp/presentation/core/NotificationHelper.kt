package dev.alexeyqqq.wordsapp.presentation.core

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import javax.inject.Inject

class NotificationHelper @Inject constructor(private val context: Context) {

    fun checkAndSetupNotifications(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_CODE_POST_NOTIFICATIONS
                )
            } else setupWorker()
        } else setupWorker()
    }

    fun handlePermissionResult(requestCode: Int, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_POST_NOTIFICATIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupWorker()
            }
        }
    }

    private fun setupWorker() {
        WorkManager.getInstance(context).enqueueUniqueWork(
            NotificationWorker::class.java.simpleName,
            ExistingWorkPolicy.REPLACE,
            NotificationWorker.makeRequest()
        )
    }

    companion object {
        private const val REQUEST_CODE_POST_NOTIFICATIONS = 1
    }
}