package com.artisticent.collegespace.ui.user_groups

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.artisticent.collegespace.R
import com.artisticent.collegespace.databinding.FragmentFeedBinding
import com.artisticent.collegespace.repository.models.PostModel
import com.artisticent.collegespace.repository.models.UserModel
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class FeedFragment : Fragment() {
    private lateinit var binding : FragmentFeedBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var fireStoreDb : FirebaseFirestore
    private lateinit var adapter: FeedListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =  DataBindingUtil.inflate(inflater,R.layout.fragment_feed, container, false)
        adapter = FeedListAdapter()
        binding.postList.adapter = adapter

        val userGroup = FeedFragmentArgs.fromBundle(requireArguments()).userGroup
        fireStoreDb = Firebase.firestore
        auth = Firebase.auth

        getPosts()

        return binding.root
    }


    private fun getPosts(userGroup: String = ""){
        if(userGroup != ""){
            val postsRef = fireStoreDb.collection("posts").whereEqualTo("user_group_id", userGroup)
            postsRef.addSnapshotListener{ snapshot, exception ->
                if(exception != null || snapshot == null){
                    Timber.i("Firestore error for loading collection user_group")
                    return@addSnapshotListener
                }
                val postList = snapshot.toObjects(PostModel::class.java)
                adapter.submitList(postList)
            }
        }else{
            //get posts from all joined user_groups
            lifecycleScope.launch(Dispatchers.Main) {
                val user = fireStoreDb.collection("users").document(auth.uid!!).get().await().toObject(UserModel::class.java)
                val userGroupList = user?.joinedUserGroups
                if(userGroupList?.isEmpty() == true) adapter.submitList(listOf())
                else{
                    val postRef = fireStoreDb.collection("posts").whereIn("user_group_id", userGroupList!!).get().await()
                    val postList = postRef?.toObjects(PostModel::class.java)
                    adapter.submitList(postList)
                }
            }
        }
    }
}