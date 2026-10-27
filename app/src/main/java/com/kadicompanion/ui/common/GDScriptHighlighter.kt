package com.kadicompanion.ui.common

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.kadicompanion.data.local.datastore.CodeThemePreset
import com.kadicompanion.ui.theme.SyntaxColors

object GDScriptHighlighter {

    private val KEYWORDS = setOf(
        "func", "var", "const", "if", "elif", "else", "for", "while", "match",
        "return", "break", "continue", "pass", "and", "or", "not", "in", "as",
        "class", "class_name", "extends", "signal", "enum", "export", "onready",
        "tool", "static", "vararg", "breakpoint", "preload", "yield", "await",
        "assert", "self", "super", "true", "false", "null", "void", "int",
        "float", "bool", "String", "Array", "Dictionary", "Vector2", "Vector3",
        "Vector4", "Color", "Node", "Node2D", "Node3D", "Control", "Resource",
        "Object", "Variant", "Callable", "PackedStringArray", "PackedInt32Array",
        "Texture2D", "TextureRect", "AtlasTexture", "ResourceLoader", "AudioStreamPlayer"
    )

    private val primitiveTypes = setOf("int", "float", "bool", "string", "void", "null")

    data class CodePalette(
        val background: Color,
        val headerBg: Color,
        val borderColor: Color,
        val keyword: Color,
        val type: Color,
        val string: Color,
        val comment: Color,
        val number: Color,
        val function: Color,
        val plain: Color
    )

    fun paletteFor(preset: CodeThemePreset, dark: Boolean): CodePalette = when (preset) {
        CodeThemePreset.GODOT_EDITOR -> if (dark) CodePalette(
            background = Color(0xFF1D2229),
            headerBg = Color(0xFF161A20),
            borderColor = Color(0xFF2D3540),
            keyword = Color(0xFFFF7085),
            type = Color(0xFF8EFFDA),
            string = Color(0xFFFFEDA1),
            comment = Color(0xFF79828E),
            number = Color(0xFFA1FFE0),
            function = Color(0xFF57B3FF),
            plain = Color(0xFFE0E5EC)
        ) else lightFallbackPalette()

        CodeThemePreset.DRACULA_NEON -> if (dark) CodePalette(
            background = Color(0xFF1E1F29),
            headerBg = Color(0xFF171821),
            borderColor = Color(0xFF383A59),
            keyword = Color(0xFFFF79C6),
            type = Color(0xFF8BE9FD),
            string = Color(0xFFF1FA8C),
            comment = Color(0xFF6272A4),
            number = Color(0xFFBD93F9),
            function = Color(0xFF50FA7B),
            plain = Color(0xFFF8F8F2)
        ) else lightFallbackPalette()

        CodeThemePreset.MONOKAI_PRO -> if (dark) CodePalette(
            background = Color(0xFF221F22),
            headerBg = Color(0xFF19181A),
            borderColor = Color(0xFF403E41),
            keyword = Color(0xFFFF6188),
            type = Color(0xFF78DCE8),
            string = Color(0xFFFFD866),
            comment = Color(0xFF727072),
            number = Color(0xFFAB9DF2),
            function = Color(0xFFA9DC76),
            plain = Color(0xFFFCFCFA)
        ) else lightFallbackPalette()

        CodeThemePreset.NORD_FROST -> if (dark) CodePalette(
            background = Color(0xFF242933),
            headerBg = Color(0xFF1E222A),
            borderColor = Color(0xFF3B4252),
            keyword = Color(0xFF81A1C1),
            type = Color(0xFF8FBCBB),
            string = Color(0xFFA3BE8C),
            comment = Color(0xFF616E88),
            number = Color(0xFFB48EAD),
            function = Color(0xFF88C0D0),
            plain = Color(0xFFECEFF4)
        ) else lightFallbackPalette()

        CodeThemePreset.HIGH_CONTRAST -> if (dark) CodePalette(
            background = Color(0xFF000000),
            headerBg = Color(0xFF0A0A0A),
            borderColor = Color(0xFF475569),
            keyword = Color(0xFFFF5555),
            type = Color(0xFF00FA9A),
            string = Color(0xFFFFEA00),
            comment = Color(0xFF94A3B8),
            number = Color(0xFF38BDF8),
            function = Color(0xFFE879F9),
            plain = Color(0xFFFFFFFF)
        ) else CodePalette(
            background = Color(0xFFFFFFFF),
            headerBg = Color(0xFFF1F5F9),
            borderColor = Color(0xFF334155),
            keyword = Color(0xFFB91C1C),
            type = Color(0xFF047857),
            string = Color(0xFF1D4ED8),
            comment = Color(0xFF475569),
            number = Color(0xFF6D28D9),
            function = Color(0xFF7E22CE),
            plain = Color(0xFF000000)
        )

        CodeThemePreset.GITHUB_STUDIO -> if (dark) CodePalette(
            background = SyntaxColors.darkBackground,
            headerBg = Color(0xFF131B2B),
            borderColor = Color(0xFF24324B),
            keyword = SyntaxColors.darkKeyword,
            type = SyntaxColors.darkType,
            string = SyntaxColors.darkString,
            comment = SyntaxColors.darkComment,
            number = SyntaxColors.darkNumber,
            function = SyntaxColors.darkFunction,
            plain = SyntaxColors.darkPlain
        ) else lightFallbackPalette()
    }

