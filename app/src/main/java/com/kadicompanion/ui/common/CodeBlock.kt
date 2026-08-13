package com.kadicompanion.ui.common

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kadicompanion.domain.model.CodeSnippet
import com.kadicompanion.ui.theme.CodeFontFamily
import com.kadicompanion.ui.theme.SyntaxColors
import com.kadicompanion.ui.theme.isDarkScheme

/**
 * Editor-style code panel: a faux window title bar (three suit-colored dots and
 * a language tag), syntax-highlighted body that scrolls horizontally instead of
 * wrapping, and the caption set as a quiet footnote.
 */
@Composable
fun CodeBlock(snippet: CodeSnippet, modifier: Modifier = Modifier) {
    val isDark = isDarkScheme()
    val context = LocalContext.current
    val lineCount = remember(snippet.code) { snippet.code.count { it == '\n' } + 1 }
    val highlighted = remember(snippet.code, isDark) {
        GDScriptHighlighter.highlight(snippet.code, isDark)
    }
    val bg = if (isDark) SyntaxColors.darkBackground else SyntaxColors.lightBackground
    val chromeColor = if (isDark) Color.White.copy(alpha = 0.05f) else Color.Black.copy(alpha = 0.04f)
    val headerColor = if (isDark) SyntaxColors.darkComment else SyntaxColors.lightComment
    val hScroll = rememberScrollState()

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
                    .padding(start = 14.dp, end = 6.dp, top = 6.dp, bottom = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                WindowDot(MaterialTheme.colorScheme.tertiary)
                WindowDot(MaterialTheme.colorScheme.primary)
                WindowDot(MaterialTheme.colorScheme.secondary)
                Text(
                    text = snippet.language.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = headerColor,
                    fontWeight = FontWeight.Bold,
                    fontFamily = CodeFontFamily,
                    modifier = Modifier.padding(start = 6.dp).weight(1f)
                )
                IconButton(onClick = { copyToClipboard(context, snippet.code) }, modifier = Modifier.size(40.dp)) {
                    Icon(
                        imageVector = Icons.Outlined.ContentCopy,
                        contentDescription = "Copy code",
                        tint = headerColor,
                        modifier = Modifier.size(17.dp)
                    )
                }
            }
            SelectionContainer(
                modifier = Modifier
                    .horizontalScroll(hScroll)
                    .padding(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 14.dp)
            ) {
                BasicText(
                    text = highlighted,
                    style = TextStyle(fontFamily = CodeFontFamily, fontSize = 13.sp, lineHeight = 20.sp)
                )
            }
            snippet.caption?.let { cap ->
                Text(
                    text = cap,
                    style = MaterialTheme.typography.labelMedium,
                    color = headerColor,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(start = 14.dp, end = 14.dp, bottom = 12.dp)
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
