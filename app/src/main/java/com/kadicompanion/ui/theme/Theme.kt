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
import androidx.compose.ui.graphics.lerp
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
    surfaceTint = DarkPrimary,
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
    surfaceTint = LightPrimary,
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

/** Soft, generous corners everywhere — the app should feel like rounded playing cards. */
val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(12.dp),
    small = RoundedCornerShape(16.dp),
    medium = RoundedCornerShape(22.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(36.dp)
)

/** Semantic success colors (Material 3 has no success role). */
@Composable
fun successColor(): Color = if (isDarkScheme()) SuccessGreen else SuccessGreenLight

@Composable
fun successContainerColor(): Color = if (isDarkScheme()) SuccessContainerDark else SuccessContainerLight

/** True when the active scheme is a dark one — works for dynamic color too. */
@Composable
fun isDarkScheme(): Boolean {
    val bg = MaterialTheme.colorScheme.background
    return (0.299f * bg.red + 0.587f * bg.green + 0.114f * bg.blue) < 0.5f
}

// ===================================================================
// Hero text: always white/light regardless of theme, because hero
// panels use vivid gradients where dark onPrimary is unreadable.
// ===================================================================

/** Primary text on hero panels — always white. */
@Composable fun heroTextPrimary(): Color = HeroTextPrimary

/** Secondary text on hero panels — 87% white. */
@Composable fun heroTextSecondary(): Color = HeroTextSecondary

/** Tertiary/muted text on hero panels — 70% white. */
@Composable fun heroTextTertiary(): Color = HeroTextTertiary

// ===================================================================
// Brand brushes
// ===================================================================

/**
 * A warm, on-brand diagonal gradient built from the active color scheme.
 * Uses primary -> tertiary/secondary so it adapts to both the brand palette
 * and Material You dynamic color. Pair with [heroTextPrimary] for readable
 * content on top.
 */
@Composable
fun brandGradient(): Brush = Brush.linearGradient(
    colors = listOf(
        MaterialTheme.colorScheme.primary,
        lerp(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary, 0.55f),
        MaterialTheme.colorScheme.tertiary
    )
)

/** Cooler sibling of [brandGradient] — used for informational heroes (docs, glossary). */
@Composable
fun coolGradient(): Brush = Brush.linearGradient(
    colors = listOf(
        MaterialTheme.colorScheme.secondary,
        lerp(MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.primary, 0.45f)
    )
)

/**
 * A subtle vertical wash used behind screen headers — fades from a tinted
 * primary into the background so scrollable content blends seamlessly.
 */
@Composable
fun headerWashGradient(): Brush = Brush.verticalGradient(
    colors = listOf(
        MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
        MaterialTheme.colorScheme.background.copy(alpha = 0f)
    )
)

/**
 * Ambient app background: a barely-there double tint (gold from the top-left,
 * crimson from the bottom-right) over the base background color. It gives every
 * screen depth without competing with content.
 */
@Composable
fun appBackgroundBrush(): Brush {
    val scheme = MaterialTheme.colorScheme
    val strength = if (isDarkScheme()) 0.09f else 0.06f
    return Brush.linearGradient(
        colors = listOf(
            lerp(scheme.background, scheme.primary, strength),
            scheme.background,
            lerp(scheme.background, scheme.tertiary, strength * 0.6f),
            lerp(scheme.background, scheme.secondary, strength * 0.3f)
        )
    )
}

/** Faint top-light sheen laid over cards so large surfaces are not dead flat. */
@Composable
fun cardSheenBrush(): Brush = Brush.verticalGradient(
    colors = listOf(
        MaterialTheme.colorScheme.onSurface.copy(alpha = if (isDarkScheme()) 0.05f else 0.03f),
        Color.Transparent
    )
)

/** Glassmorphic overlay for surfaces that want a frosted glass feel. */
@Composable
fun glassSurfaceBrush(): Brush = Brush.verticalGradient(
    colors = listOf(
        if (isDarkScheme()) CardGlassDark else CardGlassLight,
        Color.Transparent,
        if (isDarkScheme()) CardGlassDark.copy(alpha = 0.06f) else CardGlassLight.copy(alpha = 0.03f)
    )
)

/** Glass border color for glassmorphic surfaces. */
@Composable
fun glassBorderColor(): Color = if (isDarkScheme()) GlassBorderDark else GlassBorderLight

/**
 * A soft radial glow gradient centred behind progress rings and icons.
 * Fades to transparent so it blends into any surface.
 */
@Composable
fun accentGlowBrush(): Brush = Brush.radialGradient(
    colors = listOf(
        MaterialTheme.colorScheme.primary.copy(alpha = if (isDarkScheme()) 0.16f else 0.10f),
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
