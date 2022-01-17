package com.github.rahul_gill.collegespace.domain

import android.graphics.Bitmap
import android.net.Uri
import com.github.rahul_gill.collegespace.domain.models.PostModel
import com.github.rahul_gill.collegespace.domain.models.UserGroupModel
import com.github.rahul_gill.collegespace.domain.models.UserModel


interface FirebaseRepository{
    fun userLoggedIn(): Boolean
    suspend fun login(email: String, password: String): String
    suspend fun signup(username: String, email: String, password: String)
    suspend fun createUser(uid: String,username: String)
    suspend fun updateUser(modifiedUser: UserModel)
    suspend fun getLoggedInUser(): UserModel?
    suspend fun getJoinedUserGroups(groupNames: List<String>): List<UserGroupModel>
    suspend fun uploadUserImage(bitmap: Bitmap): Uri?

    suspend fun createPost(postText: String, postImage: Bitmap?, userGroup: String)
    suspend fun getPosts(userGroup: String = ""): List<PostModel>
    suspend fun sendPasswordResetToEmail(email: String): Boolean
}