package com.github.rahul_gill.collegespace.domain.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@JsonClass(generateAdapter = true)
data class AttendanceRecord(
    var totalClassesUpToNow: Int = 0,
    var attended: Int = 0,
    var cancelled: Int = 0,
    var totalClasses: Int? = null,
): Parcelable