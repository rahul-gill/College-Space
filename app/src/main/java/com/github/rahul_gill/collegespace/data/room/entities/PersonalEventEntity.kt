package com.github.rahul_gill.collegespace.data.room.entities

import androidx.room.Entity
import androidx.room.Index
import com.github.rahul_gill.collegespace.domain.models.Event
import com.squareup.moshi.JsonClass
import java.time.Duration
import java.time.Instant
import java.util.*

@Entity(
    primaryKeys = ["start"],
    indices = [Index(value = ["start"])]
)
@JsonClass(generateAdapter = true)
data class PersonalEventEntity(
    var eventName: String = "",
    var start: Date = Date.from(Instant.now()),
    var end: Date = Date.from(Instant.now()),
    var repeatPeriod: Duration? = null,
    var description: String? = null
){
    fun toEvent() = Event(
        eventName,
        start,
        end,
        repeatPeriod,
        description
    )
}