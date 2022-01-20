package com.github.rahul_gill.collegespace.data

import android.graphics.Bitmap
import android.net.Uri
import com.github.rahul_gill.collegespace.domain.FirebaseRepository
import com.github.rahul_gill.collegespace.domain.models.Event
import com.github.rahul_gill.collegespace.domain.models.UserGroupModel
import com.github.rahul_gill.collegespace.domain.models.UserModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.ByteArrayOutputStream
import javax.inject.Inject


class FirebaseRepositoryImpl @Inject constructor(): FirebaseRepository{
    private val auth : FirebaseAuth by lazy { Firebase.auth }
    private val fireStoreDb: FirebaseFirestore by lazy { Firebase.firestore }
    private val storage: FirebaseStorage by lazy{ Firebase.storage }
    override fun userLoggedIn(): Boolean { return auth.currentUser != null }

    override suspend fun  login(email: String, password: String): String {
        Timber.i("debug: repo2 login")
        return auth.signInWithEmailAndPassword(email, password).await().user?.uid ?: ""
    }
    private suspend fun checkUsernameAvailability(username: String): Boolean{
        Timber.i("debug: repo2 checkUserNameAvailability")
        Timber.i("checking username $username")
        val sameUsername = fireStoreDb.collection("users").whereEqualTo("username",username).get().await()
        return sameUsername.isEmpty
    }

    override suspend fun signup(username: String, email: String, password: String){
        Timber.i("debug: repo2 signup")
        val registerUser = auth.createUserWithEmailAndPassword(email, password)
        if(checkUsernameAvailability(username)){
            registerUser.await()
        }else{
            throw ExceptionInInitializerError("Username already exists")
        }
    }


    private var currentUser : UserModel? = null
    override suspend fun getLoggedInUser(): UserModel? {
        Timber.i("debug: repo2 getLoggedInUser")
        return if(currentUser != null) currentUser!!
        else{
            if(auth.uid == null) return null
            val res = fireStoreDb.collection("users").document(auth.uid!!).get().await()
            currentUser = res.toObject(UserModel::class.java)
            Timber.i("$currentUser")
            currentUser!!
        }
    }

    override suspend fun createUser(uid: String, username: String){
        Timber.i("debug: repo2 createUser")
        val newUser = UserModel(username = username, joined = Timestamp.now())
        fireStoreDb.collection("users").document(uid).set(newUser).await()
    }

    override suspend fun updateUser(modifiedUser: UserModel){
        //val data = modifiedUser to HashMap::class.java
        val available = (modifiedUser.name == currentUser?.name) || checkUsernameAvailability(modifiedUser.username)
        if(!available) throw ExceptionInInitializerError("Username already exists")
        fireStoreDb.collection("users").document(auth.uid!!).set(modifiedUser).addOnFailureListener {
            throw it
        }
    }

    override suspend fun getJoinedUserGroups(groupNames: List<String>): List<UserGroupModel>{
        if(groupNames.isEmpty()) return listOf()
        val groupsReference = fireStoreDb.collection("user_group").whereIn("user_group_id", groupNames)
        return groupsReference.get().await().toObjects(UserGroupModel::class.java)
    }

    private suspend fun uploadImage(bitmap: Bitmap, storageRef: StorageReference): Uri? {
        Timber.i("debug: repo2 uploadImage")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        return storageRef.putBytes(data).continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let { throw it }
            }
            storageRef.downloadUrl
        }.await()
    }

    override suspend fun uploadUserImage(bitmap: Bitmap): Uri? {
        Timber.i("debug: repo2 uploadUserImage")
        val userImgRef = storage.reference.child("images/${auth.uid}_profile_img.jpg")
        return uploadImage(bitmap, userImgRef)
    }

    override suspend fun createPost(event: Event){
        Timber.i("debug: repo2 createPost")
        withContext(Dispatchers.IO) {
            fireStoreDb.collection("posts").add(event).await()
        }
    }

    override suspend fun getPosts(userGroup: String): List<Event>{
        Timber.i("debug: repo2 getPosts")
        return if(userGroup != ""){
            val postsRef = fireStoreDb.collection("posts").whereEqualTo("userGroupName", userGroup).get().await()
            postsRef.toObjects(Event::class.java)
        }else{
            //get posts from all joined user_groups
            val user = fireStoreDb.collection("users").document(auth.uid!!).get().await().toObject(
                UserModel::class.java)
            Timber.i("debug: repo2 getPosts $user")
            val userGroupList = user?.joinedUserGroups
            if(userGroupList.isNullOrEmpty()){
                listOf()
            }
            else {
                val postsRef =fireStoreDb.collection("posts").whereIn("user_group_id", userGroupList).get().await()
                Timber.i("debug: repo2 getPosts $postsRef ")
                postsRef.toObjects(Event::class.java)
            }
        }.also { Timber.i("debug: repo2 getPosts $it ") }
    }

    override suspend fun sendPasswordResetToEmail(email: String): Boolean{
        var result = false
        auth.sendPasswordResetEmail(email).addOnSuccessListener { result = true }.await()
        return result
    }

}