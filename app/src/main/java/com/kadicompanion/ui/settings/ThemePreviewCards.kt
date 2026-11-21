package com.kadicompanion.ui.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kadicompanion.data.local.datastore.AccentPreset
import com.kadicompanion.data.local.datastore.CodeThemePreset
import com.kadicompanion.data.local.datastore.ContrastLevel
import com.kadicompanion.data.local.datastore.KadiFeltPreset
import com.kadicompanion.data.local.datastore.StudioThemeBundle
import com.kadicompanion.data.local.datastore.ThemePref
import com.kadicompanion.data.local.datastore.UiPreferences
import com.kadicompanion.ui.theme.CodeFontFamily
import com.kadicompanion.ui.theme.ThemeEngine
import com.kadicompanion.ui.theme.resolveCodePalette

/**
 * Horizontal One-Tap Studio Presets card gallery allowing users to apply a complete
 * theme + accent + contrast + card style + code theme + Kadi felt bundle in one tap.
 */
@Composable
fun StudioThemeBundleGallery(
    uiPrefs: UiPreferences,
    onApplyBundle: (StudioThemeBundle) -> Unit,
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    val systemDark = isSystemInDarkTheme()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.spacedBy(if (compact) 8.dp else 10.dp)
    ) {
        StudioThemeBundle.entries.forEach { bundle ->
            StudioBundleCard(
                bundle = bundle,
                selected = bundle.matches(uiPrefs),
                systemDark = systemDark,
                compact = compact,
                onClick = { onApplyBundle(bundle) },
                modifier = Modifier
                    .width(if (compact) 156.dp else 188.dp)
                    .fillMaxHeight()
            )
        }
    }
}

