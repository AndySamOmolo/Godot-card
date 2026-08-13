package com.kadicompanion.ui.common

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
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
        "Object", "Variant", "Callable", "PackedStringArray", "PackedInt32Array"
    )

    private val primitiveTypes = setOf("int", "float", "bool", "string", "void", "null")

    private data class Palette(
        val keyword: Color, val type: Color, val string: Color,
        val comment: Color, val number: Color, val function: Color, val plain: Color
    )

    private val darkPalette = Palette(
        SyntaxColors.darkKeyword, SyntaxColors.darkType, SyntaxColors.darkString,
        SyntaxColors.darkComment, SyntaxColors.darkNumber, SyntaxColors.darkFunction, SyntaxColors.darkPlain
    )

    private val lightPalette = Palette(
        SyntaxColors.lightKeyword, SyntaxColors.lightType, SyntaxColors.lightString,
        SyntaxColors.lightComment, SyntaxColors.lightNumber, SyntaxColors.lightFunction, SyntaxColors.lightPlain
    )

    private val tokenRegex = Regex(
        "(\\#[^\\n]*)" +
        "|(\"(?:[^\"\\\\]|\\\\.)*\"|'(?:[^'\\\\]|\\\\.)*')" +
        "|(\\b\\d[\\d_]*\\.?\\d*([eE][+-]?\\d+)?\\b|0x[0-9A-Fa-f]+)" +
        "|([A-Za-z_]\\w*)" +
        "|(\\n)" +
        "|([^\\n])"
    )

    fun highlight(code: String, dark: Boolean): AnnotatedString {
        val c = if (dark) darkPalette else lightPalette
        return buildAnnotatedString {
            var lastWasDef = false
            var lastWasDot = false
            for (m in tokenRegex.findAll(code)) {
                val text = m.value
                when {
                    m.groups[1] != null -> withStyle(SpanStyle(color = c.comment, fontStyle = FontStyle.Italic)) { append(text) }
                    m.groups[2] != null -> withStyle(SpanStyle(color = c.string)) { append(text) }
                    m.groups[3] != null -> withStyle(SpanStyle(color = c.number)) { append(text) }
                    m.groups[5] != null -> {
                        when {
                            text in KEYWORDS && (text.first().isUpperCase() || text.lowercase() in primitiveTypes) ->
                                withStyle(SpanStyle(color = c.type, fontWeight = FontWeight.Medium)) { append(text) }
                            text in KEYWORDS ->
                                withStyle(SpanStyle(color = c.keyword, fontWeight = FontWeight.Bold)) { append(text) }
                            lastWasDef || lastWasDot -> withStyle(SpanStyle(color = c.function)) { append(text) }
                            else -> withStyle(SpanStyle(color = c.plain)) { append(text) }
                        }
                    }
                    else -> append(text)
                }
                lastWasDef = (text == "func")
                lastWasDot = (text == ".")
            }
        }
    }
}
