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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kadicompanion.R
import com.kadicompanion.domain.model.CodeSnippet
import com.kadicompanion.ui.theme.CodeFontFamily
import com.kadicompanion.ui.theme.LocalUiPreferences
import com.kadicompanion.ui.theme.isDarkScheme
import com.kadicompanion.ui.theme.successColor
import kotlinx.coroutines.delay

/**
 * Studio IDE Code Block with 6 customizable syntax palettes (including Godot 4.3 Script Editor),
 * word-wrap toggle for phones, adjustable code font scale, optional line-number gutter, and one-tap copy.
 */
@Composable
fun CodeBlock(snippet: CodeSnippet, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val uiPrefs = LocalUiPreferences.current
    val isDark = isDarkScheme()
    val codePalette = remember(uiPrefs.codeThemePreset, isDark) {
        GDScriptHighlighter.paletteFor(uiPrefs.codeThemePreset, isDark)
    }
    val panelColor = codePalette.background
    val headerBg = codePalette.headerBg
    val gutterColor = codePalette.comment.copy(alpha = 0.65f)
    val borderColor = codePalette.borderColor
    val lines = remember(snippet.code) { snippet.code.lines() }
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
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (uiPrefs.showCodeWindowDots) {
                        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFEF4444).copy(alpha = 0.8f)))
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFF59E0B).copy(alpha = 0.8f)))
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF10B981).copy(alpha = 0.8f)))
                        }
                    }
                    Text(
                        text = "${snippet.language.uppercase()} · ${lines.size} ${if (lines.size == 1) "LINE" else "LINES"}",
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = CodeFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = codePalette.comment
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
                                color = wrapTint
                            )
                        }
                    }

                    val copyTint = if (copied) successColor() else MaterialTheme.colorScheme.primary
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
                                color = copyTint
                            )
                        }
                    }
                }
            }

            HorizontalDivider(thickness = 1.dp, color = borderColor.copy(alpha = 0.6f))

            if (wrapLines) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    lines.forEachIndexed { idx, lineText ->
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
                                text = GDScriptHighlighter.highlight(lineText, isDark, uiPrefs.codeThemePreset),
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
                        lines.forEach { lineText ->
                            Text(
                                text = GDScriptHighlighter.highlight(lineText, isDark, uiPrefs.codeThemePreset),
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
                HorizontalDivider(thickness = 1.dp, color = borderColor.copy(alpha = 0.5f))
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
