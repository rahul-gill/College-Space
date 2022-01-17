package com.github.rahul_gill.collegespace.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.github.rahul_gill.collegespace.domain.EventRepository
import retrofit2.HttpException
import javax.inject.Inject


class RefreshDataWorker @Inject constructor(
    appContext: Context,
    params: WorkerParameters,
    private val eventRepository: EventRepository
) : CoroutineWorker(appContext, params){

    companion object{
        const val WORK_NAME = "RefreshDataWorker"
    }
    override suspend fun doWork(): Result{
        return try{
            eventRepository.loadContestDataFromNetwork()
            Result.success()
        }catch (e: HttpException){
            Result.failure()
        }
    }

}