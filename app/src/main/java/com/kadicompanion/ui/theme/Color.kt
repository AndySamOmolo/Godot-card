package com.kadicompanion.ui.theme

import androidx.compose.ui.graphics.Color

/*
 * Kadi palette.
 *
 * The brand is built on three notes borrowed from a Kenyan card table:
 *   - Gold   (primary)   : lamplight over the table, progress, achievement
 *   - Teal   (secondary) : cool counterpoint for information and links
 *   - Crimson(tertiary)  : the red suits, penalties, urgency
 * Dark mode sits on an "ink" background so gold reads like light in a dark room;
 * light mode sits on warm ivory so it feels like paper rather than a hospital wall.
 */

// ---------------------------------------------------------------- dark scheme
val DarkBackground = Color(0xFF090D13)
val DarkSurface = Color(0xFF10161F)
val DarkSurfaceContainerLowest = Color(0xFF0B1017)
val DarkSurfaceContainerLow = Color(0xFF131A24)
val DarkSurfaceContainer = Color(0xFF171F2A)
val DarkSurfaceContainerHigh = Color(0xFF1E2733)
val DarkSurfaceContainerHighest = Color(0xFF25303D)
val DarkSurfaceVariant = Color(0xFF232C38)
val DarkInverseSurface = Color(0xFFE6EDF3)
val DarkInverseOnSurface = Color(0xFF10161F)
val DarkOnBackground = Color(0xFFECF2F8)
val DarkOnSurface = Color(0xFFECF2F8)
val DarkOnSurfaceVariant = Color(0xFFB6C0CC)

val DarkPrimary = Color(0xFFFFC44D)
val DarkOnPrimary = Color(0xFF2B1D00)
val DarkPrimaryContainer = Color(0xFF4A3300)
val DarkOnPrimaryContainer = Color(0xFFFFE1A6)

val DarkSecondary = Color(0xFF63DCEC)
val DarkOnSecondary = Color(0xFF00343B)
val DarkSecondaryContainer = Color(0xFF004E57)
val DarkOnSecondaryContainer = Color(0xFFB6F2FB)

val DarkTertiary = Color(0xFFFF6B77)
val DarkOnTertiary = Color(0xFF480009)
val DarkTertiaryContainer = Color(0xFF7A1420)
val DarkOnTertiaryContainer = Color(0xFFFFD9DC)

val DarkError = Color(0xFFFF8A8A)
val DarkOnError = Color(0xFF4A0004)
val DarkErrorContainer = Color(0xFF7A1417)
val DarkOnErrorContainer = Color(0xFFFFDAD8)

val DarkOutline = Color(0xFF44505F)
val DarkOutlineVariant = Color(0xFF2A333F)
val DarkScrim = Color(0xFF000000)

// --------------------------------------------------------------- light scheme
val LightBackground = Color(0xFFFCFAF5)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceContainerLowest = Color(0xFFFFFFFF)
val LightSurfaceContainerLow = Color(0xFFFBF7EF)
val LightSurfaceContainer = Color(0xFFF6F1E7)
val LightSurfaceContainerHigh = Color(0xFFF0EADD)
val LightSurfaceContainerHighest = Color(0xFFEAE3D4)
val LightSurfaceVariant = Color(0xFFF2EDE2)
val LightInverseSurface = Color(0xFF2B2A26)
val LightInverseOnSurface = Color(0xFFFCFAF5)
val LightOnBackground = Color(0xFF1B1A16)
val LightOnSurface = Color(0xFF1B1A16)
val LightOnSurfaceVariant = Color(0xFF56503F)

val LightPrimary = Color(0xFF9A6200)
val LightOnPrimary = Color(0xFFFFFFFF)
val LightPrimaryContainer = Color(0xFFFFE1A6)
val LightOnPrimaryContainer = Color(0xFF2B1D00)

val LightSecondary = Color(0xFF006874)
val LightOnSecondary = Color(0xFFFFFFFF)
val LightSecondaryContainer = Color(0xFFB6F2FB)
val LightOnSecondaryContainer = Color(0xFF00343B)

