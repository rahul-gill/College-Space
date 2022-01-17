package com.github.rahul_gill.collegespace.di

import android.content.Context
import androidx.room.Room
import com.github.rahul_gill.collegespace.data.EventRepositoryImpl
import com.github.rahul_gill.collegespace.data.FirebaseRepositoryImpl
import com.github.rahul_gill.collegespace.data.contestsApi.ContestApi
import com.github.rahul_gill.collegespace.data.contestsApi.NetworkInterceptor
import com.github.rahul_gill.collegespace.data.room.AppDatabase
import com.github.rahul_gill.collegespace.data.room.EventDatabaseDao
import com.github.rahul_gill.collegespace.data.room.entities.ModelJsonConverter
import com.github.rahul_gill.collegespace.domain.EventRepository
import com.github.rahul_gill.collegespace.domain.FirebaseRepository
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule{

    @Singleton
    @Provides
    fun getMoshiInstance(): Moshi = Moshi.Builder()
        .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
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
    fun getEventDatabaseInstance(@ApplicationContext context: Context, moshi: Moshi): AppDatabase{
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "Event Database"
        )
            .addTypeConverter(ModelJsonConverter(moshi))
            .build()
    }

    @Singleton
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
    fun getEventRepository(
        databaseDao: EventDatabaseDao,
        contestApi: ContestApi
    ): EventRepository{
        return EventRepositoryImpl(databaseDao, contestApi)
    }
}