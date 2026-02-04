package com.hilmyfhauzan.tokopakadam.presentation.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryOrange,
    onPrimary = Color.White,
    primaryContainer = DeepOrange,
    onPrimaryContainer = LightOrange,

    secondary = SecondaryBlue,
    onSecondary = Color.White,
    secondaryContainer = DarkBlueContainer,
    onSecondaryContainer = LightBlue,

    tertiary = SuccessGreen,
    onTertiary = Color.Black,
    tertiaryContainer = DarkSuccessGreenContainer,
    onTertiaryContainer = OnDarkSuccessGreenContainer,

    background = DarkBackground,
    onBackground = Color.White,

    surface = DarkSurface,
    onSurface = Color.White,

    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextGray,

    error = DangerRed,
    onError = Color.Black,
    errorContainer = DarkRedContainer,
    onErrorContainer = OnDarkRedContainer,

    outline = OutlineDark,
    outlineVariant = OutlineLight
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryOrange,
    onPrimary = Color.White,
    primaryContainer = LightOrange,
    onPrimaryContainer = DarkOrangeText,

    secondary = SecondaryBlue,
    onSecondary = Color.White,
    secondaryContainer = LightBlue,
    onSecondaryContainer = IconGray,

    tertiary = SuccessGreen,
    onTertiary = Color.White,
    tertiaryContainer = LightSuccessGreen,
    onTertiaryContainer = SuccessGreen,

    background = BackgroundGray,
    onBackground = TextBlack,

    surface = Color.White,
    onSurface = TextBlack,

    surfaceVariant = Color.White,
    onSurfaceVariant = TextGray,

    error = DangerRed,
    onError = Color.White,
    errorContainer = LightRed,
    onErrorContainer = DangerRed,

    outline = TextGray,
    outlineVariant = OutlineLight
)

@Composable
fun TokoPakAdamTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    themePreference: Boolean? = null, // New parameter
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    onToggleTheme: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val colorScheme =
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (darkTheme) dynamicDarkColorScheme(context)
                else dynamicLightColorScheme(context)
            }
            darkTheme -> DarkColorScheme
            else -> LightColorScheme
        }

    CompositionLocalProvider(
        LocalThemeMode provides darkTheme,
        LocalThemePreference provides themePreference, // Provide the preference
        LocalThemeToggle provides onToggleTheme
    ) {
        MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
    }
}

val LocalThemeMode = compositionLocalOf { false }
val LocalThemePreference = compositionLocalOf<Boolean?> { null } // New Local for persisting "Auto" state
val LocalThemeToggle = compositionLocalOf { {} }
