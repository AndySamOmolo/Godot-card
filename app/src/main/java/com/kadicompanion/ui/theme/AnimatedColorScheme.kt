package com.kadicompanion.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
private fun animColor(
    target: Color,
    reduceMotion: Boolean,
    durationMs: Int,
    label: String
): Color {
    if (reduceMotion || durationMs <= 0) return target
    return animateColorAsState(
        targetValue = target,
        animationSpec = tween(durationMillis = durationMs, easing = FastOutSlowInEasing),
        label = label
    ).value
}

/**
 * Smoothly animates Material 3 ColorScheme transitions unless reduceMotion is enabled
 * or durationMs <= 0.
 */
@Composable
fun animateColorScheme(
    target: ColorScheme,
    reduceMotion: Boolean,
    durationMs: Int = 260
): ColorScheme {
    val effectiveDuration = if (reduceMotion || durationMs <= 0) 0 else durationMs
    if (effectiveDuration == 0) return target
    return target.copy(
        primary = animColor(target.primary, false, effectiveDuration, "primary"),
        onPrimary = animColor(target.onPrimary, false, effectiveDuration, "onPrimary"),
        primaryContainer = animColor(target.primaryContainer, false, effectiveDuration, "primaryContainer"),
        onPrimaryContainer = animColor(target.onPrimaryContainer, false, effectiveDuration, "onPrimaryContainer"),
        secondary = animColor(target.secondary, false, effectiveDuration, "secondary"),
        onSecondary = animColor(target.onSecondary, false, effectiveDuration, "onSecondary"),
        secondaryContainer = animColor(target.secondaryContainer, false, effectiveDuration, "secondaryContainer"),
        onSecondaryContainer = animColor(target.onSecondaryContainer, false, effectiveDuration, "onSecondaryContainer"),
        tertiary = animColor(target.tertiary, false, effectiveDuration, "tertiary"),
        onTertiary = animColor(target.onTertiary, false, effectiveDuration, "onTertiary"),
        tertiaryContainer = animColor(target.tertiaryContainer, false, effectiveDuration, "tertiaryContainer"),
        onTertiaryContainer = animColor(target.onTertiaryContainer, false, effectiveDuration, "onTertiaryContainer"),
        background = animColor(target.background, false, effectiveDuration, "background"),
        onBackground = animColor(target.onBackground, false, effectiveDuration, "onBackground"),
        surface = animColor(target.surface, false, effectiveDuration, "surface"),
        onSurface = animColor(target.onSurface, false, effectiveDuration, "onSurface"),
        surfaceVariant = animColor(target.surfaceVariant, false, effectiveDuration, "surfaceVariant"),
        onSurfaceVariant = animColor(target.onSurfaceVariant, false, effectiveDuration, "onSurfaceVariant"),
        surfaceContainerLowest = animColor(target.surfaceContainerLowest, false, effectiveDuration, "surfaceContainerLowest"),
        surfaceContainerLow = animColor(target.surfaceContainerLow, false, effectiveDuration, "surfaceContainerLow"),
        surfaceContainer = animColor(target.surfaceContainer, false, effectiveDuration, "surfaceContainer"),
        surfaceContainerHigh = animColor(target.surfaceContainerHigh, false, effectiveDuration, "surfaceContainerHigh"),
        surfaceContainerHighest = animColor(target.surfaceContainerHighest, false, effectiveDuration, "surfaceContainerHighest"),
        inverseSurface = animColor(target.inverseSurface, false, effectiveDuration, "inverseSurface"),
        inverseOnSurface = animColor(target.inverseOnSurface, false, effectiveDuration, "inverseOnSurface"),
        error = animColor(target.error, false, effectiveDuration, "error"),
        onError = animColor(target.onError, false, effectiveDuration, "onError"),
        errorContainer = animColor(target.errorContainer, false, effectiveDuration, "errorContainer"),
        onErrorContainer = animColor(target.onErrorContainer, false, effectiveDuration, "onErrorContainer"),
        outline = animColor(target.outline, false, effectiveDuration, "outline"),
        outlineVariant = animColor(target.outlineVariant, false, effectiveDuration, "outlineVariant")
    )
}