    private fun lightFallbackPalette() = CodePalette(
        background = SyntaxColors.lightBackground,
        headerBg = Color(0xFFE2E8F0),
        borderColor = Color(0xFFCBD5E1),
        keyword = SyntaxColors.lightKeyword,
        type = SyntaxColors.lightType,
        string = SyntaxColors.lightString,
        comment = SyntaxColors.lightComment,
        number = SyntaxColors.lightNumber,
        function = SyntaxColors.lightFunction,
        plain = SyntaxColors.lightPlain
    )

    private val tokenRegex = Regex(
        "(\\#[^\\n]*)" +
        "|(\"(?:[^\"\\\\]|\\\\.)*\"|'(?:[^'\\\\]|\\\\.)*')" +
        "|(\\b\\d[\\d_]*\\.?\\d*([eE][+-]?\\d+)?\\b|0x[0-9A-Fa-f]+)" +
        "|(@[A-Za-z_]\\w*)" +
        "|([A-Za-z_]\\w*)" +
        "|(\\n)" +
        "|([^\\n])"
    )

    fun highlight(
        code: String,
        dark: Boolean,
        preset: CodeThemePreset = CodeThemePreset.GODOT_EDITOR
    ): AnnotatedString {
        val c = paletteFor(preset, dark)
        return buildAnnotatedString {
            var lastWasDef = false
            var lastWasDot = false
            for (m in tokenRegex.findAll(code)) {
                val text = m.value
                when {
                    m.groups[1] != null -> withStyle(SpanStyle(color = c.comment, fontStyle = FontStyle.Italic)) { append(text) }
                    m.groups[2] != null -> withStyle(SpanStyle(color = c.string)) { append(text) }
                    m.groups[3] != null -> withStyle(SpanStyle(color = c.number)) { append(text) }
                    m.groups[5] != null -> withStyle(SpanStyle(color = c.keyword, fontWeight = FontWeight.Bold)) { append(text) }
                    m.groups[6] != null -> {
                        when {
                            text in KEYWORDS && (text.first().isUpperCase() || text.lowercase() in primitiveTypes) ->
                                withStyle(SpanStyle(color = c.type, fontWeight = FontWeight.Medium)) { append(text) }
                            text in KEYWORDS ->
                                withStyle(SpanStyle(color = c.keyword, fontWeight = FontWeight.Bold)) { append(text) }
                            lastWasDef || lastWasDot -> withStyle(SpanStyle(color = c.function)) { append(text) }
                            else -> withStyle(SpanStyle(color = c.plain)) { append(text) }
                        }
                    }
                    else -> withStyle(SpanStyle(color = c.plain)) { append(text) }
                }
                lastWasDef = (text == "func" || text == "signal")
                lastWasDot = (text == ".")
            }
        }
    }
}
