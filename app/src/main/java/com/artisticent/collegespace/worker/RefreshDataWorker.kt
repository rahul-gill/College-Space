package com.artisticent.collegespace.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.artisticent.collegespace.domain.Repository
import retrofit2.HttpException
import javax.inject.Inject


class RefreshDataWorker @Inject constructor(
    appContext: Context,
    params: WorkerParameters,
    val repository: Repository
    ) : CoroutineWorker(appContext, params){

    companion object{
        const val WORK_NAME = "RefreshDataWorker"
    }
    override suspend fun doWork(): Result{
        return try{
            repository.loadContestDataFromNetwork()
            Result.success()
        }catch (e: HttpException){
            Result.failure()
        }
    }

}