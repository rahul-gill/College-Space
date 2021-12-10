package com.artisticent.collegespace.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.artisticent.collegespace.domain.models.EventModel

@Entity
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val eventId: Int = 0,
    @ColumnInfo(name = "event")
    val event: EventModel
)