package com.mobdeve.s18.roman.isaacnathan.alleymate.theme

import androidx.compose.ui.graphics.Color
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = AlleyMainPurple,
    secondary = AlleyLightGray,
    tertiary = AlleyBlue,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = AlleyWhite,
    onSecondary = AlleyText,
    onBackground = AlleyWhite,
    onSurface = AlleyWhite,
    onSurfaceVariant = AlleyInactiveButton
)

private val LightColorScheme = lightColorScheme(
    primary = AlleyMainPurple,
    secondary = AlleyDarkBlue,
    tertiary = AlleyLightGray,
    background = AlleyWhite,
    surface = AlleyWhite,
    onPrimary = AlleyWhite,
    onSecondary = AlleyWhite,
    onBackground = AlleyText,
    onSurface = AlleyText,
    onSurfaceVariant = AlleyInactiveButton
)

@Composable
fun AlleyMateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}