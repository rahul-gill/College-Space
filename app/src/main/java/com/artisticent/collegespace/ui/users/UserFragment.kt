package com.artisticent.collegespace.ui.users

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.artisticent.collegespace.R
import com.artisticent.collegespace.databinding.FragmentUserBinding
import com.artisticent.collegespace.repository.models.UserGroupModel
import com.artisticent.collegespace.repository.models.UserModel
import com.artisticent.collegespace.ui.LoginSignupActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class UserFragment : Fragment() {
    lateinit var binding : FragmentUserBinding
    private lateinit var auth : FirebaseAuth
    lateinit var fireStoreDb: FirebaseFirestore
    private lateinit var adapter: GroupListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_user, container, false)
        adapter = GroupListAdapter {
            findNavController().navigate(UserFragmentDirections.actionUserFragmentToFeedFragment(it.user_group_id))
        }
        binding.groupRecyclerView.adapter = adapter
        auth = Firebase.auth
        fireStoreDb = Firebase.firestore


        val userRef = fireStoreDb.collection("users").document(auth.uid!!)
        userRef.addSnapshotListener{ snapshot, exception ->
            if(exception != null || snapshot == null){
                Timber.i("Firestore error for loading user document")
                return@addSnapshotListener
            }
            val user = snapshot.toObject(UserModel::class.java)
            Glide.with(binding.root).load(user?.userImg).into(binding.userImage)
            binding.userName.text = user?.name
            binding.about.text = user?.about
        }


        val groupsReference = fireStoreDb.collection("user_group")
        groupsReference.addSnapshotListener{ snapshot,exception ->
            if(exception != null || snapshot == null){
                Timber.i("Firestore error for loading collection user_group")
                return@addSnapshotListener
            }
            val groupsList = snapshot.toObjects(UserGroupModel::class.java)
            adapter.submitList(groupsList)
        }

        binding.editModeButton.setOnClickListener{
            findNavController().navigate(UserFragmentDirections.actionUserFragmentToUserEditFragment())
        }
        binding.signoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(requireContext(), LoginSignupActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        return binding.root
    }


}