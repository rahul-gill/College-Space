package com.artisticent.collegespace.ui.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artisticent.collegespace.repository.FirebaseRepository
import com.artisticent.collegespace.repository.models.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(val repository: FirebaseRepository): ViewModel() {
    val userLoggedIn: Boolean
        get() = repository.userLoggedIn()

    suspend fun getCurrentUser(): UserModel {
        return repository.getLoggedInUser()
    }
    fun setCurrentUser(value: UserModel){
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

    fun updateUserDetails(name: String, username: String, about: String){
        viewModelScope.launch {
            val currentUser = getCurrentUser()
            currentUser.apply {
                this.name = name
                this.username = username
                this.about = about
            }
            setCurrentUser(currentUser)
        }
    }
}