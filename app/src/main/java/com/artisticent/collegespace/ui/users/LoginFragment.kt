package com.artisticent.collegespace.ui.users

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.artisticent.collegespace.R
import com.artisticent.collegespace.databinding.FragmentLoginBinding
import com.artisticent.collegespace.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment() {

    private lateinit var auth : FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = Firebase.auth
        val currentUser = auth.currentUser
        if(currentUser != null){
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        val binding : FragmentLoginBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login,
            container,
            false)


        binding.loginButton.setOnClickListener {
            it.isClickable = false
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()
            login(email,password)
            it.isClickable = true
        }



        binding.goToSignup.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragment2ToSignupFragment2())
        }
        return binding.root
    }

    private fun login(email: String,password: String){
        if(email.isBlank() || password.isBlank()){
            Toast.makeText(requireContext(), "Email/Password can't be empty", Toast.LENGTH_SHORT).show()
        }
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }else{
                Toast.makeText(requireContext(), "authentication failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}