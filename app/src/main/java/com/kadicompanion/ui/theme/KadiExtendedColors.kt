package com.kadicompanion.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Extended semantic color tokens beyond Material 3's standard ColorScheme.
 * Covers status semantics, hero banners, callouts, code chrome, and Kadi tabletop visuals.
 */
@Immutable
data class KadiExtendedColors(
    val isDark: Boolean,

    // Semantic status roles
    val success: Color,
    val onSuccess: Color,
    val successContainer: Color,
    val onSuccessContainer: Color,

    val warning: Color,
    val onWarning: Color,
    val warningContainer: Color,
    val onWarningContainer: Color,

    val info: Color,
    val onInfo: Color,
    val infoContainer: Color,
    val onInfoContainer: Color,

    // Hero banner tokens
    val heroGradientStart: Color,
    val heroGradientMid: Color,
    val heroGradientEnd: Color,
    val onHeroPrimary: Color,
    val onHeroSecondary: Color,
    val onHeroMuted: Color,
    val heroCtaContainer: Color,
    val onHeroCta: Color,
    val heroBorder: Color,
    val bugHeroGradientStart: Color,
    val bugHeroGradientMid: Color,
    val bugHeroGradientEnd: Color,

    // Callout tokens
    val calloutWhyBg: Color,
    val calloutWhyAccent: Color,
    val calloutMistakeBg: Color,
    val calloutMistakeAccent: Color,
    val calloutBody: Color,

    // Code & window chrome tokens
    val inlineCodeBg: Color,
    val inlineCodeFg: Color,
    val glassSurface: Color,
    val glassBorder: Color,
    val windowDotClose: Color,
    val windowDotMin: Color,
    val windowDotMax: Color,

    // Kadi tabletop tokens
    val feltStart: Color,
    val feltEnd: Color,
    val feltBorder: Color,
    val cardStock: Color,
    val cardStockSelected: Color,
    val cardBorder: Color,
    val cardBorderSelected: Color,
    val cardSuitRed: Color,
    val cardSuitBlack: Color
)

internal fun defaultDarkExtendedColors(): KadiExtendedColors = KadiExtendedColors(
    isDark = true,
    success = Color(0xFF34D399),
    onSuccess = Color(0xFF022C22),
    successContainer = Color(0xFF064E3B),
    onSuccessContainer = Color(0xFFD1FAE5),
    warning = Color(0xFFFBBF24),
    onWarning = Color(0xFF3B1D00),
    warningContainer = Color(0xFF78350F),
    onWarningContainer = Color(0xFFFEF3C7),
    info = Color(0xFF38BDF8),
    onInfo = Color(0xFF062032),
    infoContainer = Color(0xFF0C4A6E),
    onInfoContainer = Color(0xFFE0F2FE),
    heroGradientStart = Color(0xFF0F172A),
    heroGradientMid = Color(0xFF1E3A8A),
    heroGradientEnd = Color(0xFF0369A1),
    onHeroPrimary = Color(0xFFFFFFFF),
    onHeroSecondary = Color(0xFFE2E8F0),
    onHeroMuted = Color(0xFFCBD5E1),
    heroCtaContainer = Color(0xFFFFFFFF),
    onHeroCta = Color(0xFF0F172A),
    heroBorder = Color(0x29FFFFFF),
    bugHeroGradientStart = Color(0xFF2A0A18),
    bugHeroGradientMid = Color(0xFF7F1D1D),
    bugHeroGradientEnd = Color(0xFFB91C1C),
    calloutWhyBg = Color(0xFF0C2922),
    calloutWhyAccent = Color(0xFF34D399),
    calloutMistakeBg = Color(0xFF2A1215),
    calloutMistakeAccent = Color(0xFFF87171),
    calloutBody = Color(0xFFE2E8F0),
    inlineCodeBg = Color(0xFF1E293B),
    inlineCodeFg = Color(0xFF38BDF8),
    glassSurface = Color(0x14FFFFFF),
    glassBorder = Color(0xFF24324B),
    windowDotClose = Color(0xFFEF4444),
    windowDotMin = Color(0xFFF59E0B),
    windowDotMax = Color(0xFF10B981),
    feltStart = Color(0xFF063B2D),
    feltEnd = Color(0xFF04261D),
    feltBorder = Color(0xFF10B981),
    cardStock = Color(0xFFF8FAFC),
    cardStockSelected = Color(0xFFFEF9C3),
    cardBorder = Color(0xFFCBD5E1),
    cardBorderSelected = Color(0xFFF59E0B),
    cardSuitRed = Color(0xFFDC2626),
    cardSuitBlack = Color(0xFF0F172A)
)

val LocalKadiColors = staticCompositionLocalOf { defaultDarkExtendedColors() }
