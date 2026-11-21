package com.kadicompanion.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.kadicompanion.data.local.datastore.AccentPreset
import com.kadicompanion.data.local.datastore.CodeThemePreset
import com.kadicompanion.data.local.datastore.ContrastLevel

/**
 * Complete, high-contrast syntax palette for GDScript code blocks,
 * supporting distinct Light and Dark variants for every CodeThemePreset.
 */
@Immutable
data class CodePalette(
    val background: Color,
    val headerBackground: Color,
    val border: Color,
    val lineNumber: Color,
    val gutterDivider: Color,
    val keyword: Color,
    val type: Color,
    val string: Color,
    val comment: Color,
    val number: Color,
    val function: Color,
    val plain: Color
) {
    val headerBg: Color get() = headerBackground
    val borderColor: Color get() = border
}

/**
 * Resolves the effective CodePalette for a preset, active accent, dark/light polarity,
 * and contrast level. Every preset provides distinct, authentic Light and Dark palettes
 * with WCAG AA+ readability on syntax tokens (and AAA-boosted tokens in HIGH contrast).
 */
fun resolveCodePalette(
    preset: CodeThemePreset,
    accentPreset: AccentPreset = AccentPreset.GODOT_SAPPHIRE,
    isDark: Boolean,
    contrastLevel: ContrastLevel = ContrastLevel.STANDARD
): CodePalette {
    val effectivePreset = when (preset) {
        CodeThemePreset.AUTO_MATCH -> when (accentPreset) {
            AccentPreset.GODOT_SAPPHIRE -> CodeThemePreset.GODOT_EDITOR
            AccentPreset.CYBER_VIOLET -> CodeThemePreset.DRACULA_NEON
            AccentPreset.NAIROBI_GOLD, AccentPreset.SUNSET_ORANGE -> CodeThemePreset.MONOKAI_PRO
            AccentPreset.EMERALD_FELT -> CodeThemePreset.NORD_FROST
            AccentPreset.CRIMSON_ACE, AccentPreset.MONOCHROME_SLATE -> CodeThemePreset.GITHUB_STUDIO
        }
        else -> preset
    }

    if (contrastLevel == ContrastLevel.HIGH || effectivePreset == CodeThemePreset.HIGH_CONTRAST) {
        return highContrastCodePalette(isDark)
    }

    val base = when (effectivePreset) {
        CodeThemePreset.GODOT_EDITOR -> if (isDark) CodePalette(
            background = Color(0xFF1A2029),
            headerBackground = Color(0xFF131820),
            border = Color(0xFF2D3748),
            lineNumber = Color(0xFF8A9AB0),
            gutterDivider = Color(0xFF2A3444),
            keyword = Color(0xFFFF7A8F),
            type = Color(0xFF7CF7CE),
            string = Color(0xFFFFE58F),
            comment = Color(0xFF93A5BC),
            number = Color(0xFF91F7D5),
            function = Color(0xFF67BDFF),
            plain = Color(0xFFE6EDF5)
        ) else CodePalette(
            background = Color(0xFFF6F8FB),
            headerBackground = Color(0xFFEAEFF6),
            border = Color(0xFFCBD5E1),
            lineNumber = Color(0xFF526277),
            gutterDivider = Color(0xFFD5DFEB),
            keyword = Color(0xFFC2184B),
            type = Color(0xFF046C4E),
            string = Color(0xFF8A4B00),
            comment = Color(0xFF4B5B70),
            number = Color(0xFF006266),
            function = Color(0xFF1D4ED8),
            plain = Color(0xFF111827)
        )

        CodeThemePreset.DRACULA_NEON -> if (isDark) CodePalette(
            background = Color(0xFF1D1E2B),
            headerBackground = Color(0xFF151621),
            border = Color(0xFF393C5C),
            lineNumber = Color(0xFF8D99C4),
            gutterDivider = Color(0xFF2E314A),
            keyword = Color(0xFFFF79C6),
            type = Color(0xFF8BE9FD),
            string = Color(0xFFF1FA8C),
            comment = Color(0xFF98A8DB),
            number = Color(0xFFC9A6FF),
            function = Color(0xFF50FA7B),
            plain = Color(0xFFF8F8F2)
        ) else CodePalette(
            background = Color(0xFFF8F6FC),
            headerBackground = Color(0xFFECE8F7),
            border = Color(0xFFD0C7E6),
            lineNumber = Color(0xFF5B5675),
            gutterDivider = Color(0xFFDDD6F0),
            keyword = Color(0xFFB5176E),
            type = Color(0xFF0E6B82),
            string = Color(0xFF7D4E00),
            comment = Color(0xFF565173),
            number = Color(0xFF6B21A8),
            function = Color(0xFF116D36),
            plain = Color(0xFF1E1B2E)
        )

        CodeThemePreset.MONOKAI_PRO -> if (isDark) CodePalette(
            background = Color(0xFF211E22),
            headerBackground = Color(0xFF181619),
            border = Color(0xFF423E45),
            lineNumber = Color(0xFFA39DA6),
            gutterDivider = Color(0xFF353138),
            keyword = Color(0xFFFF6B92),
            type = Color(0xFF78DCE8),
            string = Color(0xFFFFD866),
            comment = Color(0xFFA8A2AB),
            number = Color(0xFFBBA8FF),
            function = Color(0xFFA9DC76),
            plain = Color(0xFFFCFCFA)
        ) else CodePalette(
            background = Color(0xFFFAF7F2),
            headerBackground = Color(0xFFF0EAE1),
            border = Color(0xFFD9CFC2),
            lineNumber = Color(0xFF665D54),
            gutterDivider = Color(0xFFE4DDD2),
            keyword = Color(0xFFBE185D),
            type = Color(0xFF0E6B78),
            string = Color(0xFF854D0E),
            comment = Color(0xFF5E564E),
            number = Color(0xFF6D28D9),
            function = Color(0xFF3F6212),
            plain = Color(0xFF1F1C18)
        )

        CodeThemePreset.NORD_FROST -> if (isDark) CodePalette(
            background = Color(0xFF222833),
            headerBackground = Color(0xFF1B2029),
            border = Color(0xFF3B4557),
            lineNumber = Color(0xFF93A3B8),
            gutterDivider = Color(0xFF303847),
            keyword = Color(0xFF8FB8E3),
            type = Color(0xFF8FCEC9),
            string = Color(0xFFB3D196),
            comment = Color(0xFF99AABF),
            number = Color(0xFFCBA6C6),
            function = Color(0xFF88D0E3),
            plain = Color(0xFFECEFF4)
        ) else CodePalette(
            background = Color(0xFFF2F5F9),
            headerBackground = Color(0xFFE5ECF4),
            border = Color(0xFFC5D1E0),
            lineNumber = Color(0xFF4C5B6E),
            gutterDivider = Color(0xFFD3DDE9),
            keyword = Color(0xFF2B5482),
            type = Color(0xFF1A6B68),
            string = Color(0xFF3B6622),
            comment = Color(0xFF4B5B6E),
            number = Color(0xFF7B3E78),
            function = Color(0xFF0F607A),
            plain = Color(0xFF1B2430)
        )

        CodeThemePreset.GITHUB_STUDIO,
        CodeThemePreset.HIGH_CONTRAST,
        CodeThemePreset.AUTO_MATCH -> if (isDark) CodePalette(
            background = Color(0xFF0D1117),
            headerBackground = Color(0xFF161B22),
            border = Color(0xFF30363D),
            lineNumber = Color(0xFF8B949E),
            gutterDivider = Color(0xFF21262D),
            keyword = Color(0xFFFF7B72),
            type = Color(0xFFFFA657),
            string = Color(0xFFA5D6FF),
            comment = Color(0xFF95A0AC),
            number = Color(0xFF79C0FF),
            function = Color(0xFFD2A8FF),
            plain = Color(0xFFE6EDF3)
        ) else CodePalette(
            background = Color(0xFFFBFCFD),
            headerBackground = Color(0xFFF3F6FA),
            border = Color(0xFFD0D7DE),
            lineNumber = Color(0xFF57606A),
            gutterDivider = Color(0xFFE1E7EE),
            keyword = Color(0xFFCF222E),
            type = Color(0xFF953800),
            string = Color(0xFF0A3069),
            comment = Color(0xFF57606A),
            number = Color(0xFF0550AE),
            function = Color(0xFF7738D6),
            plain = Color(0xFF1F2328)
        )
    }

    return if (contrastLevel == ContrastLevel.MEDIUM) {
        if (isDark) {
            base.copy(
                border = Color(0xFF52627A),
                comment = Color(0xFFAFBED0),
                lineNumber = Color(0xFFA3B3C6),
                plain = Color(0xFFFFFFFF)
            )
        } else {
            base.copy(
                border = Color(0xFF64748B),
                comment = Color(0xFF334155),
                lineNumber = Color(0xFF334155),
                plain = Color(0xFF090D16)
            )
        }
    } else {
        base
    }
}

