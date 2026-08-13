package com.kadicompanion.ui.common

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kadicompanion.ui.theme.appBackgroundBrush
import com.kadicompanion.ui.theme.brandGradient
import com.kadicompanion.ui.theme.cardSheenBrush
import com.kadicompanion.ui.theme.heroTextPrimary
import com.kadicompanion.ui.theme.heroTextSecondary
import com.kadicompanion.ui.theme.heroTextTertiary
import com.kadicompanion.ui.theme.isDarkScheme

/*
 * The Kadi design kit.
 *
 * Every screen is built from these primitives: ambient background, clean top bar,
 * elevated action cards, quiet section headers, soft cards, and loading skeletons.
 */

const val SPADE = "\u2660"
const val HEART = "\u2665"
const val CLUB = "\u2663"
const val DIAMOND = "\u2666"

/** Fills the window with the ambient brand wash. Wrap screen content in this. */
@Composable
fun KadiBackground(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    Box(modifier = modifier.fillMaxSize().background(appBackgroundBrush()), content = content)
}

/** Transparent, tightly-set top bar with an optional eyebrow subtitle. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KadiTopBar(
    title: String,
    subtitle: String? = null,
    onBack: (() -> Unit)? = null,
    actions: @Composable () -> Unit = {}
) {
    TopAppBar(
        title = {
            Column {
                if (subtitle != null) {
                    Text(
                        text = subtitle.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                }
                Text(
                    text = title,
                    style = if (subtitle == null) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }
        },
        navigationIcon = {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
                }
            }
        },
        actions = { actions() },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground
        )
    )
}

/**
 * An elevated, sleek focus card. Redesigned from a loud banner to a refined,
 * premium card with rich subtle depth, gentle lighting sheen, and crisp typography.
 */
@Composable
fun HeroPanel(
    modifier: Modifier = Modifier,
    gradient: Brush = brandGradient(),
    shape: RoundedCornerShape = RoundedCornerShape(24.dp),
    onClick: (() -> Unit)? = null,
    contentPadding: androidx.compose.foundation.layout.PaddingValues = androidx.compose.foundation.layout.PaddingValues(20.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    val dark = isDarkScheme()
    val base = modifier
        .fillMaxWidth()
        .shadow(
            elevation = if (dark) 8.dp else 4.dp,
            shape = shape,
            ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.20f),
            spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
        )
        .clip(shape)
        .background(gradient)
    Box(modifier = if (onClick != null) base.clickable(onClick = onClick) else base) {
        // Subtle top glass highlight
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = if (dark) 0.15f else 0.22f),
                            Color.Transparent
                        )
                    )
                )
        )
        SuitWatermarks()
        Column(
            modifier = Modifier.fillMaxWidth().padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            content = content
        )
    }
}

/** Decorative card suits used inside [HeroPanel]. */
@Composable
private fun BoxScope.SuitWatermarks() {
    val heroWhite = heroTextPrimary()
    Text(
        text = SPADE,
        fontSize = 52.sp,
        fontWeight = FontWeight.Black,
        color = heroWhite,
        modifier = Modifier
            .align(Alignment.TopEnd)
            .offset(x = 4.dp, y = (-12).dp)
            .rotate(16f)
            .alpha(0.09f)
    )
    Text(
        text = HEART,
        fontSize = 42.sp,
        fontWeight = FontWeight.Black,
        color = heroWhite,
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .offset(x = (-16).dp, y = 8.dp)
            .rotate(-12f)
            .alpha(0.08f)
    )
    Text(
        text = DIAMOND,
        fontSize = 34.sp,
        fontWeight = FontWeight.Black,
        color = heroWhite,
        modifier = Modifier
            .align(Alignment.CenterEnd)
            .offset(x = 2.dp)
            .rotate(8f)
            .alpha(0.06f)
    )
}

/** Small uppercase eyebrow + title, with a clean decorative accent bar. */
@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    eyebrow: String? = null,
    trailing: @Composable (() -> Unit)? = null
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                if (eyebrow != null) {
                    Text(
                        text = eyebrow.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.6.sp
                    )
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )
            }
            trailing?.invoke()
        }
        Spacer(Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            primaryColor.copy(alpha = 0.30f),
                            primaryColor.copy(alpha = 0.05f),
                            Color.Transparent
                        )
                    )
                )
        )
    }
}

/**
 * The default content card: a soft container with a hairline outline and an
 * optional accent color.
 */
