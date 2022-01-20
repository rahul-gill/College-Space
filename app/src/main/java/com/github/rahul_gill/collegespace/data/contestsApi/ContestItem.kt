package com.github.rahul_gill.collegespace.data.contestsApi

import com.github.rahul_gill.collegespace.domain.models.ContestModel
import com.github.rahul_gill.collegespace.domain.models.ContestPlatform
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
        val startTime = Calendar.getInstance()
        val endTime = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss.000'Z'", Locale.getDefault())
        val sdf2 = SimpleDateFormat("yyyy-MM-dd kk:mm:ss z", Locale.getDefault())
        try{
            startTime.time = sdf.parse(start_time)!!
        }catch (e: Exception){
            startTime.time = sdf2.parse(start_time)!!
        }
        try{
            endTime.time = sdf.parse(end_time)!!
        }catch (e: Exception){
            startTime.time = sdf2.parse(end_time)!!
        }
        return ContestModel(
            name.hashCode(),
            name,
            when(site){
                "CodeForces"      -> ContestPlatform.CODEFORCES
                "CodeForces::Gym" -> ContestPlatform.CODEFORCES
                "TopCoder"        -> ContestPlatform.TOPCODER
                "AtCoder"         -> ContestPlatform.ATCODER
                "CS Academy"      -> ContestPlatform.CSACADEMY
                "CodeChef"        -> ContestPlatform.CODECHEF
                "HackerRank"      -> ContestPlatform.HACKERRANK
                "HackerEarth"     -> ContestPlatform.HACKEREARTH
                "Kick Start"      -> ContestPlatform.KICKSTART
                "LeetCode"        -> ContestPlatform.LEETCODE
                else              -> ContestPlatform.TOPH
                      },
            startTime.time,
            endTime.time,
            when(status){
                "CODING" -> ContestModel.Status.RUNNING
                else     -> ContestModel.Status.BEFORE
            }
        )
    }
}

