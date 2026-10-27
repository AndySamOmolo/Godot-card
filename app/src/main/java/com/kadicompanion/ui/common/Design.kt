package com.kadicompanion.ui.common

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kadicompanion.data.local.datastore.CardStyle
import com.kadicompanion.data.local.datastore.HeroStyle
import com.kadicompanion.data.local.datastore.TopBarStyle
import com.kadicompanion.data.local.datastore.UiDensity
import com.kadicompanion.ui.theme.CodeFontFamily
import com.kadicompanion.ui.theme.LocalOpenQuickCustomizer
import com.kadicompanion.ui.theme.LocalUiPreferences
import com.kadicompanion.ui.theme.brandGradient
import com.kadicompanion.ui.theme.isDarkScheme

const val SPADE = "\u2660"
const val HEART = "\u2665"
const val CLUB = "\u2663"
const val DIAMOND = "\u2666"

/** Returns screen horizontal padding scaled by user's selected UI density. */
@Composable
fun studioScreenPadding(): Dp = when (LocalUiPreferences.current.uiDensity) {
    UiDensity.COMPACT -> 12.dp
    UiDensity.COMFORTABLE -> 15.dp
    UiDensity.SPACIOUS -> 20.dp
}

/** Returns vertical spacing between cards scaled by user's selected UI density. */
@Composable
fun studioCardSpacing(): Dp = when (LocalUiPreferences.current.uiDensity) {
    UiDensity.COMPACT -> 8.dp
    UiDensity.COMFORTABLE -> 12.dp
    UiDensity.SPACIOUS -> 16.dp
}

/** Clean studio background container. */
@Composable
fun KadiBackground(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        content = content
    )
}

/**
 * Sleek, compact phone Top Bar (36dp–50dp height instead of Material 3's bulky 64dp TopAppBar).
 * Responds live to `LocalUiPreferences.current.topBarStyle` (`ULTRA_SLIM`, `COMPACT`, `STANDARD`).
 */
@Composable
fun KadiTopBar(
    title: String,
    subtitle: String? = null,
    onBack: (() -> Unit)? = null,
    showCustomizerButton: Boolean = true,
    actions: @Composable () -> Unit = {}
) {
    val uiPrefs = LocalUiPreferences.current
    val openCustomizer = LocalOpenQuickCustomizer.current
    val cleanTitle = cleanPlainText(title)
    val cleanSub = subtitle?.let(::cleanPlainText)
    val topStyle = uiPrefs.topBarStyle

    val barHeight = when (topStyle) {
        TopBarStyle.ULTRA_SLIM -> 36.dp
        TopBarStyle.COMPACT -> 42.dp
        TopBarStyle.STANDARD -> 50.dp
    }
    val btnSize = when (topStyle) {
        TopBarStyle.ULTRA_SLIM -> 30.dp
        TopBarStyle.COMPACT -> 34.dp
        TopBarStyle.STANDARD -> 38.dp
    }
    val iconSize = when (topStyle) {
        TopBarStyle.ULTRA_SLIM -> 17.dp
        TopBarStyle.COMPACT -> 18.dp
        TopBarStyle.STANDARD -> 20.dp
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(barHeight)
                .padding(horizontal = if (onBack != null) 6.dp else 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (onBack != null) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.size(btnSize)
                ) {
                    Icon(
                        Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(iconSize)
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                if (cleanSub != null && topStyle != TopBarStyle.ULTRA_SLIM) {
                    Text(
                        text = cleanSub.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = CodeFontFamily,
                            fontSize = 9.5.sp,
                            lineHeight = 11.sp
                        ),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.6.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    text = cleanTitle,
                    style = when (topStyle) {
                        TopBarStyle.ULTRA_SLIM -> MaterialTheme.typography.titleSmall
                        TopBarStyle.COMPACT -> MaterialTheme.typography.titleSmall.copy(fontSize = 15.sp)
                        TopBarStyle.STANDARD -> MaterialTheme.typography.titleMedium
                    },
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                actions()
                if (showCustomizerButton && openCustomizer != null) {
                    IconButton(
                        onClick = openCustomizer,
                        modifier = Modifier.size(btnSize)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Tune,
                            contentDescription = "Customize UI",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(iconSize)
                        )
                    }
                }
            }
        }
        HorizontalDivider(
            thickness = 0.75.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f)
        )
    }
}

