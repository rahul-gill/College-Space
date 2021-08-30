package com.artisticent.collegespace.remote.apis

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Contest(
    var durationSeconds: Int,
    var frozen: Boolean,
    var id: Int,
    var name: String,
    var phase: String,
    var relativeTimeSeconds: Long,
    var startTimeSeconds: Long,
    var type: String
)