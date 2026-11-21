package com.kadicompanion.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.kadicompanion.data.local.datastore.AccentPreset
import com.kadicompanion.data.local.datastore.ContrastLevel
import com.kadicompanion.data.local.datastore.HomeHeroPalette
import com.kadicompanion.data.local.datastore.KadiFeltPreset
import com.kadicompanion.data.local.datastore.ThemePref
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

/**
 * Complete opaque, contrast-verified Material 3 accent role specification
 * for a single AccentPreset across both Dark and Light modes.
 */
@Immutable
data class AccentRoleSpec(
    val darkPrimary: Color,
    val darkOnPrimary: Color,
    val darkPrimaryContainer: Color,
    val darkOnPrimaryContainer: Color,
    val darkSecondary: Color,
    val darkOnSecondary: Color,
    val darkSecondaryContainer: Color,
    val darkOnSecondaryContainer: Color,
    val darkTertiary: Color,
    val darkOnTertiary: Color,
    val darkTertiaryContainer: Color,
    val darkOnTertiaryContainer: Color,
    val lightPrimary: Color,
    val lightOnPrimary: Color,
    val lightPrimaryContainer: Color,
    val lightOnPrimaryContainer: Color,
    val lightSecondary: Color,
    val lightOnSecondary: Color,
    val lightSecondaryContainer: Color,
    val lightOnSecondaryContainer: Color,
    val lightTertiary: Color,
    val lightOnTertiary: Color,
    val lightTertiaryContainer: Color,
    val lightOnTertiaryContainer: Color,
    val darkHeroStops: List<Color>,
    val lightHeroStops: List<Color>,
    val swatch: Color
)

/**
 * Pure, deterministic, JVM-testable theme resolution engine.
 */
object ThemeEngine {

