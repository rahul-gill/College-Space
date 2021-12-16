package com.artisticent.collegespace.domain.models

import java.time.Duration
import java.util.*


data class Event(
    var eventName: String = "",
    var start: Date,
    var end: Date,
    var repeatPeriod: Duration? = null,
    var description: String? = null,
    var attendanceRecord: AttendanceRecord? = null,
    var className: String? = null,
    var userGroupName: String? = null
)