@Composable
private fun StudioBundleCard(
    bundle: StudioThemeBundle,
    selected: Boolean,
    systemDark: Boolean,
    compact: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = ThemeEngine.isDarkTheme(bundle.theme, systemDark)
    val scheme = remember(bundle, systemDark) {
        ThemeEngine.resolveColorScheme(
            themePref = bundle.theme,
            accentPreset = bundle.accentPreset,
            contrastLevel = bundle.contrastLevel,
            systemDark = systemDark
        )
    }
    val extended = remember(scheme, bundle, isDark) {
        ThemeEngine.resolveExtendedColors(
            colorScheme = scheme,
            accentPreset = bundle.accentPreset,
            contrastLevel = bundle.contrastLevel,
            isDark = isDark,
            isAmoled = bundle.theme == ThemePref.AMOLED,
            isDynamic = false,
            kadiFeltPreset = bundle.kadiFeltPreset
        )
    }
    val borderColor = if (selected) scheme.primary else MaterialTheme.colorScheme.outlineVariant

    Surface(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        color = scheme.background,
        border = BorderStroke(if (selected) 2.dp else 1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier.padding(if (compact) 8.dp else 10.dp),
            verticalArrangement = Arrangement.spacedBy(if (compact) 6.dp else 7.dp)
        ) {
            // Mini hero gradient strip with active checkmark
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (compact) 22.dp else 28.dp)
                    .clip(RoundedCornerShape(7.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                extended.heroGradientStart,
                                extended.heroGradientMid,
                                extended.heroGradientEnd
                            )
                        )
                    )
                    .padding(horizontal = 7.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = bundle.label,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = extended.onHeroPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    if (selected) {
                        Box(
                            modifier = Modifier
                                .size(15.dp)
                                .clip(CircleShape)
                                .background(scheme.primary)
                                .border(1.dp, Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Check,
                                contentDescription = "Active preset",
                                tint = scheme.onPrimary,
                                modifier = Modifier.size(10.dp)
                            )
                        }
                    }
                }
            }

            // Mini accent + felt swatch strip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(10.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(scheme.primary)
                )
                Box(
                    modifier = Modifier
                        .weight(0.7f)
                        .height(10.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(scheme.secondary)
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(10.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Brush.horizontalGradient(listOf(extended.feltStart, extended.feltEnd)))
                        .border(0.75.dp, extended.feltBorder, RoundedCornerShape(4.dp))
                )
            }

            if (!compact) {
                Text(
                    text = bundle.subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = scheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/**
 * 2-column visual Kadi Table Felt gallery showing live felt gradient previews,
 * felt border trim, and miniature playing card selection highlight.
 */
@Composable
fun KadiFeltGallery(
    selectedPreset: KadiFeltPreset,
    themePref: ThemePref,
    accentPreset: AccentPreset,
    contrastLevel: ContrastLevel,
    onSelectPreset: (KadiFeltPreset) -> Unit,
    modifier: Modifier = Modifier
) {
    val systemDark = isSystemInDarkTheme()
    val isDark = ThemeEngine.isDarkTheme(themePref, systemDark)
    val previewScheme = remember(themePref, accentPreset, contrastLevel, systemDark) {
        ThemeEngine.resolveColorScheme(
            themePref = themePref,
            accentPreset = accentPreset,
            contrastLevel = contrastLevel,
            systemDark = systemDark
        )
    }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        KadiFeltPreset.entries.chunked(2).forEach { rowPresets ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rowPresets.forEach { preset ->
                    KadiFeltPreviewCard(
                        preset = preset,
                        previewScheme = previewScheme,
                        isDark = isDark,
                        selected = selectedPreset == preset,
                        onClick = { onSelectPreset(preset) },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )
                }
                if (rowPresets.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun KadiFeltPreviewCard(
    preset: KadiFeltPreset,
    previewScheme: androidx.compose.material3.ColorScheme,
    isDark: Boolean,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val felt = remember(preset, previewScheme, isDark) {
        ThemeEngine.resolveFeltTokens(preset, previewScheme, isDark)
    }
    val activeBorder = if (selected) felt.feltBorder else MaterialTheme.colorScheme.outlineVariant

    Surface(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        border = BorderStroke(if (selected) 2.dp else 1.dp, activeBorder)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Live felt gradient tabletop swatch with miniature lifted card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(RoundedCornerShape(9.dp))
                    .background(Brush.verticalGradient(listOf(felt.feltStart, felt.feltEnd)))
                    .border(1.5.dp, felt.feltBorder, RoundedCornerShape(9.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Normal mini card
                    Box(
                        modifier = Modifier
                            .width(22.dp)
                            .height(30.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFF8FAFC))
                            .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "7♠",
                            style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                            color = Color(0xFF0F172A),
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                    // Highlighted selected mini card
                    Box(
                        modifier = Modifier
                            .width(24.dp)
                            .height(34.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFFEF9C3))
                            .border(1.5.dp, felt.cardBorderSelected, RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "A♥",
                            style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                            color = Color(0xFFDC2626),
                            fontWeight = FontWeight.ExtraBold,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = preset.label,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = preset.subtitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (selected) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(felt.feltBorder),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = "Selected felt",
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * 2x2 visual Theme Mode Gallery showing live miniature phone/card previews
 * for DARK, AMOLED, LIGHT, and SYSTEM.
 */
@Composable
fun ThemeModeGallery(
    selectedTheme: ThemePref,
    accentPreset: AccentPreset,
    contrastLevel: ContrastLevel,
    onSelectTheme: (ThemePref) -> Unit,
    modifier: Modifier = Modifier
) {
    val systemDark = isSystemInDarkTheme()
    val items = listOf(
        Triple(ThemePref.DARK, "Studio Dark", "Deep carbon-slate"),
        Triple(ThemePref.AMOLED, "AMOLED Black", "True #000000 OLED"),
        Triple(ThemePref.LIGHT, "Studio Light", "Crisp alabaster"),
        Triple(ThemePref.SYSTEM, "System Auto", if (systemDark) "Following Dark" else "Following Light")
    )

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rowItems.forEach { (mode, title, subtitle) ->
                    ThemeModePreviewTile(
                        mode = mode,
                        title = title,
                        subtitle = subtitle,
                        accentPreset = accentPreset,
                        contrastLevel = contrastLevel,
                        systemDark = systemDark,
                        selected = selectedTheme == mode,
                        onClick = { onSelectTheme(mode) },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )
                }
            }
        }
    }
}

@Composable
private fun ThemeModePreviewTile(
    mode: ThemePref,
    title: String,
    subtitle: String,
    accentPreset: AccentPreset,
    contrastLevel: ContrastLevel,
    systemDark: Boolean,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val previewScheme = remember(mode, accentPreset, contrastLevel, systemDark) {
        ThemeEngine.resolveColorScheme(
            themePref = mode,
            accentPreset = accentPreset,
            contrastLevel = contrastLevel,
            systemDark = systemDark
        )
    }
    val activeBorder = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant

    Surface(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        border = BorderStroke(if (selected) 2.dp else 1.dp, activeBorder)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Miniature phone viewport preview
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(74.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(previewScheme.background)
                    .border(1.dp, previewScheme.outlineVariant, RoundedCornerShape(10.dp))
                    .padding(7.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    // Mini top bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .width(34.dp)
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(previewScheme.onBackground.copy(alpha = 0.85f))
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(previewScheme.primary)
                        )
                    }
                    // Mini surface card
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(previewScheme.surfaceContainer)
                            .border(0.75.dp, previewScheme.outlineVariant, RoundedCornerShape(6.dp))
                            .padding(6.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Box(
                                modifier = Modifier
                                    .width(24.dp)
                                    .height(5.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(previewScheme.primary)
                            )
                            Box(
                                modifier = Modifier
                                    .width(18.dp)
                                    .height(5.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(previewScheme.secondary)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.78f)
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(previewScheme.onSurfaceVariant.copy(alpha = 0.55f))
                        )
                    }
                    // Mini bottom pill bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Box(modifier = Modifier.width(16.dp).height(4.dp).clip(CircleShape).background(previewScheme.primary))
                        Box(modifier = Modifier.width(12.dp).height(4.dp).clip(CircleShape).background(previewScheme.outlineVariant))
                        Box(modifier = Modifier.width(12.dp).height(4.dp).clip(CircleShape).background(previewScheme.outlineVariant))
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (selected) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = "Selected",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * 2-column visual Accent Palette Gallery for all 7 AccentPresets, showing live background,
 * card surface, primary/secondary/tertiary pills, mini progress bar, 2-line GDScript syntax strip,
 * and active checkmark ring.
 */
@Composable
fun AccentPaletteGallery(
    selectedPreset: AccentPreset,
    themePref: ThemePref,
    contrastLevel: ContrastLevel,
    onSelectPreset: (AccentPreset) -> Unit,
    modifier: Modifier = Modifier
) {
    val systemDark = isSystemInDarkTheme()
    val presets = AccentPreset.entries

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        presets.chunked(2).forEach { rowPresets ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rowPresets.forEach { preset ->
                    AccentPalettePreviewCard(
                        preset = preset,
                        themePref = themePref,
                        contrastLevel = contrastLevel,
                        systemDark = systemDark,
                        selected = selectedPreset == preset,
                        onClick = { onSelectPreset(preset) },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )
                }
                if (rowPresets.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun AccentPalettePreviewCard(
    preset: AccentPreset,
    themePref: ThemePref,
    contrastLevel: ContrastLevel,
    systemDark: Boolean,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = ThemeEngine.isDarkTheme(themePref, systemDark)
    val scheme = remember(themePref, preset, contrastLevel, systemDark) {
        ThemeEngine.resolveColorScheme(
            themePref = themePref,
            accentPreset = preset,
            contrastLevel = contrastLevel,
            systemDark = systemDark
        )
    }
    val extended = remember(scheme, preset, contrastLevel, isDark, themePref) {
        ThemeEngine.resolveExtendedColors(
            colorScheme = scheme,
            accentPreset = preset,
            contrastLevel = contrastLevel,
            isDark = isDark,
            isAmoled = themePref == ThemePref.AMOLED,
            isDynamic = false
        )
    }
    val syntax = remember(preset, isDark, contrastLevel) {
        resolveCodePalette(
            preset = CodeThemePreset.AUTO_MATCH,
            accentPreset = preset,
            isDark = isDark,
            contrastLevel = contrastLevel
        )
    }

    val ringColor = if (selected) scheme.primary else MaterialTheme.colorScheme.outlineVariant

    Surface(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        color = scheme.background,
        border = BorderStroke(if (selected) 2.dp else 1.dp, ringColor)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            // Mini hero gradient header + checkmark ring
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(26.dp)
                    .clip(RoundedCornerShape(7.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                extended.heroGradientStart,
                                extended.heroGradientMid,
                                extended.heroGradientEnd
                            )
                        )
                    )
                    .padding(horizontal = 7.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = preset.label,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = extended.onHeroPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    if (selected) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(scheme.primary)
                                .border(1.5.dp, Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Check,
                                contentDescription = "Active preset",
                                tint = scheme.onPrimary,
                                modifier = Modifier.size(10.dp)
                            )
                        }
                    }
                }
            }

            // Mini card surface with primary/secondary/tertiary pills + progress bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(7.dp))
                    .background(scheme.surfaceContainer)
                    .border(0.75.dp, scheme.outlineVariant, RoundedCornerShape(7.dp))
                    .padding(7.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(10.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(scheme.primary)
                    )
                    Box(
                        modifier = Modifier
                            .weight(0.75f)
                            .height(10.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(scheme.secondary)
                    )
                    Box(
                        modifier = Modifier
                            .weight(0.55f)
                            .height(10.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(scheme.tertiary)
                    )
                }
                // Mini progress bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(CircleShape)
                        .background(scheme.surfaceContainerHighest)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.68f)
                            .height(4.dp)
                            .clip(CircleShape)
                            .background(scheme.primary)
                    )
                }
            }

            // 2-line GDScript syntax strip
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(syntax.background)
                    .border(0.75.dp, syntax.border, RoundedCornerShape(6.dp))
                    .padding(horizontal = 7.dp, vertical = 5.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "func",
                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                        fontWeight = FontWeight.Bold,
                        color = syntax.keyword,
                        maxLines = 1,
                        softWrap = false
                    )
                    Text(
                        text = "deal()",
                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                        color = syntax.function,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "var",
                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                        fontWeight = FontWeight.Bold,
                        color = syntax.keyword,
                        maxLines = 1,
                        softWrap = false
                    )
                    Text(
                        text = "deck",
                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                        color = syntax.plain,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "52",
                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                        color = syntax.number,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }
    }
}

/**
 * Interactive Contrast Level selector (Standard / Medium / High Contrast)
 * with visual WCAG contrast preview pills.
 */
@Composable
fun ContrastLevelSelector(
    selectedLevel: ContrastLevel,
    themePref: ThemePref,
    accentPreset: AccentPreset,
    onSelectLevel: (ContrastLevel) -> Unit,
    modifier: Modifier = Modifier
) {
    val systemDark = isSystemInDarkTheme()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ContrastLevel.entries.forEach { level ->
            val selected = selectedLevel == level
            val previewScheme = remember(themePref, accentPreset, level, systemDark) {
                ThemeEngine.resolveColorScheme(
                    themePref = themePref,
                    accentPreset = accentPreset,
                    contrastLevel = level,
                    systemDark = systemDark
                )
            }
            val badgeText = when (level) {
                ContrastLevel.STANDARD -> "AA"
                ContrastLevel.MEDIUM -> "AA+"
                ContrastLevel.HIGH -> "AAA"
            }

            Surface(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(MaterialTheme.shapes.small)
                    .clickable { onSelectLevel(level) },
                shape = MaterialTheme.shapes.small,
                color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerLow,
                border = BorderStroke(
                    width = if (selected) 2.dp else 1.dp,
                    color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Contrast preview pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(previewScheme.primary)
                            .border(1.dp, previewScheme.outline, RoundedCornerShape(6.dp))
                            .padding(horizontal = 10.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = badgeText,
                            style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                            fontWeight = FontWeight.ExtraBold,
                            color = previewScheme.onPrimary,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                    Text(
                        text = level.label,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
                        color = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