    fun accentSpec(preset: AccentPreset): AccentRoleSpec = when (preset) {
        AccentPreset.GODOT_SAPPHIRE -> AccentRoleSpec(
            darkPrimary = Color(0xFF38BDF8),
            darkOnPrimary = Color(0xFF041D2E),
            darkPrimaryContainer = Color(0xFF0C4A6E),
            darkOnPrimaryContainer = Color(0xFFE0F2FE),
            darkSecondary = Color(0xFF34D399),
            darkOnSecondary = Color(0xFF022C22),
            darkSecondaryContainer = Color(0xFF064E3B),
            darkOnSecondaryContainer = Color(0xFFD1FAE5),
            darkTertiary = Color(0xFFFBBF24),
            darkOnTertiary = Color(0xFF331800),
            darkTertiaryContainer = Color(0xFF78350F),
            darkOnTertiaryContainer = Color(0xFFFEF3C7),
            lightPrimary = Color(0xFF1D4ED8),
            lightOnPrimary = Color(0xFFFFFFFF),
            lightPrimaryContainer = Color(0xFFDBEAFE),
            lightOnPrimaryContainer = Color(0xFF1E3A8A),
            lightSecondary = Color(0xFF047857),
            lightOnSecondary = Color(0xFFFFFFFF),
            lightSecondaryContainer = Color(0xFFD1FAE5),
            lightOnSecondaryContainer = Color(0xFF064E3B),
            lightTertiary = Color(0xFFB45309),
            lightOnTertiary = Color(0xFFFFFFFF),
            lightTertiaryContainer = Color(0xFFFEF3C7),
            lightOnTertiaryContainer = Color(0xFF78350F),
            darkHeroStops = listOf(Color(0xFF0B1324), Color(0xFF163269), Color(0xFF035388)),
            lightHeroStops = listOf(Color(0xFF0F172A), Color(0xFF1E3A8A), Color(0xFF0369A1)),
            swatch = Color(0xFF38BDF8)
        )

        AccentPreset.NAIROBI_GOLD -> AccentRoleSpec(
            darkPrimary = Color(0xFFFBBF24),
            darkOnPrimary = Color(0xFF2B1400),
            darkPrimaryContainer = Color(0xFF6B320B),
            darkOnPrimaryContainer = Color(0xFFFEF3C7),
            darkSecondary = Color(0xFF34D399),
            darkOnSecondary = Color(0xFF022C22),
            darkSecondaryContainer = Color(0xFF064E3B),
            darkOnSecondaryContainer = Color(0xFFD1FAE5),
            darkTertiary = Color(0xFFFB7185),
            darkOnTertiary = Color(0xFF3B0514),
            darkTertiaryContainer = Color(0xFF7F1230),
            darkOnTertiaryContainer = Color(0xFFFFE4E6),
            lightPrimary = Color(0xFF9A3412),
            lightOnPrimary = Color(0xFFFFFFFF),
            lightPrimaryContainer = Color(0xFFFEF3C7),
            lightOnPrimaryContainer = Color(0xFF6B2806),
            lightSecondary = Color(0xFF047857),
            lightOnSecondary = Color(0xFFFFFFFF),
            lightSecondaryContainer = Color(0xFFD1FAE5),
            lightOnSecondaryContainer = Color(0xFF064E3B),
            lightTertiary = Color(0xFFBE123C),
            lightOnTertiary = Color(0xFFFFFFFF),
            lightTertiaryContainer = Color(0xFFFFE4E6),
            lightOnTertiaryContainer = Color(0xFF881337),
            darkHeroStops = listOf(Color(0xFF1C1612), Color(0xFF5C280B), Color(0xFF92400E)),
            lightHeroStops = listOf(Color(0xFF291709), Color(0xFF78350F), Color(0xFFB45309)),
            swatch = Color(0xFFFBBF24)
        )

        AccentPreset.EMERALD_FELT -> AccentRoleSpec(
            darkPrimary = Color(0xFF34D399),
            darkOnPrimary = Color(0xFF02261D),
            darkPrimaryContainer = Color(0xFF064E3B),
            darkOnPrimaryContainer = Color(0xFFD1FAE5),
            darkSecondary = Color(0xFFFBBF24),
            darkOnSecondary = Color(0xFF2E1600),
            darkSecondaryContainer = Color(0xFF713F12),
            darkOnSecondaryContainer = Color(0xFFFEF3C7),
            darkTertiary = Color(0xFF38BDF8),
            darkOnTertiary = Color(0xFF041D2E),
            darkTertiaryContainer = Color(0xFF0C4A6E),
            darkOnTertiaryContainer = Color(0xFFE0F2FE),
            lightPrimary = Color(0xFF047857),
            lightOnPrimary = Color(0xFFFFFFFF),
            lightPrimaryContainer = Color(0xFFD1FAE5),
            lightOnPrimaryContainer = Color(0xFF064E3B),
            lightSecondary = Color(0xFFB45309),
            lightOnSecondary = Color(0xFFFFFFFF),
            lightSecondaryContainer = Color(0xFFFEF3C7),
            lightOnSecondaryContainer = Color(0xFF78350F),
            lightTertiary = Color(0xFF0369A1),
            lightOnTertiary = Color(0xFFFFFFFF),
            lightTertiaryContainer = Color(0xFFE0F2FE),
            lightOnTertiaryContainer = Color(0xFF0C4A6E),
            darkHeroStops = listOf(Color(0xFF03231D), Color(0xFF064E3B), Color(0xFF047857)),
            lightHeroStops = listOf(Color(0xFF042F2E), Color(0xFF065F46), Color(0xFF047857)),
            swatch = Color(0xFF34D399)
        )

        AccentPreset.CYBER_VIOLET -> AccentRoleSpec(
            darkPrimary = Color(0xFFC084FC),
            darkOnPrimary = Color(0xFF22053D),
            darkPrimaryContainer = Color(0xFF4C1D95),
            darkOnPrimaryContainer = Color(0xFFF3E8FF),
            darkSecondary = Color(0xFF22D3EE),
            darkOnSecondary = Color(0xFF03262E),
            darkSecondaryContainer = Color(0xFF164E63),
            darkOnSecondaryContainer = Color(0xFFCFFAFE),
            darkTertiary = Color(0xFFF472B6),
            darkOnTertiary = Color(0xFF3B0422),
            darkTertiaryContainer = Color(0xFF831843),
            darkOnTertiaryContainer = Color(0xFFFCE7F3),
            lightPrimary = Color(0xFF6D28D9),
            lightOnPrimary = Color(0xFFFFFFFF),
            lightPrimaryContainer = Color(0xFFEDE9FE),
            lightOnPrimaryContainer = Color(0xFF4C1D95),
            lightSecondary = Color(0xFF0E7490),
            lightOnSecondary = Color(0xFFFFFFFF),
            lightSecondaryContainer = Color(0xFFCFFAFE),
            lightOnSecondaryContainer = Color(0xFF164E63),
            lightTertiary = Color(0xFFBE185D),
            lightOnTertiary = Color(0xFFFFFFFF),
            lightTertiaryContainer = Color(0xFFFCE7F3),
            lightOnTertiaryContainer = Color(0xFF831843),
            darkHeroStops = listOf(Color(0xFF16133B), Color(0xFF3B1578), Color(0xFF6D28D9)),
            lightHeroStops = listOf(Color(0xFF1E1B4B), Color(0xFF4C1D95), Color(0xFF6D28D9)),
            swatch = Color(0xFFC084FC)
        )

        AccentPreset.CRIMSON_ACE -> AccentRoleSpec(
            darkPrimary = Color(0xFFFB7185),
            darkOnPrimary = Color(0xFF3A0412),
            darkPrimaryContainer = Color(0xFF7F1230),
            darkOnPrimaryContainer = Color(0xFFFFE4E6),
            darkSecondary = Color(0xFFFBBF24),
            darkOnSecondary = Color(0xFF2E1600),
            darkSecondaryContainer = Color(0xFF78350F),
            darkOnSecondaryContainer = Color(0xFFFEF3C7),
            darkTertiary = Color(0xFF93A5FF),
            darkOnTertiary = Color(0xFF0D1445),
            darkTertiaryContainer = Color(0xFF312E81),
            darkOnTertiaryContainer = Color(0xFFE0E7FF),
            lightPrimary = Color(0xFFBE123C),
            lightOnPrimary = Color(0xFFFFFFFF),
            lightPrimaryContainer = Color(0xFFFFE4E6),
            lightOnPrimaryContainer = Color(0xFF881337),
            lightSecondary = Color(0xFFB45309),
            lightOnSecondary = Color(0xFFFFFFFF),
            lightSecondaryContainer = Color(0xFFFEF3C7),
            lightOnSecondaryContainer = Color(0xFF78350F),
            lightTertiary = Color(0xFF4338CA),
            lightOnTertiary = Color(0xFFFFFFFF),
            lightTertiaryContainer = Color(0xFFE0E7FF),
            lightOnTertiaryContainer = Color(0xFF312E81),
            darkHeroStops = listOf(Color(0xFF260815), Color(0xFF70102D), Color(0xFFBE123C)),
            lightHeroStops = listOf(Color(0xFF36091B), Color(0xFF881337), Color(0xFFBE123C)),
            swatch = Color(0xFFFB7185)
        )

        AccentPreset.SUNSET_ORANGE -> AccentRoleSpec(
            darkPrimary = Color(0xFFFB923C),
            darkOnPrimary = Color(0xFF2D1102),
            darkPrimaryContainer = Color(0xFF7C2D12),
            darkOnPrimaryContainer = Color(0xFFFFEDD5),
            darkSecondary = Color(0xFF2DD4BF),
            darkOnSecondary = Color(0xFF032622),
            darkSecondaryContainer = Color(0xFF115E59),
            darkOnSecondaryContainer = Color(0xFFCCFBF1),
            darkTertiary = Color(0xFFFACC15),
            darkOnTertiary = Color(0xFF2E2200),
            darkTertiaryContainer = Color(0xFF713F12),
            darkOnTertiaryContainer = Color(0xFFFEF9C3),
            lightPrimary = Color(0xFFC2410C),
            lightOnPrimary = Color(0xFFFFFFFF),
            lightPrimaryContainer = Color(0xFFFFEDD5),
            lightOnPrimaryContainer = Color(0xFF7C2D12),
            lightSecondary = Color(0xFF0F766E),
            lightOnSecondary = Color(0xFFFFFFFF),
            lightSecondaryContainer = Color(0xFFCCFBF1),
            lightOnSecondaryContainer = Color(0xFF115E59),
            lightTertiary = Color(0xFFA16207),
            lightOnTertiary = Color(0xFFFFFFFF),
            lightTertiaryContainer = Color(0xFFFEF9C3),
            lightOnTertiaryContainer = Color(0xFF713F12),
            darkHeroStops = listOf(Color(0xFF231005), Color(0xFF6B260E), Color(0xFFC2410C)),
            lightHeroStops = listOf(Color(0xFF2B1306), Color(0xFF7C2D12), Color(0xFFC2410C)),
            swatch = Color(0xFFFB923C)
        )

        AccentPreset.MONOCHROME_SLATE -> AccentRoleSpec(
            darkPrimary = Color(0xFFE2E8F0),
            darkOnPrimary = Color(0xFF0F172A),
            darkPrimaryContainer = Color(0xFF334155),
            darkOnPrimaryContainer = Color(0xFFF8FAFC),
            darkSecondary = Color(0xFFCBD5E1),
            darkOnSecondary = Color(0xFF0F172A),
            darkSecondaryContainer = Color(0xFF1E293B),
            darkOnSecondaryContainer = Color(0xFFF1F5F9),
            darkTertiary = Color(0xFF94A3B8),
            darkOnTertiary = Color(0xFF090D16),
            darkTertiaryContainer = Color(0xFF1E293B),
            darkOnTertiaryContainer = Color(0xFFE2E8F0),
            lightPrimary = Color(0xFF0F172A),
            lightOnPrimary = Color(0xFFFFFFFF),
            lightPrimaryContainer = Color(0xFFE2E8F0),
            lightOnPrimaryContainer = Color(0xFF0F172A),
            lightSecondary = Color(0xFF334155),
            lightOnSecondary = Color(0xFFFFFFFF),
            lightSecondaryContainer = Color(0xFFF1F5F9),
            lightOnSecondaryContainer = Color(0xFF1E293B),
            lightTertiary = Color(0xFF475569),
            lightOnTertiary = Color(0xFFFFFFFF),
            lightTertiaryContainer = Color(0xFFE2E8F0),
            lightOnTertiaryContainer = Color(0xFF1E293B),
            darkHeroStops = listOf(Color(0xFF090D16), Color(0xFF1E293B), Color(0xFF334155)),
            lightHeroStops = listOf(Color(0xFF0F172A), Color(0xFF1E293B), Color(0xFF334155)),
            swatch = Color(0xFF94A3B8)
        )
    }

