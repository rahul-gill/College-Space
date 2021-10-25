package com.artisticent.collegespace.presentation.ui.users

import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.artisticent.collegespace.R
import com.artisticent.collegespace.databinding.FragmentUserEditBinding
import com.artisticent.collegespace.presentation.ui.utils.toast
import com.artisticent.collegespace.presentation.viewmodels.UserViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class UserEditFragment : Fragment() {
    lateinit var binding: FragmentUserEditBinding
    val viewModel : UserViewModel by viewModels()
    private var imageUri: Uri? = null
    var userImageChanged = false

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

        binding.userImage.setOnClickListener{
            try{
                getImageContent.launch("image/*")
            }catch (e: Exception){
                toast(requireContext(), e.message)
            }
        }

        binding.doneButton.setOnClickListener {
            try {
                lifecycleScope.launch {
                    val name = binding.userRealName.text.toString()
                    val username = binding.username.text.toString()
                    val about = binding.about.text.toString()
                    val userImageBitmap = if(userImageChanged) (binding.userImage.drawable as BitmapDrawable).bitmap else null
                    viewModel.updateUserDetails(
                        name,
                        username,
                        about,
                        userImageBitmap
                    )
                    findNavController().navigate(UserEditFragmentDirections.actionUserEditFragmentToUserFragment())
                }
            }catch (e: Exception){
                toast(requireContext(), e.message)
            }
        }

        return binding.root
    }

    private val getImageContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
        imageUri = it
        binding.userImage.setImageURI(imageUri)
        userImageChanged = true
    }
}