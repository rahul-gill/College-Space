package com.artisticent.collegespace.presentation.ui.users

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.rememberImagePainter
import com.artisticent.collegespace.presentation.AppTheme

@Composable
fun UserEditScreen(
    onUpdateDetails: (name: String, description: String) -> Unit,
    onImageClick: () -> Unit,
    userImageUrl: String?,
    localImageUri: Uri? = null
) = AppTheme{
    var name by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){}

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        val painter = rememberImagePainter(
            data = when {
                localImageUri != null -> localImageUri
                userImageUrl.isNullOrBlank() -> ""
                else -> userImageUrl
            },
            builder ={
                placeholder(drawableResId = com.artisticent.collegespace.R.drawable.ic_user)
                error(drawableResId = com.artisticent.collegespace.R.drawable.ic_user)
                crossfade(true)
            },
        )
        Image(
            contentDescription = "",
            modifier = Modifier
                .padding(top = 64.dp)
                .size(100.dp)
                .clip(RoundedCornerShape(percent = 15))
                .border(2.dp, Color.Black, RoundedCornerShape(percent = 15))
                .clickable {
                    when (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )) {
                        PackageManager.PERMISSION_GRANTED -> {
                            onImageClick()
                        }
                        else -> {
                            launcher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }
                    }
                },
            painter = painter
        )


        OutlinedTextField(
            value = name,
            label = { Text(text = "your name") },
            onValueChange = { name = it },
            maxLines = 1,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp)
        )
        OutlinedTextField(
            value = description,
            label = { Text(text = "about you") },
            onValueChange = { description = it },
            modifier = Modifier.padding(start = 8.dp, end = 8.dp)
        )
        Button(
            modifier = Modifier.padding(16.dp),
            onClick = { onUpdateDetails(name, description) }
        ) {
            Text("Done")
        }
    }
}
