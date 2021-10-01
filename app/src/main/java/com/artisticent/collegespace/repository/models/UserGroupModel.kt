package com.artisticent.collegespace.repository.models

import com.google.firebase.Timestamp

data class UserGroupModel(
    var admins: List<String> = listOf(""),
    var created_at: Timestamp = Timestamp.now(),
    var description: String = "",
    var name: String = "",
    var user_group_id: String = "",
    var user_group_img_url: String = ""
)
