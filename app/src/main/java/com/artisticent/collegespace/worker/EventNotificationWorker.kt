package com.artisticent.collegespace.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.artisticent.collegespace.domain.EventRepository
import com.artisticent.collegespace.presentation.ui.events.triggerEventNotification
import javax.inject.Inject

class EventNotificationWorker @Inject constructor(
    appContext: Context,
    params: WorkerParameters,
    val eventRepository: EventRepository
) : CoroutineWorker(appContext, params){

    companion object{
        const val WORK_NAME = "EventNotificationWorker"
    }
    override suspend fun doWork(): Result {
        triggerEventNotification(applicationContext, "Sample Name")
        return Result.success()
    }

}