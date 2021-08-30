package com.artisticent.collegespace.di

import com.artisticent.collegespace.remote.apis.CodeforcesApi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule{

    @Singleton
    @Provides
    fun getMoshiInstance(): Moshi = Moshi.Builder()
        .build()


    @Singleton
    @Provides
    fun getCodeforcesApiInstance(moshi: Moshi): CodeforcesApi = Retrofit.Builder()
        .baseUrl("https://codeforces.com/api/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(CodeforcesApi::class.java)
}