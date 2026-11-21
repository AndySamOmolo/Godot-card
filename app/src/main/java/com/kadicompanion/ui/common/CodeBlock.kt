package com.kadicompanion.ui.common

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.WrapText
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kadicompanion.R
import com.kadicompanion.domain.model.CodeSnippet
import com.kadicompanion.ui.theme.CodeFontFamily
import com.kadicompanion.ui.theme.LocalUiPreferences
import com.kadicompanion.ui.theme.codePalette
import com.kadicompanion.ui.theme.kadiColors
import kotlinx.coroutines.delay

/**
 * Studio IDE Code Block with customizable syntax palettes (including Godot 4.3 Script Editor
 * and Auto-Match), word-wrap toggle for phones, adjustable code font scale, optional line-number
 * gutter, and one-tap copy.
 */
@Composable
fun CodeBlock(snippet: CodeSnippet, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val uiPrefs = LocalUiPreferences.current
    val codePalette = MaterialTheme.codePalette
    val kadiColors = MaterialTheme.kadiColors
    val panelColor = codePalette.background
    val headerBg = codePalette.headerBackground
    val gutterColor = codePalette.lineNumber
    val borderColor = codePalette.border
    val lines = remember(snippet.code) { snippet.code.lines() }
    val highlightedLines = remember(lines, codePalette) {
        lines.map { lineText ->
            GDScriptHighlighter.highlight(lineText, codePalette)
        }
    }
    var copied by remember { mutableStateOf(false) }
    var wrapLines by remember(uiPrefs.codeWordWrap) { mutableStateOf(uiPrefs.codeWordWrap) }

    val codeFontSize = (13f * uiPrefs.codeFontScale).sp
    val codeLineHeight = (20f * uiPrefs.codeFontScale).sp
    val gutterFontSize = (12f * uiPrefs.codeFontScale).sp

    LaunchedEffect(copied) {
        if (copied) {
            delay(1800)
            copied = false
        }
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        color = panelColor,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // IDE Window Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(headerBg)
                    .padding(horizontal = 12.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (uiPrefs.showCodeWindowDots) {
                        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(kadiColors.windowDotClose.copy(alpha = 0.85f)))
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(kadiColors.windowDotMin.copy(alpha = 0.85f)))
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(kadiColors.windowDotMax.copy(alpha = 0.85f)))
                        }
                    }
                    Text(
                        text = "${snippet.language.uppercase(java.util.Locale.ROOT)} · ${lines.size} ${if (lines.size == 1) "LINE" else "LINES"}",
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = CodeFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = codePalette.comment,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val wrapTint = if (wrapLines) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant
                    Surface(
                        shape = MaterialTheme.shapes.extraSmall,
                        color = if (wrapLines) wrapTint.copy(alpha = 0.14f) else Color.Transparent,
                        border = BorderStroke(1.dp, wrapTint.copy(alpha = 0.30f)),
                        modifier = Modifier.clickable { wrapLines = !wrapLines }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.WrapText,
                                contentDescription = "Toggle word wrap",
                                tint = wrapTint,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = if (wrapLines) "Wrap" else "Scroll",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = wrapTint,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }

                    val copyTint = if (copied) kadiColors.success else MaterialTheme.colorScheme.primary
                    Surface(
                        shape = MaterialTheme.shapes.extraSmall,
                        color = copyTint.copy(alpha = 0.14f),
                        border = BorderStroke(1.dp, copyTint.copy(alpha = 0.30f)),
                        modifier = Modifier.clickable {
                            val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            cm.setPrimaryClip(ClipData.newPlainText("GDScript", snippet.code))
                            copied = true
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = if (copied) Icons.Outlined.Check else Icons.Outlined.ContentCopy,
                                contentDescription = stringResource(R.string.action_copy_code),
                                tint = copyTint,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = if (copied) "Copied" else "Copy",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = copyTint,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }
            }

            HorizontalDivider(thickness = 1.dp, color = codePalette.gutterDivider)

            if (wrapLines) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    lines.forEachIndexed { idx, _ ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top
                        ) {
                            if (uiPrefs.showCodeLineNumbers) {
                                Text(
                                    text = "${idx + 1}",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontFamily = CodeFontFamily,
                                        fontSize = gutterFontSize,
                                        lineHeight = codeLineHeight
                                    ),
                                    color = gutterColor,
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.width(26.dp).padding(end = 8.dp)
                                )
                            }
                            Text(
                                text = highlightedLines[idx],
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontFamily = CodeFontFamily,
                                    fontSize = codeFontSize,
                                    lineHeight = codeLineHeight
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    if (uiPrefs.showCodeLineNumbers) {
                        Column(
                            modifier = Modifier.padding(end = 10.dp),
                            horizontalAlignment = Alignment.End
                        ) {
                            lines.forEachIndexed { idx, _ ->
                                Text(
                                    text = "${idx + 1}",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontFamily = CodeFontFamily,
                                        fontSize = gutterFontSize,
                                        lineHeight = codeLineHeight
                                    ),
                                    color = gutterColor,
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.width(24.dp)
                                )
                            }
                        }
                    }
                    Column {
                        lines.forEachIndexed { idx, _ ->
                            Text(
                                text = highlightedLines[idx],
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontFamily = CodeFontFamily,
                                    fontSize = codeFontSize,
                                    lineHeight = codeLineHeight
                                )
                            )
                        }
                    }
                }
            }

            snippet.caption?.let {
                HorizontalDivider(thickness = 1.dp, color = codePalette.gutterDivider)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(headerBg.copy(alpha = 0.5f))
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                ) {
                    RichInlineText(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
