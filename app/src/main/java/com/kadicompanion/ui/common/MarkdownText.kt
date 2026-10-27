package com.kadicompanion.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.kadicompanion.domain.model.CodeSnippet
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
        for ((index, block) in blocks.withIndex()) {
            when (block) {
                is MarkdownBlock.Heading -> {
                    val style = when (block.level) {
                        1 -> MaterialTheme.typography.headlineSmall
                        2 -> MaterialTheme.typography.titleLarge
                        else -> MaterialTheme.typography.titleMedium
                    }
                    val color = when (block.level) {
                        1 -> MaterialTheme.colorScheme.primary
                        2 -> textColor
                        else -> MaterialTheme.colorScheme.secondary
                    }
                    Text(
                        text = block.text,
                        style = style,
                        color = color,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 14.dp, bottom = 4.dp)
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
                            modifier = Modifier.padding(start = 8.dp, top = 3.dp, bottom = 3.dp),
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
                            modifier = Modifier.padding(start = 8.dp, top = 3.dp, bottom = 3.dp),
                            onTermClick = onTermClick
                        )
                    }
                }
                is MarkdownBlock.Table -> {
                    MarkdownTableView(
                        headers = block.headers,
                        rows = block.rows,
                        linkColor = linkColor,
                        textColor = textColor,
                        codeBg = codeBg,
                        codeFg = codeFg,
                        onTermClick = onTermClick,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                is MarkdownBlock.Callout -> {
                    val calloutType = when (block.kind.uppercase()) {
                        "WHY" -> CalloutType.WHY
                        "MISTAKE", "WARNING" -> CalloutType.MISTAKE
                        "GODOT", "EDITOR" -> CalloutType.GODOT
                        "KADI", "RULE" -> CalloutType.KADI
                        "TIP", "NOTE" -> CalloutType.TIP
                        else -> CalloutType.WHY
                    }
                    Callout(
                        type = calloutType,
                        body = block.body.trim(),
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }
                is MarkdownBlock.CodeBlock -> {
                    CodeBlock(
                        snippet = CodeSnippet(
                            id = "inline_md_$index",
                            language = block.language.ifBlank { "gdscript" },
                            code = block.code,
                            caption = null
                        ),
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}

@Composable
private fun MarkdownTableView(
    headers: List<String>,
    rows: List<List<String>>,
    linkColor: Color,
    textColor: Color,
    codeBg: Color,
    codeFg: Color,
    onTermClick: ((String) -> Unit)?,
    modifier: Modifier = Modifier
) {
    val scheme = MaterialTheme.colorScheme
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = scheme.surfaceContainerHigh.copy(alpha = 0.7f),
        border = BorderStroke(1.dp, scheme.outlineVariant.copy(alpha = 0.6f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            // Header row
            Row(
                modifier = Modifier
                    .background(scheme.primary.copy(alpha = 0.14f))
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                headers.forEach { header ->
                    Text(
                        text = header,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = scheme.primary,
                        modifier = Modifier.widthIn(min = 110.dp, max = 260.dp).padding(end = 14.dp)
                    )
                }
            }
            HorizontalDivider(color = scheme.outlineVariant.copy(alpha = 0.5f))
            // Data rows
            rows.forEachIndexed { rIdx, row ->
                Row(
                    modifier = Modifier
                        .background(
                            if (rIdx % 2 == 1) scheme.surfaceContainerLowest.copy(alpha = 0.35f)
                            else Color.Transparent
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    headers.indices.forEach { cIdx ->
                        val cellText = row.getOrElse(cIdx) { "" }
                        InlineText(
                            text = renderInline(cellText, linkColor, textColor, codeBg, codeFg),
                            style = MaterialTheme.typography.bodyMedium.copy(color = textColor),
                            modifier = Modifier.widthIn(min = 110.dp, max = 260.dp).padding(end = 14.dp),
                            onTermClick = onTermClick
                        )
                    }
                }
                if (rIdx < rows.size - 1) {
                    HorizontalDivider(color = scheme.outlineVariant.copy(alpha = 0.25f))
                }
            }
        }
    }
}

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
    data class Table(val headers: List<String>, val rows: List<List<String>>) : MarkdownBlock()
    data class Callout(val kind: String, val body: String) : MarkdownBlock()
    data class CodeBlock(val language: String, val code: String) : MarkdownBlock()
}

private object MarkdownParser {
    private fun splitTableRow(line: String): List<String> =
        line.trim().removePrefix("|").removeSuffix("|").split("|").map { it.trim() }

    private fun isTableSeparator(line: String): Boolean {
        val trimmed = line.trim()
        return trimmed.startsWith("|") && trimmed.contains("---")
    }

    fun parse(md: String): List<MarkdownBlock> {
        val blocks = mutableListOf<MarkdownBlock>()
        val lines = md.lines()
        var i = 0
        while (i < lines.size) {
            val line = lines[i]
            when {
                line.startsWith("```") -> {
                    val lang = line.removePrefix("```").trim()
                    val sb = StringBuilder()
                    i++
                    while (i < lines.size && !lines[i].startsWith("```")) {
                        sb.append(lines[i]).append('\n')
                        i++
                    }
                    if (i < lines.size) i++
                    blocks += MarkdownBlock.CodeBlock(lang, sb.toString().trimEnd('\n'))
                }
                line.trim().startsWith("|") && i + 1 < lines.size && isTableSeparator(lines[i + 1]) -> {
                    val headers = splitTableRow(line)
                    i += 2 // skip header and separator row
                    val rows = mutableListOf<List<String>>()
                    while (i < lines.size && lines[i].trim().startsWith("|")) {
                        if (!isTableSeparator(lines[i])) {
                            rows += splitTableRow(lines[i])
                        }
                        i++
                    }
                    blocks += MarkdownBlock.Table(headers, rows)
                }
                line.startsWith("> [!") -> {
                    val kind = line.removePrefix("> [!").substringBefore(']').trim()
                    val body = StringBuilder()
                    i++
                    while (i < lines.size && lines[i].startsWith(">")) {
                        body.append(lines[i].removePrefix("> ").removePrefix(">")).append('\n')
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
                        !lines[i].trim().startsWith("|") &&
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
