package com.jalloft.jarflix.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = CarmineRed,
    secondary = Gunmetal,
    tertiary = DavysGrey,
    background = ChineseBlack,
    surface = ChineseBlack,
    onSurface = White,

    /* Other default colors to override
      background = Color(0xFFFFFBFE),
      surface = Color(0xFFFFFBFE),
      onPrimary = Color.White,
      onSecondary = Color.White,
      onTertiary = Color.White,
      onBackground = Color(0xFF1C1B1F),
      onSurface = Color(0xFF1C1B1F),
      */

)


@Composable
fun JarflixTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        val currentWindow = (view.context as? Activity)?.window
            ?: throw Exception("Not in an activity - unable to get Window reference")

        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(currentWindow, view)?.isAppearanceLightStatusBars =
                false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}