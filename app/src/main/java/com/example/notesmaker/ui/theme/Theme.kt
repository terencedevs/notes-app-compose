package com.example.notesmaker.ui.theme

import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
)

/**
 * Animates transitions between [ColorScheme] colors when switching theme mode.
 */
@Composable
private fun ColorScheme.animateColors(
    animationSpec: AnimationSpec<Color> = tween(
        durationMillis = 400,
        easing = FastOutSlowInEasing
    )
): ColorScheme {
    return copy(
        primary = animateColorAsState(primary, animationSpec, label = "primary").value,
        onPrimary = animateColorAsState(onPrimary, animationSpec, label = "onPrimary").value,
        primaryContainer = animateColorAsState(primaryContainer, animationSpec, label = "primaryContainer").value,
        onPrimaryContainer = animateColorAsState(onPrimaryContainer, animationSpec, label = "onPrimaryContainer").value,
        inversePrimary = animateColorAsState(inversePrimary, animationSpec, label = "inversePrimary").value,
        secondary = animateColorAsState(secondary, animationSpec, label = "secondary").value,
        onSecondary = animateColorAsState(onSecondary, animationSpec, label = "onSecondary").value,
        secondaryContainer = animateColorAsState(secondaryContainer, animationSpec, label = "secondaryContainer").value,
        onSecondaryContainer = animateColorAsState(onSecondaryContainer, animationSpec, label = "onSecondaryContainer").value,
        tertiary = animateColorAsState(tertiary, animationSpec, label = "tertiary").value,
        onTertiary = animateColorAsState(onTertiary, animationSpec, label = "onTertiary").value,
        tertiaryContainer = animateColorAsState(tertiaryContainer, animationSpec, label = "tertiaryContainer").value,
        onTertiaryContainer = animateColorAsState(onTertiaryContainer, animationSpec, label = "onTertiaryContainer").value,
        background = animateColorAsState(background, animationSpec, label = "background").value,
        onBackground = animateColorAsState(onBackground, animationSpec, label = "onBackground").value,
        surface = animateColorAsState(surface, animationSpec, label = "surface").value,
        onSurface = animateColorAsState(onSurface, animationSpec, label = "onSurface").value,
        surfaceVariant = animateColorAsState(surfaceVariant, animationSpec, label = "surfaceVariant").value,
        onSurfaceVariant = animateColorAsState(onSurfaceVariant, animationSpec, label = "onSurfaceVariant").value,
        surfaceTint = animateColorAsState(surfaceTint, animationSpec, label = "surfaceTint").value,
        inverseSurface = animateColorAsState(inverseSurface, animationSpec, label = "inverseSurface").value,
        inverseOnSurface = animateColorAsState(inverseOnSurface, animationSpec, label = "inverseOnSurface").value,
        error = animateColorAsState(error, animationSpec, label = "error").value,
        onError = animateColorAsState(onError, animationSpec, label = "onError").value,
        errorContainer = animateColorAsState(errorContainer, animationSpec, label = "errorContainer").value,
        onErrorContainer = animateColorAsState(onErrorContainer, animationSpec, label = "onErrorContainer").value,
        outline = animateColorAsState(outline, animationSpec, label = "outline").value,
        outlineVariant = animateColorAsState(outlineVariant, animationSpec, label = "outlineVariant").value,
        scrim = animateColorAsState(scrim, animationSpec, label = "scrim").value
    )
}

/**
 * Custom Material 3 Theme providing smooth animated transitions between Light and Dark themes.
 */
@Composable
fun NotesMakerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val targetColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val animatedColorScheme = targetColorScheme.animateColors()

    MaterialTheme(
        colorScheme = animatedColorScheme,
        typography = Typography,
        content = content
    )
}

