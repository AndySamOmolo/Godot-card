package com.kadicompanion.ui.theme

import androidx.compose.ui.graphics.Color

/*
 * Kadi Studio Palette — Redesigned from scratch.
 *
 * Inspired by Godot 4.3 Engine, Linear, and Raycast:
 * - Dark mode uses deep carbon-slate backgrounds (#0B0F17) and crisp elevated
 *   slate containers (#111827 / #161F30) with sharp hairline borders (#24324B).
 * - Light mode uses cool studio alabaster (#F8FAFC) and pure white surfaces
 *   (#FFFFFF) with clean slate borders (#E2E8F0).
 * - Accents:
 *   - Primary   (Godot Azure / Sapphire) : #38BDF8 (dark) / #2563EB (light)
 *   - Secondary (Emerald Mint)           : #10B981 (dark) / #059669 (light)
 *   - Tertiary  (Warm Amber)             : #F59E0B (dark) / #D97706 (light)
 */

// ---------------------------------------------------------------- dark scheme
val DarkBackground = Color(0xFF0B0F17)
val DarkSurface = Color(0xFF111827)
val DarkSurfaceContainerLowest = Color(0xFF080B11)
val DarkSurfaceContainerLow = Color(0xFF0F1623)
val DarkSurfaceContainer = Color(0xFF151E2E)
val DarkSurfaceContainerHigh = Color(0xFF1C273C)
val DarkSurfaceContainerHighest = Color(0xFF24324B)
val DarkSurfaceVariant = Color(0xFF1E293B)
val DarkInverseSurface = Color(0xFFF1F5F9)
val DarkInverseOnSurface = Color(0xFF0F172A)
val DarkOnBackground = Color(0xFFF1F5F9)
val DarkOnSurface = Color(0xFFF1F5F9)
val DarkOnSurfaceVariant = Color(0xFF94A3B8)

val DarkPrimary = Color(0xFF38BDF8)
val DarkOnPrimary = Color(0xFF062032)
val DarkPrimaryContainer = Color(0xFF0C4A6E)
val DarkOnPrimaryContainer = Color(0xFFE0F2FE)

val DarkSecondary = Color(0xFF10B981)
val DarkOnSecondary = Color(0xFF022C22)
val DarkSecondaryContainer = Color(0xFF064E3B)
val DarkOnSecondaryContainer = Color(0xFFD1FAE5)

val DarkTertiary = Color(0xFFF59E0B)
val DarkOnTertiary = Color(0xFF3B1D00)
val DarkTertiaryContainer = Color(0xFF78350F)
val DarkOnTertiaryContainer = Color(0xFFFEF3C7)

val DarkError = Color(0xFFF87171)
val DarkOnError = Color(0xFF450A0A)
val DarkErrorContainer = Color(0xFF7F1D1D)
val DarkOnErrorContainer = Color(0xFFFEE2E2)

val DarkOutline = Color(0xFF334155)
val DarkOutlineVariant = Color(0xFF24324B)
val DarkScrim = Color(0xFF000000)

// --------------------------------------------------------------- light scheme
val LightBackground = Color(0xFFF8FAFC)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceContainerLowest = Color(0xFFFFFFFF)
val LightSurfaceContainerLow = Color(0xFFF1F5F9)
val LightSurfaceContainer = Color(0xFFFFFFFF)
val LightSurfaceContainerHigh = Color(0xFFF1F5F9)
val LightSurfaceContainerHighest = Color(0xFFE2E8F0)
val LightSurfaceVariant = Color(0xFFE2E8F0)
val LightInverseSurface = Color(0xFF0F172A)
val LightInverseOnSurface = Color(0xFFF8FAFC)
val LightOnBackground = Color(0xFF0F172A)
val LightOnSurface = Color(0xFF0F172A)
val LightOnSurfaceVariant = Color(0xFF475569)

