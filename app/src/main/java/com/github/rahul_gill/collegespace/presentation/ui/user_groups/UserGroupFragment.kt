package com.github.rahul_gill.collegespace.presentation.ui.user_groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.rahul_gill.collegespace.R
import com.github.rahul_gill.collegespace.databinding.FragmentUserGroupBinding
import com.github.rahul_gill.collegespace.presentation.viewmodels.PostsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserGroupFragment : Fragment() {
    private lateinit var binding : FragmentUserGroupBinding
    val viewModel : PostsViewModel by viewModels()
    private lateinit var adapter: FeedListAdapter
    private val args: UserGroupFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =  DataBindingUtil.inflate(inflater,R.layout.fragment_user_group, container, false)
        val userGroup = args.userGroup

        adapter = FeedListAdapter {
            findNavController().navigate(UserGroupFragmentDirections.actionFeedFragmentToNewPostFragment(userGroup))
        }
        binding.postList.adapter = adapter

        val refreshLayout = binding.postsListRefreshLayout
        refreshLayout.setOnRefreshListener {
            viewModel.getPosts(userGroup)
        }
        refreshLayout.isRefreshing = true
        viewModel.getPosts(userGroup)
        viewModel.posts.observe(viewLifecycleOwner){
            if(it.isEmpty() && userGroup.isEmpty()){
                binding.postList.visibility = View.GONE
                binding.emptyListMessage.visibility = View.VISIBLE

            }else {
                adapter.addHeaderAndSubmitList(it, userGroup)
                binding.postList.visibility = View.VISIBLE
                binding.emptyListMessage.visibility = View.GONE
            }
        }
        viewModel.isRefreshing.observe(viewLifecycleOwner){
            refreshLayout.isRefreshing = it
        }
        return binding.root
    }
}