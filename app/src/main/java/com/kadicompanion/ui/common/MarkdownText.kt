package com.kadicompanion.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.TableChart
import androidx.compose.material.icons.outlined.ViewAgenda
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kadicompanion.data.local.datastore.InlineCodeStyle
import com.kadicompanion.data.local.datastore.TableDisplayMode
import com.kadicompanion.domain.model.CodeSnippet
import com.kadicompanion.ui.theme.CodeFontFamily
import com.kadicompanion.ui.theme.LocalUiPreferences
import com.kadicompanion.ui.theme.isDarkScheme

private const val TERM_TAG = "term"

/**
 * Strips any raw markdown markers (`backticks`, `**bold**`, `[[Term|Alias]]`) from single-line plain strings
 * such as top app bar titles or compact badges so raw `''` or `[]` characters never appear.
 */
fun cleanPlainText(raw: String): String {
    if (raw.isEmpty()) return raw
    return raw
        .replace(Regex("\\[\\[([^\\]|]+)\\|([^\\]]+)\\]\\]"), "$2")
        .replace(Regex("\\[\\[([^\\]]+)\\]\\]"), "$1")
        .replace(Regex("\\[([^\\]]+)\\]\\([^)]+\\)"), "$1")
        .replace("**", "")
        .replace("``", "")
        .replace("`", "")
}

/**
 * Universal rich inline text component that renders `**bold**`, `*italic*`, `` `inline_code` ``,
 * `[[GlossaryTerm|Alias]]`, and `[label](url)` cleanly on any screen without leaking raw `''` or `[]`.
 */
@Composable
fun RichInlineText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = MaterialTheme.colorScheme.onSurface,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    textDecoration: TextDecoration? = null,
    onTermClick: ((String) -> Unit)? = null
) {
    val uiPrefs = LocalUiPreferences.current
    val linkColor = MaterialTheme.colorScheme.primary
    val codeBg = if (isDarkScheme()) Color.White.copy(alpha = 0.10f) else Color.Black.copy(alpha = 0.06f)
    val codeFg = MaterialTheme.colorScheme.primary

    val annotated = remember(text, linkColor, color, codeBg, codeFg, uiPrefs.inlineCodeStyle, uiPrefs.codeFontScale) {
        renderInline(
            text = text,
            linkColor = linkColor,
            textColor = color,
            codeBg = codeBg,
            codeFg = codeFg,
            inlineCodeStyle = uiPrefs.inlineCodeStyle,
            codeFontScale = uiPrefs.codeFontScale
        )
    }

    val effectiveStyle = style.copy(
        color = color,
        textDecoration = textDecoration ?: style.textDecoration
    )

    if (onTermClick != null && annotated.getStringAnnotations(TERM_TAG, 0, annotated.length).isNotEmpty()) {
        ClickableText(
            text = annotated,
            style = effectiveStyle,
            maxLines = maxLines,
            overflow = overflow,
            modifier = modifier
        ) { offset ->
            annotated.getStringAnnotations(tag = TERM_TAG, start = offset, end = offset)
                .firstOrNull()
                ?.let { onTermClick(it.item) }
        }
    } else {
        Text(
            text = annotated,
            style = effectiveStyle,
            maxLines = maxLines,
            overflow = overflow,
            modifier = modifier
        )
    }
}

