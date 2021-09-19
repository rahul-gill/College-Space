package com.artisticent.collegespace.repository.models

import com.alamkanak.weekview.WeekViewEvent
import com.artisticent.collegespace.data.room.EventEntity

class EventModel : WeekViewEvent(){
    fun eventModelToEventEntity(): EventEntity {
        return EventEntity(
            event = this
        )
    }
}