    /**
     * W3C WCAG 2.1 relative luminance contrast ratio in [1.0, 21.0].
     */
    fun contrastRatio(foreground: Color, background: Color): Double {
        val l1 = relativeLuminance(foreground)
        val l2 = relativeLuminance(background)
        val lighter = max(l1, l2)
        val darker = min(l1, l2)
        return (lighter + 0.05) / (darker + 0.05)
    }

    private fun relativeLuminance(color: Color): Double {
        fun linearize(channel: Float): Double {
            val c = channel.toDouble().coerceIn(0.0, 1.0)
            return if (c <= 0.03928) c / 12.92 else ((c + 0.055) / 1.055).pow(2.4)
        }
        return 0.2126 * linearize(color.red) +
            0.7152 * linearize(color.green) +
            0.0722 * linearize(color.blue)
    }

    fun isDarkTheme(themePref: ThemePref, systemDark: Boolean): Boolean = when (themePref) {
        ThemePref.DARK, ThemePref.AMOLED -> true
        ThemePref.LIGHT -> false
        ThemePref.SYSTEM -> systemDark
    }

    /**
     * Resolves the Material 3 ColorScheme with:
     * - Opaque, contrast-verified primary/secondary/tertiary containers (zero alpha hacks)
     * - Dynamic Color + AMOLED preservation (keeps dynamic accents while applying true #000000 OLED surfaces)
     * - WCAG AA (>= 4.5:1) across all modes/presets and AAA (>= 7.0:1) on primary text/surfaces in HIGH contrast
     */
    fun resolveColorScheme(
        themePref: ThemePref,
        accentPreset: AccentPreset,
        contrastLevel: ContrastLevel,
        systemDark: Boolean,
        dynamicColorScheme: ColorScheme? = null
    ): ColorScheme {
        val isDark = isDarkTheme(themePref, systemDark)
        val isAmoled = themePref == ThemePref.AMOLED

        val base = if (dynamicColorScheme != null) {
            // Preserve dynamic accents; fix Material You + AMOLED by overriding only background/surfaces
            if (isDark && isAmoled) {
                applyAmoledSurfaces(dynamicColorScheme)
            } else {
                dynamicColorScheme.copy(surfaceTint = Color.Transparent)
            }
        } else {
            val spec = accentSpec(accentPreset)
            val built = if (isDark) {
                darkColorScheme(
                    primary = spec.darkPrimary,
                    onPrimary = spec.darkOnPrimary,
                    primaryContainer = spec.darkPrimaryContainer,
                    onPrimaryContainer = spec.darkOnPrimaryContainer,
                    secondary = spec.darkSecondary,
                    onSecondary = spec.darkOnSecondary,
                    secondaryContainer = spec.darkSecondaryContainer,
                    onSecondaryContainer = spec.darkOnSecondaryContainer,
                    tertiary = spec.darkTertiary,
                    onTertiary = spec.darkOnTertiary,
                    tertiaryContainer = spec.darkTertiaryContainer,
                    onTertiaryContainer = spec.darkOnTertiaryContainer,
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
            } else {
                lightColorScheme(
                    primary = spec.lightPrimary,
                    onPrimary = spec.lightOnPrimary,
                    primaryContainer = spec.lightPrimaryContainer,
                    onPrimaryContainer = spec.lightOnPrimaryContainer,
                    secondary = spec.lightSecondary,
                    onSecondary = spec.lightOnSecondary,
                    secondaryContainer = spec.lightSecondaryContainer,
                    onSecondaryContainer = spec.lightOnSecondaryContainer,
                    tertiary = spec.lightTertiary,
                    onTertiary = spec.lightOnTertiary,
                    tertiaryContainer = spec.lightTertiaryContainer,
                    onTertiaryContainer = spec.lightOnTertiaryContainer,
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
            }
            if (isDark && isAmoled) applyAmoledSurfaces(built) else built
        }

        return applyContrastLevel(
            scheme = base,
            isDark = isDark,
            isAmoled = isAmoled,
            contrastLevel = contrastLevel
        )
    }

    private fun applyAmoledSurfaces(scheme: ColorScheme): ColorScheme = scheme.copy(
        background = Color(0xFF000000),
        surface = Color(0xFF05070B),
        surfaceContainerLowest = Color(0xFF000000),
        surfaceContainerLow = Color(0xFF070A10),
        surfaceContainer = Color(0xFF0C111B),
        surfaceContainerHigh = Color(0xFF131B29),
        surfaceContainerHighest = Color(0xFF1B2537),
        surfaceTint = Color.Transparent,
        outlineVariant = Color(0xFF223047)
    )

    private fun applyContrastLevel(
        scheme: ColorScheme,
        isDark: Boolean,
        isAmoled: Boolean,
        contrastLevel: ContrastLevel
    ): ColorScheme = when (contrastLevel) {
        ContrastLevel.STANDARD -> scheme
        ContrastLevel.MEDIUM -> if (isDark) {
            scheme.copy(
                onBackground = Color(0xFFF8FAFC),
                onSurface = Color(0xFFF8FAFC),
                onSurfaceVariant = Color(0xFFCBD5E1),
                outline = Color(0xFF64748B),
                outlineVariant = Color(0xFF3E4F69),
                primary = ensureMinContrast(scheme.primary, scheme.onPrimary, targetRatio = 6.0, lighten = true),
                error = Color(0xFFFCA5A5),
                onError = Color(0xFF330606)
            )
        } else {
            scheme.copy(
                onBackground = Color(0xFF090D16),
                onSurface = Color(0xFF090D16),
                onSurfaceVariant = Color(0xFF334155),
                outline = Color(0xFF475569),
                outlineVariant = Color(0xFF94A3B8),
                primary = ensureMinContrast(scheme.primary, Color.White, targetRatio = 6.0, lighten = false),
                error = Color(0xFFB91C1C),
                onError = Color.White
            )
        }

        ContrastLevel.HIGH -> if (isDark) {
            val highPrimary = ensureMinContrast(scheme.primary, Color(0xFF000000), targetRatio = 8.5, lighten = true)
            val highSecondary = ensureMinContrast(scheme.secondary, Color(0xFF000000), targetRatio = 8.5, lighten = true)
            val highTertiary = ensureMinContrast(scheme.tertiary, Color(0xFF000000), targetRatio = 8.5, lighten = true)
            val highPrimaryContainer = ensureMinContrast(scheme.primaryContainer, Color.White, targetRatio = 8.0, lighten = false)
            val highSecondaryContainer = ensureMinContrast(scheme.secondaryContainer, Color.White, targetRatio = 8.0, lighten = false)
            val highTertiaryContainer = ensureMinContrast(scheme.tertiaryContainer, Color.White, targetRatio = 8.0, lighten = false)
            scheme.copy(
                background = if (isAmoled) Color(0xFF000000) else Color(0xFF04060A),
                surface = if (isAmoled) Color(0xFF000000) else Color(0xFF0A0F18),
                surfaceContainerLowest = Color(0xFF000000),
                surfaceContainerLow = Color(0xFF080C14),
                surfaceContainer = Color(0xFF0E1522),
                surfaceContainerHigh = Color(0xFF151F30),
                surfaceContainerHighest = Color(0xFF1E2B42),
                onBackground = Color(0xFFFFFFFF),
                onSurface = Color(0xFFFFFFFF),
                onSurfaceVariant = Color(0xFFE2E8F0),
                primary = highPrimary,
                onPrimary = Color(0xFF000000),
                primaryContainer = highPrimaryContainer,
                onPrimaryContainer = Color(0xFFFFFFFF),
                secondary = highSecondary,
                onSecondary = Color(0xFF000000),
                secondaryContainer = highSecondaryContainer,
                onSecondaryContainer = Color(0xFFFFFFFF),
                tertiary = highTertiary,
                onTertiary = Color(0xFF000000),
                tertiaryContainer = highTertiaryContainer,
                onTertiaryContainer = Color(0xFFFFFFFF),
                error = Color(0xFFFFB4B4),
                onError = Color(0xFF000000),
                errorContainer = Color(0xFF5C0A0A),
                onErrorContainer = Color(0xFFFFFFFF),
                outline = Color(0xFFCBD5E1),
                outlineVariant = Color(0xFF64748B)
            )
        } else {
            val highPrimary = ensureMinContrast(scheme.primary, Color.White, targetRatio = 7.5, lighten = false)
            val highSecondary = ensureMinContrast(scheme.secondary, Color.White, targetRatio = 7.5, lighten = false)
            val highTertiary = ensureMinContrast(scheme.tertiary, Color.White, targetRatio = 7.5, lighten = false)
            val highPrimaryContainer = ensureMinContrast(scheme.primaryContainer, Color(0xFF000000), targetRatio = 8.5, lighten = true)
            val highSecondaryContainer = ensureMinContrast(scheme.secondaryContainer, Color(0xFF000000), targetRatio = 8.5, lighten = true)
            val highTertiaryContainer = ensureMinContrast(scheme.tertiaryContainer, Color(0xFF000000), targetRatio = 8.5, lighten = true)
            scheme.copy(
                background = Color(0xFFFFFFFF),
                surface = Color(0xFFFFFFFF),
                surfaceContainerLowest = Color(0xFFFFFFFF),
                surfaceContainerLow = Color(0xFFF8FAFC),
                surfaceContainer = Color(0xFFF1F5F9),
                surfaceContainerHigh = Color(0xFFE2E8F0),
                surfaceContainerHighest = Color(0xFFCBD5E1),
                onBackground = Color(0xFF000000),
                onSurface = Color(0xFF000000),
                onSurfaceVariant = Color(0xFF1E293B),
                primary = highPrimary,
                onPrimary = Color(0xFFFFFFFF),
                primaryContainer = highPrimaryContainer,
                onPrimaryContainer = Color(0xFF000000),
                secondary = highSecondary,
                onSecondary = Color(0xFFFFFFFF),
                secondaryContainer = highSecondaryContainer,
                onSecondaryContainer = Color(0xFF000000),
                tertiary = highTertiary,
                onTertiary = Color(0xFFFFFFFF),
                tertiaryContainer = highTertiaryContainer,
                onTertiaryContainer = Color(0xFF000000),
                error = Color(0xFF991B1B),
                onError = Color(0xFFFFFFFF),
                errorContainer = Color(0xFFFEE2E2),
                onErrorContainer = Color(0xFF000000),
                outline = Color(0xFF0F172A),
                outlineVariant = Color(0xFF475569)
            )
        }
    }

    /**
     * Adjusts a color toward white (lighten=true) or black (lighten=false) in small linear steps
     * until contrastRatio(adjusted, reference) >= targetRatio.
     */
    private fun ensureMinContrast(
        color: Color,
        reference: Color,
        targetRatio: Double,
        lighten: Boolean
    ): Color {
        if (contrastRatio(color, reference) >= targetRatio) return color
        val target = if (lighten) Color.White else Color.Black
        var best = color
        for (step in 1..20) {
            val t = step / 20f
            val candidate = lerpColor(color, target, t)
            best = candidate
            if (contrastRatio(candidate, reference) >= targetRatio) return candidate
        }
        return best
    }

    private fun lerpColor(start: Color, end: Color, fraction: Float): Color {
        val f = fraction.coerceIn(0f, 1f)
        return Color(
            red = start.red + (end.red - start.red) * f,
            green = start.green + (end.green - start.green) * f,
            blue = start.blue + (end.blue - start.blue) * f,
            alpha = start.alpha + (end.alpha - start.alpha) * f
        )
    }

    /**
     * Resolves all extended semantic tokens, hero gradients, callouts, code chrome, and tabletop tokens.
     */
    @Immutable
    data class FeltVisualTokens(
        val feltStart: Color,
        val feltEnd: Color,
        val feltBorder: Color,
        val cardBorderSelected: Color
    )

    fun resolveFeltTokens(
        preset: KadiFeltPreset,
        colorScheme: ColorScheme,
        isDark: Boolean
    ): FeltVisualTokens = when (preset) {
        KadiFeltPreset.CLASSIC_EMERALD -> FeltVisualTokens(
            feltStart = if (isDark) Color(0xFF063B2D) else Color(0xFF065F46),
            feltEnd = if (isDark) Color(0xFF04261D) else Color(0xFF064E3B),
            feltBorder = if (isDark) Color(0xFF10B981) else Color(0xFF047857),
            cardBorderSelected = Color(0xFFD97706)
        )
        KadiFeltPreset.MIDNIGHT_NAVY -> FeltVisualTokens(
            feltStart = if (isDark) Color(0xFF0F1E3A) else Color(0xFF1E3A5F),
            feltEnd = if (isDark) Color(0xFF091226) else Color(0xFF152A47),
            feltBorder = if (isDark) Color(0xFF38BDF8) else Color(0xFF0284C7),
            cardBorderSelected = Color(0xFF38BDF8)
        )
        KadiFeltPreset.ROYAL_VELVET -> FeltVisualTokens(
            feltStart = if (isDark) Color(0xFF2B1242) else Color(0xFF4C1D70),
            feltEnd = if (isDark) Color(0xFF1A092B) else Color(0xFF361254),
            feltBorder = if (isDark) Color(0xFFC084FC) else Color(0xFF9333EA),
            cardBorderSelected = Color(0xFFFBBF24)
        )
        KadiFeltPreset.OBSIDIAN_GOLD -> FeltVisualTokens(
            feltStart = if (isDark) Color(0xFF16181F) else Color(0xFF272A34),
            feltEnd = if (isDark) Color(0xFF0B0D12) else Color(0xFF1A1D24),
            feltBorder = if (isDark) Color(0xFFF59E0B) else Color(0xFFD97706),
            cardBorderSelected = Color(0xFFFBBF24)
        )
        KadiFeltPreset.MATCH_THEME -> FeltVisualTokens(
            feltStart = lerpColor(colorScheme.primaryContainer, Color.Black, if (isDark) 0.55f else 0.35f),
            feltEnd = lerpColor(colorScheme.primaryContainer, Color.Black, if (isDark) 0.78f else 0.55f),
            feltBorder = colorScheme.primary,
            cardBorderSelected = colorScheme.tertiary
        )
    }

    fun resolveExtendedColors(
        colorScheme: ColorScheme,
        accentPreset: AccentPreset,
        contrastLevel: ContrastLevel,
        isDark: Boolean,
        isAmoled: Boolean,
        isDynamic: Boolean,
        kadiFeltPreset: KadiFeltPreset = KadiFeltPreset.CLASSIC_EMERALD
    ): KadiExtendedColors {
        val felt = resolveFeltTokens(kadiFeltPreset, colorScheme, isDark)
        val spec = accentSpec(accentPreset)
        val high = contrastLevel == ContrastLevel.HIGH

        val heroStops = if (isDynamic) {
            val start = lerpColor(colorScheme.primaryContainer, Color.Black, if (isDark) 0.55f else 0.72f)
            val mid = lerpColor(colorScheme.primary, Color.Black, 0.52f)
            val end = lerpColor(colorScheme.tertiary, Color.Black, 0.45f)
            listOf(start, mid, end)
        } else {
            if (isDark) spec.darkHeroStops else spec.lightHeroStops
        }

        val success = when {
            isDark && high -> Color(0xFF6EE7B7)
            isDark -> Color(0xFF34D399)
            high -> Color(0xFF065F46)
            else -> Color(0xFF047857)
        }
        val onSuccess = if (isDark) Color(0xFF02261D) else Color(0xFFFFFFFF)
        val successContainer = when {
            isDark && high -> Color(0xFF043B2C)
            isDark -> Color(0xFF064E3B)
            else -> Color(0xFFD1FAE5)
        }
        val onSuccessContainer = if (isDark) Color(0xFFFFFFFF) else Color(0xFF022C22)

        val warning = when {
            isDark && high -> Color(0xFFFDE047)
            isDark -> Color(0xFFFBBF24)
            high -> Color(0xFF78350F)
            else -> Color(0xFFB45309)
        }
        val onWarning = if (isDark) Color(0xFF2B1400) else Color(0xFFFFFFFF)
        val warningContainer = when {
            isDark && high -> Color(0xFF522306)
            isDark -> Color(0xFF78350F)
            else -> Color(0xFFFEF3C7)
        }
        val onWarningContainer = if (isDark) Color(0xFFFFFFFF) else Color(0xFF451A03)

        val info = when {
            isDark && high -> Color(0xFF7DD3FC)
            isDark -> Color(0xFF38BDF8)
            high -> Color(0xFF0C4A6E)
            else -> Color(0xFF0369A1)
        }
        val onInfo = if (isDark) Color(0xFF041D2E) else Color(0xFFFFFFFF)
        val infoContainer = when {
            isDark && high -> Color(0xFF082F49)
            isDark -> Color(0xFF0C4A6E)
            else -> Color(0xFFE0F2FE)
        }
        val onInfoContainer = if (isDark) Color(0xFFFFFFFF) else Color(0xFF082F49)

        return KadiExtendedColors(
            isDark = isDark,
            success = success,
            onSuccess = onSuccess,
            successContainer = successContainer,
            onSuccessContainer = onSuccessContainer,
            warning = warning,
            onWarning = onWarning,
            warningContainer = warningContainer,
            onWarningContainer = onWarningContainer,
            info = info,
            onInfo = onInfo,
            infoContainer = infoContainer,
            onInfoContainer = onInfoContainer,
            heroGradientStart = heroStops[0],
            heroGradientMid = heroStops[1],
            heroGradientEnd = heroStops[2],
            onHeroPrimary = Color(0xFFFFFFFF),
            onHeroSecondary = if (high) Color(0xFFFFFFFF) else Color(0xFFE8EEF5),
            onHeroMuted = if (high) Color(0xFFE2E8F0) else Color(0xFFCBD5E1),
            heroCtaContainer = Color(0xFFFFFFFF),
            onHeroCta = Color(0xFF0F172A),
            heroBorder = if (high) Color(0x80FFFFFF) else Color(0x29FFFFFF),
            bugHeroGradientStart = Color(0xFF2A0A18),
            bugHeroGradientMid = Color(0xFF7F1D1D),
            bugHeroGradientEnd = Color(0xFFB91C1C),
            calloutWhyBg = if (isDark) (if (isAmoled) Color(0xFF071C17) else Color(0xFF0C2922)) else Color(0xFFECFDF5),
            calloutWhyAccent = success,
            calloutMistakeBg = if (isDark) (if (isAmoled) Color(0xFF1F0A0D) else Color(0xFF2A1215)) else Color(0xFFFEF2F2),
            calloutMistakeAccent = colorScheme.error,
            calloutBody = colorScheme.onSurface,
            inlineCodeBg = if (isDark) colorScheme.surfaceContainerHighest else colorScheme.surfaceContainerHigh,
            inlineCodeFg = colorScheme.primary,
            glassSurface = if (isDark) Color(0x14FFFFFF) else Color(0x0A0F172A),
            glassBorder = colorScheme.outlineVariant,
            windowDotClose = Color(0xFFEF4444),
            windowDotMin = Color(0xFFF59E0B),
            windowDotMax = Color(0xFF10B981),
            feltStart = felt.feltStart,
            feltEnd = felt.feltEnd,
            feltBorder = felt.feltBorder,
            cardStock = if (isDark) Color(0xFFF8FAFC) else Color(0xFFFFFFFF),
            cardStockSelected = Color(0xFFFEF9C3),
            cardBorder = if (isDark) Color(0xFFCBD5E1) else Color(0xFF94A3B8),
            cardBorderSelected = felt.cardBorderSelected,
            cardSuitRed = Color(0xFFDC2626),
            cardSuitBlack = Color(0xFF0F172A)
        )
    }

    /** Single source of truth for AccentPreset swatch colors. */
    fun accentSwatchColor(preset: AccentPreset): Color = accentSpec(preset).swatch

    /** Single source of truth for HomeHeroPalette gradient stops. */
    fun heroPaletteGradient(
        palette: HomeHeroPalette,
        extended: KadiExtendedColors
    ): List<Color> = when (palette) {
        HomeHeroPalette.FOLLOW_ACCENT -> listOf(
            extended.heroGradientStart,
            extended.heroGradientMid,
            extended.heroGradientEnd
        )
        HomeHeroPalette.GODOT_SAPPHIRE -> accentSpec(AccentPreset.GODOT_SAPPHIRE).let {
            if (extended.isDark) it.darkHeroStops else it.lightHeroStops
        }
        HomeHeroPalette.EMERALD_TABLE -> accentSpec(AccentPreset.EMERALD_FELT).let {
            if (extended.isDark) it.darkHeroStops else it.lightHeroStops
        }
        HomeHeroPalette.NAIROBI_GOLD -> accentSpec(AccentPreset.NAIROBI_GOLD).let {
            if (extended.isDark) it.darkHeroStops else it.lightHeroStops
        }
        HomeHeroPalette.CYBER_PURPLE -> accentSpec(AccentPreset.CYBER_VIOLET).let {
            if (extended.isDark) it.darkHeroStops else it.lightHeroStops
        }
        HomeHeroPalette.CRIMSON_ROYAL -> accentSpec(AccentPreset.CRIMSON_ACE).let {
            if (extended.isDark) it.darkHeroStops else it.lightHeroStops
        }
        HomeHeroPalette.OBSIDIAN_DARK -> listOf(
            Color(0xFF090D16),
            Color(0xFF111827),
            Color(0xFF1E293B)
        )
        HomeHeroPalette.SURFACE_CARD -> listOf(
            extended.heroGradientStart,
            extended.heroGradientMid,
            extended.heroGradientEnd
        )
    }

    /** Single source of truth for HomeHeroPalette swatch color. */
    fun heroPaletteSwatchColor(
        palette: HomeHeroPalette,
        accentPreset: AccentPreset
    ): Color = when (palette) {
        HomeHeroPalette.FOLLOW_ACCENT -> accentSwatchColor(accentPreset)
        HomeHeroPalette.GODOT_SAPPHIRE -> Color(0xFF0284C7)
        HomeHeroPalette.EMERALD_TABLE -> Color(0xFF10B981)
        HomeHeroPalette.NAIROBI_GOLD -> Color(0xFFF59E0B)
        HomeHeroPalette.CYBER_PURPLE -> Color(0xFF8B5CF6)
        HomeHeroPalette.CRIMSON_ROYAL -> Color(0xFFF43F5E)
        HomeHeroPalette.OBSIDIAN_DARK -> Color(0xFF64748B)
        HomeHeroPalette.SURFACE_CARD -> Color(0xFF94A3B8)
    }
}

fun accentSwatchColor(preset: AccentPreset): Color = ThemeEngine.accentSwatchColor(preset)

fun heroPaletteGradient(palette: HomeHeroPalette, extended: KadiExtendedColors): List<Color> =
    ThemeEngine.heroPaletteGradient(palette, extended)

fun heroPaletteSwatchColor(palette: HomeHeroPalette, accentPreset: AccentPreset): Color =
    ThemeEngine.heroPaletteSwatchColor(palette, accentPreset)
