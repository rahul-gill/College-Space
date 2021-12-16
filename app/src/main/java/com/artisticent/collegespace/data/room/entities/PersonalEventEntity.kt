package com.artisticent.collegespace.data.room.entities

import androidx.room.Entity
import androidx.room.Index
import com.artisticent.collegespace.domain.models.AttendanceRecord
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
)