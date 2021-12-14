package com.artisticent.collegespace.util

import android.content.Context
import android.widget.Toast

object Util{
    fun verifyLoginSignupCredentials(
        username: String? = null,
        email: String? = null,
        password: String? = null,
        confirmPassword: String? = null
    ): String{
        return when{
            email != null && email.isBlank() ->
                "Email can't be blank"
            password != null && password.isBlank() ->
                "Password can't be blank"
            username != null && username.isBlank() ->
                "username can't be blank"
            confirmPassword != null && password != confirmPassword ->
                "Password didn't match"
            else -> ""
        }
    }
    fun toast(context: Context, message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}