@Composable
fun MarkdownText(
    markdown: String,
    modifier: Modifier = Modifier,
    onTermClick: ((String) -> Unit)? = null
) {
    val uiPrefs = LocalUiPreferences.current
    val linkColor = MaterialTheme.colorScheme.primary
    val textColor = MaterialTheme.colorScheme.onBackground
    val codeBg = if (isDarkScheme()) Color.White.copy(alpha = 0.10f) else Color.Black.copy(alpha = 0.06f)
    val codeFg = MaterialTheme.colorScheme.primary

    Column(modifier = modifier.fillMaxWidth()) {
        val blocks = remember(markdown) { MarkdownParser.parse(markdown) }
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
                    RichInlineText(
                        text = block.text,
                        style = style.copy(fontWeight = FontWeight.Bold),
                        color = color,
                        modifier = Modifier.padding(top = 12.dp, bottom = 4.dp),
                        onTermClick = onTermClick
                    )
                }
                is MarkdownBlock.Paragraph -> {
                    RichInlineText(
                        text = block.text,
                        style = MaterialTheme.typography.bodyLarge,
                        color = textColor,
                        modifier = Modifier.padding(vertical = 4.dp),
                        onTermClick = onTermClick
                    )
                }
                is MarkdownBlock.BulletList -> {
                    for (item in block.items) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 4.dp, top = 3.dp, bottom = 3.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "\u2022",
                                style = MaterialTheme.typography.bodyLarge,
                                color = linkColor,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.width(18.dp)
                            )
                            RichInlineText(
                                text = item,
                                style = MaterialTheme.typography.bodyLarge,
                                color = textColor,
                                modifier = Modifier.weight(1f),
                                onTermClick = onTermClick
                            )
                        }
                    }
                }
                is MarkdownBlock.NumberedList -> {
                    for ((idx, item) in block.items.withIndex()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 4.dp, top = 3.dp, bottom = 3.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "${idx + 1}.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = linkColor,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.width(24.dp)
                            )
                            RichInlineText(
                                text = item,
                                style = MaterialTheme.typography.bodyLarge,
                                color = textColor,
                                modifier = Modifier.weight(1f),
                                onTermClick = onTermClick
                            )
                        }
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
                        defaultTableMode = uiPrefs.tableDisplayMode,
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
                        onTermClick = onTermClick,
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
    defaultTableMode: TableDisplayMode,
    onTermClick: ((String) -> Unit)?,
    modifier: Modifier = Modifier
) {
    val scheme = MaterialTheme.colorScheme
    val isTwoColumn = headers.size == 2
    var stackedOverride by remember(defaultTableMode) {
        mutableStateOf(defaultTableMode == TableDisplayMode.STACKED_CARDS)
    }

    val useStackedCards = stackedOverride
    val useFullWidthTwoCol = !useStackedCards && isTwoColumn && defaultTableMode != TableDisplayMode.HORIZONTAL_SCROLL

    // Calculate synchronized column widths for 3+ column scrollable tables so every row aligns cleanly
    val colWidths: List<Dp> = remember(headers, rows) {
        headers.indices.map { cIdx ->
            val maxLen = maxOf(
                cleanPlainText(headers[cIdx]).length,
                rows.maxOfOrNull { cleanPlainText(it.getOrElse(cIdx) { "" }).length } ?: 10
            )
            when {
                cIdx == 0 && maxLen <= 10 -> 78.dp
                maxLen <= 14 -> 110.dp
                maxLen <= 28 -> 165.dp
                maxLen <= 48 -> 215.dp
                else -> 260.dp
            }
        }
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = scheme.surfaceContainerHigh.copy(alpha = 0.72f),
        border = BorderStroke(1.dp, scheme.outlineVariant.copy(alpha = 0.65f))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Optional mobile view switcher bar for 3+ column tables (or when stacked mode is active)
            if (headers.size >= 3 || defaultTableMode == TableDisplayMode.STACKED_CARDS) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(scheme.surfaceContainerHighest.copy(alpha = 0.55f))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (useStackedCards) "MOBILE CARD VIEW (${rows.size} ROWS)" else "TABLE VIEW · SWIPE HORIZONTALLY",
                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                        color = scheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )
                    Surface(
                        shape = MaterialTheme.shapes.extraSmall,
                        color = scheme.primary.copy(alpha = 0.14f),
                        border = BorderStroke(1.dp, scheme.primary.copy(alpha = 0.30f)),
                        modifier = Modifier.clickable { stackedOverride = !stackedOverride }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = if (useStackedCards) Icons.Outlined.TableChart else Icons.Outlined.ViewAgenda,
                                contentDescription = "Toggle table layout",
                                tint = scheme.primary,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = if (useStackedCards) "Grid View" else "Mobile Cards",
                                style = MaterialTheme.typography.labelSmall,
                                color = scheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                HorizontalDivider(color = scheme.outlineVariant.copy(alpha = 0.4f))
            }

            when {
                useStackedCards -> {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rows.forEach { row ->
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = MaterialTheme.shapes.small,
                                color = scheme.surfaceContainer,
                                border = BorderStroke(1.dp, scheme.outlineVariant.copy(alpha = 0.5f))
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth().padding(10.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    headers.indices.forEach { cIdx ->
                                        val hdr = cleanPlainText(headers[cIdx])
                                        val cell = row.getOrElse(cIdx) { "" }
                                        if (cell.isNotBlank()) {
                                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                                Text(
                                                    text = hdr.uppercase(),
                                                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                                                    color = scheme.primary,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                RichInlineText(
                                                    text = cell,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = textColor,
                                                    onTermClick = onTermClick
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                useFullWidthTwoCol -> {
                    // Phone-native 2-column table that fits 100% of the screen width with zero horizontal scrolling
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(scheme.primary.copy(alpha = 0.14f))
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            RichInlineText(
                                text = headers[0],
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = scheme.primary,
                                modifier = Modifier.weight(0.38f),
                                onTermClick = onTermClick
                            )
                            RichInlineText(
                                text = headers[1],
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = scheme.primary,
                                modifier = Modifier.weight(0.62f),
                                onTermClick = onTermClick
                            )
                        }
                        HorizontalDivider(color = scheme.outlineVariant.copy(alpha = 0.5f))
                        rows.forEachIndexed { rIdx, row ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (rIdx % 2 == 1) scheme.surfaceContainerLowest.copy(alpha = 0.35f)
                                        else Color.Transparent
                                    )
                                    .padding(horizontal = 12.dp, vertical = 9.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                RichInlineText(
                                    text = row.getOrElse(0) { "" },
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = textColor,
                                    modifier = Modifier.weight(0.38f),
                                    onTermClick = onTermClick
                                )
                                RichInlineText(
                                    text = row.getOrElse(1) { "" },
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = textColor,
                                    modifier = Modifier.weight(0.62f),
                                    onTermClick = onTermClick
                                )
                            }
                            if (rIdx < rows.size - 1) {
                                HorizontalDivider(color = scheme.outlineVariant.copy(alpha = 0.25f))
                            }
                        }
                    }
                }
                else -> {
                    // Synchronized-column-width horizontal scroll table
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                    ) {
                        Row(
                            modifier = Modifier
                                .background(scheme.primary.copy(alpha = 0.14f))
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            headers.forEachIndexed { cIdx, header ->
                                RichInlineText(
                                    text = header,
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = scheme.primary,
                                    modifier = Modifier.width(colWidths[cIdx]).padding(end = 12.dp),
                                    onTermClick = onTermClick
                                )
                            }
                        }
                        HorizontalDivider(color = scheme.outlineVariant.copy(alpha = 0.5f))
                        rows.forEachIndexed { rIdx, row ->
                            Row(
                                modifier = Modifier
                                    .background(
                                        if (rIdx % 2 == 1) scheme.surfaceContainerLowest.copy(alpha = 0.35f)
                                        else Color.Transparent
                                    )
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                headers.indices.forEach { cIdx ->
                                    val cellText = row.getOrElse(cIdx) { "" }
                                    RichInlineText(
                                        text = cellText,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = textColor,
                                        modifier = Modifier.width(colWidths[cIdx]).padding(end = 12.dp),
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
        }
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
    /**
     * Splits a markdown table row on `|` while ignoring `|` inside `[[Term|Alias]]` or `` `code` ``.
     */
    private fun splitTableRow(line: String): List<String> {
        val trimmed = line.trim().removePrefix("|").removeSuffix("|")
        val cells = mutableListOf<String>()
        val current = StringBuilder()
        var inBackticks = false
        var bracketDepth = 0
        var i = 0
        while (i < trimmed.length) {
            val ch = trimmed[i]
            when {
                ch == '\\' && i + 1 < trimmed.length && trimmed[i + 1] == '|' -> {
                    current.append('·')
                    i += 2
                    continue
                }
                ch == '`' -> {
                    inBackticks = !inBackticks
                    current.append(ch)
                }
                ch == '[' && !inBackticks -> {
                    bracketDepth++
                    current.append(ch)
                }
                ch == ']' && !inBackticks && bracketDepth > 0 -> {
                    bracketDepth--
                    current.append(ch)
                }
                ch == '|' && !inBackticks && bracketDepth == 0 -> {
                    cells.add(current.toString().trim())
                    current.clear()
                }
                else -> current.append(ch)
            }
            i++
        }
        cells.add(current.toString().trim())
        return cells
    }

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
                line.trimStart().startsWith("```") -> {
                    val lang = line.trimStart().removePrefix("```").trim()
                    val sb = StringBuilder()
                    i++
                    while (i < lines.size && !lines[i].trimStart().startsWith("```")) {
                        sb.append(lines[i]).append('\n')
                        i++
                    }
                    if (i < lines.size) i++
                    blocks += MarkdownBlock.CodeBlock(lang, sb.toString().trimEnd('\n'))
                }
                line.trim().startsWith("|") && i + 1 < lines.size && isTableSeparator(lines[i + 1]) -> {
                    val headers = splitTableRow(line)
                    i += 2
                    val rows = mutableListOf<List<String>>()
                    while (i < lines.size && lines[i].trim().startsWith("|")) {
                        if (!isTableSeparator(lines[i])) {
                            rows += splitTableRow(lines[i])
                        }
                        i++
                    }
                    blocks += MarkdownBlock.Table(headers, rows)
                }
                line.trimStart().startsWith("> [!") -> {
                    val kind = line.trimStart().removePrefix("> [!").substringBefore(']').trim()
                    val body = StringBuilder()
                    i++
                    while (i < lines.size && lines[i].trimStart().startsWith(">")) {
                        body.append(lines[i].trimStart().removePrefix("> ").removePrefix(">")).append('\n')
                        i++
                    }
                    blocks += MarkdownBlock.Callout(kind, body.toString())
                }
                line.startsWith("#### ") -> { blocks += MarkdownBlock.Heading(3, line.removePrefix("#### ").trim()); i++ }
                line.startsWith("### ") -> { blocks += MarkdownBlock.Heading(3, line.removePrefix("### ").trim()); i++ }
                line.startsWith("## ") -> { blocks += MarkdownBlock.Heading(2, line.removePrefix("## ").trim()); i++ }
                line.startsWith("# ") -> { blocks += MarkdownBlock.Heading(1, line.removePrefix("# ").trim()); i++ }
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
                        !lines[i].trimStart().startsWith("> ") &&
                        !lines[i].trimStart().startsWith("```") &&
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

/**
 * Recursive inline markdown parser that cleanly handles nested formatting (such as `` `code` `` or
 * `[[Term|Alias]]` inside `**bold**` spans), prevents `snake_case` from triggering italics, and strips
 * any stray unmatched markdown punctuation so raw `''` or `[]` artifacts never appear.
 */
private val INLINE_TOKEN_REGEX = Regex(
    "`+([^`]+?)`+" +                                        // Group 1: `inline code`
    "|\\[\\[([^\\]]+?)\\]\\]" +                             // Group 2: [[Term]] or [[Term|Alias]]
    "|\\[([^\\]]+?)\\]\\(([^)]+?)\\)" +                     // Group 3 & 4: [label](url)
    "|\\*\\*(.+?)\\*\\*" +                                  // Group 5: **bold**
    "|__(.+?)__" +                                          // Group 6: __bold__
    "|(?<!\\*)\\*([^*\n]+?)\\*(?!\\*)" +                    // Group 7: *italic*
    "|(?<![A-Za-z0-9_])_([^_\n]+?)_(?![A-Za-z0-9_])"        // Group 8: _italic_ (safe for snake_case!)
)

private fun sanitizeResidualText(raw: String): String =
    raw.replace("``", "")
        .replace("**", "")
        .replace("[[", "")
        .replace("]]", "")

private fun renderInline(
    text: String,
    linkColor: Color,
    textColor: Color,
    codeBg: Color,
    codeFg: Color,
    inlineCodeStyle: InlineCodeStyle = InlineCodeStyle.PILL_HIGHLIGHT,
    codeFontScale: Float = 1.0f
): AnnotatedString = buildAnnotatedString {
    appendInlineRecursive(
        text = text,
        linkColor = linkColor,
        textColor = textColor,
        codeBg = codeBg,
        codeFg = codeFg,
        inlineCodeStyle = inlineCodeStyle,
        codeFontScale = codeFontScale,
        bold = false,
        italic = false,
        depth = 0
    )
}

private fun AnnotatedString.Builder.appendInlineRecursive(
    text: String,
    linkColor: Color,
    textColor: Color,
    codeBg: Color,
    codeFg: Color,
    inlineCodeStyle: InlineCodeStyle,
    codeFontScale: Float,
    bold: Boolean,
    italic: Boolean,
    depth: Int
) {
    val baseSpan = SpanStyle(
        color = textColor,
        fontWeight = if (bold) FontWeight.Bold else null,
        fontStyle = if (italic) FontStyle.Italic else null
    )
    if (depth > 4 || text.isEmpty()) {
        withStyle(baseSpan) { append(sanitizeResidualText(text)) }
        return
    }

    var idx = 0
    for (m in INLINE_TOKEN_REGEX.findAll(text)) {
        if (m.range.first > idx) {
            withStyle(baseSpan) {
                append(sanitizeResidualText(text.substring(idx, m.range.first)))
            }
        }
        when {
            m.groups[1] != null -> {
                val rawCode = m.groupValues[1].trim()
                val codeSpan = when (inlineCodeStyle) {
                    InlineCodeStyle.PILL_HIGHLIGHT -> SpanStyle(
                        fontFamily = CodeFontFamily,
                        background = codeBg,
                        color = codeFg,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = (13.5f * codeFontScale).sp
                    )
                    InlineCodeStyle.COLORED_TEXT -> SpanStyle(
                        fontFamily = CodeFontFamily,
                        color = codeFg,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = (13.5f * codeFontScale).sp
                    )
                    InlineCodeStyle.PLAIN_BOLD -> SpanStyle(
                        color = textColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                withStyle(codeSpan) {
                    if (inlineCodeStyle == InlineCodeStyle.PILL_HIGHLIGHT) {
                        append(" $rawCode ")
                    } else {
                        append(rawCode)
                    }
                }
            }
            m.groups[2] != null -> {
                val rawLink = m.groupValues[2].trim()
                val parts = rawLink.split("|", limit = 2)
                val targetTerm = parts[0].trim()
                val displayLabel = parts.getOrElse(1) { targetTerm }.trim()
                pushStringAnnotation(tag = TERM_TAG, annotation = targetTerm)
                val termStyle = when (inlineCodeStyle) {
                    InlineCodeStyle.PLAIN_BOLD -> SpanStyle(
                        color = linkColor,
                        fontWeight = FontWeight.SemiBold
                    )
                    else -> SpanStyle(
                        color = linkColor,
                        fontWeight = FontWeight.SemiBold,
                        textDecoration = TextDecoration.Underline
                    )
                }
                withStyle(termStyle) {
                    append(displayLabel)
                }
                pop()
            }
            m.groups[3] != null -> {
                val label = m.groupValues[3].trim()
                withStyle(SpanStyle(color = linkColor, fontWeight = FontWeight.SemiBold)) {
                    append(label)
                }
            }
            m.groups[5] != null -> {
                appendInlineRecursive(
                    text = m.groupValues[5],
                    linkColor = linkColor,
                    textColor = textColor,
                    codeBg = codeBg,
                    codeFg = codeFg,
                    inlineCodeStyle = inlineCodeStyle,
                    codeFontScale = codeFontScale,
                    bold = true,
                    italic = italic,
                    depth = depth + 1
                )
            }
            m.groups[6] != null -> {
                appendInlineRecursive(
                    text = m.groupValues[6],
                    linkColor = linkColor,
                    textColor = textColor,
                    codeBg = codeBg,
                    codeFg = codeFg,
                    inlineCodeStyle = inlineCodeStyle,
                    codeFontScale = codeFontScale,
                    bold = true,
                    italic = italic,
                    depth = depth + 1
                )
            }
            m.groups[7] != null -> {
                appendInlineRecursive(
                    text = m.groupValues[7],
                    linkColor = linkColor,
                    textColor = textColor,
                    codeBg = codeBg,
                    codeFg = codeFg,
                    inlineCodeStyle = inlineCodeStyle,
                    codeFontScale = codeFontScale,
                    bold = bold,
                    italic = true,
                    depth = depth + 1
                )
            }
            m.groups[8] != null -> {
                appendInlineRecursive(
                    text = m.groupValues[8],
                    linkColor = linkColor,
                    textColor = textColor,
                    codeBg = codeBg,
                    codeFg = codeFg,
                    inlineCodeStyle = inlineCodeStyle,
                    codeFontScale = codeFontScale,
                    bold = bold,
                    italic = true,
                    depth = depth + 1
                )
            }
        }
        idx = m.range.last + 1
    }
    if (idx < text.length) {
        withStyle(baseSpan) {
            append(sanitizeResidualText(text.substring(idx)))
        }
    }
}
