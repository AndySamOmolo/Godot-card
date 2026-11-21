package com.kadicompanion.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Style
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kadicompanion.ui.theme.CodeFontFamily
import com.kadicompanion.ui.theme.kadiColors

enum class CalloutType { WHY, MISTAKE, GODOT, KADI, TIP }

private data class CalloutStyle(
    val background: Color,
    val border: Color,
    val accent: Color,
    val body: Color,
    val title: String,
    val icon: ImageVector
)

/**
 * Crisp developer-docs callout banner with a 4dp left stripe, header badge,
 * and full RichInlineText parsing so inline code and glossary links render cleanly.
 */
@Composable
fun Callout(
    type: CalloutType,
    body: String,
    modifier: Modifier = Modifier,
    onTermClick: ((String) -> Unit)? = null
) {
    val ext = MaterialTheme.kadiColors
    val scheme = MaterialTheme.colorScheme
    val style = when (type) {
        CalloutType.WHY -> CalloutStyle(
            background = ext.calloutWhyBg,
            border = ext.calloutWhyAccent,
            accent = ext.calloutWhyAccent,
            body = ext.calloutBody,
            title = "ARCHITECTURAL WHY",
            icon = Icons.Outlined.Lightbulb
        )
        CalloutType.MISTAKE -> CalloutStyle(
            background = ext.calloutMistakeBg,
            border = ext.calloutMistakeAccent,
            accent = ext.calloutMistakeAccent,
            body = ext.calloutBody,
            title = "COMMON PITFALL",
            icon = Icons.Outlined.WarningAmber
        )
        CalloutType.GODOT -> CalloutStyle(
            background = scheme.primary.copy(alpha = if (ext.isDark) 0.11f else 0.08f),
            border = scheme.primary.copy(alpha = 0.45f),
            accent = scheme.primary,
            body = ext.calloutBody,
            title = "GODOT 4.3 EDITOR",
            icon = Icons.Outlined.Build
        )
        CalloutType.KADI -> CalloutStyle(
            background = scheme.secondary.copy(alpha = if (ext.isDark) 0.11f else 0.08f),
            border = scheme.secondary.copy(alpha = 0.45f),
            accent = scheme.secondary,
            body = ext.calloutBody,
            title = "KADI TABLE RULE",
            icon = Icons.Outlined.Style
        )
        CalloutType.TIP -> CalloutStyle(
            background = ext.warning.copy(alpha = if (ext.isDark) 0.11f else 0.08f),
            border = ext.warning.copy(alpha = 0.45f),
            accent = ext.warning,
            body = ext.calloutBody,
            title = "PRO WORKFLOW TIP",
            icon = Icons.Outlined.Bolt
        )
    }
    val shape = MaterialTheme.shapes.small
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clip(shape)
            .background(style.background)
            .border(1.dp, style.border.copy(alpha = 0.45f), shape)
    ) {
        Box(modifier = Modifier.width(4.dp).fillMaxHeight().background(style.accent))
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(style.icon, contentDescription = null, tint = style.accent, modifier = Modifier.size(16.dp))
                Text(
                    text = style.title,
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                    fontWeight = FontWeight.Bold,
                    color = style.accent,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            RichInlineText(
                text = body,
                style = MaterialTheme.typography.bodyMedium,
                color = style.body,
                onTermClick = onTermClick
            )
        }
    }
}
