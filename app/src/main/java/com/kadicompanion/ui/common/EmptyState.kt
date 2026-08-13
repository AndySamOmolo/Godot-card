package com.kadicompanion.ui.common

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kadicompanion.ui.theme.LocalReduceMotion

/**
 * Full-area placeholder shown when a list or screen has no data.
 * Features floating suit glyphs behind the icon and a pulsing icon halo.
 */
@Composable
fun EmptyState(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Outlined.Inbox
) {
    val reduceMotion = LocalReduceMotion.current
    val glowColor = MaterialTheme.colorScheme.primary

    // Pulsing glow behind the icon
    val haloAlpha = if (!reduceMotion) {
        val transition = rememberInfiniteTransition(label = "emptyHalo")
        val alpha by transition.animateFloat(
            initialValue = 0.08f,
            targetValue = 0.18f,
            animationSpec = infiniteRepeatable(tween(1600), repeatMode = RepeatMode.Reverse),
            label = "haloAlpha"
        )
        alpha
    } else {
        0.12f
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 60.dp),
        contentAlignment = Alignment.Center
    ) {
        // Floating suit watermarks
        Text(
            text = SPADE,
            fontSize = 40.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f),
            fontWeight = FontWeight.Black,
            modifier = Modifier
                .align(Alignment.TopStart)
                .rotate(-15f)
        )
        Text(
            text = HEART,
            fontSize = 32.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.03f),
            fontWeight = FontWeight.Black,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .rotate(12f)
        )
        Text(
            text = DIAMOND,
            fontSize = 28.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f),
            fontWeight = FontWeight.Black,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .rotate(20f)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .drawBehind {
                        drawCircle(
                            color = glowColor.copy(alpha = haloAlpha),
                            radius = size.minDimension / 2 + 12.dp.toPx(),
                            center = Offset(size.width / 2, size.height / 2)
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.size(48.dp)
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
