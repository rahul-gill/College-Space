package com.artisticent.collegespace.di

import android.content.Context
import androidx.room.Room
import com.artisticent.collegespace.data.cf.CodeforcesApi
import com.artisticent.collegespace.data.room.EventDatabase
import com.artisticent.collegespace.data.room.EventDatabaseDao
import com.artisticent.collegespace.data.room.WeekViewEventConverter
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.nio.file.WatchEvent
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