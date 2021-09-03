package com.artisticent.collegespace

import android.app.Application
import com.artisticent.collegespace.repository.models.Repository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltAndroidApp
class ThisApplication : Application(){
    @Inject lateinit var repository: Repository

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        GlobalScope.launch(Dispatchers.IO) {
            repository.loadContestDataFromNetwork()
        }
    }
}