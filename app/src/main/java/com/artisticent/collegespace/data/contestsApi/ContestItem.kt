package com.artisticent.collegespace.data.contestsApi

import com.artisticent.collegespace.domain.models.ContestModel
import com.squareup.moshi.JsonClass
import java.text.SimpleDateFormat
import java.util.*


@JsonClass(generateAdapter = true)
data class ContestItem(
    val duration: String,
    val end_time: String,
    val in_24_hours: String,
    val name: String,
    val site: String,
    val start_time: String,
    val status: String,
    val url: String
){
    fun toContestModel() : ContestModel{
        val startTimeModified = start_time
        val startTime = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss.000'Z'", Locale.getDefault())
        val sdf2 = SimpleDateFormat("yyyy-MM-dd kk:mm:ss z", Locale.getDefault())
        try{
            startTime.time = sdf.parse(startTimeModified)!!
        }catch (e: Exception){
            startTime.time = sdf2.parse(startTimeModified)!!
        }
        return ContestModel(
            name.hashCode(),
            name,
            when(site){
                "CodeForces"      -> ContestModel.Platform.CODEFORCES
                "CodeForces::Gym" -> ContestModel.Platform.CODEFORCES_GYM
                "TopCoder"        -> ContestModel.Platform.TOPCODER
                "AtCoder"         -> ContestModel.Platform.ATCODER
                "CS Academy"      -> ContestModel.Platform.CSACADEMY
                "CodeChef"        -> ContestModel.Platform.CODECHEF
                "HackerRank"      -> ContestModel.Platform.HACKERRANK
                "HackerEarth"     -> ContestModel.Platform.HACKEREARTH
                "Kick Start"      -> ContestModel.Platform.KICKSTART
                "LeetCode"        -> ContestModel.Platform.LEETCODE
                else              -> ContestModel.Platform.TOPH
                      },
            startTime.time,
            when(status){
                "CODING" -> ContestModel.Status.RUNNING
                else     -> ContestModel.Status.BEFORE
            }
        )
    }
}

