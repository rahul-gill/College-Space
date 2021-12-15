package com.artisticent.collegespace.presentation.ui.users

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.artisticent.collegespace.presentation.LoadingScreen
import com.artisticent.collegespace.presentation.ui.MainActivity
import com.artisticent.collegespace.presentation.viewmodels.UserViewModel
import com.artisticent.collegespace.util.Util
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        if (viewModel.userLoggedIn) {
            navigateMainActivity()
        }

        return ComposeView(requireContext()).apply{
            setContent {
                var isLoading by rememberSaveable { mutableStateOf(false) }
                var forgetPasswordState by rememberSaveable { mutableStateOf(false) }
                if(forgetPasswordState){
                    PasswordResetScreen(
                        onSendPasswordResetLink = { email -> sendPasswordResetLink(email) },
                        onMoveToLoginScreen = { forgetPasswordState = false }
                    )
                }
                else LoadingScreen(inProgress = isLoading, message = "Logging in") {
                    LoginScreen(
                        onMoveToSignupScreen = { onMoveToSignupScreen() },
                        onForgetPassword = { forgetPasswordState = true },
                        onLogin = { email, password ->
                            val error = Util.verifyLoginSignupCredentials(email = email,password = password)
                            if (error != "") Util.toast(context, error)
                            else lifecycleScope.launch{
                                isLoading = true
                                try {
                                    viewModel.login(email, password)
                                    navigateMainActivity()
                                } catch (e: Exception) {
                                    withContext(Dispatchers.Main) {
                                        Util.toast(requireContext(), e.message)
                                        isLoading = false
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    private fun onMoveToSignupScreen(){
        findNavController().navigate(LoginFragmentDirections.actionLoginFragment2ToSignupFragment2())
    }

    private fun sendPasswordResetLink(resetEmail: String) = lifecycleScope.launch {
        val wasSuccessful = viewModel.sendPasswordResetToEmail(resetEmail)
        if(wasSuccessful) Util.toast(requireContext(), "The reset link was sent successfully.")
        else Util.toast(requireContext(), "Some error occurred.")
    }


    private fun navigateMainActivity() {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

}