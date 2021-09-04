package com.artisticent.collegespace

import android.app.Application
import com.artisticent.collegespace.repository.Repository
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject


@HiltAndroidApp
class ThisApplication : Application(){
    @Inject lateinit var repository: Repository

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}