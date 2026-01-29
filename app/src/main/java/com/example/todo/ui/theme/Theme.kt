package com.example.todo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = SpotifyGreenMuted,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD1F7DE),
    onPrimaryContainer = SpotifyGreenMuted,

    secondary = SpotifyDeepGreyText,
    onSecondary = Color.White,

    background = SpotifyLightGreyBg,
    onBackground = SpotifyDeepGreyText,

    surface = SpotifySurfaceWhite,
    onSurface = SpotifyDeepGreyText,
    surfaceVariant = SpotifyLightGreyVariant,
    onSurfaceVariant = SpotifyMutedText,

    outline = SpotifyLightGreyVariant,
    error = ErrorRed
)

private val DarkColorScheme = darkColorScheme(
    primary = SpotifyGreenMuted,
    onPrimary = Color.White,

    background = SpotifyBlack,
    onBackground = Color.White,

    surface = Color(0xFF181818),
    onSurface = Color.White,

    outline = Color(0xFF282828)
)

@Composable
fun TodoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}