/**
 * Customizable Studio Hero Panel supporting Rich Gradient, Compact Banner, and Flat Surface modes.
 */
@Composable
fun HeroPanel(
    modifier: Modifier = Modifier,
    gradient: Brush = brandGradient(),
    shape: Shape = MaterialTheme.shapes.large,
    onClick: (() -> Unit)? = null,
    contentPadding: PaddingValues? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val uiPrefs = LocalUiPreferences.current
    val effectivePadding = contentPadding ?: when {
        uiPrefs.heroStyle == HeroStyle.COMPACT_CARD || uiPrefs.uiDensity == UiDensity.COMPACT -> PaddingValues(14.dp)
        uiPrefs.uiDensity == UiDensity.SPACIOUS -> PaddingValues(20.dp)
        else -> PaddingValues(16.dp)
    }
    val effectiveBrush = when (uiPrefs.heroStyle) {
        HeroStyle.SURFACE_FLAT -> Brush.linearGradient(
            colors = listOf(
                Color(0xFF0F172A),
                Color(0xFF1E293B)
            )
        )
        else -> gradient
    }
    val borderColor = when (uiPrefs.heroStyle) {
        HeroStyle.SURFACE_FLAT -> MaterialTheme.colorScheme.primary.copy(alpha = 0.45f)
        else -> Color.White.copy(alpha = 0.16f)
    }

    val base = modifier
        .fillMaxWidth()
        .clip(shape)
        .background(effectiveBrush)
        .border(1.dp, borderColor, shape)
    Box(modifier = if (onClick != null) base.clickable(onClick = onClick) else base) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(effectivePadding),
            verticalArrangement = Arrangement.spacedBy(if (uiPrefs.heroStyle == HeroStyle.COMPACT_CARD) 6.dp else 8.dp),
            content = content
        )
    }
}

/** Clean studio section header with optional monospace eyebrow and trailing slot. */
@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    eyebrow: String? = null,
    trailing: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(top = 2.dp, bottom = 1.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(1.dp)) {
            if (eyebrow != null) {
                Text(
                    text = cleanPlainText(eyebrow).uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily, fontSize = 10.sp),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.7.sp
                )
            }
            Text(
                text = cleanPlainText(title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
        }
        trailing?.invoke()
    }
}

/**
 * Customizable Studio Card reacting to `cardStyle`, `cornerRadiusDp`, `cardBorderWidthDp`, `showCardAccentBar`, and `uiDensity`.
 */
