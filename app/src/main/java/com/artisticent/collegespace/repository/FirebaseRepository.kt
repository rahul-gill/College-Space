package com.artisticent.collegespace.repository

import com.artisticent.collegespace.repository.models.UserModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class FirebaseRepository @Inject constructor(){
    private val auth : FirebaseAuth by lazy { Firebase.auth }
    private val fireStoreDb: FirebaseFirestore by lazy { Firebase.firestore }

    fun userLoggedIn(): Boolean { return auth.currentUser != null }

    suspend fun  login(email: String, password: String): String {
        return auth.signInWithEmailAndPassword(email, password).await().user?.uid ?: ""
    }
    private suspend fun checkUsernameAvailability(username: String): Boolean{
        Timber.i("checking username $username")
        val sameUsername = fireStoreDb.collection("users").whereEqualTo("username",username).get().await()
        return sameUsername.isEmpty
    }

    suspend fun signup(username: String, email: String, password: String){
        val registerUser = auth.createUserWithEmailAndPassword(email, password)
        if(checkUsernameAvailability(username)){
            registerUser.await()
        }else{
                throw ExceptionInInitializerError("Username already exists")
        }
    }


    private var currentUser : UserModel? = null
    suspend fun getLoggedInUser(): UserModel {
        return if(currentUser != null) currentUser!!
        else{
            if(auth.uid == null) throw IllegalAccessException("No user logged in currently")
            val res = fireStoreDb.collection("users").document(auth.uid!!).get().await()
            currentUser = res.toObject(UserModel::class.java)
            Timber.i("$currentUser")
            return currentUser!!
        }
    }

    suspend fun createUser(uid: String,username: String){
        val newUser = UserModel(username = username, joined = Timestamp.now())
        fireStoreDb.collection("users").document(uid).set(newUser).await()
    }

    suspend fun updateUser(modifiedUser: UserModel){
        //val data = modifiedUser to HashMap::class.java
        val available = (modifiedUser.name == currentUser?.name) || checkUsernameAvailability(modifiedUser.username)
        if(!available) throw ExceptionInInitializerError("Username already exists")
        fireStoreDb.collection("users").document(auth.uid!!).set(modifiedUser).addOnFailureListener {
            throw it
        }
    }


}