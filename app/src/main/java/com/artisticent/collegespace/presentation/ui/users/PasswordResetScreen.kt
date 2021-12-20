package com.artisticent.collegespace.presentation.ui.users

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.artisticent.collegespace.presentation.AppTheme
import com.artisticent.collegespace.util.Util

@ExperimentalComposeUiApi
@Composable
@Preview
fun PasswordResetScreen(
    onSendPasswordResetLink: (email: String) -> Unit = {_ ->},
    onMoveToLoginScreen: () -> Unit = {}
) = AppTheme{
    val context = LocalContext.current
    var email by rememberSaveable { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        Text(
            text = "Password Reset",
            modifier = Modifier.padding(8.dp),
            fontSize = 30.sp
        )
        Text(
            text = "A password reset will be sent to your email.\nEnter your email address.",
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
            style = TextStyle(textAlign = TextAlign.Start)
        )
        OutlinedTextField(
            value = email,
            label = { Text(text = "email") },
            onValueChange = { email = it },
            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }
            ),
            singleLine = true
        )
        Button(
            onClick = {
                val error = Util.verifyLoginSignupCredentials(email = email)
                if(error != "") Util.toast(context, error)
                else onSendPasswordResetLink(email)
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Send Email")
        }

        Text(
            text = "login",
            fontSize = 16.sp,
            style = TextStyle(textDecoration = TextDecoration.Underline),
            modifier = Modifier.clickable(enabled = true) { onMoveToLoginScreen() }
        )
    }
}