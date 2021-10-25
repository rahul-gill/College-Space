package com.artisticent.collegespace.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.artisticent.collegespace.domain.Repository
import com.artisticent.collegespace.presentation.ui.events.triggerEventNotification
import retrofit2.HttpException
import javax.inject.Inject

class EventNotificationWorker @Inject constructor(
    appContext: Context,
    params: WorkerParameters,
    val repository: Repository
) : CoroutineWorker(appContext, params){

    companion object{
        const val WORK_NAME = "EventNotificationWorker"
    }
    override suspend fun doWork(): Result {
        triggerEventNotification(applicationContext)
        return Result.success()
    }

}