package com.artisticent.collegespace.ui.users

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.artisticent.collegespace.R
import com.artisticent.collegespace.databinding.FragmentUserBinding
import com.artisticent.collegespace.ui.LoginSignupActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UserFragment : Fragment() {
    lateinit var binding : FragmentUserBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_user, container, false)

        val auth = Firebase.auth

        val userName = auth.currentUser?.displayName
        val userEmail =auth.currentUser?.email
        binding.userEmail.text = userEmail
        binding.userName.text = userName
        binding.signoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(requireContext(), LoginSignupActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        return binding.root
    }

}