/**
 * Smoothly animates KadiExtendedColors transitions unless reduceMotion is enabled
 * or durationMs <= 0.
 */
@Composable
fun animateExtendedColors(
    target: KadiExtendedColors,
    reduceMotion: Boolean,
    durationMs: Int = 260
): KadiExtendedColors {
    val effectiveDuration = if (reduceMotion || durationMs <= 0) 0 else durationMs
    if (effectiveDuration == 0) return target
    return target.copy(
        success = animColor(target.success, false, effectiveDuration, "success"),
        onSuccess = animColor(target.onSuccess, false, effectiveDuration, "onSuccess"),
        successContainer = animColor(target.successContainer, false, effectiveDuration, "successContainer"),
        onSuccessContainer = animColor(target.onSuccessContainer, false, effectiveDuration, "onSuccessContainer"),
        warning = animColor(target.warning, false, effectiveDuration, "warning"),
        onWarning = animColor(target.onWarning, false, effectiveDuration, "onWarning"),
        warningContainer = animColor(target.warningContainer, false, effectiveDuration, "warningContainer"),
        onWarningContainer = animColor(target.onWarningContainer, false, effectiveDuration, "onWarningContainer"),
        info = animColor(target.info, false, effectiveDuration, "info"),
        onInfo = animColor(target.onInfo, false, effectiveDuration, "onInfo"),
        infoContainer = animColor(target.infoContainer, false, effectiveDuration, "infoContainer"),
        onInfoContainer = animColor(target.onInfoContainer, false, effectiveDuration, "onInfoContainer"),
        heroGradientStart = animColor(target.heroGradientStart, false, effectiveDuration, "heroGradientStart"),
        heroGradientMid = animColor(target.heroGradientMid, false, effectiveDuration, "heroGradientMid"),
        heroGradientEnd = animColor(target.heroGradientEnd, false, effectiveDuration, "heroGradientEnd"),
        onHeroPrimary = animColor(target.onHeroPrimary, false, effectiveDuration, "onHeroPrimary"),
        onHeroSecondary = animColor(target.onHeroSecondary, false, effectiveDuration, "onHeroSecondary"),
        onHeroMuted = animColor(target.onHeroMuted, false, effectiveDuration, "onHeroMuted"),
        heroCtaContainer = animColor(target.heroCtaContainer, false, effectiveDuration, "heroCtaContainer"),
        onHeroCta = animColor(target.onHeroCta, false, effectiveDuration, "onHeroCta"),
        heroBorder = animColor(target.heroBorder, false, effectiveDuration, "heroBorder"),
        calloutWhyBg = animColor(target.calloutWhyBg, false, effectiveDuration, "calloutWhyBg"),
        calloutWhyAccent = animColor(target.calloutWhyAccent, false, effectiveDuration, "calloutWhyAccent"),
        calloutMistakeBg = animColor(target.calloutMistakeBg, false, effectiveDuration, "calloutMistakeBg"),
        calloutMistakeAccent = animColor(target.calloutMistakeAccent, false, effectiveDuration, "calloutMistakeAccent"),
        calloutBody = animColor(target.calloutBody, false, effectiveDuration, "calloutBody"),
        inlineCodeBg = animColor(target.inlineCodeBg, false, effectiveDuration, "inlineCodeBg"),
        inlineCodeFg = animColor(target.inlineCodeFg, false, effectiveDuration, "inlineCodeFg"),
        glassSurface = animColor(target.glassSurface, false, effectiveDuration, "glassSurface"),
        glassBorder = animColor(target.glassBorder, false, effectiveDuration, "glassBorder"),
        feltStart = animColor(target.feltStart, false, effectiveDuration, "feltStart"),
        feltEnd = animColor(target.feltEnd, false, effectiveDuration, "feltEnd"),
        feltBorder = animColor(target.feltBorder, false, effectiveDuration, "feltBorder"),
        cardBorderSelected = animColor(target.cardBorderSelected, false, effectiveDuration, "cardBorderSelected")
    )
}
