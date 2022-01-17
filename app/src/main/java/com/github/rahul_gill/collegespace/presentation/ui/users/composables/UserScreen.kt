package com.github.rahul_gill.collegespace.presentation.ui.users.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.github.rahul_gill.collegespace.R
import com.github.rahul_gill.collegespace.domain.models.UserModel
import com.github.rahul_gill.collegespace.presentation.theme.AppTheme

@Composable
fun UserScreen(
    userData: UserModel,
    onProfileEdit: () -> Unit,
    onLogout: () -> Unit
) = AppTheme{

    val painter = rememberImagePainter(
        data = userData.userImg,
        builder = {
            placeholder(drawableResId = R.drawable.ic_user)
            error(drawableResId = R.drawable.ic_user)
            crossfade(true)
        },
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Image(
                    contentDescription = "",
                    modifier = Modifier
                        .padding(8.dp)
                        .size(100.dp)
                        .clip(RoundedCornerShape(percent = 15))
                        .border(2.dp, Color.Black, RoundedCornerShape(percent = 15)),
                    painter = painter
                )
                Column {
                    Text(
                        text = userData.name,
                        modifier = Modifier
                            .padding(top = 16.dp),
                        fontSize = 32.sp
                    )
                    Text(
                        modifier = Modifier
                            .padding(top = 8.dp),
                        text = userData.about,
                        fontSize = 16.sp
                    )
                }
            }
            Divider(
                thickness = 2.dp,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                onClick = { onProfileEdit() }
            ) {
                Text(text = "Edit Profile")
            }

        }
        Button(
            onClick = { onLogout() },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        ) {
                Text(text = "Log out")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UserScreenPreview() = UserScreen(
    userData = UserModel(
        about = "I'm a good man",
        username = "rahul_gill",
        name = "Rahul Gill"
    ),
    onProfileEdit = {},
    onLogout = {}
)