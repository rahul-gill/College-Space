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
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@ExperimentalComposeUiApi
@Composable
fun SignupScreen(
    onSignup: (username: String, email: String, password: String, passwordConfirm: String) -> Unit = {_,_,_,_ ->},
    onMoveToLoginScreen: () -> Unit = {}
){
    var username by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordConfirm by rememberSaveable { mutableStateOf("") }
    var passwordVisibility by rememberSaveable { mutableStateOf(false) }
    var passwordConfirmVisibility by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val focusRequesters =  List(3) { FocusRequester() }
        val keyboardController = LocalSoftwareKeyboardController.current

        Text(
            text = "College Space Signup",
            modifier = Modifier.padding(8.dp),
            fontSize = 30.sp
        )

        OutlinedTextField(
            value = username,
            label = { Text(text = "username") },
            onValueChange = { username = it },
            singleLine = true,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusRequesters[0].requestFocus() }
            ),
        )
        OutlinedTextField(
            value = email,
            label = { Text(text = "email") },
            onValueChange = { email = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusRequesters[1].requestFocus() }
            ),
            singleLine = true,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp).focusRequester(focusRequesters[0])
        )

        OutlinedTextField(
            value = password,
            label = { Text(text = "password") },
            onValueChange = { password = it },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusRequesters[2].requestFocus() }
            ),
            trailingIcon = {
                val image = if (passwordVisibility)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(imageVector  = image, "")
                }
            },
            singleLine = true,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp).focusRequester(focusRequesters[1])
        )
        OutlinedTextField(
            value = passwordConfirm,
            label = { Text(text = "confirm password") },
            onValueChange = { passwordConfirm = it },
            visualTransformation = if (passwordConfirmVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }
            ),
            trailingIcon = {
                val image = if (passwordConfirmVisibility)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                IconButton(onClick = { passwordConfirmVisibility = !passwordConfirmVisibility }) {
                    Icon(imageVector  = image, "")
                }
            },
            singleLine = true,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp).focusRequester(focusRequesters[2])
        )

        Button(
            modifier = Modifier.padding(16.dp),
            onClick = {
                onSignup(username, email, password, passwordConfirm)
            }
        ) {
            Text("Signup")
        }
        Text(
            text = "login instead",
            fontSize = 16.sp,
            style = TextStyle(textDecoration = TextDecoration.Underline),
            modifier = Modifier.clickable(enabled = true) { onMoveToLoginScreen() }
        )
    }

}