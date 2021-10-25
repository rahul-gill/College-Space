package com.artisticent.collegespace.domain.models

import com.google.firebase.Timestamp

data class PostModel(
    val author: String = "",
    val createdAt: Timestamp = Timestamp.now(),
    val imageUrl: String = "",
    val text: String = "",
    val upVotes: Int = 0,
    val user_group_id: String = ""
)