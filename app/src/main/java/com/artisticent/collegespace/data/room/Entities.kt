package com.artisticent.collegespace.data.room

import androidx.room.*
import com.alamkanak.weekview.WeekViewEvent
import com.artisticent.collegespace.repository.models.ContestModel
import com.google.gson.Gson


@Entity
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val eventId: Long = 0L,
    @ColumnInfo(name = "event")
    val weekViewEvent: WeekViewEvent
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
    fun fromWeekViewEventToJson(value : WeekViewEvent): String {
        return gson.toJson(value)
    }
    @TypeConverter
    fun fromJsonToWeekViewEvent(value: String): WeekViewEvent {
        return gson.fromJson(value, WeekViewEvent::class.java)
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