private fun highContrastCodePalette(isDark: Boolean): CodePalette = if (isDark) {
    CodePalette(
        background = Color(0xFF000000),
        headerBackground = Color(0xFF0D0D0D),
        border = Color(0xFF94A3B8),
        lineNumber = Color(0xFFCBD5E1),
        gutterDivider = Color(0xFF64748B),
        keyword = Color(0xFFFF8585),
        type = Color(0xFF5CFFB8),
        string = Color(0xFFFFF066),
        comment = Color(0xFFC5D0DE),
        number = Color(0xFF7DD3FC),
        function = Color(0xFFF0ABFC),
        plain = Color(0xFFFFFFFF)
    )
} else {
    CodePalette(
        background = Color(0xFFFFFFFF),
        headerBackground = Color(0xFFF1F5F9),
        border = Color(0xFF1E293B),
        lineNumber = Color(0xFF1E293B),
        gutterDivider = Color(0xFF475569),
        keyword = Color(0xFF991B1B),
        type = Color(0xFF065F46),
        string = Color(0xFF1E3A8A),
        comment = Color(0xFF334155),
        number = Color(0xFF5B21B6),
        function = Color(0xFF6B21A8),
        plain = Color(0xFF000000)
    )
}

val LocalCodePalette = staticCompositionLocalOf {
    resolveCodePalette(
        preset = CodeThemePreset.GODOT_EDITOR,
        accentPreset = AccentPreset.GODOT_SAPPHIRE,
        isDark = true,
        contrastLevel = ContrastLevel.STANDARD
    )
}
