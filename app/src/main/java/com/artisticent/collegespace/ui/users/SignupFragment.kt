package com.artisticent.collegespace.ui.users

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withCreated
import androidx.navigation.fragment.findNavController
import com.artisticent.collegespace.R
import com.artisticent.collegespace.databinding.FragmentSignupBinding
import com.artisticent.collegespace.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@AndroidEntryPoint
class SignupFragment : Fragment() {
    private val viewModel: UserViewModel by viewModels()
    private lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding : FragmentSignupBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_signup,container,false)
        binding.goToLogin.apply {
            paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
        }

        auth = Firebase.auth
        binding.signupButton.setOnClickListener {
            it.isClickable = false
            Timber.i("debug: button not  clickable now")
            val username = binding.userName.text.toString()
            val email = binding.signupEmail.text.toString()
            val password = binding.signupPass.text.toString()
            val confirmPass = binding.signupConfirmPass.text.toString()


            if(password != confirmPass){
                Toast.makeText(requireContext(), "Password didn't match.",Toast.LENGTH_SHORT).show()
            }else if(email.isBlank() || password.isBlank() || username.isBlank()){
                Toast.makeText(requireContext(), "Fields can't be empty",Toast.LENGTH_SHORT).show()
            }else{
                lifecycleScope.launch {
                    try {
                        viewModel.signup(username, email, password)
                        val uid = viewModel.login(email, password)
                        viewModel.createUser(uid,username)
                        Timber.i("debug: signup login finish")
                        navigateMainActivity()
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                            Timber.i("debug: button clickable now")
                            it.isClickable = true
                        }
                    }
                }
            }

        }

        binding.goToLogin.setOnClickListener {
            findNavController().navigate(SignupFragmentDirections.actionSignupFragment2ToLoginFragment2())
        }


        return binding.root
    }
    private fun navigateMainActivity() {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("navigation-dest","edit-fragment")
        startActivity(intent)
        requireActivity().finish()
    }

}