val LightTertiary = Color(0xFFB3261E)
val LightOnTertiary = Color(0xFFFFFFFF)
val LightTertiaryContainer = Color(0xFFFFD9DC)
val LightOnTertiaryContainer = Color(0xFF480009)

val LightError = Color(0xFFB3261E)
val LightOnError = Color(0xFFFFFFFF)
val LightErrorContainer = Color(0xFFFFDAD6)
val LightOnErrorContainer = Color(0xFF410002)

val LightOutline = Color(0xFF827A66)
val LightOutlineVariant = Color(0xFFDDD5C3)
val LightScrim = Color(0xFF000000)

// -------------------------------------------------- brand accents (both modes)
val BrandGradientStart = Color(0xFFFFC44D)
val BrandGradientMid = Color(0xFFFF8A3D)
val BrandGradientEnd = Color(0xFFE63946)

val SuccessGreen = Color(0xFF4CAF7D)
val OnSuccessGreen = Color(0xFF00391E)
val SuccessGreenLight = Color(0xFF1E7A4F)
val SuccessContainerDark = Color(0xFF10412C)
val SuccessContainerLight = Color(0xFFCDEEDB)

// ------------------------------------------------ hero text (always light/white)
/**
 * Hero panels use vivid gradients in both light and dark modes, so text on top
 * must always be white/light — the default Material `onPrimary` is dark brown
 * in dark mode which looks black on the gradient.
 */
val HeroTextPrimary = Color(0xFFFFFFFF)
val HeroTextSecondary = Color(0xDEFFFFFF) // 87% white
val HeroTextTertiary = Color(0xB3FFFFFF) // 70% white

// ------------------------------------------------ glow & glass accents
val GlowGold = Color(0xFFFFD666)
val GlowTeal = Color(0xFF7EE8F5)
val GlowCrimson = Color(0xFFFF8A94)

val CardGlassDark = Color(0x1AFFFFFF)     // 10% white overlay
val CardGlassLight = Color(0x0DFFFFFF)    // 5% white overlay
val GlassBorderDark = Color(0x26FFFFFF)   // 15% white border
val GlassBorderLight = Color(0x1AFFFFFF)  // 10% white border

val ShimmerGold = Color(0x33FFC44D)       // 20% gold for shimmer effects

object SyntaxColors {
    val darkKeyword = Color(0xFFFF7B72)
    val darkType = Color(0xFFFFA657)
    val darkString = Color(0xFFA5D6FF)
    val darkComment = Color(0xFF8B949E)
    val darkNumber = Color(0xFF79C0FF)
    val darkFunction = Color(0xFFD2A8FF)
    val darkPlain = Color(0xFFE6EDF3)
    val darkBackground = Color(0xFF0B1017)

    val lightKeyword = Color(0xFFCF222E)
    val lightType = Color(0xFF953800)
    val lightString = Color(0xFF0A3069)
    val lightComment = Color(0xFF6E7781)
    val lightNumber = Color(0xFF0550AE)
    val lightFunction = Color(0xFF8250DF)
    val lightPlain = Color(0xFF24292F)
    val lightBackground = Color(0xFFF7F4EC)
}

/**
 * Callout palettes. Both a dark and a light variant exist so the "why" and
 * "common mistake" boxes stay readable in either theme.
 */
object CalloutColors {
    val whyBackgroundDark = Color(0xFF12281D)
    val whyBorderDark = Color(0xFF3E7A55)
    val whyIconDark = Color(0xFF7BD88F)
    val whyBackgroundLight = Color(0xFFE6F4EA)
    val whyBorderLight = Color(0xFF9CCBAC)
    val whyIconLight = Color(0xFF1E7A4F)

    val mistakeBackgroundDark = Color(0xFF2C1518)
    val mistakeBorderDark = Color(0xFFB05252)
    val mistakeIconDark = Color(0xFFFF9F9F)
    val mistakeBackgroundLight = Color(0xFFFDECEC)
    val mistakeBorderLight = Color(0xFFE0A5A5)
    val mistakeIconLight = Color(0xFFB3261E)

    val bodyDark = Color(0xFFE6EDF3)
    val bodyLight = Color(0xFF2A2622)
}
