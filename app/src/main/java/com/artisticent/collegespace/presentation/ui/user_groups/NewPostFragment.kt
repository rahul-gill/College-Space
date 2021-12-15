package com.artisticent.collegespace.presentation.ui.user_groups

import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.artisticent.collegespace.R
import com.artisticent.collegespace.databinding.FragmentNewPostBinding
import com.artisticent.collegespace.presentation.viewmodels.PostsViewModel
import com.artisticent.collegespace.util.Util
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewPostFragment : Fragment() {
    private lateinit var binding: FragmentNewPostBinding
    private val args: NewPostFragmentArgs by navArgs()
    val viewModel: PostsViewModel by viewModels()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_post, container, false)

        binding.addImageButton.setOnClickListener {
            selectPostImage()
            it.visibility = View.GONE
            binding.newPostImage.visibility = View.VISIBLE
        }
        binding.newPostImage.setOnClickListener {
            selectPostImage()
        }

        binding.postText.addTextChangedListener {
            val length = it!!.length
            val charCountView = binding.characterCount
            charCountView.text =(1000 -length).toString()
            if(length == 1000)
                charCountView.setTextColor(Color.parseColor("#FF0000"))
        }

        val userGroup = args.userGroup
        binding.postFinishButton.setOnClickListener {
            val postText = binding.postText.text.toString()
            val postImage = (binding.newPostImage.drawable as BitmapDrawable?)?.bitmap
            try {
                lifecycleScope.launch {
                    setupVisibility(posting = true)
                    viewModel.createPost(postText, postImage, userGroup)
                }.invokeOnCompletion {
                    findNavController().navigate(NewPostFragmentDirections.actionNewPostFragmentToFeedFragment(userGroup))
                }
            }catch (e: Exception){
                setupVisibility(posting = false)
                Util.toast(requireContext(), e.message)
            }
        }
        return binding.root
    }

    private fun setupVisibility(posting: Boolean = true){
        val loading = if(posting) View.VISIBLE else View.GONE
        val others = if(posting) View.GONE else View.VISIBLE
        binding.loadingBar.visibility = loading
        binding.postText.visibility = others
        binding.postFinishButton.visibility = others
        binding.newPostImage.visibility = others
        binding.addImageButton.visibility = others
    }


    private val getImageContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
        val imageUri = it
        binding.newPostImage.setImageURI(imageUri)
    }
    private fun selectPostImage(){
        getImageContent.launch("image/*")
    }
}