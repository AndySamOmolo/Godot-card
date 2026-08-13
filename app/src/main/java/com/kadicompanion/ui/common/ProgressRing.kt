package com.kadicompanion.ui.common

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kadicompanion.ui.theme.LocalReduceMotion

/**
 * Animated completion ring with a soft radial glow behind the arc.
 * Draws: radial glow → soft track → faint outer glow arc → gradient arc.
 * At 100%, the percentage text pulses gently.
 */
@Composable
fun ProgressRing(
    fraction: Float,
    modifier: Modifier = Modifier,
    size: Dp = 96.dp,
    strokeWidth: Dp = 10.dp,
    label: String? = null
) {
    val reduceMotion = LocalReduceMotion.current
    val animated by animateFloatAsState(
        targetValue = fraction.coerceIn(0f, 1f),
        animationSpec = if (reduceMotion) tween(durationMillis = 0) else tween(durationMillis = 700),
        label = "progress"
    )
    val trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.10f)
    val progressStart = MaterialTheme.colorScheme.primary
    val progressEnd = MaterialTheme.colorScheme.tertiary
    val glowColor = MaterialTheme.colorScheme.primary

    // Pulse at 100%
    val pulseAlpha = if (fraction >= 1f && !reduceMotion) {
        val transition = rememberInfiniteTransition(label = "ringPulse")
        val pulse by transition.animateFloat(
            initialValue = 1f,
            targetValue = 0.6f,
            animationSpec = infiniteRepeatable(tween(800), repeatMode = RepeatMode.Reverse),
            label = "pulseAlpha"
        )
        pulse
    } else {
        1f
    }

    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(size)) {
            val strokePx = strokeWidth.toPx()
            val stroke = Stroke(width = strokePx, cap = StrokeCap.Round)
            val glowStroke = Stroke(width = strokePx * 2.2f, cap = StrokeCap.Round)
            val inset = strokePx / 2
            val arcSize = Size(this.size.width - strokePx, this.size.height - strokePx)
            val progressBrush = Brush.sweepGradient(
                colors = listOf(progressStart, progressEnd, progressStart),
                center = Offset(this.size.width / 2, this.size.height / 2)
            )

            // Background radial glow
            drawCircle(
                color = glowColor.copy(alpha = 0.08f * animated),
                radius = this.size.minDimension / 2 + strokePx,
                center = Offset(this.size.width / 2, this.size.height / 2)
            )

            // Track
            drawArc(color = trackColor, startAngle = 0f, sweepAngle = 360f, useCenter = false, topLeft = Offset(inset, inset), size = arcSize, style = stroke)

            // Outer glow arc
            drawArc(brush = progressBrush, startAngle = -90f, sweepAngle = 360f * animated, useCenter = false, topLeft = Offset(inset, inset), size = arcSize, style = glowStroke, alpha = 0.16f)

            // Main progress arc
            drawArc(brush = progressBrush, startAngle = -90f, sweepAngle = 360f * animated, useCenter = false, topLeft = Offset(inset, inset), size = arcSize, style = stroke)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(0.dp)) {
            Text(
                text = "${(fraction * 100).toInt()}%",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = pulseAlpha)
            )
            if (label != null) {
                Text(
                    text = label.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
