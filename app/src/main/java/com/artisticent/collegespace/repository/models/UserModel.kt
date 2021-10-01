package com.artisticent.collegespace.repository.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class UserModel(
    var about: String = "",
    var joined: Timestamp = Timestamp.now(),
    @get:PropertyName("joined_user_groups")
    @set:PropertyName("joined_user_groups")
    var joinedUserGroups: List<String> = listOf(),
    var name: String = "",
    @get:PropertyName("user_img")
    @set:PropertyName("user_img")
    var userImg: String = "",
    var username: String = ""
)