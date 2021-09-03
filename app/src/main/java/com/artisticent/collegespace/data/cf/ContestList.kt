package com.artisticent.collegespace.data.cf

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ContestList(
    var result: List<Contest>,
    var status: String
)