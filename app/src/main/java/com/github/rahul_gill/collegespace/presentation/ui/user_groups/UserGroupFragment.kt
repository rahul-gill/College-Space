package com.github.rahul_gill.collegespace.presentation.ui.user_groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.rahul_gill.collegespace.MainNavGraphDirections
import com.github.rahul_gill.collegespace.presentation.viewmodels.PostsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserGroupFragment : Fragment() {
    val viewModel : PostsViewModel by viewModels()
    private val args: UserGroupFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var userGroup = args.userGroup
        userGroup = "another_group"
        viewModel.getPosts(userGroup)
        return ComposeView(requireContext()).apply {
            setContent {
                FeedScreen(
                    posts = viewModel.posts.observeAsState().value!!,
                    isRefreshing = viewModel.isRefreshing.observeAsState().value!!,
                    onRefresh = { viewModel.getPosts(userGroup) },
                    onCreateNewPost = {
                        findNavController().navigate(UserGroupFragmentDirections.actionFeedFragmentToNewPostFragment(userGroup))
                    },
                    onAddToCalendar = { event ->
                        findNavController().navigate(MainNavGraphDirections.toEventFragment(event))
                    }
                )
            }
        }
    }
}