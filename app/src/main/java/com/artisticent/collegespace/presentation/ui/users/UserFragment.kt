package com.artisticent.collegespace.presentation.ui.users

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.artisticent.collegespace.presentation.LoadingScreen
import com.artisticent.collegespace.presentation.ui.LoginSignupActivity
import com.artisticent.collegespace.presentation.viewmodels.UserViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : Fragment() {
    private val viewModel: UserViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val currentUserData by viewModel.currentUserData.observeAsState()
                LoadingScreen(inProgress = currentUserData == null) {
                    UserScreen(
                        userData = currentUserData!!,
                        onProfileEdit = { onProfileEdit() },
                        onLogout = { onLogout() }
                    )
                }
            }
        }
    }

    private fun onProfileEdit(){
        findNavController().navigate(UserFragmentDirections.actionUserFragmentToUserEditFragment())
    }

    private fun onLogout(){
        Firebase.auth.signOut()
        val intent = Intent(requireContext(), LoginSignupActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}