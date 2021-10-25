package com.artisticent.collegespace.presentation.ui.users

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.artisticent.collegespace.R
import com.artisticent.collegespace.databinding.FragmentLoginBinding
import com.artisticent.collegespace.presentation.ui.MainActivity
import com.artisticent.collegespace.presentation.ui.utils.toast
import com.artisticent.collegespace.presentation.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
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
        binding.forgetPassword.apply {
            paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
        }
        binding.goToSignup.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragment2ToSignupFragment2())
        }
        binding.loginButton.setOnClickListener {
            doLogin(it)
        }
        binding.forgetPassword.setOnClickListener {
            createForgetPasswordDialog()
        }


        return binding.root
    }

    private fun createForgetPasswordDialog(){
        val emailInput = EditText(requireContext())
        emailInput.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        AlertDialog.Builder(requireContext())
            .setTitle("Password Reset")
            .setMessage("Enter the email, to send the password reset link to")
            .setView(emailInput)
            .setPositiveButton("OK"){ _, _ ->
                val resetEmail = emailInput.text.toString()
                lifecycleScope.launch {
                    val wasSuccessful = viewModel.sendPasswordResetToEmail(resetEmail)
                    if(wasSuccessful) toast(requireContext(), "The reset link was sent successfully.")
                    else toast(requireContext(), "Reset link send was unsuccessful.")
                }
            }
            .setNegativeButton("Cancel"){ dialog,_ ->
                dialog.cancel()
            }
            .show()
    }

    private fun doLogin(button: View){
        button.isClickable = false
        val email = binding.emailInput.text.toString()
        val password = binding.passwordInput.text.toString()
        if (email.isBlank() || password.isBlank()) {
            toast(requireContext(),"Email/Password can't be empty")
        }
        lifecycleScope.launch {
            try {
                viewModel.login(email, password)
                navigateMainActivity()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    toast(requireContext(), e.message)
                    button.isClickable = true
                }
            }
        }
    }

    private fun navigateMainActivity() {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

}