package com.github.rahul_gill.collegespace.domain.models

import androidx.annotation.Keep
import com.github.rahul_gill.collegespace.R
import com.squareup.moshi.JsonClass
import java.text.SimpleDateFormat
import java.util.*


@Keep
@JsonClass(generateAdapter = true)
data class ContestModel(
    var id: Int,
    var name: String,
    var platform: ContestPlatform,
    var start_time: Date,
    var end_time: Date,
    val status : Status,
){

    enum class Status(val status_string: String) {
        RUNNING("running"), BEFORE("not started")
    }
    fun timeString(): String {
        val sdf = SimpleDateFormat("hh:mm aa EEE dd MMM yyyy", Locale.getDefault())
        return sdf.format(start_time.time)
    }
}

enum class ContestPlatform(val title: String){
    CODEFORCES("codeforces"), TOPCODER("topcoder"), ATCODER("atcoder"), CSACADEMY("csacademy"), CODECHEF("codechef"),
    HACKERRANK("hackerrank"), HACKEREARTH("hackerearth"), KICKSTART("kickstart"), LEETCODE("leetcode"), TOPH("toph")
}

val ContestPlatform.imageId
    get() = when(this){
        ContestPlatform.CODECHEF -> R.drawable.ic_codechef
        ContestPlatform.LEETCODE -> R.drawable.ic_leetcode
        ContestPlatform.CODEFORCES -> R.drawable.ic_codeforces
        ContestPlatform.TOPCODER -> R.drawable.ic_topcoder
        ContestPlatform.KICKSTART -> R.drawable.ic_google
        ContestPlatform.HACKERRANK -> R.drawable.ic_hackerrank
        ContestPlatform.HACKEREARTH -> R.drawable.ic_hackerearth
        ContestPlatform.CSACADEMY -> R.drawable.ic_csacademy
        ContestPlatform.ATCODER -> R.drawable.ic_atcoder
        ContestPlatform.TOPH -> R.drawable.ic_toph
    }
