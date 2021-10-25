package com.artisticent.collegespace.presentation.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artisticent.collegespace.domain.FirebaseRepository
import com.artisticent.collegespace.domain.models.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(val repository: FirebaseRepository): ViewModel() {
    val userLoggedIn: Boolean
        get() = repository.userLoggedIn()

    suspend fun getCurrentUser(): UserModel {
        return repository.getLoggedInUser()
    }
    private fun setCurrentUser(value: UserModel){
        viewModelScope.launch {
            repository.updateUser(value)
        }
    }

    suspend fun login(email: String,password: String): String {
        val res = viewModelScope.async {
            repository.login(email, password)
        }
        return res.await()
    }
    suspend fun signup(username: String, email: String, password: String) {
        val res = viewModelScope.async {
            repository.signup(username, email, password)
        }
        return res.await()
    }
    suspend fun createUser(uid: String, username: String){
        withContext(viewModelScope.coroutineContext) {
            repository.createUser(uid,username)
        }
    }

    suspend fun updateUserDetails(name: String, username: String, about: String, imageBitmap: Bitmap? = null){
        withContext(viewModelScope.coroutineContext) {
            val currentUser = getCurrentUser()
            currentUser.apply {
                this.name = name
                this.username = username
                this.about = about
                imageBitmap?.let { this.userImg = repository.uploadUserImage(it).toString() }
                Timber.i("debug: uploaded image url = $userImg")
            }
            setCurrentUser(currentUser)
        }
    }

    suspend fun getJoinedUserGroups(groupNames: List<String>) = repository.getJoinedUserGroups(groupNames)
    suspend fun sendPasswordResetToEmail(email:String) = repository.sendPasswordResetToEmail(email)
}