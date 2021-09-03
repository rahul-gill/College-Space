package com.artisticent.collegespace.repository.models

enum class Platform{
    CODEFORCES, LEETCODE, CODECHEF, HACKEREARTH, GOOGLE
}

data class ContestModel(
    var id: Int,
    var name: String,
    var platform: Platform,
    var timeString: String
)
