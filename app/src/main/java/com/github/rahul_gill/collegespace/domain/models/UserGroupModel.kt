package com.github.rahul_gill.collegespace.domain.models

import androidx.annotation.Keep
import com.google.firebase.Timestamp


@Keep
data class UserGroupModel(
    var admins: List<String> = listOf(""),
    var created_at: Timestamp = Timestamp.now(),
    var description: String = "",
    var name: String = "",
    var user_group_id: String = "",
    var user_group_img_url: String = ""
)
