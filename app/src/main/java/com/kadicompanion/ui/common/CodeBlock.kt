package com.kadicompanion.ui.common

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.selection.SelectionContainer
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kadicompanion.domain.model.CodeSnippet
import com.kadicompanion.ui.theme.CodeFontFamily
import com.kadicompanion.ui.theme.SyntaxColors
import com.kadicompanion.ui.theme.isDarkScheme
import com.kadicompanion.ui.theme.successColor
import kotlinx.coroutines.delay

/**
 * Editor-style code panel:
 * - Faux window title bar (three suit-colored dots, language tag, line count badge, and animated Copy/Copied! pill)
 * - Left-rail line numbers gutter (1..N) aligned with syntax-highlighted code lines so Line-by-Line Walkthrough tables match 1-to-1
 * - Optional caption footer
 */
@Composable
fun CodeBlock(snippet: CodeSnippet, modifier: Modifier = Modifier) {
    val isDark = isDarkScheme()
    val context = LocalContext.current
    val lines = remember(snippet.code) { snippet.code.lines() }
    val lineCount = lines.size
    val highlighted = remember(snippet.code, isDark) {
        GDScriptHighlighter.highlight(snippet.code, isDark)
    }
    val bg = if (isDark) SyntaxColors.darkBackground else SyntaxColors.lightBackground
    val chromeColor = if (isDark) Color.White.copy(alpha = 0.05f) else Color.Black.copy(alpha = 0.04f)
    val gutterBg = if (isDark) Color.White.copy(alpha = 0.025f) else Color.Black.copy(alpha = 0.025f)
    val headerColor = if (isDark) SyntaxColors.darkComment else SyntaxColors.lightComment
    val hScroll = rememberScrollState()

    var copied by remember { mutableStateOf(false) }
    LaunchedEffect(copied) {
        if (copied) {
            delay(1800)
            copied = false
        }
    }

    Surface(
        modifier = modifier.fillMaxWidth().semantics { contentDescription = "code block, $lineCount lines" },
        shape = RoundedCornerShape(18.dp),
        color = bg,
        tonalElevation = 0.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f))
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(chromeColor)
                    .padding(start = 14.dp, end = 10.dp, top = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                WindowDot(MaterialTheme.colorScheme.tertiary)
                WindowDot(MaterialTheme.colorScheme.primary)
                WindowDot(MaterialTheme.colorScheme.secondary)
                Text(
                    text = "${snippet.language.uppercase()} · $lineCount ${if (lineCount == 1) "LINE" else "LINES"}",
                    style = MaterialTheme.typography.labelSmall,
                    color = headerColor,
                    fontWeight = FontWeight.Bold,
                    fontFamily = CodeFontFamily,
                    modifier = Modifier.padding(start = 6.dp).weight(1f)
                )
                val copyAccent = if (copied) successColor() else MaterialTheme.colorScheme.primary
                Surface(
                    shape = RoundedCornerShape(50),
                    color = copyAccent.copy(alpha = 0.14f),
                    border = BorderStroke(0.5.dp, copyAccent.copy(alpha = 0.35f)),
                    modifier = Modifier.clickable {
                        copyToClipboard(context, snippet.code)
                        copied = true
                    }
                ) {
                    AnimatedContent(
                        targetState = copied,
                        transitionSpec = { fadeIn(tween(150)) togetherWith fadeOut(tween(150)) },
                        label = "copyFeedback"
                    ) { isCopied ->
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = if (isCopied) Icons.Outlined.Check else Icons.Outlined.ContentCopy,
                                contentDescription = "Copy code",
                                tint = copyAccent,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = if (isCopied) "Copied!" else "Copy",
                                style = MaterialTheme.typography.labelSmall,
                                color = copyAccent,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                // Line numbers gutter
                Column(
                    modifier = Modifier
                        .background(gutterBg)
                        .padding(start = 10.dp, end = 8.dp, top = 12.dp, bottom = 14.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    for (lineNum in 1..lineCount) {
                        BasicText(
                            text = lineNum.toString(),
                            style = TextStyle(
                                fontFamily = CodeFontFamily,
                                fontSize = 12.sp,
                                lineHeight = 20.sp,
                                color = headerColor.copy(alpha = 0.55f),
                                textAlign = TextAlign.End
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.width(2.dp))
                SelectionContainer(
                    modifier = Modifier
                        .weight(1f)
                        .horizontalScroll(hScroll)
                        .padding(start = 10.dp, end = 14.dp, top = 12.dp, bottom = 14.dp)
                ) {
                    BasicText(
                        text = highlighted,
                        style = TextStyle(fontFamily = CodeFontFamily, fontSize = 13.sp, lineHeight = 20.sp)
                    )
                }
            }
            snippet.caption?.let { cap ->
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                Text(
                    text = cap,
                    style = MaterialTheme.typography.labelMedium,
                    color = headerColor,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                )
            }
        }
    }
}

@Composable
private fun WindowDot(color: Color) {
    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color.copy(alpha = 0.65f)))
}

private fun copyToClipboard(context: Context, text: String) {
    val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    cm.setPrimaryClip(ClipData.newPlainText("Kadi code", text))
}
