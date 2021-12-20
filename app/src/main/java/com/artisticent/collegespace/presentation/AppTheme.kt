package com.artisticent.collegespace.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val DarkColors = darkColors(
    primary = Color(0xff4681B9),
    secondary = Color(0xffB97E46),
)

val LightColors = lightColors(
    primary = Color(0xff266199),
    secondary = Color(0xff995E26),
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}
