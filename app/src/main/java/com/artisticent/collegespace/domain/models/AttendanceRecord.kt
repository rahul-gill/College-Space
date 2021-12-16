package com.artisticent.collegespace.domain.models

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class AttendanceRecord(
    var totalClassesUpToNow: Int = 0,
    var attended: Int = 0,
    var cancelled: Int = 0,
    var totalClasses: Int? = null,
)