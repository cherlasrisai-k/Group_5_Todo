package com.example.todo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = RubyRed,
    onPrimary = Color.White,
    primaryContainer = DarkRuby,
    secondary = Color(0xFF26C6DA),
    background = DeepSpace,
    surface = SlateGray,
    onSurface = Color.White,
    onBackground = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = RubyRed,
    onPrimary = Color.White,
    primaryContainer = LightRuby,
    background = SoftWhite,
    surface = Color.White,
    onSurface = DeepSpace,
    onBackground = DeepSpace
)

@Composable
fun TodoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
