package com.example.todo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

//private val DarkColorScheme = darkColorScheme(
//    primary = RubyRed,
//    onPrimary = Color.White,
//    primaryContainer = DarkRuby,
//    secondary = Color(0xFF26C6DA),
//    background = DeepSpace,
//    surface = SlateGray,
//    onSurface = Color.White,
//    onBackground = Color.White
//)
//private val LightColors = lightColorScheme(
//    primary = BluePrimary,
//    secondary = CyanSecondary,
//    background = LightBackground,
//    surface = WhiteSurface,
//    error = RedError,
//    onPrimary = Color.White,
//    onSecondary = Color.White,
//    onBackground = Color.Black,
//    onSurface = Color.Black
//)
//
//private val LightColorScheme = lightColorScheme(
//    primary = RubyRed,
//    onPrimary = Color.White,
//    primaryContainer = LightRuby,
//    background = SoftWhite,
//    surface = Color.White,
//    onSurface = DeepSpace,
//    onBackground = DeepSpace
//)
//
//@Composable
//fun TodoTheme(
//    darkTheme: Boolean = isSystemInDarkTheme(),
//    // Set dynamicColor to FALSE to force your RubyRed theme
//    dynamicColor: Boolean = false,
//    content: @Composable () -> Unit
//) {
//    val colorScheme = when {
//        // Comment out or remove the dynamicColor block to force your colors
//        /* dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        } */
//        darkTheme -> DarkColorScheme
//        else -> LightColorScheme
//    }
//
//    MaterialTheme(
//        colorScheme = colorScheme,
//        content = content
//    )
//}

private val DarkColorScheme = darkColorScheme(
    primary = RubyRed,
    onPrimary = Color.White,
    background = DeepSpace,
    surface = CardDark,
    outline = OutlineDark,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = RubyRed,
    onPrimary = Color.White,

    background = Color(0xFFF8FAFC),   // very soft light grey
    surface = Color.White,             // cards pure white
    outline = Color(0xFFE2E8F0),        // soft border

    onBackground = Color(0xFF020617),
    onSurface = Color(0xFF020617)
)

@Composable
fun TodoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
