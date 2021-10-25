package com.artisticent.collegespace.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.artisticent.collegespace.R
import com.artisticent.collegespace.databinding.ActivityLoginSignupBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LoginSignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.i("debug: LoginSignupActivity created")
        super.onCreate(savedInstanceState)
        val binding : ActivityLoginSignupBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_login_signup)

    }
}