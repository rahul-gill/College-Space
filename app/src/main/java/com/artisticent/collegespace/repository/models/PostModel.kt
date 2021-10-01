package com.artisticent.collegespace.repository.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.sql.Time

data class PostModel(
    val author: String = "",
    val createdAt: Timestamp = Timestamp.now(),
    val imageUrl: String = "",
    val text: String = "",
    val upVotes: Int = 0,
    val user_group_id: String = ""
)