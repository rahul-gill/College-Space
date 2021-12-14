package com.artisticent.collegespace.data.room.entities

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.artisticent.collegespace.domain.models.ContestModel
import com.artisticent.collegespace.domain.models.EventModel
import com.squareup.moshi.Moshi
import javax.inject.Inject

@ProvidedTypeConverter
class ModelJsonConverter @Inject constructor(moshi: Moshi) {
    private val moshiEventModelAdapter = moshi.adapter(EventModel::class.java)
    private val moshiContestModelAdapter = moshi.adapter(ContestModel::class.java)

    @TypeConverter
    fun fromEventModelToJson(value : EventModel): String {
        return moshiEventModelAdapter.toJson(value)
    }
    @TypeConverter
    fun fromJsonToEventModel(value: String): EventModel {
        return moshiEventModelAdapter.fromJson(value)!!
    }

    @TypeConverter
    fun fromContestModelToJson(value : ContestModel): String {
        return moshiContestModelAdapter.toJson(value)
    }
    @TypeConverter
    fun fromJsonToContestModel(value: String): ContestModel {
        return moshiContestModelAdapter.fromJson(value)!!
    }
}