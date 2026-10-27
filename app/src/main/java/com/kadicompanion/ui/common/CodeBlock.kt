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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.kadicompanion.ui.theme.SyntaxColors
import com.kadicompanion.ui.theme.isDarkScheme
import com.kadicompanion.ui.theme.successColor
import kotlinx.coroutines.delay

/**
 * Studio IDE Code Block with macOS/Godot-style window header dots, language + line count badge,
 * one-tap copy button with animated "Copied!" confirmation, and a line-numbered gutter.
 */
@Composable
fun CodeBlock(snippet: CodeSnippet, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val isDark = isDarkScheme()
    val panelColor = if (isDark) SyntaxColors.darkBackground else SyntaxColors.lightBackground
    val headerBg = if (isDark) Color(0xFF131B2B) else Color(0xFFE2E8F0)
    val gutterColor = if (isDark) SyntaxColors.darkComment.copy(alpha = 0.55f) else SyntaxColors.lightComment.copy(alpha = 0.65f)
    val borderColor = if (isDark) Color(0xFF24324B) else Color(0xFFCBD5E1)
    val lines = remember(snippet.code) { snippet.code.lines() }
    var copied by remember { mutableStateOf(false) }

    LaunchedEffect(copied) {
        if (copied) {
            delay(1800)
            copied = false
        }
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = panelColor,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // IDE Window Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(headerBg)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFEF4444).copy(alpha = 0.8f)))
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFF59E0B).copy(alpha = 0.8f)))
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF10B981).copy(alpha = 0.8f)))
                    }
                    Text(
                        text = "${snippet.language.uppercase()} · ${lines.size} ${if (lines.size == 1) "LINE" else "LINES"}",
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = CodeFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) Color(0xFF94A3B8) else Color(0xFF475569)
                    )
                }

                val copyTint = if (copied) successColor() else MaterialTheme.colorScheme.primary
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = copyTint.copy(alpha = 0.14f),
                    border = BorderStroke(1.dp, copyTint.copy(alpha = 0.30f)),
                    modifier = Modifier.clickable {
                        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        cm.setPrimaryClip(ClipData.newPlainText("GDScript", snippet.code))
                        copied = true
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
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

            HorizontalDivider(thickness = 1.dp, color = borderColor.copy(alpha = 0.6f))

            // Code body with line-number gutter
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Column(
                    modifier = Modifier.padding(end = 10.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    lines.forEachIndexed { idx, _ ->
                        Text(
                            text = "${idx + 1}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = CodeFontFamily,
                                fontSize = 12.5.sp,
                                lineHeight = 20.sp
                            ),
                            color = gutterColor,
                            textAlign = TextAlign.End,
                            modifier = Modifier.width(24.dp)
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .padding(end = 10.dp)
                )
                Column {
                    lines.forEach { lineText ->
                        Text(
                            text = GDScriptHighlighter.highlight(lineText, isDark),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = CodeFontFamily,
                                fontSize = 13.sp,
                                lineHeight = 20.sp
                            )
                        )
                    }
                }
            }

            snippet.caption?.let {
                HorizontalDivider(thickness = 1.dp, color = borderColor.copy(alpha = 0.5f))
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(headerBg.copy(alpha = 0.5f))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
        }
    }
}
