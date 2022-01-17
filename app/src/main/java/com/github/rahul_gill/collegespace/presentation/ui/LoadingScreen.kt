package com.github.rahul_gill.collegespace.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.rahul_gill.collegespace.presentation.theme.AppTheme


@Composable
fun LoadingScreen(
    inProgress: Boolean = false,
    message: String = "",
    content: @Composable () -> Unit
) = AppTheme {
    if(inProgress){
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if(message != "") {
                    Text(
                        text = message,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(32.dp)
                    )
                }
                CircularProgressIndicator(
                    modifier = Modifier.height(64.dp).width(64.dp)
                )
            }
        }
    }
    else content()

}