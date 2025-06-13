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
import android.app.Activity
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = AlleyMainPurple,
    secondary = AlleyLightGray,
    tertiary = AlleyBlue,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = AlleyWhite,
    onSecondary = AlleyText,
    onBackground = AlleyWhite,
    //onSurface = AlleyWhite,
    //onSurfaceVariant = AlleyInactiveButton

    // --- CARD-SPECIFIC COLORS ---
    surfaceVariant = AlleyInactiveButton, // The light lavender background for cards
    onSurfaceVariant = Color.DarkGray, // Default text color on that background
    outlineVariant = Color.LightGray.copy(alpha = 0.3f) // The light border color]

)

private val LightColorScheme = lightColorScheme(
    primary = AlleyMainPurple,
    secondary = AlleyMainOrange,
    tertiary = AlleyLightGray,
    background = AlleyWhite,
    surface = AlleyWhite,
    onPrimary = AlleyWhite,
    onSecondary = AlleyWhite,
    onBackground = AlleyText,
    onSurface = AlleyText,
    //onSurfaceVariant = AlleyInactiveButton

    // --- CARD-SPECIFIC COLORS ---

    surfaceVariant = Color(0xFFFFFFFF), // BG
    onSurfaceVariant = AlleyInactiveButton,
    outlineVariant = Color.LightGray.copy(alpha = 0.5f) // The light border color]
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

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }


    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}