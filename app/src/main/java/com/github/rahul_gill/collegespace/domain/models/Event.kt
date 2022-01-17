package com.github.rahul_gill.collegespace.domain.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import java.time.Duration
import java.util.*


@Keep
@Parcelize
data class Event(
    var eventName: String = "",
    var start: Date,
    var end: Date,
    var repeatPeriod: Duration? = null,
    var description: String? = null,
    var attendanceRecord: AttendanceRecord? = null,
    var className: String? = null,
    var userGroupName: String? = null
): Parcelable