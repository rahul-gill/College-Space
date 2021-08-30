package com.artisticent.collegespace.util

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

fun secondsToHourString(seconds: Int): String{
    return "${seconds/3600}:${seconds%3600}"
}

fun dateFormatter(epoch: Long): String {
    val date = Date(epoch * 1000L)
    val sdf = SimpleDateFormat("HH:mm dd-MM ")
    return sdf.format(date)
}