package com.artisticent.collegespace.di

import com.artisticent.collegespace.remote.apis.CodeforcesApi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
object AppModule{

    @Singleton
    @Provides
    fun getCodeforcesApiInstance(): CodeforcesApi = Retrofit.Builder()
        .baseUrl("https://codeforces.com/api")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build().
        create(CodeforcesApi::class.java)
}