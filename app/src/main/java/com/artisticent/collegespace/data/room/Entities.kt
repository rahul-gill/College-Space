package com.artisticent.collegespace.data.room

import androidx.room.*
import com.artisticent.collegespace.repository.models.ContestModel
import com.artisticent.collegespace.repository.models.EventModel
import com.google.gson.Gson


@Entity
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val eventId: Int = 0,
    @ColumnInfo(name = "event")
    val event: EventModel
)

@Entity
data class ContestEntity(
    @PrimaryKey(autoGenerate = false)
    val eventId: Int = 0,
    @ColumnInfo(name = "contest")
    val contest: ContestModel
)

class WeekViewEventConverter{
    private val gson = Gson()

    @TypeConverter
    fun fromEventModelToJson(value : EventModel): String {
        return gson.toJson(value)
    }
    @TypeConverter
    fun fromJsonToEventModel(value: String): EventModel {
        return gson.fromJson(value, EventModel::class.java)
    }

    @TypeConverter
    fun fromContestModelToJson(value : ContestModel): String {
        return gson.toJson(value)
    }
    @TypeConverter
    fun fromJsonToContestModel(value: String): ContestModel {
        return gson.fromJson(value, ContestModel::class.java)
    }
}