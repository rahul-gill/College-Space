package com.artisticent.collegespace.presentation.ui.users

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@ExperimentalComposeUiApi
@Preview
@Composable
fun LoginScreen(
    onLogin: (email: String,password: String) -> Unit = {_, _ ->},
    onMoveToSignupScreen: () -> Unit = {},
    onForgetPassword: () -> Unit = {}
){
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisibility by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable { focusManager.clearFocus() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val (focusRequester) =  FocusRequester.createRefs()
        val keyboardController = LocalSoftwareKeyboardController.current
        Text(
            text = "College Space Login",
            modifier = Modifier.padding(8.dp),
            fontSize = 30.sp
        )

        OutlinedTextField(
            value = email,
            label = { Text(text = "email") },
            onValueChange = { email = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusRequester.requestFocus() }
            ),
            singleLine = true
        )

        OutlinedTextField(
            value = password,
            label = { Text(text = "password") },
            onValueChange = { password = it },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }
            ),
            trailingIcon = {
                val image = if (passwordVisibility)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(imageVector  = image, "")
                }
            },
            singleLine = true
        )

        Button(
            modifier = Modifier.padding(16.dp),
            onClick = { onLogin(email,password) }
        ) {
            Text("Login")
        }
        Text(
            text = "signup instead",
            fontSize = 16.sp,
            style = TextStyle(textDecoration = TextDecoration.Underline),
            modifier = Modifier.clickable(enabled = true) { onMoveToSignupScreen() }
        )
        Text(
            text = "forget password",
            fontSize = 16.sp,
            style = TextStyle(textDecoration = TextDecoration.Underline),
            modifier = Modifier.clickable(enabled = true) { onForgetPassword() }
        )
    }

}