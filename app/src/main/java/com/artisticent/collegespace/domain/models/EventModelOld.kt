package com.artisticent.collegespace.domain.models

import com.alamkanak.weekview.WeekViewEvent
import java.util.*


class EventModelOld(var id: Int = Int.MIN_VALUE) : WeekViewEvent(){
    private var overdue = false

    fun checkAndUpdateOverdueStatus(){
        if(!overdue && endTime <= Calendar.getInstance()){
            overdue = true
        }
    }
}

