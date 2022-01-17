package com.github.rahul_gill.collegespace.domain.models

import androidx.annotation.Keep
import com.alamkanak.weekview.WeekViewEvent
import java.util.*


@Keep
class EventModelOld(var id: Int = Int.MIN_VALUE) : WeekViewEvent(){
    private var overdue = false

    fun checkAndUpdateOverdueStatus(){
        if(!overdue && endTime <= Calendar.getInstance()){
            overdue = true
        }
    }
}

