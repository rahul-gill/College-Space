package com.github.rahul_gill.collegespace.presentation.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.rahul_gill.collegespace.domain.FirebaseRepository
import com.github.rahul_gill.collegespace.domain.models.PostModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostsViewModel  @Inject constructor(private val repository: FirebaseRepository) : ViewModel(){

    suspend fun createPost(postText: String, postImage: Bitmap?, userGroup: String) = repository.createPost(
        postText,
        postImage,
        userGroup
    )
    var posts = MutableLiveData(listOf<PostModel>())
    val isRefreshing = MutableLiveData(false)
    fun getPosts(userGroup: String = ""){
        viewModelScope.launch {
            isRefreshing.value = true
            posts.value = repository.getPosts(userGroup)
            isRefreshing.value = false
        }
    }
}