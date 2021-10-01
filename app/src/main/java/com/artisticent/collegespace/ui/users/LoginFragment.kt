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
import com.artisticent.collegespace.databinding.FragmentLoginBinding
import com.artisticent.collegespace.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: UserViewModel by viewModels()
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        if (viewModel.userLoggedIn) {
            navigateMainActivity()
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.goToSignup.apply {
            paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
        }
        binding.goToSignup.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragment2ToSignupFragment2())
        }

        binding.loginButton.setOnClickListener {
            it.isClickable = false
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()
            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(
                    requireContext(),
                    "Email/Password can't be empty",
                    Toast.LENGTH_SHORT
                ).show()
            }
            lifecycleScope.launch {
                try {
                    viewModel.login(email, password)
                    navigateMainActivity()
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "${e.message}", Toast.LENGTH_SHORT).show()
                        it.isClickable = true
                    }
                }
            }
        }



        return binding.root
    }


    private fun navigateMainActivity() {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

}