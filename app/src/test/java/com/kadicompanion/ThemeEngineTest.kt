package com.kadicompanion

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.kadicompanion.data.local.datastore.AccentPreset
import com.kadicompanion.data.local.datastore.CodeThemePreset
import com.kadicompanion.data.local.datastore.ContrastLevel
import com.kadicompanion.data.local.datastore.FontFamilyPreset
import com.kadicompanion.data.local.datastore.HeadingWeightPreset
import com.kadicompanion.data.local.datastore.LineSpacingPreset
import com.kadicompanion.data.local.datastore.ThemePref
import com.kadicompanion.ui.theme.ThemeEngine
import com.kadicompanion.ui.theme.buildCustomizableShapes
import com.kadicompanion.ui.theme.buildCustomizableTypography
import com.kadicompanion.ui.theme.resolveCodePalette
import com.kadicompanion.ui.theme.scaledDisplayHero
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ThemeEngineTest {

    @Test
    fun allThemeAccentContrastCombinations_meetWcagAA_andOpaqueContainers() {
        for (themePref in ThemePref.entries) {
            val systemDarkOptions = if (themePref == ThemePref.SYSTEM) listOf(true, false) else listOf(false)
            for (systemDark in systemDarkOptions) {
                val isDark = ThemeEngine.isDarkTheme(themePref, systemDark)
                val isAmoled = themePref == ThemePref.AMOLED
                for (accent in AccentPreset.entries) {
                    for (contrast in ContrastLevel.entries) {
                        val scheme = ThemeEngine.resolveColorScheme(
                            themePref = themePref,
                            accentPreset = accent,
                            contrastLevel = contrast,
                            systemDark = systemDark
                        )
                        val ext = ThemeEngine.resolveExtendedColors(
                            colorScheme = scheme,
                            accentPreset = accent,
                            contrastLevel = contrast,
                            isDark = isDark,
                            isAmoled = isAmoled,
                            isDynamic = false
                        )
                        val tag = "$themePref/$accent/$contrast/sysDark=$systemDark"

                        // Zero semi-transparent alpha hacks on container roles
                        assertEquals("$tag primaryContainer alpha", 1.0f, scheme.primaryContainer.alpha, 0.001f)
                        assertEquals("$tag secondaryContainer alpha", 1.0f, scheme.secondaryContainer.alpha, 0.001f)
                        assertEquals("$tag tertiaryContainer alpha", 1.0f, scheme.tertiaryContainer.alpha, 0.001f)

                        // WCAG AA (>= 4.5:1) across all core M3 and semantic pairs
                        assertMinContrast("$tag onBackground/background", scheme.onBackground, scheme.background, 4.5)
                        assertMinContrast("$tag onSurface/surface", scheme.onSurface, scheme.surface, 4.5)
                        assertMinContrast("$tag onSurfaceVariant/surface", scheme.onSurfaceVariant, scheme.surface, 4.5)
                        assertMinContrast("$tag onPrimary/primary", scheme.onPrimary, scheme.primary, 4.5)
                        assertMinContrast("$tag onPrimaryContainer/primaryContainer", scheme.onPrimaryContainer, scheme.primaryContainer, 4.5)
                        assertMinContrast("$tag onSecondary/secondary", scheme.onSecondary, scheme.secondary, 4.5)
                        assertMinContrast("$tag onSecondaryContainer/secondaryContainer", scheme.onSecondaryContainer, scheme.secondaryContainer, 4.5)
                        assertMinContrast("$tag onTertiary/tertiary", scheme.onTertiary, scheme.tertiary, 4.5)
                        assertMinContrast("$tag onTertiaryContainer/tertiaryContainer", scheme.onTertiaryContainer, scheme.tertiaryContainer, 4.5)
                        assertMinContrast("$tag onError/error", scheme.onError, scheme.error, 4.5)
                        assertMinContrast("$tag onErrorContainer/errorContainer", scheme.onErrorContainer, scheme.errorContainer, 4.5)
                        assertMinContrast("$tag onSuccess/success", ext.onSuccess, ext.success, 4.5)
                        assertMinContrast("$tag onSuccessContainer/successContainer", ext.onSuccessContainer, ext.successContainer, 4.5)
                        assertMinContrast("$tag onWarning/warning", ext.onWarning, ext.warning, 4.5)
                        assertMinContrast("$tag onWarningContainer/warningContainer", ext.onWarningContainer, ext.warningContainer, 4.5)
                        assertMinContrast("$tag onInfo/info", ext.onInfo, ext.info, 4.5)
                        assertMinContrast("$tag onInfoContainer/infoContainer", ext.onInfoContainer, ext.infoContainer, 4.5)
                    }
                }
            }
        }
    }

    @Test
    fun highContrastLevel_meetsWcagAAA_onPrimaryTextAndSurfaces() {
        for (themePref in listOf(ThemePref.DARK, ThemePref.AMOLED, ThemePref.LIGHT)) {
            for (accent in AccentPreset.entries) {
                val scheme = ThemeEngine.resolveColorScheme(
                    themePref = themePref,
                    accentPreset = accent,
                    contrastLevel = ContrastLevel.HIGH,
                    systemDark = false
                )
                val tag = "HIGH/$themePref/$accent"
                assertMinContrast("$tag onBackground/background", scheme.onBackground, scheme.background, 7.0)
                assertMinContrast("$tag onSurface/surface", scheme.onSurface, scheme.surface, 7.0)
                assertMinContrast("$tag onSurfaceVariant/surface", scheme.onSurfaceVariant, scheme.surface, 7.0)
                assertMinContrast("$tag onPrimary/primary", scheme.onPrimary, scheme.primary, 7.0)
                assertMinContrast("$tag onPrimaryContainer/primaryContainer", scheme.onPrimaryContainer, scheme.primaryContainer, 7.0)
                assertMinContrast("$tag onSecondary/secondary", scheme.onSecondary, scheme.secondary, 7.0)
                assertMinContrast("$tag onError/error", scheme.onError, scheme.error, 7.0)
            }
        }
    }

    @Test
    fun amoledInvariant_andDynamicColorAmoledPreservation() {
        // Pure AMOLED true-black background across all accents
        for (accent in AccentPreset.entries) {
            val scheme = ThemeEngine.resolveColorScheme(
                themePref = ThemePref.AMOLED,
                accentPreset = accent,
                contrastLevel = ContrastLevel.STANDARD,
                systemDark = false
            )
            assertEquals("AMOLED background must be true black", Color(0xFF000000), scheme.background)
            assertEquals("AMOLED lowest container must be true black", Color(0xFF000000), scheme.surfaceContainerLowest)
        }

        // Dynamic Color + AMOLED must preserve dynamic accents while setting true-black OLED surfaces
        val customDynamicPrimary = Color(0xFF9ECAFF)
        val customDynamicSecondary = Color(0xFFBBC7DB)
        val customDynamicTertiary = Color(0xFFD6BEE4)
        val fakeDynamic = darkColorScheme(
            primary = customDynamicPrimary,
            onPrimary = Color(0xFF003258),
            secondary = customDynamicSecondary,
            onSecondary = Color(0xFF253140),
            tertiary = customDynamicTertiary,
            onTertiary = Color(0xFF3B2948),
            background = Color(0xFF1A1C1E),
            surface = Color(0xFF1A1C1E)
        )

        val resolved = ThemeEngine.resolveColorScheme(
            themePref = ThemePref.AMOLED,
            accentPreset = AccentPreset.CRIMSON_ACE,
            contrastLevel = ContrastLevel.STANDARD,
            systemDark = true,
            dynamicColorScheme = fakeDynamic
        )
        assertEquals("AMOLED + Dynamic must use #000000 background", Color(0xFF000000), resolved.background)
        assertEquals("AMOLED + Dynamic must preserve dynamic primary", customDynamicPrimary, resolved.primary)
        assertEquals("AMOLED + Dynamic must preserve dynamic secondary", customDynamicSecondary, resolved.secondary)
        assertEquals("AMOLED + Dynamic must preserve dynamic tertiary", customDynamicTertiary, resolved.tertiary)
    }

    @Test
    fun syntaxPalettes_distinctLightAndDark_autoMatch_andReadableContrast() {
        val concretePresets = listOf(
            CodeThemePreset.GODOT_EDITOR,
            CodeThemePreset.GITHUB_STUDIO,
            CodeThemePreset.DRACULA_NEON,
            CodeThemePreset.MONOKAI_PRO,
            CodeThemePreset.NORD_FROST,
            CodeThemePreset.HIGH_CONTRAST
        )

        val lightBackgrounds = mutableSetOf<Color>()
        val lightKeywords = mutableSetOf<Color>()

        for (preset in concretePresets) {
            val dark = resolveCodePalette(preset, AccentPreset.GODOT_SAPPHIRE, isDark = true, ContrastLevel.STANDARD)
            val light = resolveCodePalette(preset, AccentPreset.GODOT_SAPPHIRE, isDark = false, ContrastLevel.STANDARD)

            assertNotEquals("$preset light and dark backgrounds must differ", dark.background, light.background)
            assertNotEquals("$preset light and dark plain text must differ", dark.plain, light.plain)

            lightBackgrounds.add(light.background)
            lightKeywords.add(light.keyword)

            for ((mode, pal) in listOf("dark" to dark, "light" to light)) {
                val tag = "$preset/$mode"
                assertMinContrast("$tag plain/bg", pal.plain, pal.background, 7.0)
                assertMinContrast("$tag keyword/bg", pal.keyword, pal.background, 4.5)
                assertMinContrast("$tag type/bg", pal.type, pal.background, 4.5)
                assertMinContrast("$tag string/bg", pal.string, pal.background, 4.5)
                assertMinContrast("$tag number/bg", pal.number, pal.background, 4.5)
                assertMinContrast("$tag function/bg", pal.function, pal.background, 4.5)
                assertMinContrast("$tag comment/bg", pal.comment, pal.background, 4.5)
            }
        }

        // Verify light palettes are genuinely distinct (not collapsed into a single fallback)
        assertEquals("All 6 light code presets must have distinct backgrounds", 6, lightBackgrounds.size)
        assertTrue("Light code presets must have distinct keyword colors", lightKeywords.size >= 5)

        // Verify AUTO_MATCH pairs with accent presets
        val autoSapphire = resolveCodePalette(CodeThemePreset.AUTO_MATCH, AccentPreset.GODOT_SAPPHIRE, isDark = true)
        val godotEditor = resolveCodePalette(CodeThemePreset.GODOT_EDITOR, AccentPreset.GODOT_SAPPHIRE, isDark = true)
        assertEquals(godotEditor, autoSapphire)

        val autoViolet = resolveCodePalette(CodeThemePreset.AUTO_MATCH, AccentPreset.CYBER_VIOLET, isDark = true)
        val dracula = resolveCodePalette(CodeThemePreset.DRACULA_NEON, AccentPreset.CYBER_VIOLET, isDark = true)
        assertEquals(dracula, autoViolet)

        // Verify HIGH contrast level boosts code palette to pure black/white canvas
        val highDark = resolveCodePalette(CodeThemePreset.GODOT_EDITOR, AccentPreset.GODOT_SAPPHIRE, isDark = true, ContrastLevel.HIGH)
        assertEquals(Color(0xFF000000), highDark.background)
        assertEquals(Color(0xFFFFFFFF), highDark.plain)

        val highLight = resolveCodePalette(CodeThemePreset.GODOT_EDITOR, AccentPreset.GODOT_SAPPHIRE, isDark = false, ContrastLevel.HIGH)
        assertEquals(Color(0xFFFFFFFF), highLight.background)
        assertEquals(Color(0xFF000000), highLight.plain)
    }

    @Test
    fun typographyClamping_dyslexiaBoosts_andShapeClamping() {
        val minTypo = buildCustomizableTypography(fontScale = 0.20f)
        val exactMin = buildCustomizableTypography(fontScale = 0.80f)
        assertEquals(exactMin.bodyLarge.fontSize, minTypo.bodyLarge.fontSize)

        val maxTypo = buildCustomizableTypography(fontScale = 3.00f)
        val exactMax = buildCustomizableTypography(fontScale = 1.35f)
        assertEquals(exactMax.bodyLarge.fontSize, maxTypo.bodyLarge.fontSize)

        assertEquals(scaledDisplayHero(0.80f).fontSize, scaledDisplayHero(0.10f).fontSize)
        assertEquals(scaledDisplayHero(1.35f).fontSize, scaledDisplayHero(5.00f).fontSize)

        val normal = buildCustomizableTypography(
            fontScale = 1.0f,
            fontFamilyPreset = FontFamilyPreset.SANS_SERIF,
            headingWeightPreset = HeadingWeightPreset.BOLD,
            lineSpacingPreset = LineSpacingPreset.COMFORTABLE,
            dyslexiaFont = false
        )
        val dyslexia = buildCustomizableTypography(
            fontScale = 1.0f,
            fontFamilyPreset = FontFamilyPreset.SANS_SERIF,
            headingWeightPreset = HeadingWeightPreset.BOLD,
            lineSpacingPreset = LineSpacingPreset.COMFORTABLE,
            dyslexiaFont = true
        )
        assertTrue(
            "Dyslexia mode should widen letter spacing",
            dyslexia.bodyLarge.letterSpacing.value > normal.bodyLarge.letterSpacing.value
        )
        assertTrue(
            "Dyslexia mode should boost body line height",
            dyslexia.bodyLarge.lineHeight.value > normal.bodyLarge.lineHeight.value
        )

        val density = Density(1f)
        val clampedLow = buildCustomizableShapes(-10)
        val exactZero = buildCustomizableShapes(0)
        assertEquals(
            exactZero.medium.topStart.toPx(androidx.compose.ui.geometry.Size(100f, 100f), density),
            clampedLow.medium.topStart.toPx(androidx.compose.ui.geometry.Size(100f, 100f), density),
            0.01f
        )

        val clampedHigh = buildCustomizableShapes(99)
        val exactMaxShape = buildCustomizableShapes(32)
        assertEquals(
            exactMaxShape.medium.topStart.toPx(androidx.compose.ui.geometry.Size(100f, 100f), density),
            clampedHigh.medium.topStart.toPx(androidx.compose.ui.geometry.Size(100f, 100f), density),
            0.01f
        )
    }

    private fun assertMinContrast(label: String, fg: Color, bg: Color, minRatio: Double) {
        val ratio = ThemeEngine.contrastRatio(fg, bg)
        assertTrue(
            "$label contrast ratio was %.2f:1 (expected >= %.1f:1)".format(ratio, minRatio),
            ratio >= minRatio
        )
    }
}
