package com.artisticent.collegespace.data.cf

import com.artisticent.collegespace.repository.models.ContestModel
import com.artisticent.collegespace.repository.models.Platform
import com.artisticent.collegespace.util.dateFormatter
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

fun Contest.toContestModel(): ContestModel {
    return ContestModel(
        id,
        name,
        Platform.CODEFORCES,
        dateFormatter(startTimeSeconds)
    )
}