package com.example.shrimpcaring.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColors = darkColorScheme(

    primary = Blue80,
    secondary = Green80,
    tertiary = Orange80,

    background = BackgroundDark,
    surface = SurfaceDark,

    onPrimary = TextDark,
    onSecondary = TextDark,
    onBackground = TextDark,
    onSurface = TextDark

)

private val LightColors = lightColorScheme(

    primary = Blue40,
    secondary = Green40,
    tertiary = Orange40,

    background = BackgroundLight,
    surface = SurfaceLight,

    onPrimary = TextLight,
    onSecondary = TextLight,
    onBackground = TextLight,
    onSurface = TextLight

)

@Composable
fun ShrimpCaringTheme(

    darkTheme: Boolean = isSystemInDarkTheme(),

    content: @Composable () -> Unit

) {

    MaterialTheme(

        colorScheme =
            if (darkTheme) DarkColors
            else LightColors,

        typography = AppTypography,

        content = content

    )

}