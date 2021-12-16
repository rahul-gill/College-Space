package com.artisticent.collegespace.domain.models

import com.alamkanak.weekview.WeekViewEvent
import com.artisticent.collegespace.data.room.entities.EventEntity
import java.util.*


class EventModelOld(var id: Int = Int.MIN_VALUE) : WeekViewEvent(){
    private var overdue = false
    fun eventModelToEventEntity(): EventEntity {
        return EventEntity(
            eventId = this.id,
            event = this
        )
    }

    fun checkAndUpdateOverdueStatus(){
        if(!overdue && endTime <= Calendar.getInstance()){
            overdue = true
        }
    }
}

