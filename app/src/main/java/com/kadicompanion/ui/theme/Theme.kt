package com.kadicompanion.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
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
import com.kadicompanion.data.local.datastore.AccentPreset
import com.kadicompanion.data.local.datastore.ThemePref
import com.kadicompanion.data.local.datastore.UiPreferences

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
val LocalUiPreferences = staticCompositionLocalOf { UiPreferences() }
val LocalOpenQuickCustomizer = staticCompositionLocalOf<(() -> Unit)?> { null }

val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(18.dp),
    extraLarge = RoundedCornerShape(22.dp)
)

fun buildCustomizableShapes(cornerRadiusDp: Int): Shapes {
    val base = cornerRadiusDp.coerceIn(0, 32)
    val xs = (base * 0.55f).toInt().dp
    val sm = (base * 0.75f).toInt().dp
    val md = base.dp
    val lg = (base * 1.25f).toInt().coerceAtMost(32).dp
    val xl = (base * 1.5f).toInt().coerceAtMost(36).dp
    return Shapes(
        extraSmall = RoundedCornerShape(xs),
        small = RoundedCornerShape(sm),
        medium = RoundedCornerShape(md),
        large = RoundedCornerShape(lg),
        extraLarge = RoundedCornerShape(xl)
    )
}

private data class AccentPaletteTriplet(
    val darkPrimary: Color,
    val darkSecondary: Color,
    val darkTertiary: Color,
    val lightPrimary: Color,
    val lightSecondary: Color,
    val lightTertiary: Color,
    val heroStart: Color,
    val heroMid: Color,
    val heroEnd: Color
)

private fun paletteFor(preset: AccentPreset): AccentPaletteTriplet = when (preset) {
    AccentPreset.GODOT_SAPPHIRE -> AccentPaletteTriplet(
        darkPrimary = Color(0xFF38BDF8),
        darkSecondary = Color(0xFF10B981),
        darkTertiary = Color(0xFFF59E0B),
        lightPrimary = Color(0xFF2563EB),
        lightSecondary = Color(0xFF059669),
        lightTertiary = Color(0xFFD97706),
        heroStart = Color(0xFF0F172A),
        heroMid = Color(0xFF1E3A8A),
        heroEnd = Color(0xFF0284C7)
    )
    AccentPreset.NAIROBI_GOLD -> AccentPaletteTriplet(
        darkPrimary = Color(0xFFFBBF24),
        darkSecondary = Color(0xFF34D399),
        darkTertiary = Color(0xFFFB7185),
        lightPrimary = Color(0xFFB45309),
        lightSecondary = Color(0xFF047857),
        lightTertiary = Color(0xFFE11D48),
        heroStart = Color(0xFF1C1917),
        heroMid = Color(0xFF78350F),
        heroEnd = Color(0xFFB45309)
    )
    AccentPreset.EMERALD_FELT -> AccentPaletteTriplet(
        darkPrimary = Color(0xFF34D399),
        darkSecondary = Color(0xFFFBBF24),
        darkTertiary = Color(0xFF38BDF8),
        lightPrimary = Color(0xFF059669),
        lightSecondary = Color(0xFFD97706),
        lightTertiary = Color(0xFF0284C7),
        heroStart = Color(0xFF042F2E),
        heroMid = Color(0xFF065F46),
        heroEnd = Color(0xFF059669)
    )
    AccentPreset.CYBER_VIOLET -> AccentPaletteTriplet(
        darkPrimary = Color(0xFFC084FC),
        darkSecondary = Color(0xFF22D3EE),
        darkTertiary = Color(0xFFF472B6),
        lightPrimary = Color(0xFF7C3AED),
        lightSecondary = Color(0xFF0891B2),
        lightTertiary = Color(0xFFDB2777),
        heroStart = Color(0xFF1E1B4B),
        heroMid = Color(0xFF4C1D95),
        heroEnd = Color(0xFF7C3AED)
    )
    AccentPreset.CRIMSON_ACE -> AccentPaletteTriplet(
        darkPrimary = Color(0xFFFB7185),
        darkSecondary = Color(0xFFFBBF24),
        darkTertiary = Color(0xFF818CF8),
        lightPrimary = Color(0xFFE11D48),
        lightSecondary = Color(0xFFD97706),
        lightTertiary = Color(0xFF4F46E5),
        heroStart = Color(0xFF2A0A18),
        heroMid = Color(0xFF881337),
        heroEnd = Color(0xFFE11D48)
    )
    AccentPreset.SUNSET_ORANGE -> AccentPaletteTriplet(
        darkPrimary = Color(0xFFFB923C),
        darkSecondary = Color(0xFF2DD4BF),
        darkTertiary = Color(0xFFFACC15),
        lightPrimary = Color(0xFFEA580C),
        lightSecondary = Color(0xFF0D9488),
        lightTertiary = Color(0xFFCA8A04),
        heroStart = Color(0xFF271206),
        heroMid = Color(0xFF7C2D12),
        heroEnd = Color(0xFFEA580C)
    )
    AccentPreset.MONOCHROME_SLATE -> AccentPaletteTriplet(
        darkPrimary = Color(0xFFE2E8F0),
        darkSecondary = Color(0xFF94A3B8),
        darkTertiary = Color(0xFFCBD5E1),
        lightPrimary = Color(0xFF0F172A),
        lightSecondary = Color(0xFF334155),
        lightTertiary = Color(0xFF475569),
        heroStart = Color(0xFF090D16),
        heroMid = Color(0xFF1E293B),
        heroEnd = Color(0xFF334155)
    )
}

