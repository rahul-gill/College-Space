package com.github.rahul_gill.collegespace.presentation.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.github.rahul_gill.collegespace.util.InternetConnectivityInfo


@Composable
fun ConnectivitySnack(){
    val connected2 = InternetConnectivityInfo(LocalContext.current.applicationContext).observeAsState()
    var snackBarState by remember { mutableStateOf(false) }
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = if (connected2.value == true) Color.Green else Color.Red )){
                append("second")
            }
        },
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
    if(connected2.value == true) {
        snackBarState = true
        Snackbar(
            action = {
                Button(onClick = { snackBarState = false }) {
                    Text("dismiss")
                }
            },
            modifier = Modifier.padding(8.dp)
        ) { Text(text = if (connected2.value == true) "back online" else "network connected lost") }
    }
}