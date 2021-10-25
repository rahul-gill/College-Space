package com.artisticent.collegespace.domain

import android.graphics.Bitmap
import android.net.Uri
import com.artisticent.collegespace.domain.models.PostModel
import com.artisticent.collegespace.domain.models.UserGroupModel
import com.artisticent.collegespace.domain.models.UserModel
import com.bumptech.glide.Glide
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


class FirebaseRepository @Inject constructor(){
    private val auth : FirebaseAuth by lazy { Firebase.auth }
    private val fireStoreDb: FirebaseFirestore by lazy { Firebase.firestore }
    private val storage: FirebaseStorage by lazy{ Firebase.storage }
    fun userLoggedIn(): Boolean { return auth.currentUser != null }

    suspend fun  login(email: String, password: String): String {
        Timber.i("debug: repo2 login")
        return auth.signInWithEmailAndPassword(email, password).await().user?.uid ?: ""
    }
    private suspend fun checkUsernameAvailability(username: String): Boolean{
        Timber.i("debug: repo2 checkUserNameAvailability")
        Timber.i("checking username $username")
        val sameUsername = fireStoreDb.collection("users").whereEqualTo("username",username).get().await()
        return sameUsername.isEmpty
    }

    suspend fun signup(username: String, email: String, password: String){
        Timber.i("debug: repo2 signup")
        val registerUser = auth.createUserWithEmailAndPassword(email, password)
        if(checkUsernameAvailability(username)){
            registerUser.await()
        }else{
                throw ExceptionInInitializerError("Username already exists")
        }
    }


    private var currentUser : UserModel? = null
    suspend fun getLoggedInUser(): UserModel {
        Timber.i("debug: repo2 getLoggedInUser")
        return if(currentUser != null) currentUser!!
        else{
            if(auth.uid == null) throw IllegalAccessException("No user logged in currently")
            val res = fireStoreDb.collection("users").document(auth.uid!!).get().await()
            currentUser = res.toObject(UserModel::class.java)
            Timber.i("$currentUser")
            currentUser!!
        }
    }

    suspend fun createUser(uid: String,username: String){
        Timber.i("debug: repo2 createUser")
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

    suspend fun getJoinedUserGroups(groupNames: List<String>): List<UserGroupModel>{
        val groupsReference = fireStoreDb.collection("user_group").whereIn("user_group_id", groupNames)
        return groupsReference.get().await().toObjects(UserGroupModel::class.java)
    }

    private suspend fun uploadImage(bitmap: Bitmap, storageRef: StorageReference): Uri?{
        Timber.i("debug: repo2 uploadImage")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val res = storageRef.putBytes(data).continueWithTask { task ->
            if(!task.isSuccessful){
                task.exception?.let { throw it }
            }
            storageRef.downloadUrl
        }.await()
        return res
    }

    suspend fun uploadUserImage(bitmap: Bitmap): Uri? {
        Timber.i("debug: repo2 uploadUserImage")
        val userImgRef = storage.reference.child("images/${auth.uid}_profile_img.jpg")
        return uploadImage(bitmap, userImgRef)
    }

    suspend fun createPost(postText: String, postImage: Bitmap?, userGroup: String){
        Timber.i("debug: repo2 createPost")
        var imageUri: Uri? = null
        if(postImage != null) {
            val postImgRef = storage.reference.child("images/${auth.uid}_post_${userGroup}.jpg")
            withContext(Dispatchers.IO) {
                imageUri = uploadImage(postImage, postImgRef)
            }
        }
        val newPost = PostModel(
            author = getLoggedInUser().username,
            imageUrl = imageUri?.toString() ?: "",
            text = postText,
            user_group_id = userGroup
        )
        withContext(Dispatchers.IO) {
            fireStoreDb.collection("posts").add(newPost).await()
        }
    }

    suspend fun getPosts(userGroup: String = ""): List<PostModel>{
        Timber.i("debug: repo2 getPosts")
        return if(userGroup != ""){
            val postsRef = fireStoreDb.collection("posts").whereEqualTo("user_group_id", userGroup).get().await()
            postsRef.toObjects(PostModel::class.java)
        }else{
            //get posts from all joined user_groups
            val user = fireStoreDb.collection("users").document(auth.uid!!).get().await().toObject(
                UserModel::class.java)
            val userGroupList = user?.joinedUserGroups
            if(userGroupList.isNullOrEmpty()){
                listOf<PostModel>()
            }
            else {
                val postsRef =fireStoreDb.collection("posts").whereIn("user_group_id", userGroupList).get().await()
                postsRef.toObjects(PostModel::class.java)
            }
        }
    }

    suspend fun sendPasswordResetToEmail(email: String): Boolean{
        var result = false
        auth.sendPasswordResetEmail(email).addOnSuccessListener { result = true }.await()
        return result
    }

}