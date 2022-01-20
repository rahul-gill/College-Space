package com.github.rahul_gill.collegespace.data.room.entities

import androidx.room.Entity
import androidx.room.Index
import com.github.rahul_gill.collegespace.domain.models.DurationWrapper
import com.github.rahul_gill.collegespace.domain.models.Event
import com.github.rahul_gill.collegespace.util.Util
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
        DurationWrapper.from(
            Duration.between(Util.latestLocalDateTime(start) , Util.latestLocalDateTime(end))),
        repeatPeriod?.let { DurationWrapper.from(repeatPeriod!!) } ,
        description
    )
    companion object{
        fun from(event: Event) = PersonalEventEntity(
            eventName = event.eventName,
            start = event.start,
            end = event.duration?.let { Util.toDate(Util.toLocalDateTime(event.start)!!.plus(it.toDuration())) } ?: event.start,
            description = event.description,
            repeatPeriod = event.repeatPeriod?.toDuration()
        )
    }
}