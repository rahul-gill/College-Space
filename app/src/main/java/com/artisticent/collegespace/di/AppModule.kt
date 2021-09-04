package com.artisticent.collegespace.di

import android.content.Context
import androidx.room.Room
import com.artisticent.collegespace.data.contestsApi.ContestApi
import com.artisticent.collegespace.data.room.EventDatabase
import com.artisticent.collegespace.data.room.EventDatabaseDao
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun getContestApi(moshi: Moshi) : ContestApi = Retrofit.Builder()
        .baseUrl("https://www.kontests.net/api/v1/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(ContestApi::class.java)

    @Singleton
    @Provides
    fun getEventDatabaseInstance(@ApplicationContext context: Context): EventDatabase{
        return Room.databaseBuilder(
            context,
            EventDatabase::class.java,
            "Event Database"
        )
            .build()
    }

    @Provides
    fun getEventDatabaseDao(database: EventDatabase) : EventDatabaseDao{
        return database.eventDatabaseDao
    }

}