private fun applyPresetAndMode(
    base: ColorScheme,
    preset: AccentPreset,
    darkTheme: Boolean,
    amoled: Boolean
): ColorScheme {
    val p = paletteFor(preset)
    val customized = if (darkTheme) {
        base.copy(
            primary = p.darkPrimary,
            secondary = p.darkSecondary,
            tertiary = p.darkTertiary,
            primaryContainer = p.darkPrimary.copy(alpha = 0.22f),
            secondaryContainer = p.darkSecondary.copy(alpha = 0.22f),
            tertiaryContainer = p.darkTertiary.copy(alpha = 0.22f)
        )
    } else {
        base.copy(
            primary = p.lightPrimary,
            secondary = p.lightSecondary,
            tertiary = p.lightTertiary,
            primaryContainer = p.lightPrimary.copy(alpha = 0.15f),
            secondaryContainer = p.lightSecondary.copy(alpha = 0.15f),
            tertiaryContainer = p.lightTertiary.copy(alpha = 0.15f)
        )
    }

    return if (darkTheme && amoled) {
        customized.copy(
            background = Color(0xFF000000),
            surface = Color(0xFF05070B),
            surfaceContainerLowest = Color(0xFF000000),
            surfaceContainerLow = Color(0xFF06090F),
            surfaceContainer = Color(0xFF0A0F18),
            surfaceContainerHigh = Color(0xFF111826),
            surfaceContainerHighest = Color(0xFF192234),
            outlineVariant = Color(0xFF1E293B)
        )
    } else {
        customized
    }
}

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

@Composable
fun brandGradient(): Brush {
    val p = paletteFor(LocalUiPreferences.current.accentPreset)
    return Brush.linearGradient(
        colors = listOf(p.heroStart, p.heroMid, p.heroEnd)
    )
}

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
    uiPrefs: UiPreferences = UiPreferences(
        theme = if (darkTheme) ThemePref.DARK else ThemePref.LIGHT,
        dynamicColor = dynamicColor,
        reduceMotion = reduceMotion,
        dyslexiaFont = dyslexiaFont
    ),
    onOpenQuickCustomizer: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val systemDark = isSystemInDarkTheme()
    val effectiveDark = when (uiPrefs.theme) {
        ThemePref.DARK, ThemePref.AMOLED -> true
        ThemePref.LIGHT -> false
        ThemePref.SYSTEM -> systemDark
    }
    val isAmoled = uiPrefs.theme == ThemePref.AMOLED

    val baseColors = when {
        uiPrefs.dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val ctx = LocalContext.current
            val dyn = if (effectiveDark) dynamicDarkColorScheme(ctx) else dynamicLightColorScheme(ctx)
            if (isAmoled) applyPresetAndMode(dyn, uiPrefs.accentPreset, true, true) else dyn
        }
        effectiveDark -> applyPresetAndMode(DarkColors, uiPrefs.accentPreset, darkTheme = true, amoled = isAmoled)
        else -> applyPresetAndMode(LightColors, uiPrefs.accentPreset, darkTheme = false, amoled = false)
    }

    val typography = buildCustomizableTypography(
        fontScale = uiPrefs.fontScale,
        fontFamilyPreset = uiPrefs.fontFamilyPreset,
        headingWeightPreset = uiPrefs.headingWeightPreset,
        lineSpacingPreset = uiPrefs.lineSpacingPreset,
        dyslexiaFont = uiPrefs.dyslexiaFont
    )
    val shapes = buildCustomizableShapes(uiPrefs.cornerRadiusDp)

    CompositionLocalProvider(
        LocalReduceMotion provides uiPrefs.reduceMotion,
        LocalDyslexiaFont provides uiPrefs.dyslexiaFont,
        LocalUiPreferences provides uiPrefs,
        LocalOpenQuickCustomizer provides onOpenQuickCustomizer
    ) {
        MaterialTheme(
            colorScheme = baseColors,
            typography = typography,
            shapes = shapes,
            content = content
        )
    }
}
