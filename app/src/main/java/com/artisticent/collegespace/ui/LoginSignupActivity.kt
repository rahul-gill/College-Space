package com.artisticent.collegespace.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.artisticent.collegespace.R
import com.artisticent.collegespace.databinding.ActivityLoginSignupBinding

class LoginSignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityLoginSignupBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_login_signup)

    }
}