@Composable
fun KadiCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    accent: Color? = null,
    enabled: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    val dark = isDarkScheme()
    val orangeSeed = accent ?: MaterialTheme.colorScheme.primary
    val container = if (accent != null) {
        orangeSeed.copy(alpha = if (dark) 0.12f else 0.08f)
    } else {
        MaterialTheme.colorScheme.surfaceContainer
    }
    val bodyBrush = if (accent != null) {
        Brush.linearGradient(
            colors = listOf(
                orangeSeed.copy(alpha = if (dark) 0.30f else 0.24f),
                MaterialTheme.colorScheme.surfaceContainer.copy(alpha = if (dark) 0.96f else 1f),
                orangeSeed.copy(alpha = if (dark) 0.14f else 0.10f)
            )
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                MaterialTheme.colorScheme.surfaceContainer,
                MaterialTheme.colorScheme.surfaceContainerHigh
            )
        )
    }
    val border = BorderStroke(
        if (accent != null) 1.dp else 0.5.dp,
        (accent ?: MaterialTheme.colorScheme.outlineVariant).copy(alpha = if (accent != null) 0.34f else 0.6f)
    )
    val colors = CardDefaults.cardColors(containerColor = container)
    val elevation = CardDefaults.cardElevation(
        defaultElevation = if (dark) 4.dp else 2.dp,
        pressedElevation = if (dark) 8.dp else 4.dp
    )
    if (onClick != null) {
        Card(
            modifier = modifier.fillMaxWidth(),
            onClick = onClick,
            enabled = enabled,
            colors = colors,
            border = border,
            shape = MaterialTheme.shapes.medium,
            elevation = elevation
        ) { CardBody(content, bodyBrush) }
    } else {
        Card(
            modifier = modifier.fillMaxWidth(),
            colors = colors,
            border = border,
            shape = MaterialTheme.shapes.medium,
            elevation = elevation
        ) { CardBody(content, bodyBrush) }
    }
}

@Composable
private fun CardBody(content: @Composable ColumnScope.() -> Unit, bodyBrush: Brush) {
    Box(modifier = Modifier.fillMaxWidth()) {
        SuitBackdrop(modifier = Modifier.fillMaxSize())
        Column(
            modifier = Modifier.fillMaxWidth().background(bodyBrush).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            content = content
        )
    }
}

@Composable
private fun SuitBackdrop(modifier: Modifier = Modifier) {
    val tint = MaterialTheme.colorScheme.primary.copy(alpha = if (isDarkScheme()) 0.06f else 0.08f)
    Box(modifier = modifier) {
        Text(
            text = SPADE,
            fontSize = 44.sp,
            fontWeight = FontWeight.Black,
            color = tint,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 6.dp, y = (-10).dp)
                .rotate(14f)
        )
        Text(
            text = HEART,
            fontSize = 30.sp,
            fontWeight = FontWeight.Black,
            color = tint,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(x = 4.dp)
                .rotate(-10f)
        )
        Text(
            text = CLUB,
            fontSize = 26.sp,
            fontWeight = FontWeight.Black,
            color = tint,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-2).dp, y = 6.dp)
                .rotate(-14f)
        )
    }
}

/** Compact metric tile with a gradient-tinted background. */
@Composable
fun StatTile(
    value: String,
    label: String,
    accent: Color,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    val dark = isDarkScheme()
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        accent.copy(alpha = if (dark) 0.16f else 0.12f),
                        accent.copy(alpha = if (dark) 0.06f else 0.04f)
                    )
                )
            )
            .padding(vertical = 14.dp, horizontal = 10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            if (icon != null) {
                Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(16.dp))
            }
            Text(value, style = MaterialTheme.typography.headlineSmall, color = accent, fontWeight = FontWeight.ExtraBold)
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )
        }
    }
}

/** Rounded tag pill with an optional leading icon. */
@Composable
fun Pill(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    icon: ImageVector? = null,
    filled: Boolean = true
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(
                if (filled) Brush.horizontalGradient(
                    colors = listOf(
                        color.copy(alpha = 0.20f),
                        color.copy(alpha = 0.12f)
                    )
                ) else Brush.horizontalGradient(
                    colors = listOf(Color.Transparent, Color.Transparent)
                )
            )
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (icon != null) Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(12.dp))
        Text(text, style = MaterialTheme.typography.labelSmall, color = color, fontWeight = FontWeight.Bold)
    }
}

/** Circular icon chip used in list rows. */
@Composable
fun IconBubble(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.primary,
    size: Dp = 40.dp
) {
    val glowAlpha = if (isDarkScheme()) 0.08f else 0.05f
    Box(
        modifier = modifier
            .size(size)
            .drawBehind {
                drawCircle(
                    color = tint.copy(alpha = glowAlpha),
                    radius = this.size.minDimension / 2 + 3.dp.toPx(),
                    center = Offset(this.size.width / 2, this.size.height / 2)
                )
            }
            .clip(CircleShape)
            .background(tint.copy(alpha = 0.14f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(size * 0.5f))
    }
}

/** Shimmer loading skeleton used when data is being fetched. */
@Composable
fun LoadingSkeleton(lines: Int = 5, modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "skeleton")
    val alpha by transition.animateFloat(
        initialValue = 0.15f,
        targetValue = 0.45f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "skeletonAlpha"
    )
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(lines) { index ->
            val fraction = when (index % 3) {
                0 -> 1f
                1 -> 0.78f
                else -> 0.55f
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction)
                    .height(if (index == 0) 24.dp else 16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = alpha))
            )
        }
    }
}