val LightPrimary = Color(0xFF2563EB)
val LightOnPrimary = Color(0xFFFFFFFF)
val LightPrimaryContainer = Color(0xFFDBEAFE)
val LightOnPrimaryContainer = Color(0xFF1E3A8A)

val LightSecondary = Color(0xFF059669)
val LightOnSecondary = Color(0xFFFFFFFF)
val LightSecondaryContainer = Color(0xFFD1FAE5)
val LightOnSecondaryContainer = Color(0xFF064E3B)

val LightTertiary = Color(0xFFD97706)
val LightOnTertiary = Color(0xFFFFFFFF)
val LightTertiaryContainer = Color(0xFFFEF3C7)
val LightOnTertiaryContainer = Color(0xFF78350F)

val LightError = Color(0xFFDC2626)
val LightOnError = Color(0xFFFFFFFF)
val LightErrorContainer = Color(0xFFFEE2E2)
val LightOnErrorContainer = Color(0xFF7F1D1D)

val LightOutline = Color(0xFF94A3B8)
val LightOutlineVariant = Color(0xFFE2E8F0)
val LightScrim = Color(0xFF000000)

// -------------------------------------------------- studio hero & status colors
val StudioHeroStart = Color(0xFF0F172A)
val StudioHeroMid = Color(0xFF1E3A8A)
val StudioHeroEnd = Color(0xFF0284C7)

val SuccessGreen = Color(0xFF10B981)
val OnSuccessGreen = Color(0xFF022C22)
val SuccessGreenLight = Color(0xFF059669)
val SuccessContainerDark = Color(0xFF064E3B)
val SuccessContainerLight = Color(0xFFD1FAE5)

// ------------------------------------------------ hero text (always crisp light)
val HeroTextPrimary = Color(0xFFFFFFFF)
val HeroTextSecondary = Color(0xE6F1F5F9)
val HeroTextTertiary = Color(0xB394A3B8)

// ------------------------------------------------ borders & glass accents
val CardGlassDark = Color(0x14FFFFFF)
val CardGlassLight = Color(0x0A0F172A)
val GlassBorderDark = Color(0xFF24324B)
val GlassBorderLight = Color(0xFFE2E8F0)

object SyntaxColors {
    val darkKeyword = Color(0xFFFF7B72)
    val darkType = Color(0xFFFFA657)
    val darkString = Color(0xFFA5D6FF)
    val darkComment = Color(0xFF8B949E)
    val darkNumber = Color(0xFF79C0FF)
    val darkFunction = Color(0xFFD2A8FF)
    val darkPlain = Color(0xFFE6EDF3)
    val darkBackground = Color(0xFF090D16)

    val lightKeyword = Color(0xFFCF222E)
    val lightType = Color(0xFF953800)
    val lightString = Color(0xFF0A3069)
    val lightComment = Color(0xFF6E7781)
    val lightNumber = Color(0xFF0550AE)
    val lightFunction = Color(0xFF8250DF)
    val lightPlain = Color(0xFF1E293B)
    val lightBackground = Color(0xFFF1F5F9)
}

object CalloutColors {
    val whyBackgroundDark = Color(0xFF0C2922)
    val whyBorderDark = Color(0xFF10B981)
    val whyIconDark = Color(0xFF34D399)
    val whyBackgroundLight = Color(0xFFECFDF5)
    val whyBorderLight = Color(0xFF10B981)
    val whyIconLight = Color(0xFF059669)

    val mistakeBackgroundDark = Color(0xFF2A1215)
    val mistakeBorderDark = Color(0xFFF87171)
    val mistakeIconDark = Color(0xFFFCA5A5)
    val mistakeBackgroundLight = Color(0xFFFEF2F2)
    val mistakeBorderLight = Color(0xFFEF4444)
    val mistakeIconLight = Color(0xFFDC2626)

    val bodyDark = Color(0xFFE2E8F0)
    val bodyLight = Color(0xFF1E293B)
}
