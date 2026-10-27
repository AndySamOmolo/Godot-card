package com.kadicompanion.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import com.kadicompanion.data.local.datastore.FontFamilyPreset
import com.kadicompanion.data.local.datastore.HeadingWeightPreset
import com.kadicompanion.data.local.datastore.LineSpacingPreset

val CodeFontFamily = FontFamily.Monospace

val DisplayHero = TextStyle(
    fontWeight = FontWeight.Black,
    fontSize = 46.sp,
    lineHeight = 50.sp,
    letterSpacing = (-1.5).sp
)

private val ProseLineHeight = LineHeightStyle(
    alignment = LineHeightStyle.Alignment.Center,
    trim = LineHeightStyle.Trim.None
)

val AppTypography = Typography(
    displayLarge = TextStyle(fontWeight = FontWeight.Black, fontSize = 40.sp, lineHeight = 46.sp, letterSpacing = (-1).sp),
    displayMedium = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 34.sp, lineHeight = 40.sp, letterSpacing = (-0.75).sp),
    displaySmall = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 29.sp, lineHeight = 36.sp, letterSpacing = (-0.5).sp),
    headlineLarge = TextStyle(fontWeight = FontWeight.Bold, fontSize = 26.sp, lineHeight = 33.sp, letterSpacing = (-0.4).sp),
    headlineMedium = TextStyle(fontWeight = FontWeight.Bold, fontSize = 22.sp, lineHeight = 29.sp, letterSpacing = (-0.3).sp),
    headlineSmall = TextStyle(fontWeight = FontWeight.Bold, fontSize = 19.sp, lineHeight = 25.sp, letterSpacing = (-0.2).sp),
    titleLarge = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 18.sp, lineHeight = 24.sp, letterSpacing = (-0.2).sp),
    titleMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 16.sp, lineHeight = 22.sp),
    titleSmall = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 14.sp, lineHeight = 20.sp),
    bodyLarge = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 26.sp, lineHeightStyle = ProseLineHeight),
    bodyMedium = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.5.sp, lineHeight = 23.sp, lineHeightStyle = ProseLineHeight),
    bodySmall = TextStyle(fontWeight = FontWeight.Normal, fontSize = 12.5.sp, lineHeight = 18.sp),
    labelLarge = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.1.sp),
    labelMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.6.sp),
    labelSmall = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 11.sp, lineHeight = 15.sp, letterSpacing = 0.5.sp)
)

fun buildCustomizableTypography(
    fontScale: Float = 1.0f,
    fontFamilyPreset: FontFamilyPreset = FontFamilyPreset.SANS_SERIF,
    headingWeightPreset: HeadingWeightPreset = HeadingWeightPreset.BOLD,
    lineSpacingPreset: LineSpacingPreset = LineSpacingPreset.COMFORTABLE,
    dyslexiaFont: Boolean = false
): Typography {
    val s = fontScale.coerceIn(0.80f, 1.35f)
    val lh = lineSpacingPreset.multiplier
    val family = when (fontFamilyPreset) {
        FontFamilyPreset.SANS_SERIF,
        FontFamilyPreset.SANS_MEDIUM,
        FontFamilyPreset.SANS_COMPACT,
        FontFamilyPreset.SANS_WIDE -> FontFamily.SansSerif
        FontFamilyPreset.SERIF,
        FontFamilyPreset.SERIF_CRISP -> FontFamily.Serif
        FontFamilyPreset.MONOSPACE,
        FontFamilyPreset.MONO_COMPACT -> FontFamily.Monospace
        FontFamilyPreset.CURSIVE -> FontFamily.Cursive
        FontFamilyPreset.ROUNDED -> FontFamily.Default
    }

    val bodyWeightOverride: FontWeight? = when (fontFamilyPreset) {
        FontFamilyPreset.SANS_MEDIUM, FontFamilyPreset.SERIF_CRISP -> FontWeight.Medium
        else -> null
    }

    val presetTrackingDeltaSp: Float = when (fontFamilyPreset) {
        FontFamilyPreset.SANS_COMPACT, FontFamilyPreset.MONO_COMPACT -> -0.3f
        FontFamilyPreset.SANS_WIDE -> 0.35f
        FontFamilyPreset.ROUNDED -> 0.12f
        else -> 0f
    }

    val headingWeight = when (headingWeightPreset) {
        HeadingWeightPreset.MEDIUM -> FontWeight.Medium
        HeadingWeightPreset.SEMI_BOLD -> FontWeight.SemiBold
        HeadingWeightPreset.BOLD -> FontWeight.Bold
        HeadingWeightPreset.EXTRA_BOLD -> FontWeight.ExtraBold
        HeadingWeightPreset.BLACK -> FontWeight.Black
    }

    fun TextStyle.scaleStyle(isBody: Boolean = false, isHeading: Boolean = false): TextStyle {
        val scaledSize = (fontSize.value * s).sp
        val baseLh = lineHeight.value * s * (if (isBody) lh else 1.0f)
        val baseTrack = if (letterSpacing.isSp) letterSpacing.value else 0f
        val dysTrack = if (dyslexiaFont && isBody) 0.45f else 0f
        return copy(
            fontFamily = family,
            fontWeight = when {
                isHeading -> headingWeight
                isBody && bodyWeightOverride != null -> bodyWeightOverride
                else -> fontWeight
            },
            fontSize = scaledSize,
            lineHeight = baseLh.sp,
            letterSpacing = (baseTrack + presetTrackingDeltaSp + dysTrack).sp
        )
    }

    return Typography(
        displayLarge = AppTypography.displayLarge.scaleStyle(isHeading = true),
        displayMedium = AppTypography.displayMedium.scaleStyle(isHeading = true),
        displaySmall = AppTypography.displaySmall.scaleStyle(isHeading = true),
        headlineLarge = AppTypography.headlineLarge.scaleStyle(isHeading = true),
        headlineMedium = AppTypography.headlineMedium.scaleStyle(isHeading = true),
        headlineSmall = AppTypography.headlineSmall.scaleStyle(isHeading = true),
        titleLarge = AppTypography.titleLarge.scaleStyle(isHeading = true),
        titleMedium = AppTypography.titleMedium.scaleStyle(isHeading = true),
        titleSmall = AppTypography.titleSmall.scaleStyle(isHeading = true),
        bodyLarge = AppTypography.bodyLarge.scaleStyle(isBody = true),
        bodyMedium = AppTypography.bodyMedium.scaleStyle(isBody = true),
        bodySmall = AppTypography.bodySmall.scaleStyle(isBody = true),
        labelLarge = AppTypography.labelLarge.scaleStyle(),
        labelMedium = AppTypography.labelMedium.scaleStyle(),
        labelSmall = AppTypography.labelSmall.scaleStyle()
    )
}
