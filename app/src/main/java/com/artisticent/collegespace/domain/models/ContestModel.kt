package com.artisticent.collegespace.domain.models

import com.squareup.moshi.JsonClass
import java.text.SimpleDateFormat
import java.util.*

@JsonClass(generateAdapter = true)
data class ContestModel(
    var id: Int,
    var name: String,
    var platform: Platform,
    var start_time: Date,
    val status : Status,
){
    enum class Platform{
        CODEFORCES, CODEFORCES_GYM, TOPCODER, ATCODER, CSACADEMY, CODECHEF,
        HACKERRANK, HACKEREARTH, KICKSTART, LEETCODE, TOPH
    }
    enum class Status{
        RUNNING, BEFORE
    }
    fun timeString(): String {
        val sdf = SimpleDateFormat("hh:mm aa EEE dd MMM yyyy", Locale.getDefault())
        return sdf.format(start_time.time)
    }
}

