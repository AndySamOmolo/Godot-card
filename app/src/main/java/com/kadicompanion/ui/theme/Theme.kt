package com.kadicompanion.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

private val DarkColors = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnPrimaryContainer,
    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,
    secondaryContainer = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,
    tertiary = DarkTertiary,
    onTertiary = DarkOnTertiary,
    tertiaryContainer = DarkTertiaryContainer,
    onTertiaryContainer = DarkOnTertiaryContainer,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    surfaceContainerLowest = DarkSurfaceContainerLowest,
    surfaceContainerLow = DarkSurfaceContainerLow,
    surfaceContainer = DarkSurfaceContainer,
    surfaceContainerHigh = DarkSurfaceContainerHigh,
    surfaceContainerHighest = DarkSurfaceContainerHighest,
    surfaceTint = Color.Transparent,
    inverseSurface = DarkInverseSurface,
    inverseOnSurface = DarkInverseOnSurface,
    error = DarkError,
    onError = DarkOnError,
    errorContainer = DarkErrorContainer,
    onErrorContainer = DarkOnErrorContainer,
    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant,
    scrim = DarkScrim
)

private val LightColors = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    primaryContainer = LightPrimaryContainer,
    onPrimaryContainer = LightOnPrimaryContainer,
    secondary = LightSecondary,
    onSecondary = LightOnSecondary,
    secondaryContainer = LightSecondaryContainer,
    onSecondaryContainer = LightOnSecondaryContainer,
    tertiary = LightTertiary,
    onTertiary = LightOnTertiary,
    tertiaryContainer = LightTertiaryContainer,
    onTertiaryContainer = LightOnTertiaryContainer,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    surfaceContainerLowest = LightSurfaceContainerLowest,
    surfaceContainerLow = LightSurfaceContainerLow,
    surfaceContainer = LightSurfaceContainer,
    surfaceContainerHigh = LightSurfaceContainerHigh,
    surfaceContainerHighest = LightSurfaceContainerHighest,
    surfaceTint = Color.Transparent,
    inverseSurface = LightInverseSurface,
    inverseOnSurface = LightInverseOnSurface,
    error = LightError,
    onError = LightOnError,
    errorContainer = LightErrorContainer,
    onErrorContainer = LightOnErrorContainer,
    outline = LightOutline,
    outlineVariant = LightOutlineVariant,
    scrim = LightScrim
)

val LocalReduceMotion = staticCompositionLocalOf { false }
val LocalDyslexiaFont = staticCompositionLocalOf { false }

/** Crisp architectural studio corners across all components. */
val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(18.dp),
    extraLarge = RoundedCornerShape(22.dp)
)

@Composable
fun successColor(): Color = if (isDarkScheme()) SuccessGreen else SuccessGreenLight

@Composable
fun successContainerColor(): Color = if (isDarkScheme()) SuccessContainerDark else SuccessContainerLight

@Composable
fun isDarkScheme(): Boolean {
    val bg = MaterialTheme.colorScheme.background
    return (0.299f * bg.red + 0.587f * bg.green + 0.114f * bg.blue) < 0.5f
}

@Composable fun heroTextPrimary(): Color = HeroTextPrimary
@Composable fun heroTextSecondary(): Color = HeroTextSecondary
@Composable fun heroTextTertiary(): Color = HeroTextTertiary

/**
 * Deep Navy-to-Sapphire Studio Hero gradient. Always provides crisp contrast
 * for white hero text in both light and dark modes without loud neon glare.
 */
@Composable
fun brandGradient(): Brush = Brush.linearGradient(
    colors = listOf(
        StudioHeroStart,
        StudioHeroMid,
        StudioHeroEnd
    )
)

@Composable
fun coolGradient(): Brush = Brush.linearGradient(
    colors = listOf(
        Color(0xFF0F172A),
        Color(0xFF065F46),
        Color(0xFF0284C7)
    )
)

@Composable
fun headerWashGradient(): Brush = Brush.verticalGradient(
    colors = listOf(
        MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
        Color.Transparent
    )
)

/**
 * Clean, solid studio background with zero muddy color washes.
 */
@Composable
fun appBackgroundBrush(): Brush {
    val bg = MaterialTheme.colorScheme.background
    return Brush.verticalGradient(colors = listOf(bg, bg))
}

@Composable
fun cardSheenBrush(): Brush = Brush.verticalGradient(
    colors = listOf(Color.Transparent, Color.Transparent)
)

@Composable
fun glassSurfaceBrush(): Brush = Brush.verticalGradient(
    colors = listOf(
        if (isDarkScheme()) CardGlassDark else CardGlassLight,
        Color.Transparent
    )
)

@Composable
fun glassBorderColor(): Color = if (isDarkScheme()) GlassBorderDark else GlassBorderLight

@Composable
fun accentGlowBrush(): Brush = Brush.radialGradient(
    colors = listOf(
        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
        Color.Transparent
    )
)

@Composable
fun KadiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    reduceMotion: Boolean = false,
    dyslexiaFont: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val ctx = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(ctx) else dynamicLightColorScheme(ctx)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }
    CompositionLocalProvider(
        LocalReduceMotion provides reduceMotion,
        LocalDyslexiaFont provides dyslexiaFont
    ) {
        MaterialTheme(colorScheme = colors, typography = AppTypography, shapes = AppShapes, content = content)
    }
}
