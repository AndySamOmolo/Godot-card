package com.kadicompanion.ui.common

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.kadicompanion.data.local.datastore.AccentPreset
import com.kadicompanion.data.local.datastore.CodeThemePreset
import com.kadicompanion.data.local.datastore.ContrastLevel
import com.kadicompanion.ui.theme.CodePalette
import com.kadicompanion.ui.theme.resolveCodePalette

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

    fun paletteFor(
        preset: CodeThemePreset,
        dark: Boolean,
        accentPreset: AccentPreset = AccentPreset.GODOT_SAPPHIRE,
        contrastLevel: ContrastLevel = ContrastLevel.STANDARD
    ): CodePalette = resolveCodePalette(
        preset = preset,
        accentPreset = accentPreset,
        isDark = dark,
        contrastLevel = contrastLevel
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
    ): AnnotatedString = highlight(code, paletteFor(preset, dark))

    fun highlight(
        code: String,
        palette: CodePalette
    ): AnnotatedString {
        return buildAnnotatedString {
            var lastWasDef = false
            var lastWasDot = false
            for (m in tokenRegex.findAll(code)) {
                val text = m.value
                when {
                    m.groups[1] != null -> withStyle(SpanStyle(color = palette.comment, fontStyle = FontStyle.Italic)) { append(text) }
                    m.groups[2] != null -> withStyle(SpanStyle(color = palette.string)) { append(text) }
                    m.groups[3] != null -> withStyle(SpanStyle(color = palette.number)) { append(text) }
                    m.groups[5] != null -> withStyle(SpanStyle(color = palette.keyword, fontWeight = FontWeight.Bold)) { append(text) }
                    m.groups[6] != null -> {
                        when {
                            text in KEYWORDS && (text.first().isUpperCase() || text.lowercase(java.util.Locale.ROOT) in primitiveTypes) ->
                                withStyle(SpanStyle(color = palette.type, fontWeight = FontWeight.Medium)) { append(text) }
                            text in KEYWORDS ->
                                withStyle(SpanStyle(color = palette.keyword, fontWeight = FontWeight.Bold)) { append(text) }
                            lastWasDef || lastWasDot -> withStyle(SpanStyle(color = palette.function)) { append(text) }
                            else -> withStyle(SpanStyle(color = palette.plain)) { append(text) }
                        }
                    }
                    else -> withStyle(SpanStyle(color = palette.plain)) { append(text) }
                }
                lastWasDef = (text == "func" || text == "signal")
                lastWasDot = (text == ".")
            }
        }
    }
}