@Composable
fun KadiCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    accent: Color? = null,
    enabled: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    val uiPrefs = LocalUiPreferences.current
    val dark = isDarkScheme()
    val containerColor = when (uiPrefs.cardStyle) {
        CardStyle.FLAT_BORDER -> MaterialTheme.colorScheme.surfaceContainer
        CardStyle.ELEVATED_SHADOW -> MaterialTheme.colorScheme.surfaceContainerLow
        CardStyle.SUBTLE_GLASS -> MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = if (dark) 0.72f else 0.85f)
        CardStyle.OUTLINED_MINIMAL -> Color.Transparent
    }
    val borderColor = when {
        accent != null -> accent.copy(alpha = if (dark) 0.55f else 0.45f)
        uiPrefs.cardStyle == CardStyle.SUBTLE_GLASS -> MaterialTheme.colorScheme.primary.copy(alpha = 0.28f)
        uiPrefs.cardStyle == CardStyle.OUTLINED_MINIMAL -> MaterialTheme.colorScheme.outline
        else -> MaterialTheme.colorScheme.outlineVariant
    }
    val borderWidth = uiPrefs.cardBorderWidthDp.dp
    val border = if (uiPrefs.cardBorderWidthDp > 0.05f) BorderStroke(borderWidth, borderColor) else null
    val colors = CardDefaults.cardColors(
        containerColor = containerColor,
        disabledContainerColor = containerColor.copy(alpha = 0.6f)
    )
    val elevationDp = if (uiPrefs.cardStyle == CardStyle.ELEVATED_SHADOW) 4.dp else 0.dp
    val elevation = CardDefaults.cardElevation(defaultElevation = elevationDp, pressedElevation = elevationDp)

    val pad = when (uiPrefs.uiDensity) {
        UiDensity.COMPACT -> 12.dp
        UiDensity.COMFORTABLE -> 14.dp
        UiDensity.SPACIOUS -> 18.dp
    }
    val showAccentBar = accent != null && uiPrefs.showCardAccentBar

    if (onClick != null) {
        Card(
            modifier = modifier.fillMaxWidth(),
            onClick = onClick,
            enabled = enabled,
            colors = colors,
            border = border,
            shape = MaterialTheme.shapes.medium,
            elevation = elevation
        ) {
            StudioCardBody(accent = if (showAccentBar) accent else null, padding = pad, content = content)
        }
    } else {
        Card(
            modifier = modifier.fillMaxWidth(),
            colors = colors,
            border = border,
            shape = MaterialTheme.shapes.medium,
            elevation = elevation
        ) {
            StudioCardBody(accent = if (showAccentBar) accent else null, padding = pad, content = content)
        }
    }
}

@Composable
private fun StudioCardBody(accent: Color?, padding: Dp, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (accent != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(accent)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            content = content
        )
    }
}

/** Bento metric card with clean border, top icon badge, and bold value. */
@Composable
fun StatTile(
    value: String,
    label: String,
    accent: Color,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    val uiPrefs = LocalUiPreferences.current
    val compact = uiPrefs.uiDensity == UiDensity.COMPACT
    val border = if (uiPrefs.cardBorderWidthDp > 0.05f) {
        BorderStroke(uiPrefs.cardBorderWidthDp.dp, MaterialTheme.colorScheme.outlineVariant)
    } else null

    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainer,
        border = border
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = if (compact) 10.dp else 12.dp, vertical = if (compact) 8.dp else 10.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                if (icon != null) {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(accent.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(11.dp))
                    }
                }
                Text(
                    text = cleanPlainText(label),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/** Clean studio badge pill with subtle border. */
@Composable
fun Pill(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    icon: ImageVector? = null,
    filled: Boolean = true
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraSmall,
        color = if (filled) color.copy(alpha = 0.13f) else Color.Transparent,
        border = BorderStroke(1.dp, color.copy(alpha = 0.30f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (icon != null) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(11.dp))
            }
            Text(
                text = cleanPlainText(text),
                style = MaterialTheme.typography.labelSmall,
                color = color,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/** Rounded-square studio icon container. */
@Composable
fun IconBubble(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.primary,
    size: Dp = 36.dp
) {
    val shape = MaterialTheme.shapes.small
    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(tint.copy(alpha = 0.13f))
            .border(1.dp, tint.copy(alpha = 0.25f), shape),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(size * 0.5f))
    }
}

/** Shimmer loading skeleton. */
@Composable
fun LoadingSkeleton(lines: Int = 5, modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "skeleton")
    val alpha by transition.animateFloat(
        initialValue = 0.08f,
        targetValue = 0.22f,
        animationSpec = infiniteRepeatable(
            animation = tween(750),
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
                1 -> 0.80f
                else -> 0.60f
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction)
                    .height(if (index == 0) 28.dp else 18.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = alpha))
            )
        }
    }
}
