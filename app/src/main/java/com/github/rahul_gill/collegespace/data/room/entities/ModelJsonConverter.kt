package com.github.rahul_gill.collegespace.data.room.entities

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.github.rahul_gill.collegespace.domain.models.ContestModel
import com.squareup.moshi.Moshi
import java.time.Duration
import java.util.*
import javax.inject.Inject


@ProvidedTypeConverter
class ModelJsonConverter @Inject constructor(moshi: Moshi) {
    private val moshiContestModelAdapter = moshi.adapter(ContestModel::class.java)
    //private val moshiEventAdapter = moshi.adapter(Event::class.java)
    private val moshiDateAdapter = moshi.adapter(Date::class.java)


    @TypeConverter
    fun fromJson(value: Long?): Duration? {
        return if(value == null) null else  Duration.ofSeconds(value)
    }
    @TypeConverter
    fun toJson(value : Duration?): Long? {
        return value?.seconds
    }

    @TypeConverter
    fun fromDateToJson(value : Date): String {
        return moshiDateAdapter.toJson(value)
    }

    @TypeConverter
    fun fromJsonToDate(value: String): Date {
        return moshiDateAdapter.fromJson(value)!!
    }



//    @TypeConverter
//    fun fromEventEntityToJson(value : Event): String {
//        return moshiEventAdapter.toJson(value)
//    }
//    @TypeConverter
//    fun fromJsonToEventEntity(value: String): Event {
//        return moshiEventAdapter.fromJson(value)!!
//    }


    @TypeConverter
    fun fromContestModelToJson(value : ContestModel): String {
        return moshiContestModelAdapter.toJson(value)
    }
    @TypeConverter
    fun fromJsonToContestModel(value: String): ContestModel {
        return moshiContestModelAdapter.fromJson(value)!!
    }
}