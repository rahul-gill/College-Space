package com.github.rahul_gill.collegespace.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.github.rahul_gill.collegespace.R
import com.github.rahul_gill.collegespace.databinding.ActivityLoginSignupBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginSignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityLoginSignupBinding>(this, R.layout.activity_login_signup)

    }
}