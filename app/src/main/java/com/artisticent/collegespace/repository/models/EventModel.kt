package com.artisticent.collegespace.repository.models

import com.alamkanak.weekview.WeekViewEvent
import com.artisticent.collegespace.data.room.EventEntity
import java.util.*


class EventModel(var id: Int = Int.MIN_VALUE) : WeekViewEvent(){
    var overdue = false
    fun eventModelToEventEntity(): EventEntity {
        return EventEntity(
            event = this
        )
    }

    fun checkAndUpdateOverdueStatus(){
        if(!overdue && endTime <= Calendar.getInstance()){
            overdue = true
        }
    }
}

