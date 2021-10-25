package com.artisticent.collegespace.domain.models

import java.text.SimpleDateFormat
import java.util.*


data class ContestModel(
    var id: Int,
    var name: String,
    var platform: Platform,
    var start_time: Calendar,
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

