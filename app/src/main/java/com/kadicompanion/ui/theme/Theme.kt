package com.kadicompanion.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.kadicompanion.data.local.datastore.HeroStyle
import com.kadicompanion.data.local.datastore.ThemePref
import com.kadicompanion.data.local.datastore.UiPreferences

val LocalReduceMotion = staticCompositionLocalOf { false }
val LocalDyslexiaFont = staticCompositionLocalOf { false }
val LocalUiPreferences = staticCompositionLocalOf { UiPreferences() }
val LocalOpenQuickCustomizer = staticCompositionLocalOf<(() -> Unit)?> { null }

/** Idiomatic MaterialTheme extensions for semantic extended colors and active syntax palette. */
val MaterialTheme.kadiColors: KadiExtendedColors
    @Composable
    @ReadOnlyComposable
    get() = LocalKadiColors.current

val MaterialTheme.codePalette: CodePalette
    @Composable
    @ReadOnlyComposable
    get() = LocalCodePalette.current

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

@Composable
fun successColor(): Color = MaterialTheme.kadiColors.success

@Composable
fun successContainerColor(): Color = MaterialTheme.kadiColors.successContainer

@Composable
fun isDarkScheme(): Boolean = MaterialTheme.kadiColors.isDark

@Composable
fun heroTextPrimary(): Color =
    if (LocalUiPreferences.current.heroStyle == HeroStyle.SURFACE_FLAT && !isDarkScheme())
        MaterialTheme.colorScheme.onSurface
    else MaterialTheme.kadiColors.onHeroPrimary

@Composable
fun heroTextSecondary(): Color =
    if (LocalUiPreferences.current.heroStyle == HeroStyle.SURFACE_FLAT && !isDarkScheme())
        MaterialTheme.colorScheme.onSurfaceVariant
    else MaterialTheme.kadiColors.onHeroSecondary

@Composable
fun heroTextTertiary(): Color =
    if (LocalUiPreferences.current.heroStyle == HeroStyle.SURFACE_FLAT && !isDarkScheme())
        MaterialTheme.colorScheme.onSurfaceVariant
    else MaterialTheme.kadiColors.onHeroMuted

@Composable
fun brandGradient(): Brush {
    val ext = MaterialTheme.kadiColors
    return Brush.linearGradient(
        colors = listOf(ext.heroGradientStart, ext.heroGradientMid, ext.heroGradientEnd)
    )
}

@Composable
fun coolGradient(): Brush {
    val ext = MaterialTheme.kadiColors
    return Brush.linearGradient(
        colors = listOf(ext.feltStart, ext.heroGradientMid, ext.heroGradientEnd)
    )
}

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
        MaterialTheme.kadiColors.glassSurface,
        Color.Transparent
    )
)

@Composable
fun glassBorderColor(): Color = MaterialTheme.kadiColors.glassBorder

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
    val effectiveDark = ThemeEngine.isDarkTheme(uiPrefs.theme, systemDark)
    val isAmoled = uiPrefs.theme == ThemePref.AMOLED
    val isDynamicActive = uiPrefs.dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    val dynamicScheme = if (isDynamicActive) {
        val ctx = LocalContext.current
        if (effectiveDark) dynamicDarkColorScheme(ctx) else dynamicLightColorScheme(ctx)
    } else {
        null
    }

    val targetColorScheme = remember(
        uiPrefs.theme,
        uiPrefs.accentPreset,
        uiPrefs.contrastLevel,
        systemDark,
        dynamicScheme
    ) {
        ThemeEngine.resolveColorScheme(
            themePref = uiPrefs.theme,
            accentPreset = uiPrefs.accentPreset,
            contrastLevel = uiPrefs.contrastLevel,
            systemDark = systemDark,
            dynamicColorScheme = dynamicScheme
        )
    }

    val targetExtendedColors = remember(
        targetColorScheme,
        uiPrefs.accentPreset,
        uiPrefs.contrastLevel,
        effectiveDark,
        isAmoled,
        isDynamicActive,
        uiPrefs.kadiFeltPreset
    ) {
        ThemeEngine.resolveExtendedColors(
            colorScheme = targetColorScheme,
            accentPreset = uiPrefs.accentPreset,
            contrastLevel = uiPrefs.contrastLevel,
            isDark = effectiveDark,
            isAmoled = isAmoled,
            isDynamic = isDynamicActive,
            kadiFeltPreset = uiPrefs.kadiFeltPreset
        )
    }

    val activeCodePalette = remember(
        uiPrefs.codeThemePreset,
        uiPrefs.accentPreset,
        effectiveDark,
        uiPrefs.contrastLevel
    ) {
        resolveCodePalette(
            preset = uiPrefs.codeThemePreset,
            accentPreset = uiPrefs.accentPreset,
            isDark = effectiveDark,
            contrastLevel = uiPrefs.contrastLevel
        )
    }

    val animDurationMs = if (uiPrefs.reduceMotion) 0 else uiPrefs.animationSpeed.durationMs
    val animatedColorScheme = animateColorScheme(
        target = targetColorScheme,
        reduceMotion = uiPrefs.reduceMotion,
        durationMs = animDurationMs
    )
    val animatedExtendedColors = animateExtendedColors(
        target = targetExtendedColors,
        reduceMotion = uiPrefs.reduceMotion,
        durationMs = animDurationMs
    )

    val typography = remember(
        uiPrefs.fontScale,
        uiPrefs.fontFamilyPreset,
        uiPrefs.headingWeightPreset,
        uiPrefs.lineSpacingPreset,
        uiPrefs.dyslexiaFont
    ) {
        buildCustomizableTypography(
            fontScale = uiPrefs.fontScale,
            fontFamilyPreset = uiPrefs.fontFamilyPreset,
            headingWeightPreset = uiPrefs.headingWeightPreset,
            lineSpacingPreset = uiPrefs.lineSpacingPreset,
            dyslexiaFont = uiPrefs.dyslexiaFont
        )
    }
    val shapes = remember(uiPrefs.cornerRadiusDp) { buildCustomizableShapes(uiPrefs.cornerRadiusDp) }

    CompositionLocalProvider(
        LocalReduceMotion provides uiPrefs.reduceMotion,
        LocalDyslexiaFont provides uiPrefs.dyslexiaFont,
        LocalUiPreferences provides uiPrefs,
        LocalOpenQuickCustomizer provides onOpenQuickCustomizer,
        LocalKadiColors provides animatedExtendedColors,
        LocalCodePalette provides activeCodePalette
    ) {
        MaterialTheme(
            colorScheme = animatedColorScheme,
            typography = typography,
            shapes = shapes,
            content = content
        )
    }
}
