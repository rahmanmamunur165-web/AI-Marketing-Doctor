package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = TealLight,
    onPrimary = Color.Black,
    primaryContainer = TealDark,
    onPrimaryContainer = TealContainer,
    secondary = CyanLight,
    onSecondary = Color.Black,
    secondaryContainer = CyanSecondary,
    onSecondaryContainer = CyanContainer,
    tertiary = HealthGreen,
    background = DarkSlateBackground,
    surface = DarkSlateSurface,
    surfaceVariant = DarkSlateSurfaceContainer,
    onBackground = DarkSlateTextPrimary,
    onSurface = DarkSlateTextPrimary,
    outline = DarkSlateBorder
)

private val LightColorScheme = lightColorScheme(
    primary = TealPrimary,
    onPrimary = Color.White,
    primaryContainer = TealContainer,
    onPrimaryContainer = OnTealContainer,
    secondary = CyanSecondary,
    onSecondary = Color.White,
    secondaryContainer = CyanContainer,
    onSecondaryContainer = OnCyanContainer,
    tertiary = HealthGreen,
    background = SlateBackground,
    surface = SlateSurface,
    surfaceVariant = SlateSurfaceContainer,
    onBackground = SlateTextPrimary,
    onSurface = SlateTextPrimary,
    outline = SlateBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep consistent clinical branding
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
