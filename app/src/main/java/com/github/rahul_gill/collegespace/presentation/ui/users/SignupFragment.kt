package com.github.rahul_gill.collegespace.presentation.ui.users

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.rahul_gill.collegespace.presentation.ui.LoadingScreen
import com.github.rahul_gill.collegespace.presentation.ui.MainActivity
import com.github.rahul_gill.collegespace.presentation.ui.users.composables.SignupScreen
import com.github.rahul_gill.collegespace.presentation.viewmodels.UserViewModel
import com.github.rahul_gill.collegespace.util.Util
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SignupFragment : Fragment() {
    private val viewModel: UserViewModel by viewModels()

    @ExperimentalComposeUiApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                var isLoading by rememberSaveable { mutableStateOf(false) }
                LoadingScreen(inProgress = isLoading, message = "Signing you up") {
                    SignupScreen (
                        onMoveToLoginScreen = { onMoveToLoginScreen() },
                        onSignup = { username, email, password, passwordConfirm ->
                            val error = Util.verifyLoginSignupCredentials(username,email,password,passwordConfirm)
                            if (error != "") Util.toast(context, error)
                            else lifecycleScope.launch{
                                isLoading = true
                                try {
                                    viewModel.signup(username, email, password)
                                    val uid = viewModel.login(email, password)
                                    viewModel.createUser(uid, username)
                                    navigateMainActivity()
                                } catch (e: Exception) {
                                    withContext(Dispatchers.Main) {
                                        Util.toast(requireContext(), e.message)
                                    }
                                    isLoading = false
                                }
                            }
                        }
                    )
                }
            }
        }
    }
    private fun navigateMainActivity() {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("navigation-dest","edit-fragment")
        startActivity(intent)
        requireActivity().finish()
    }
    private fun onMoveToLoginScreen(){
        findNavController().navigate(SignupFragmentDirections.actionSignupFragment2ToLoginFragment2())
    }
}