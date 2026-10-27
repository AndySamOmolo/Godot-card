package com.kadicompanion.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import com.kadicompanion.ui.theme.CalloutColors
import com.kadicompanion.ui.theme.isDarkScheme

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
 * Inline teaching callout. Supports 5 distinct pedagogical callouts:
 * - WHY: architectural & design rationale
 * - MISTAKE: common beginner pitfalls & how to avoid them
 * - GODOT: exact step-by-step instructions inside the Godot 4.3 Editor
 * - KADI: Kenyan Kadi table rule being modeled
 * - TIP: debugging & workflow shortcuts
 */
@Composable
fun Callout(type: CalloutType, body: String, modifier: Modifier = Modifier) {
    val dark = isDarkScheme()
    val scheme = MaterialTheme.colorScheme
    val style = when (type) {
        CalloutType.WHY -> CalloutStyle(
            background = if (dark) CalloutColors.whyBackgroundDark else CalloutColors.whyBackgroundLight,
            border = if (dark) CalloutColors.whyBorderDark else CalloutColors.whyBorderLight,
            accent = if (dark) CalloutColors.whyIconDark else CalloutColors.whyIconLight,
            body = if (dark) CalloutColors.bodyDark else CalloutColors.bodyLight,
            title = "Why this matters",
            icon = Icons.Outlined.Lightbulb
        )
        CalloutType.MISTAKE -> CalloutStyle(
            background = if (dark) CalloutColors.mistakeBackgroundDark else CalloutColors.mistakeBackgroundLight,
            border = if (dark) CalloutColors.mistakeBorderDark else CalloutColors.mistakeBorderLight,
            accent = if (dark) CalloutColors.mistakeIconDark else CalloutColors.mistakeIconLight,
            body = if (dark) CalloutColors.bodyDark else CalloutColors.bodyLight,
            title = "Common mistake to avoid",
            icon = Icons.Outlined.WarningAmber
        )
        CalloutType.GODOT -> CalloutStyle(
            background = scheme.primary.copy(alpha = if (dark) 0.13f else 0.09f),
            border = scheme.primary.copy(alpha = 0.50f),
            accent = scheme.primary,
            body = if (dark) CalloutColors.bodyDark else CalloutColors.bodyLight,
            title = "In the Godot 4.3 Editor",
            icon = Icons.Outlined.Build
        )
        CalloutType.KADI -> CalloutStyle(
            background = scheme.secondary.copy(alpha = if (dark) 0.13f else 0.09f),
            border = scheme.secondary.copy(alpha = 0.50f),
            accent = scheme.secondary,
            body = if (dark) CalloutColors.bodyDark else CalloutColors.bodyLight,
            title = "Kadi Table Rule",
            icon = Icons.Outlined.Style
        )
        CalloutType.TIP -> CalloutStyle(
            background = scheme.tertiary.copy(alpha = if (dark) 0.12f else 0.08f),
            border = scheme.tertiary.copy(alpha = 0.45f),
            accent = scheme.tertiary,
            body = if (dark) CalloutColors.bodyDark else CalloutColors.bodyLight,
            title = "Pro Tip",
            icon = Icons.Outlined.Bolt
        )
    }
    val shape = RoundedCornerShape(18.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clip(shape)
            .background(style.background)
            .border(1.dp, style.border.copy(alpha = 0.55f), shape)
    ) {
        Box(modifier = Modifier.width(4.dp).fillMaxHeight().background(style.accent))
        Row(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(style.accent.copy(alpha = 0.20f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(style.icon, contentDescription = null, tint = style.accent, modifier = Modifier.size(17.dp))
            }
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(
                    text = style.title.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = style.accent
                )
                Text(
                    text = body,
                    style = MaterialTheme.typography.bodyMedium,
                    color = style.body,
                    modifier = Modifier.padding(top = 3.dp)
                )
            }
        }
    }
}
