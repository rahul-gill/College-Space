package com.artisticent.collegespace.ui.users

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.artisticent.collegespace.R
import com.artisticent.collegespace.databinding.FragmentUserEditBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

@AndroidEntryPoint
class UserEditFragment : Fragment() {
    lateinit var binding: FragmentUserEditBinding
    val viewModel : UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_edit, container, false)

        val currentUserDeferred = lifecycleScope.async { viewModel.getCurrentUser() }
        Timber.i("debug: a")
        lifecycleScope.launch(Dispatchers.Main) {
            Timber.i("debug: b1")
            val currentUser = currentUserDeferred.await()
            Glide.with(requireContext()).load(currentUser.userImg).into(binding.userImage)
            binding.about.setText(currentUser.about)
            binding.userRealName.setText(currentUser.name)
            binding.username.setText(currentUser.username)
        }
        binding.doneButton.setOnClickListener {
            try {
                viewModel.updateUserDetails(
                    binding.userRealName.text.toString(),
                    binding.username.text.toString(),
                    binding.about.text.toString()
                )
                findNavController().navigate(UserEditFragmentDirections.actionUserEditFragmentToUserFragment())
            }catch (e: Exception){
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
            }
        }

        return binding.root
    }

}