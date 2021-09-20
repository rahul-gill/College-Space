package com.artisticent.collegespace.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.artisticent.collegespace.R
import com.artisticent.collegespace.databinding.FragmentSignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignupFragment : Fragment() {

    private lateinit var auth : FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding : FragmentSignupBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_signup,
            container,
            false)


        auth = Firebase.auth
        binding.signupButton.setOnClickListener {
            it.isClickable = false
            val email = binding.signupEmail.text
            val password = binding.signupPass.text
            val confirmPass = binding.signupConfirmPass.text
            if(password.toString() != confirmPass.toString()){
                Toast.makeText(requireContext(), "Password didn't match.",Toast.LENGTH_SHORT).show()
            }else{
                signup(email.toString(), password.toString())
            }
            it.isClickable = true
        }

        binding.goToLogin.setOnClickListener {
            findNavController().navigate(SignupFragmentDirections.actionSignupFragment2ToLoginFragment2())
        }


        return binding.root
    }

    private fun signup(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Signup Successful.", Toast.LENGTH_SHORT)
                        .show()
                    findNavController().navigate(SignupFragmentDirections.actionSignupFragment2ToLoginFragment2())
                } else {
                    Toast.makeText(requireContext(), "Signup Failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

}