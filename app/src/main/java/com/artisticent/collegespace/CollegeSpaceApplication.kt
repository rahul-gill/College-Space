package com.artisticent.collegespace

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.artisticent.collegespace.worker.EventNotificationWorker
import com.artisticent.collegespace.worker.RefreshDataWorker
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit


@HiltAndroidApp
class CollegeSpaceApplication : Application(){
    private val applicationScope = CoroutineScope(Dispatchers.IO)

    private fun delayedInit() {
        applicationScope.launch {
            setupRecurringWork()
        }
    }

    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .apply {
                setRequiresDeviceIdle(true)
            }.build()
        val constraints2 = Constraints.Builder()
            .build()
        val refreshRepeatingRequest
                = PeriodicWorkRequestBuilder<RefreshDataWorker>(5, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()
        val notificationRepeatingRequest
                = PeriodicWorkRequestBuilder<EventNotificationWorker>(1, TimeUnit.MINUTES)
            .setConstraints(constraints2)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            RefreshDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            refreshRepeatingRequest)
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            EventNotificationWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            notificationRepeatingRequest)
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        delayedInit()
    }
}