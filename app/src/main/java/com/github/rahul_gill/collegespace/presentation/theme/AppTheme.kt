package com.github.rahul_gill.collegespace.presentation.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

val DarkColors = darkColors(
    primary = Color(0xff4681B9),
    secondary = Color(0xffB97E46),
    surface = Color(0xff2d2d3f)
)

val LightColors = lightColors(
    primary = Color(0xff266199),
    secondary = Color(0xff995E26),
)

private val transparentSurfaceLight =  Color(0xAAFFFFFF)
private val transparentSurfaceDark  =  Color(0x442d2d3f)

class AppColors(
    transparentSurface: Color,
    grayText: Color,
    isLight: Boolean
) {
    var transparentSurface by mutableStateOf(transparentSurface)
        private set
    var grayText by mutableStateOf(grayText)
        private set
    var isLight by mutableStateOf(isLight)
        internal set
    fun copy(
        transparentSurface: Color = this.transparentSurface,
        grayText: Color = this.grayText,
        isLight: Boolean = this.isLight
    ): AppColors = AppColors(
        transparentSurface,
        grayText,
        isLight
    )
    fun updateColorsFrom(other: AppColors) {
        transparentSurface = other.transparentSurface
        grayText = other.grayText
    }
}
fun lightExtendedColors() = AppColors(transparentSurfaceLight, Color.DarkGray, true)
fun darkExtendedColors() = AppColors(transparentSurfaceDark, Color.LightGray,false)

internal val LocalExtendedColors = staticCompositionLocalOf { lightExtendedColors() }


@Composable
fun AppTheme(
//    darkTheme: Boolean = isSystemInDarkTheme(),
    darkTheme: Boolean = true,
    colorsExtended : AppColors = darkExtendedColors(),
    content: @Composable () -> Unit
) {
    val rememberedColors = remember { colorsExtended.copy() }.apply { updateColorsFrom(colorsExtended) }
    CompositionLocalProvider(LocalExtendedColors provides rememberedColors) {
        MaterialTheme(
            colors = if (darkTheme) DarkColors else LightColors,
            content = content
        )
    }
}
