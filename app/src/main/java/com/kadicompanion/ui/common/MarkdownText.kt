package com.kadicompanion.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.kadicompanion.ui.theme.CodeFontFamily
import com.kadicompanion.ui.theme.isDarkScheme

private const val TERM_TAG = "term"

@Composable
fun MarkdownText(markdown: String, modifier: Modifier = Modifier, onTermClick: ((String) -> Unit)? = null) {
    val linkColor = MaterialTheme.colorScheme.primary
    val textColor = MaterialTheme.colorScheme.onBackground
    val codeBg = if (isDarkScheme()) Color.White.copy(alpha = 0.10f) else Color.Black.copy(alpha = 0.06f)
    val codeFg = MaterialTheme.colorScheme.primary

    Column(modifier = modifier.fillMaxWidth()) {
        val blocks = MarkdownParser.parse(markdown)
        for (block in blocks) {
            when (block) {
                is MarkdownBlock.Heading -> {
                    val style = when (block.level) {
                        1 -> MaterialTheme.typography.headlineSmall
                        2 -> MaterialTheme.typography.titleLarge
                        else -> MaterialTheme.typography.titleMedium
                    }
                    Text(
                        text = block.text,
                        style = style,
                        color = textColor,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 10.dp, bottom = 4.dp)
                    )
                }
                is MarkdownBlock.Paragraph -> {
                    InlineText(
                        text = renderInline(block.text, linkColor, textColor, codeBg, codeFg),
                        style = MaterialTheme.typography.bodyLarge.copy(color = textColor),
                        modifier = Modifier.padding(vertical = 4.dp),
                        onTermClick = onTermClick
                    )
                }
                is MarkdownBlock.BulletList -> {
                    for (item in block.items) {
                        InlineText(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(color = linkColor, fontWeight = FontWeight.Bold)) {
                                    append("\u2022  ")
                                }
                                append(renderInline(item, linkColor, textColor, codeBg, codeFg))
                            },
                            style = MaterialTheme.typography.bodyLarge.copy(color = textColor),
                            modifier = Modifier.padding(start = 8.dp, top = 2.dp, bottom = 2.dp),
                            onTermClick = onTermClick
                        )
                    }
                }
                is MarkdownBlock.NumberedList -> {
                    for ((idx, item) in block.items.withIndex()) {
                        InlineText(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(color = linkColor, fontWeight = FontWeight.Bold)) {
                                    append("${idx + 1}.  ")
                                }
                                append(renderInline(item, linkColor, textColor, codeBg, codeFg))
                            },
                            style = MaterialTheme.typography.bodyLarge.copy(color = textColor),
                            modifier = Modifier.padding(start = 8.dp, top = 2.dp, bottom = 2.dp),
                            onTermClick = onTermClick
                        )
                    }
                }
                is MarkdownBlock.Callout -> {
                    Callout(
                        type = if (block.kind == "WHY") CalloutType.WHY else CalloutType.MISTAKE,
                        body = block.body.trim(),
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }
                is MarkdownBlock.CodeBlock -> {
                    Text(
                        text = block.code,
                        style = MaterialTheme.typography.bodyMedium.copy(fontFamily = CodeFontFamily, color = textColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(codeBg)
                            .padding(10.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}

/**
 * Renders inline markdown. When [onTermClick] is provided, `[[Term]]` links become tappable and
 * invoke the callback with the term/class name; otherwise the whole string renders as plain text.
 */
@Composable
private fun InlineText(
    text: AnnotatedString,
    style: TextStyle,
    modifier: Modifier = Modifier,
    onTermClick: ((String) -> Unit)?
) {
    val defaultColor = MaterialTheme.colorScheme.onBackground
    val effectiveStyle = if (style.color != Color.Unspecified) style else style.copy(color = defaultColor)
    if (onTermClick != null) {
        ClickableText(
            text = text,
            style = effectiveStyle,
            modifier = modifier
        ) { offset ->
            text.getStringAnnotations(tag = TERM_TAG, start = offset, end = offset)
                .firstOrNull()
                ?.let { onTermClick(it.item) }
        }
    } else {
        Text(text = text, style = effectiveStyle, modifier = modifier)
    }
}

private sealed class MarkdownBlock {
    data class Heading(val level: Int, val text: String) : MarkdownBlock()
    data class Paragraph(val text: String) : MarkdownBlock()
    data class BulletList(val items: List<String>) : MarkdownBlock()
    data class NumberedList(val items: List<String>) : MarkdownBlock()
    data class Callout(val kind: String, val body: String) : MarkdownBlock()
    data class CodeBlock(val code: String) : MarkdownBlock()
}

private object MarkdownParser {
    fun parse(md: String): List<MarkdownBlock> {
        val blocks = mutableListOf<MarkdownBlock>()
        val lines = md.lines()
        var i = 0
        while (i < lines.size) {
            val line = lines[i]
            when {
                line.startsWith("```") -> {
                    val sb = StringBuilder()
                    i++
                    while (i < lines.size && !lines[i].startsWith("```")) {
                        sb.append(lines[i]).append('\n')
                        i++
                    }
                    if (i < lines.size) i++
                    blocks += MarkdownBlock.CodeBlock(sb.toString().trimEnd('\n'))
                }
                line.startsWith("> [!") -> {
                    val kind = line.removePrefix("> [!").substringBefore(']').trim()
                    val body = StringBuilder()
                    i++
                    while (i < lines.size && lines[i].startsWith("> ")) {
                        body.append(lines[i].removePrefix("> ")).append('\n')
                        i++
                    }
                    blocks += MarkdownBlock.Callout(kind, body.toString())
                }
                line.startsWith("# ") -> { blocks += MarkdownBlock.Heading(1, line.removePrefix("# ").trim()); i++ }
                line.startsWith("## ") -> { blocks += MarkdownBlock.Heading(2, line.removePrefix("## ").trim()); i++ }
                line.startsWith("### ") -> { blocks += MarkdownBlock.Heading(3, line.removePrefix("### ").trim()); i++ }
                line.trimStart().startsWith("- ") || line.trimStart().startsWith("* ") -> {
                    val items = mutableListOf<String>()
                    while (i < lines.size && (lines[i].trimStart().startsWith("- ") || lines[i].trimStart().startsWith("* "))) {
                        items += lines[i].trimStart().removePrefix("- ").removePrefix("* ")
                        i++
                    }
                    blocks += MarkdownBlock.BulletList(items)
                }
                Regex("^\\s*\\d+\\.\\s").containsMatchIn(line) -> {
                    val items = mutableListOf<String>()
                    while (i < lines.size && Regex("^\\s*\\d+\\.\\s").containsMatchIn(lines[i])) {
                        items += lines[i].trimStart().substringAfter(". ").trim()
                        i++
                    }
                    blocks += MarkdownBlock.NumberedList(items)
                }
                line.isBlank() -> i++
                else -> {
                    val sb = StringBuilder(line)
                    i++
                    while (i < lines.size && lines[i].isNotBlank() &&
                        !lines[i].startsWith("#") &&
                        !lines[i].trimStart().startsWith("- ") &&
                        !lines[i].trimStart().startsWith("* ") &&
                        !lines[i].startsWith("> ") &&
                        !lines[i].startsWith("```") &&
                        !Regex("^\\s*\\d+\\.\\s").containsMatchIn(lines[i])
                    ) {
                        sb.append(' ').append(lines[i])
                        i++
                    }
                    blocks += MarkdownBlock.Paragraph(sb.toString())
                }
            }
        }
        return blocks
    }
}

private fun renderInline(
    text: String,
    linkColor: Color,
    textColor: Color,
    codeBg: Color,
    codeFg: Color
): AnnotatedString = buildAnnotatedString {
    val regex = Regex("\\*\\*(.+?)\\*\\*|__(.+?)__|\\*(.+?)\\*|_(.+?)_|`(.+?)`|\\[\\[(.+?)\\]\\]")
    var idx = 0
    for (m in regex.findAll(text)) {
        if (m.range.first > idx) {
            withStyle(SpanStyle(color = textColor)) {
                append(text.substring(idx, m.range.first))
            }
        }
        when {
            m.groups[1] != null -> withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = textColor)) { append(m.groupValues[1]) }
            m.groups[2] != null -> withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = textColor)) { append(m.groupValues[2]) }
            m.groups[3] != null -> withStyle(SpanStyle(fontStyle = FontStyle.Italic, color = textColor)) { append(m.groupValues[3]) }
            m.groups[4] != null -> withStyle(SpanStyle(fontStyle = FontStyle.Italic, color = textColor)) { append(m.groupValues[4]) }
            m.groups[5] != null -> withStyle(SpanStyle(fontFamily = CodeFontFamily, background = codeBg, color = codeFg, fontWeight = FontWeight.SemiBold)) {
                append(" ${m.groupValues[5]} ")
            }
            m.groups[6] != null -> {
                val term = m.groupValues[6]
                pushStringAnnotation(tag = TERM_TAG, annotation = term)
                withStyle(SpanStyle(color = linkColor, fontWeight = FontWeight.SemiBold, textDecoration = TextDecoration.Underline)) {
                    append(term)
                }
                pop()
            }
        }
        idx = m.range.last + 1
    }
    if (idx < text.length) {
        withStyle(SpanStyle(color = textColor)) {
            append(text.substring(idx))
        }
    }
}
