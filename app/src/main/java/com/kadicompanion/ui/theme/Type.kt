package com.kadicompanion.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import com.kadicompanion.data.local.datastore.FontFamilyPreset
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
    lineSpacingPreset: LineSpacingPreset = LineSpacingPreset.COMFORTABLE,
    dyslexiaFont: Boolean = false
): Typography {
    val s = fontScale.coerceIn(0.80f, 1.35f)
    val lh = lineSpacingPreset.multiplier
    val extraTracking = if (dyslexiaFont) 0.45.sp else 0.sp
    val family = when (fontFamilyPreset) {
        FontFamilyPreset.SANS_SERIF -> FontFamily.SansSerif
        FontFamilyPreset.SERIF -> FontFamily.Serif
        FontFamilyPreset.MONOSPACE -> FontFamily.Monospace
        FontFamilyPreset.ROUNDED -> FontFamily.Default
    }

    fun TextStyle.scaleStyle(isBody: Boolean = false): TextStyle {
        val scaledSize = (fontSize.value * s).sp
        val baseLh = lineHeight.value * s * (if (isBody) lh else 1.0f)
        val tracking = if (dyslexiaFont && isBody) extraTracking else letterSpacing
        return copy(
            fontFamily = family,
            fontSize = scaledSize,
            lineHeight = baseLh.sp,
            letterSpacing = tracking
        )
    }

    return Typography(
        displayLarge = AppTypography.displayLarge.scaleStyle(),
        displayMedium = AppTypography.displayMedium.scaleStyle(),
        displaySmall = AppTypography.displaySmall.scaleStyle(),
        headlineLarge = AppTypography.headlineLarge.scaleStyle(),
        headlineMedium = AppTypography.headlineMedium.scaleStyle(),
        headlineSmall = AppTypography.headlineSmall.scaleStyle(),
        titleLarge = AppTypography.titleLarge.scaleStyle(),
        titleMedium = AppTypography.titleMedium.scaleStyle(),
        titleSmall = AppTypography.titleSmall.scaleStyle(),
        bodyLarge = AppTypography.bodyLarge.scaleStyle(isBody = true),
        bodyMedium = AppTypography.bodyMedium.scaleStyle(isBody = true),
        bodySmall = AppTypography.bodySmall.scaleStyle(isBody = true),
        labelLarge = AppTypography.labelLarge.scaleStyle(),
        labelMedium = AppTypography.labelMedium.scaleStyle(),
        labelSmall = AppTypography.labelSmall.scaleStyle()
    )
}
