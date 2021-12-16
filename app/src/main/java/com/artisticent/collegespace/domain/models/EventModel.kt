package com.artisticent.collegespace.domain.models

import java.util.*

data class EventModel(
    val name: String,
    val type:EventType,
    val start: Date,
    val end: Date,
    val description: String? = null,
)

enum class EventType{
    PERSONAL_EVENT, CLASS_EVENT, USER_GROUP_EVENT
}