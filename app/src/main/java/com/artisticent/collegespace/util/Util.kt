package com.artisticent.collegespace.util

import java.text.SimpleDateFormat
import java.util.*

fun secondsToHourString(seconds: Int): String{
    return "${seconds/3600}:${(seconds%3600)/60}"
}

fun dateFormatter(epoch: Long): String {
    val date = Date(epoch * 1000L)
    val sdf = SimpleDateFormat("hh:mm aaa EEE d MMM ",Locale.ENGLISH)
    return sdf.format(date)
}