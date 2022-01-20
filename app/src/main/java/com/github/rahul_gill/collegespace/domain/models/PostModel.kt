package com.github.rahul_gill.collegespace.domain.models

import androidx.annotation.Keep
import com.google.firebase.Timestamp


@Keep
data class PostModel(
    val author: String = "",
    val createdAt: Timestamp = Timestamp.now(),
    val imageUrl: String = "",
    val title: String = "",
    val description: String = "",
    val upVotes: Int = 0,
    val user_group_id: String = ""
)