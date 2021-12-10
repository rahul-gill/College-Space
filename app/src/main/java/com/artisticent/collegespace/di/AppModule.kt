package com.artisticent.collegespace.di

import android.content.Context
import androidx.room.Room
import com.artisticent.collegespace.data.EventRepositoryImpl
import com.artisticent.collegespace.data.FirebaseRepositoryImpl
import com.artisticent.collegespace.data.contestsApi.ContestApi
import com.artisticent.collegespace.data.contestsApi.NetworkInterceptor
import com.artisticent.collegespace.data.room.AppDatabase
import com.artisticent.collegespace.data.room.EventDatabaseDao
import com.artisticent.collegespace.domain.EventRepository
import com.artisticent.collegespace.domain.FirebaseRepository
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
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
    fun getOkHttpClient(@ApplicationContext context: Context): OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(NetworkInterceptor(context))
            .build()
    }
    @Singleton
    @Provides
    fun getContestApi(moshi: Moshi, okHttpClient: OkHttpClient) : ContestApi = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://www.kontests.net/api/v1/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(ContestApi::class.java)

    @Singleton
    @Provides
    fun getEventDatabaseInstance(@ApplicationContext context: Context): AppDatabase{
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "Event Database"
        )
            .build()
    }

    @Provides
    fun getEventDatabaseDao(database: AppDatabase) : EventDatabaseDao{
        return database.eventDatabaseDao
    }


    @Singleton
    @Provides
    fun getFirebaseRepository(): FirebaseRepository{
        return FirebaseRepositoryImpl()
    }

    @Singleton
    @Provides
    fun getEventRepository(): EventRepository{
        return EventRepositoryImpl()
    }
}