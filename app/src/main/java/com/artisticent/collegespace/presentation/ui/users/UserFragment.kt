package com.artisticent.collegespace.presentation.ui.users

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.artisticent.collegespace.R
import com.artisticent.collegespace.databinding.FragmentUserBinding
import com.artisticent.collegespace.presentation.ui.LoginSignupActivity
import com.artisticent.collegespace.presentation.viewmodels.UserViewModel
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserFragment : Fragment() {
    lateinit var binding : FragmentUserBinding
    private val viewModel: UserViewModel by viewModels()
    private lateinit var adapter: GroupListAdapter
    private var showGroups = false
    private var showClasses = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_user, container, false)
        adapter = GroupListAdapter {
            findNavController().navigate(UserFragmentDirections.actionUserFragmentToFeedFragment(it.user_group_id))
        }
        val dummyAdapter = GroupListAdapter {
            findNavController().navigate(UserFragmentDirections.actionUserFragmentToFeedFragment(it.user_group_id))
        }
        binding.classRecyclerView.adapter = dummyAdapter
        binding.groupRecyclerView.adapter = adapter

        lifecycleScope.launch {
            val user = viewModel.getCurrentUser()
            binding.userName.text = user.name
            binding.about.text = user.about
            if(user.userImg.isNotBlank())
                Glide.with(binding.root)
                    .load(user.userImg)
                    .into(binding.userImage)
            val userGroupsList = viewModel.getJoinedUserGroups(user.joinedUserGroups)
            adapter.submitList(userGroupsList)
            dummyAdapter.submitList(userGroupsList)
        }

        binding.editModeButton.setOnClickListener {
            findNavController().navigate(UserFragmentDirections.actionUserFragmentToUserEditFragment())
        }

        binding.showYourGroups.setOnClickListener {
            if(showGroups){
                binding.groupRecyclerView.visibility = View.GONE
                showGroups = false
            }else{
                binding.groupRecyclerView.visibility = View.VISIBLE
                showGroups = true
            }
        }

        binding.showYourClasses.setOnClickListener {
            if(showClasses){
                binding.classRecyclerView.visibility = View.GONE
                showClasses = false
            }else{
                binding.classRecyclerView.visibility = View.VISIBLE
                showClasses = true
            }
        }

        binding.signoutButton.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(requireContext(), LoginSignupActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        return binding.root
    }
}